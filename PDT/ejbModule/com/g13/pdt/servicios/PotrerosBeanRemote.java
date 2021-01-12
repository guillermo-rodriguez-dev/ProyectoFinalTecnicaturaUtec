package com.g13.pdt.servicios;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import com.g13.pdt.entidades.Potrero;
import com.g13.pdt.entidades.Predio;
import com.g13.pdt.entidades.ZonaGeografica;
import com.g13.pdt.entidades.ZonaPotrero;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IPotreros;
import com.g13.pdt.util.CadenasUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

@Stateful
@TransactionManagement(value=TransactionManagementType.BEAN)
public class PotrerosBeanRemote implements IPotreros {

	private EntityManager em;
	
	private HashMap<Integer,Potrero> listaExistentes;

	private HashMap<Integer,Potrero> listaNuevas;
	
	private HashMap<Integer,Potrero> listaDesactivar;

	private Predio predio = null;
	
	private Date fecha = new Date();
	
	public PotrerosBeanRemote() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("PDT");
		this.em = emf.createEntityManager();
	}
	
	@PostConstruct
	public void Inicializar() {
		this.listaExistentes = new HashMap<Integer,Potrero>();
		this.listaNuevas = new HashMap<Integer,Potrero>();
		this.listaDesactivar = new HashMap<Integer,Potrero>();
	}
	@Override
	public void renicializar() {
		this.listaExistentes = new HashMap<Integer,Potrero>();
		this.listaNuevas = new HashMap<Integer,Potrero>();
		this.listaDesactivar = new HashMap<Integer,Potrero>();
	}

	@Override
	public int altaPotrero(Potrero potrero) throws PotrerosException {
		if (potrero.getId() < 0) {
			//si no tiene id o es igual a cero, significa que no existe en la bd
			//As� que la mantengo el la lista de nuevas.
//			potrero.setDesde(new Date());	//Los desde los seteo todos a la vez al final
			this.listaNuevas.put(potrero.getId(), potrero);
			return potrero.getId();
		}
		//No deber�a bajar aca
		throw new DatosInvalidosException("El id " + potrero.getId() + " no es válido para el alta de Potrero");
	}

	@Override
	public int editarPotrero(Potrero potreroEditado) throws PotrerosException {
		if (potreroEditado.getId() == null || potreroEditado.getId() == 0) {
			throw new DatosInvalidosException("La Potrero a editar no tiene Id");
		}
		
		if (potreroEditado.getId() < 0) {
			//si no tiene id o es menor que cero, significa que no existe en la bd
			//As� que la mantengo en la lista de nuevas.
			this.listaNuevas.put(potreroEditado.getId(), potreroEditado);
			
		}else {
			//Mayor que cero...
			//Significa que ya existe en la bd y se modific� algo
			//Me traigo la existente
//			Potrero potreroExistente = this.listaExistentes.get(potreroEditado.getId());
//			if (potreroExistente.getForma().equals(potreroEditado.getForma())) {
//				//Si la forma sigue igual, simplemente remplazo la existente con la editada mergeado
//				this.em.merge(potreroEditado);
//				this.listaExistentes.put(potreroEditado.getId(), potreroEditado);
//			}else {
//				//Si la forma no es la misma, la editada mantiene el id, y la existente le pongo id null manteniendo lo demas
//				potreroExistente.setActivo(false);
//				potreroEditado.setId(null);	//al final habr� q recorrer los null para hacerle un persist
//				this.listaNuevas.put(potreroEditado.getId(), potreroEditado);
//			}
			//Se supone que la forma sigue igual, as� que simplemente remplazo la existente con la editada mergeado
			this.em.merge(potreroEditado);
			this.listaExistentes.put(potreroEditado.getId(), potreroEditado);
		}
		return potreroEditado.getId();
	}

	@Override
	public boolean borrarPotrero(Potrero potreroADesactivar) throws DatosInvalidosException {
		if (potreroADesactivar.getId() == null || potreroADesactivar.getId() == 0) {
			throw new DatosInvalidosException("La Potrero a editar no tiene Id");
		}
		
		if (potreroADesactivar.getId() < 0) {
			//si no tiene id o es menor que cero, significa que no existe en la bd
			//As� que la mantengo en la lista de nuevas.
			this.listaNuevas.remove(potreroADesactivar.getId(), potreroADesactivar);
			//No tiene zonapotrero asociadas al no estar en la bd
		}else {
			//Mayor que cero...
			//Significa que ya existe en la bd y se modific� algo
			//Me traigo la existente
			this.listaDesactivar.put(potreroADesactivar.getId(), potreroADesactivar);
			this.listaExistentes.remove(potreroADesactivar.getId());
		}
		return true;
	}

	@Override
	public List<Potrero> obtenerListaTodos(int idPredio) throws NoExisteElementoException {
		//Se pasa solo el id, porque de cualquier forma hay que buscar el predio
		this.predio = this.em.find(Predio.class, idPredio);
		if (this.predio == null) {
			throw new NoExisteElementoException("No existe un Predio con id " + idPredio);
		}
		// Aca se obtienen todas las Potrero activas del predio
		TypedQuery<Potrero> query = this.em.createQuery("SELECT z FROM Potrero z where z.activo=:activo and z.predio=:predio",Potrero.class);
		query.setParameter("predio", this.predio);
		query.setParameter("activo", true);
		List<Potrero> resultado = query.getResultList();
		for (Potrero potrero : resultado) {
			this.listaExistentes.put(potrero.getId(),potrero);
		}
		return resultado;
	}

	@Override
	public boolean guardarTodo() throws DatosInvalidosException {
		//La lista de nueva tiene solo id's null o negativos
		this.em.getTransaction().begin();

		for (Potrero potreroADesactivar : this.listaDesactivar.values()) {
			//Recorro la lista de zonas que tengo que desactivar
			Potrero potrero = this.em.find(Potrero.class, potreroADesactivar.getId());	//Me desactiva los cambios que le haya hecho si la borro
			this.em.refresh(potrero);
			potrero.setActivo(false);
			potrero.setHasta(fecha);
			this.desactivarZonaPotreros(potrero);
			System.out.println("Potrero borrada qued�: " + potrero.getNombre() + ", " + potrero.getActivo() + ", " + potrero.getHasta().toString());
		}
		
		try {
			this.em.flush();	//persisto las borradas por si hay nombres nuevos igual a borradas
		}catch (ConstraintViolationException e) {
            DatosInvalidosException ee = CadenasUtil.crearException(e);
			throw ee;
		}
		catch (PersistenceException e) {
			DatosInvalidosException pe = CadenasUtil.crearException(e);
			throw pe;
		}
		
		for (Potrero zona : this.listaNuevas.values()) {
			zona.setPredio(this.predio); 	//Seteo nuevamente el predio por si acaso
			if (zona.getId() == null) {
				//Los id's null son de las ediciones de las ya existentes en la bd
				
				//Este caso no se tiene que dar nunca en realidad
//				zona.setHasta(fecha);
//				this.em.persist(zona);	//Ya tiene seteado el desde
				throw new DatosInvalidosException("guardarTodo(): Id Null. No se puede guardar en la base de datos: " + zona.getNombre());
			}else if (zona.getId() < 0 ) {
				//Estas son las nuevas
				zona.setActivo(true);
				zona.setDesde(fecha);
				this.crearZonaPotreros(zona);
				zona.setId(null);
				this.em.persist(zona);
			}else {
				//id o o mayor 0
				throw new DatosInvalidosException("guardarTodo(): No se puede guardar en la base de datos " + zona.getId() + ": " + zona.getNombre());
			}
		}
		try {
			this.em.flush();
		}catch (ConstraintViolationException e) {
            DatosInvalidosException ee = CadenasUtil.crearException(e);
			throw ee;
		}
		catch (PersistenceException e) {
			DatosInvalidosException pe = CadenasUtil.crearException(e);
			throw pe;
		}
		this.em.getTransaction().commit();
		return true;
	}
	
	private void crearZonaPotreros(Potrero nuevoPotrero) {
		//Cuando se crea un nuevo potrero:
		//Tengo que intersectar el potrero con las ZonaGeografica activas que hayan en el predio
		TypedQuery<ZonaGeografica> query = this.em.createQuery("SELECT z FROM ZonaGeografica z where z.activo=:activo and z.predio=:predio",ZonaGeografica.class);
		query.setParameter("predio", this.predio);
		query.setParameter("activo", true);
		List<ZonaGeografica> listaZonaGeograficaActivas = query.getResultList();
		System.out.println("Hay " + listaZonaGeograficaActivas.size() + " ZonaGeografica activas en el predio " + this.predio.getNombre());
		for (ZonaGeografica zonaGeograficaActiva : listaZonaGeograficaActivas) {
			if (nuevoPotrero.getForma().intersects(zonaGeograficaActiva.getForma())) {
				//Si se intersecta la ZonaGeografica con el Potrero
				Geometry forma = nuevoPotrero.getForma().intersection(zonaGeograficaActiva.getForma());
				if (forma instanceof MultiPolygon) {
					//Si la intersecci�n  es un multipoligono.
					int cant = forma.getNumGeometries();
					for (int i = 0; i < cant; i++) {
						ZonaPotrero nuevaZonaPotrero = new ZonaPotrero(nuevoPotrero, zonaGeograficaActiva, new Date(), true, (Polygon) forma.getGeometryN(i));
						nuevoPotrero.addZonaPotrero(nuevaZonaPotrero);	
						this.em.persist(nuevaZonaPotrero);
					}
					
				}else if (forma instanceof Polygon) {
					ZonaPotrero nuevaZonaPotrero = new ZonaPotrero(nuevoPotrero, zonaGeograficaActiva, this.fecha, true, (Polygon) forma);
					nuevoPotrero.addZonaPotrero(nuevaZonaPotrero);	
					this.em.persist(nuevaZonaPotrero);
				}	
			}
		}
	}


	private void desactivarZonaPotreros(Potrero potreroDesactivado) {
		// Cuando se baja un potrero, tengo que desactivar todas sus ZonaPotreros con la misma fecha
		TypedQuery<ZonaPotrero> query = this.em.createQuery("SELECT zp FROM ZonaPotrero zp where zp.activo=:activo and zp.potrero=:potrero",ZonaPotrero.class);
		query.setParameter("potrero", potreroDesactivado);
		query.setParameter("activo", true);
		List<ZonaPotrero> listaZonaPotreroActivas = query.getResultList();
		for (ZonaPotrero zonaPotrero : listaZonaPotreroActivas) {
			zonaPotrero.setActivo(false);
			zonaPotrero.setHasta(this.fecha);
		}
	}
	
	@Override
	@Remove
	public boolean finalizarBean() {

		return true;
	}

	@Override
	public Potrero obtenerPorId(Integer id) throws NoExisteElementoException {
		Potrero entidadBD = this.em.find(Potrero.class, id);
		if (entidadBD == null) {
			throw new NoExisteElementoException("No existe un Potrero con id " + id);
		}
		return entidadBD;
	}
	
	@Override
	public boolean setPredio(int idPredio) throws DatosInvalidosException, NoExisteElementoException {
		this.predio = this.em.find(Predio.class, idPredio);
		if (this.predio == null) {
			throw new NoExisteElementoException("No existe un Predio con id " + idPredio);
		}
		return true;
	}
	
}