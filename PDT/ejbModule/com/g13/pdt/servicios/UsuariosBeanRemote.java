package com.g13.pdt.servicios;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import com.g13.pdt.entidades.Rol;
import com.g13.pdt.entidades.Usuario;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;
import com.g13.pdt.interfaces.IUsuarios;
import com.g13.pdt.util.CadenasUtil;

@Stateless
@Remote
public class UsuariosBeanRemote implements IUsuarios {

	@PersistenceContext
	private EntityManager em;
	
	
    public UsuariosBeanRemote() {
        
    }

	@Override
	public Usuario altaUsuario(Usuario usuario) throws DatosInvalidosException {
		try {
			usuario.setActivo(true);
			
			//Tenemos que reemplazar los roles con Roles traidos de la bd, 
			//porque hacer un loop y merge a los roles no funcion�.
			Set<Rol> rolesQueTiene = usuario.getRoles();
			usuario.setRoles(new HashSet<Rol>());
			//mergear los roles
			for (Rol rolQueTiene : rolesQueTiene) {
				Rol rolBd = this.em.find(Rol.class, rolQueTiene.getId());
				usuario.addRol(rolBd);
			}
			this.em.flush();
			this.em.persist(usuario);
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
		
		return usuario;
	}

	@Override
	public Usuario editarUsuario(Usuario usuario) throws DatosInvalidosException, NoExisteElementoException {
		this.obtenerPorId(usuario.getId());	//Si existe sigo, sino tira NoExisteElementoException
		try {
			this.em.merge(usuario);
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
		
		return usuario;
	}

	@Override
	public boolean borrarUsuario(Integer id) throws DatosInvalidosException {
	
		try {
			Usuario usuario = this.em.find(Usuario.class, id);
			usuario.setActivo(false);
//			usuario.setNombres(toString());	
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
	public List<Usuario> obtenerListaTodos() {
		TypedQuery<Usuario> query = this.em.createQuery("SELECT p FROM Usuario p",Usuario.class); 
		List<Usuario> resultado = query.getResultList();
		return resultado;
	}

	@Override
	public Usuario obtenerUsuario(String username, String password) throws DatosInvalidosException {
		TypedQuery<Usuario> query = this.em.createQuery("SELECT u FROM Usuario u where u.username=:username",Usuario.class);
		query.setParameter("username", username);
		Usuario  resultado = null;
		try {
			resultado = query.getSingleResult();
		}catch(NoResultException e){
			throw new DatosInvalidosException("No existe el usuario");
		}
		//Si llega aca es porque existe el usuario
		if (resultado.getPassword().equals(password) ) {
			return resultado;
		}else {
			throw new DatosInvalidosException("La contrase�a es incorrecta");
		}
		
		
	}

	@Override
	public Usuario obtenerPorId(Integer id) throws NoExisteElementoException {
		Usuario entidadBD = this.em.find(Usuario.class, id);
		if (entidadBD == null) {
			throw new NoExisteElementoException("No existe un Usuario con id " + id);
		}
		return entidadBD;
	}
	
}
