package com.g13.pdt.interfaces;

import java.util.List;
import com.g13.pdt.entidades.Propietario;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;

//Se define totalmente "limpia" la interfaz, para que no est� acoplada a nada mas que el Propietario
public interface IPropietarios {
	
	//ABM
	public Propietario altaPropietario(Propietario propietario) throws DatosInvalidosException;
	public Propietario editarPropietario(Propietario propietario) throws DatosInvalidosException, NoExisteElementoException;
	public Propietario desactivarPropietario(Propietario propietario) throws DatosInvalidosException, NoExisteElementoException;
	public Propietario activarPropietario(Propietario propietario) throws DatosInvalidosException, NoExisteElementoException;
	public Propietario obtenerPorId(Integer id) throws NoExisteElementoException;
	
	//Listados
	public List<Propietario> obtenerListaTodos();
	//No se definir�n mas listados hasta que se necesiten

}
