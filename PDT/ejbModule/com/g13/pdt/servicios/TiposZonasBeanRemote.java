package com.g13.pdt.servicios;

import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import com.g13.pdt.entidades.TipoZona;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;
import com.g13.pdt.interfaces.ITiposZonas;
import com.g13.pdt.util.CadenasUtil;


@Stateless
@Remote
public class TiposZonasBeanRemote implements ITiposZonas {

	@PersistenceContext
	private EntityManager em;
	
	
    public TiposZonasBeanRemote() {
        
    }


	@Override
	public TipoZona altaTipoZona(TipoZona tz) throws DatosInvalidosException {
		try {
			tz.setId(null);
			this.em.persist(tz);
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
		return tz;
	}

	@Override
	public TipoZona editarTipoZona(TipoZona tz) throws DatosInvalidosException, NoExisteElementoException {
		
		this.obtenerPorId(tz.getId());	//Si existe sigo, sino tira NoExisteElementoException
			
		try {
			
			this.em.merge(tz);
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
		
		return tz;
	}


	@Override
	public boolean borrarTipoZona(Integer id) throws DatosInvalidosException, NoExisteElementoException {
		TipoZona tipoZona = this.obtenerPorId(id);	//Si existe sigo, sino tira NoExisteElementoException;
		try {
			this.em.remove(tipoZona);
			this.em.flush();
		}
		catch (ConstraintViolationException e) {
            DatosInvalidosException ee = CadenasUtil.crearException(e);
			throw ee;
		}
		catch (IllegalArgumentException e) {
			throw new DatosInvalidosException("No existe un Tipo de zona con id " + id ,e);
		}
		catch (PersistenceException e) {
			System.out.println("DIO PERSISTENCE EXCEPTION");
			DatosInvalidosException pe = CadenasUtil.crearException(e);
			throw pe;
		}
		
		return true;
	}


	@Override
	public List<TipoZona> obtenerListaTodos() {
		TypedQuery<TipoZona> query = this.em.createQuery("SELECT tz FROM TipoZona tz",TipoZona.class); 
		List<TipoZona> resultado = query.getResultList();
//		resultado.size();
		return resultado;
	}


	@Override
	public TipoZona obtenerPorId(Integer id) throws NoExisteElementoException {

		System.out.println("Busquemos el TipoZona con id " + id);
		TipoZona entidadBD = this.em.find(TipoZona.class, id);
		if (entidadBD == null) {
			throw new NoExisteElementoException("No existe un Tipo de zona con id " + id);
		}
		System.out.println("SE ENCONTRÓ TIPO DE ZONA CON ID " + id);
		return entidadBD;

	}

	
}
