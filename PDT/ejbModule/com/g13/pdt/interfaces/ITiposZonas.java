package com.g13.pdt.interfaces;

import java.util.List;

import javax.ejb.Remote;
import com.g13.pdt.entidades.TipoZona;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;
@Remote

//Se define totalmente "limpia" la interfaz, para que no est� acoplada a nada mas que el Propietario
public interface ITiposZonas {
	
	//ABM
	public TipoZona altaTipoZona(TipoZona tz) throws DatosInvalidosException;
	public TipoZona editarTipoZona(TipoZona tz) throws DatosInvalidosException, NoExisteElementoException;
	public boolean borrarTipoZona(Integer id) throws DatosInvalidosException, NoExisteElementoException;
	public TipoZona obtenerPorId(Integer id) throws NoExisteElementoException;
	
	//Listados
	public List<TipoZona> obtenerListaTodos();
	//No se definir�n mas listados hasta que se necesiten

}
