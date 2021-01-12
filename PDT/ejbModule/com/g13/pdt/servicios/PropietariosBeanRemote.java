package com.g13.pdt.servicios;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import com.g13.pdt.entidades.Propietario;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;
import com.g13.pdt.interfaces.IPropietarios;
import com.g13.pdt.util.CadenasUtil;


@Stateless
@Remote
public class PropietariosBeanRemote implements IPropietarios {

	@PersistenceContext
	private EntityManager em;
	
    public PropietariosBeanRemote() {
        
    }

	@Override
	public Propietario altaPropietario(Propietario propietario) throws DatosInvalidosException {
		try {
			propietario.setActivo(true);
			propietario.setDesde(new Date());
			this.em.persist(propietario);
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
		
		return propietario;
	}

	@Override
	public Propietario editarPropietario(Propietario propietario) throws DatosInvalidosException, NoExisteElementoException {
		this.obtenerPorId(propietario.getId());	//Si no existe tira NoExisteElementoException
		try {
			this.em.merge(propietario);
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
		
		return propietario;
	}

	@Override
	public Propietario desactivarPropietario(Propietario propietario) throws DatosInvalidosException, NoExisteElementoException {
//		this.em.merge(propietario);
		propietario = this.obtenerPorId(propietario.getId());	//Si no existe tira NoExisteElementoException;
		try {
			propietario.setActivo(false);
			propietario.setHasta(new Date());
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
		
		return propietario;
	}
	
	@Override
	public List<Propietario> obtenerListaTodos() {
		TypedQuery<Propietario> query = this.em.createQuery("SELECT p FROM Propietario p",Propietario.class); 
		List<Propietario> resultado = query.getResultList();
//		resultado.size();
		return resultado;
	}

	@Override
	public Propietario activarPropietario(Propietario propietario) throws DatosInvalidosException, NoExisteElementoException {
		propietario = this.obtenerPorId(propietario.getId());	//Si no existe tira NoExisteElementoException;
		try {
			propietario.setActivo(true);
			propietario.setDesde(new Date());
			this.em.flush();
		}
		
		catch (ConstraintViolationException e) {
            DatosInvalidosException ee = CadenasUtil.crearException(e);
			throw ee;
		}
		catch (PersistenceException e) {
			DatosInvalidosException pe = CadenasUtil.crearException(e);
			System.out.println("DIO PERSISTENCE EXCEPTION");
			throw pe;
		}
		catch (NullPointerException e) {
			DatosInvalidosException pe = CadenasUtil.crearException(e);
			throw pe;
		}
		
		return propietario;
	}

	@Override
	public Propietario obtenerPorId(Integer id) throws NoExisteElementoException {
		Propietario entidadBD = this.em.find(Propietario.class, id);
		if (entidadBD == null) {
			throw new NoExisteElementoException("No existe un Propietario con id " + id);
		}
		return entidadBD;
	}
}
