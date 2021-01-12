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
import com.g13.pdt.interfaces.IZonasGeograficas;
import com.g13.pdt.util.CadenasUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

@Stateful
@TransactionManagement(value=TransactionManagementType.BEAN)
public class ZonasGeograficasBeanRemote implements IZonasGeograficas {

	private EntityManager em;
	
	private HashMap<Integer,ZonaGeografica> listaExistentes;
	
	private HashMap<Integer,ZonaGeografica> listaDesactivar;

	private HashMap<Integer,ZonaGeografica> listaNuevas;

	private Predio predio = null;
	
	private Date fecha = new Date();
	
	public ZonasGeograficasBeanRemote() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("PDT");
		this.em = emf.createEntityManager();
	}
	
	@PostConstruct
	public void Inicializar() {
		this.listaExistentes = new HashMap<Integer,ZonaGeografica>();
		this.listaNuevas = new HashMap<Integer,ZonaGeografica>();
		this.listaDesactivar = new HashMap<Integer,ZonaGeografica>();
	}

	@Override
	public void reinicializar() {
		this.listaExistentes = new HashMap<Integer,ZonaGeografica>();
		this.listaNuevas = new HashMap<Integer,ZonaGeografica>();
		this.listaDesactivar = new HashMap<Integer,ZonaGeografica>();
	}

	public int altaZonaGeografica(ZonaGeografica zonaGeografica) throws PotrerosException {
		if (zonaGeografica.getId() < 0) {
			//si no tiene id o es igual a cero, significa que no existe en la bd
			//As� que la mantengo el la lista de nuevas.
//			zonaGeografica.setDesde(new Date());	//Los desde los seteo todos a la vez al final
			this.listaNuevas.put(zonaGeografica.getId(), zonaGeografica);
			return zonaGeografica.getId();
		}
		//No debería bajar aca
		throw new DatosInvalidosException("El id " + zonaGeografica.getId() + " no es válido para el alta de potrero");
	}

	@Override
	public int editarZonaGeografica(ZonaGeografica zonaEditada) throws PotrerosException {
		if (zonaEditada.getId() == null || zonaEditada.getId() == 0) {
			throw new DatosInvalidosException("El id de la Zona no es correcto");
		}
		
		if (zonaEditada.getId() < 0) {
			//si no tiene id o es menor que cero, significa que no existe en la bd
			//As� que la mantengo en la lista de nuevas.
			this.listaNuevas.put(zonaEditada.getId(), zonaEditada);
			
		}else {
//			System.out.println("es zona ya existente en bd");
			//Mayor que cero...
			//Significa que ya existe en la bd y se modific� algo
			//Como esta implementada la interfez, en el editar no es posible que cambie la forma. solo en alta o borrar se asigna forma.
			//Como se supone que la forma sigue igual, simplemente remplazo la existente con la editada mergeado
//			if (zonaEditada.getNombre() == null ) {
//				throw new DatosInvalidosException("No es valido el nombre especificado");
//			}
//			if (zonaEditada.getDescripcion() == null ||  zonaEditada.getDescripcion().equals("")) {
//				throw new DatosInvalidosException("La descripcion de la zona " + zonaEditada.getNombre() + " no puede ser vac�a.");
//			}
//			if ( zonaEditada.getTipoZona() == null) {
//				throw new DatosInvalidosException("El Tipo de zona de la zona " + zonaEditada.getNombre() + " no puede ser vac�o.");
//			}
			//Se supone que la forma sigue igual, as� que simplemente remplazo la existente con la editada mergeado
			this.em.merge(zonaEditada);
			this.listaExistentes.put(zonaEditada.getId(), zonaEditada);
		}
		return zonaEditada.getId();
	}

	@Override
	public boolean borrarZonaGeografica(ZonaGeografica zonaBorrada) throws DatosInvalidosException {
		if (zonaBorrada.getId() == null || zonaBorrada.getId() == 0) {
			throw new DatosInvalidosException("La ZonaGeografica a borrar no tiene Id");
		}
		
		if (zonaBorrada.getId() < 0) {
			//si no tiene id o es menor que cero, significa que no existe en la bd. Esta en la lista de nuevas
			//y no va a tener zonapotrero asociadas
			this.listaNuevas.remove(zonaBorrada.getId(), zonaBorrada);
			
		}else {
			//Mayor que cero...
			//Significa que ya existe en la bd y se la quiere borrar		
			
			this.listaDesactivar.put(zonaBorrada.getId(), zonaBorrada);
			this.listaExistentes.remove(zonaBorrada.getId());
		}
		return true;
	}

	@Override
	public List<ZonaGeografica> obtenerListaTodosActivas(int idPredio) throws NoExisteElementoException {
		//Se pasa solo el id, porque de cualquier forma hay que buscar el predio
		this.predio = this.em.find(Predio.class, idPredio);
		if (this.predio == null) {
			throw new NoExisteElementoException("No existe un Predio con id " + idPredio);
		}
		// Aca se obtienen todas las ZonaGeografica activas del predio
		TypedQuery<ZonaGeografica> query = this.em.createQuery("SELECT z FROM ZonaGeografica z where z.activo=:activo and z.predio=:predio",ZonaGeografica.class);
		query.setParameter("predio", this.predio);
		query.setParameter("activo", true);
		List<ZonaGeografica> resultado = query.getResultList();
		for (ZonaGeografica zonaGeografica : resultado) {
			this.listaExistentes.put(zonaGeografica.getId(),zonaGeografica);
		}
		return resultado;
	}

	@Override
	public boolean guardarTodo() throws DatosInvalidosException {
		//La lista de nueva tiene solo id's null o negativos
		this.em.getTransaction().begin();
		
		for (ZonaGeografica zonaBorrada : this.listaDesactivar.values()) {
			//Recorro la lista de zonas que tengo que desactivar
			ZonaGeografica zona = this.em.find(ZonaGeografica.class, zonaBorrada.getId());	//Me desactiva los cambios que le haya hecho si la borro
			this.em.refresh(zona);
			zona.setActivo(false);
			zona.setHasta(fecha);
			this.desactivarZonaPotreros(zona);
			System.out.println("Zona borrada qued�: " + zona.getNombre() + ", " + zona.getActivo() + ", " + zona.getHasta().toString());
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
		
		for (ZonaGeografica zona : this.listaNuevas.values()) {
			zona.setPredio(this.predio); 	//Seteo nuevamente el predio por si acaso
			System.out.println("IdPredio: " + this.predio.getId());
			System.out.println("IdPredio: " + this.predio.getId());
			System.out.println("IdPredio: " + this.predio.getId());
			System.out.println("IdPredio: " + this.predio.getId());
			System.out.println("IdPredio: " + this.predio.getId());
			System.out.println("IdPredio: " + this.predio.getId());
			System.out.println("IdPredio: " + this.predio.getId());
			System.out.println("IdPredio: " + this.predio.getId());
			System.out.println("IdPredio: " + this.predio.getId());
			
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
				this.crearZonaPotreros(zona);	//Creo las nuevas intersecciones con los potreros existentes
				zona.setId(null);
				this.em.persist(zona);
			}else {
				//getId mayor 0: no deber�a pasar ya q tendr�a que estar en la lista de existentes, no en la de nuevas
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
	
	private void crearZonaPotreros(ZonaGeografica nuevaZonaGeografica) {
		//Me traigo todos los potreros activos del predio
		//Los intersectados con la ZonaGeografica anterior se desactivaron cuando se borr� la ZonaGeografica, x lo que solo tengo que crear las nuevas intersecciones
		TypedQuery<Potrero> query = this.em.createQuery("SELECT p FROM Potrero p where p.activo=:activo and p.predio=:predio",Potrero.class);
		query.setParameter("predio", this.predio);
		query.setParameter("activo", true);
		List<Potrero> listaPotrerosActivos = query.getResultList();
		System.out.println("Hay " + listaPotrerosActivos.size() + " potreros activos en el predio " + nuevaZonaGeografica.getPredio().getNombre());
		for (Potrero potreroActivo : listaPotrerosActivos) {
			if (nuevaZonaGeografica.getForma().intersects(potreroActivo.getForma())) {
				//Si el potrero se intersecta con la zonageografica
				Geometry forma = potreroActivo.getForma().intersection(nuevaZonaGeografica.getForma());
				if (forma instanceof MultiPolygon) {
					//Si la intersecci�n  es un multipoligono.
					int cant = forma.getNumGeometries();
					for (int i = 0; i < cant; i++) {
						ZonaPotrero nuevaZonaPotrero = new ZonaPotrero(potreroActivo, nuevaZonaGeografica, new Date(), true, (Polygon)forma.getGeometryN(i));
						nuevaZonaGeografica.addZonaPotrero(nuevaZonaPotrero);
						this.em.persist(nuevaZonaPotrero);
					}
					
				}else if (forma instanceof Polygon) {
					ZonaPotrero nuevaZonaPotrero = new ZonaPotrero(potreroActivo, nuevaZonaGeografica, this.fecha, true, (Polygon)forma);
					nuevaZonaGeografica.addZonaPotrero(nuevaZonaPotrero);
					this.em.persist(nuevaZonaPotrero);
				}
			}
		}
	}


	private void desactivarZonaPotreros(ZonaGeografica zonaGeograficaDesactivada) {
		// Me traigo todas las ZonaPotrero activas de la ZonaGeografica
		TypedQuery<ZonaPotrero> query = this.em.createQuery("SELECT zp FROM ZonaPotrero zp where zp.activo=:activo and zp.zonaGeografica=:zonaGeografica",ZonaPotrero.class);
		query.setParameter("zonaGeografica", zonaGeograficaDesactivada);
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
	public ZonaGeografica obtenerPorId(Integer id) throws NoExisteElementoException {
		ZonaGeografica entidadBD = this.em.find(ZonaGeografica.class, id);
		if (entidadBD == null) {
			throw new NoExisteElementoException("No existe una Zona Geografica con id " + id);
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