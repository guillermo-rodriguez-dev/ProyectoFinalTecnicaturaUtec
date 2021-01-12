package com.g13.pdt.servicios;

import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import com.g13.pdt.entidades.Indicador;
import com.g13.pdt.entidades.Predio;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;
import com.g13.pdt.interfaces.IIndicadores;
import com.g13.pdt.util.CadenasUtil;


@Stateless
@Remote
public class IndicadoresBeanRemote implements IIndicadores {

	@PersistenceContext
	private EntityManager em;
	
	
    public IndicadoresBeanRemote() {
        
    }


	@Override
	public Indicador altaIndicador(Indicador indicador) throws DatosInvalidosException {
		try {
			this.em.persist(indicador);
			this.em.flush();
		}
		catch (ConstraintViolationException e) {
            DatosInvalidosException ee = CadenasUtil.crearException(e);
			throw ee;
		}
		catch (PersistenceException e) {
			DatosInvalidosException pe = CadenasUtil.crearException(e);
			throw pe;
		}
		
		//Aca tz ya esta actualizada con el id
		return indicador;
	}
	
	@Override
	public Indicador editarIndicador(Indicador indicador) throws DatosInvalidosException, NoExisteElementoException {
		this.obtenerPorId(indicador.getId()); 	//Si existe sigo, sino tira NoExisteElementoException
		try {
			this.em.merge(indicador);
			this.em.flush();
		}
		catch (ConstraintViolationException e) {
            DatosInvalidosException ee = CadenasUtil.crearException(e);
			throw ee;
		}
		catch (PersistenceException e) {
			DatosInvalidosException pe = CadenasUtil.crearException(e);
			throw pe;
		}
		
		return indicador;
	}


	@Override
	public boolean borrarIndicador(Integer id) throws DatosInvalidosException, NoExisteElementoException {
		Indicador indicador = this.obtenerPorId(id);	//Si existe sigo sino tira NoExisteElementoException
		try {
			this.em.remove(indicador);
			this.em.flush();
		}
		catch (ConstraintViolationException e) {
            DatosInvalidosException ee = CadenasUtil.crearException(e);
			throw ee;
		}
		catch (IllegalArgumentException e) {
			throw new DatosInvalidosException("No existe un Indicador con id " + id ,e);
		}
		catch (PersistenceException e) {
			DatosInvalidosException pe = CadenasUtil.crearException(e);
			throw pe;
		}
		
		return true;
	}


	@Override
	public List<Indicador> obtenerListaTodos() {
		//La consulta solo me tiene que devolver los indicadores que no tienen hijos. TODO en otro metodo?
		TypedQuery<Indicador> query = this.em.createQuery("SELECT i FROM Indicador i",Indicador.class); 
		List<Indicador> resultado = query.getResultList();
		return resultado;
	}


	@Override
	public Indicador obtenerIndicador(String nombre) throws NoExisteElementoException, DatosInvalidosException {
		//La consulta solo me tiene que devolver los indicadores que no tienen hijos. TODO en otro metodo?
		TypedQuery<Indicador> query = this.em.createQuery("SELECT i FROM Indicador i where i.nombre=:nombre",Indicador.class); 
		query.setParameter("nombre", nombre);
		Indicador  resultado = null;
		try {
			resultado = query.getSingleResult();
		}catch(NoResultException e){
			throw new NoExisteElementoException("No existe un indicador raiz con nombre " + nombre,e);
		}catch(NonUniqueResultException e){
			throw new DatosInvalidosException("Existe mas de un indicador raíz con nombre " + nombre,e);
		}
		return resultado;
	}


	@Override
	public List<Indicador> obtenerListaSinHijosActivos() {
		//La consulta solo me tiene que devolver los indicadores que no tienen hijos. TODO en otro metodo?
		TypedQuery<Indicador> query = this.em.createQuery("SELECT i FROM Indicador i WHERE i.hijos IS EMPTY",Indicador.class); 
		List<Indicador> resultado = query.getResultList();
		return resultado;
	}


	@Override
	public Indicador obtenerIndicadorParaPredio(String nombre, int idPredio) throws NoExisteElementoException, DatosInvalidosException {
		//La consulta solo me tiene que devolver los indicadores que no tienen hijos. TODO en otro metodo?
		TypedQuery<Indicador> query = this.em.createQuery("SELECT i FROM Indicador i where i.nombre=:nombre ",Indicador.class); 
		query.setParameter("nombre", nombre);
		Indicador  resultado = null;
		Predio predio = null;
		try {
			resultado = query.getSingleResult();
			resultado.getHijos().size();
			predio = this.em.find(Predio.class, idPredio);
			predio.getPotreros().size();
		}catch(NoResultException e){
			throw new NoExisteElementoException("No existe un indicador raiz",e);
		}catch(NonUniqueResultException e){
			throw new DatosInvalidosException("Existe mas de un indicador ra�z",e);
		}
		
		predio.cargarAreaIndicador(resultado);
		return resultado;
	}


	@Override
	public Indicador obtenerPorId(Integer id) throws NoExisteElementoException {

		Indicador entidadBD = this.em.find(Indicador.class, id);
		if (entidadBD == null) {
			throw new NoExisteElementoException("No existe un Indicador con id " + id);
		}
		return entidadBD;
		
	}
	

	
}
