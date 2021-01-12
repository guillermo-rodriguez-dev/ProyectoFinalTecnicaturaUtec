package com.g13.pdt.servicios;

import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import com.g13.pdt.entidades.Rol;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;
import com.g13.pdt.interfaces.IRoles;
import com.g13.pdt.util.CadenasUtil;

@Stateless
@Remote
public class RolesBeanRemote implements IRoles {

	@PersistenceContext
	private EntityManager em;
	
	
    public RolesBeanRemote() {
        
    }

	@Override
	public Rol altaRol(Rol rol) throws DatosInvalidosException {
		try {
			this.em.persist(rol);
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
		
		return rol;
	}

	@Override
	public Rol editarRol(Rol rol) throws DatosInvalidosException, NoExisteElementoException {
		this.obtenerPorId(rol.getId());	//Si no existe tira NoExisteElementoException
		try {
			this.em.merge(rol);
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
		
		return rol;
	}

	@Override
	public boolean borrarRol(Integer id) throws DatosInvalidosException, NoExisteElementoException {
		Rol rol = this.obtenerPorId(id);	//Si no existe tira NoExisteElementoException
		try {
			this.em.remove(rol);	//Esta linea falla si hay usuarios con ese rol asignado.
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
		
		return true;
	}
	
	@Override
	public List<Rol> obtenerListaTodos() {
		TypedQuery<Rol> query = this.em.createQuery("SELECT p FROM Rol p",Rol.class); 
		List<Rol> resultado = query.getResultList();
		return resultado;
	}

	@Override
	public Rol obtenerPorId(Integer id) throws NoExisteElementoException {
		Rol entidadBD = this.em.find(Rol.class, id);
		if (entidadBD == null) {
			throw new NoExisteElementoException("No existe un Rol con id " + id);
		}
		return entidadBD;
	}
}
