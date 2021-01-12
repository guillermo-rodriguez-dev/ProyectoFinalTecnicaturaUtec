package com.g13.pdt.interfaces;

import java.util.List;
import javax.ejb.Remote;
import com.g13.pdt.entidades.Usuario;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;
	
	@Remote 

	public interface IUsuarios {
		
		//ABM
		public Usuario altaUsuario(Usuario usuario) throws DatosInvalidosException;
		public Usuario editarUsuario(Usuario usuario) throws DatosInvalidosException, NoExisteElementoException;
		public boolean borrarUsuario(Integer id) throws DatosInvalidosException;
		public Usuario obtenerPorId(Integer id) throws NoExisteElementoException;
		
		
		//Listados
		public List<Usuario> obtenerListaTodos();
		
		//Busquedas
		public Usuario obtenerUsuario(String username, String password) throws DatosInvalidosException;

}