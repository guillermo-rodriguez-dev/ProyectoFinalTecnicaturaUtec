package com.g13.pdt.interfaces;

import java.util.List;
import javax.ejb.Remote;
import com.g13.pdt.entidades.ZonaGeografica;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;
import com.g13.pdt.excepciones.PotrerosException;

@Remote
public interface IZonasGeograficas {
	
	//Listados
	public List<ZonaGeografica> obtenerListaTodosActivas(int idPredio) throws NoExisteElementoException;
	//ABM
	public int altaZonaGeografica(ZonaGeografica zonaGeografica) throws DatosInvalidosException, PotrerosException;
	public int editarZonaGeografica(ZonaGeografica zonaGeografica) throws DatosInvalidosException, PotrerosException;
	public boolean borrarZonaGeografica(ZonaGeografica zonaEditada) throws DatosInvalidosException;
	public ZonaGeografica obtenerPorId(Integer id) throws NoExisteElementoException;
	public void reinicializar();
	public boolean guardarTodo() throws DatosInvalidosException;
	public boolean setPredio(int idPredio) throws DatosInvalidosException, NoExisteElementoException;
	public boolean finalizarBean ();
	public void Inicializar ();
}
