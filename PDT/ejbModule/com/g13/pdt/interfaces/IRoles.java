package com.g13.pdt.interfaces;

import java.util.List;
import com.g13.pdt.entidades.Rol;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;

	public interface IRoles {
		
		//ABM
		public Rol altaRol(Rol rol) throws DatosInvalidosException;
		public Rol editarRol(Rol rol) throws DatosInvalidosException, NoExisteElementoException;
		public boolean borrarRol(Integer id) throws DatosInvalidosException, NoExisteElementoException;
		public Rol obtenerPorId(Integer id) throws NoExisteElementoException;
		
		//Listados
		public List<Rol> obtenerListaTodos();
		

}