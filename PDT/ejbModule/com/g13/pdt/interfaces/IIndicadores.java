package com.g13.pdt.interfaces;

import java.util.List;

import com.g13.pdt.entidades.Indicador;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;

//Se define totalmente "limpia" la interfaz, para que no est� acoplada a nada mas que el Propietario
public interface IIndicadores {
	
	//ABM
	public Indicador altaIndicador(Indicador tz) throws DatosInvalidosException;
	public Indicador editarIndicador(Indicador tz) throws DatosInvalidosException, NoExisteElementoException;
	public boolean borrarIndicador(Integer id) throws DatosInvalidosException, NoExisteElementoException;
	public Indicador obtenerPorId(Integer id) throws NoExisteElementoException;
	
	//Listados
	public List<Indicador> obtenerListaTodos();
	public List<Indicador> obtenerListaSinHijosActivos();
	public Indicador obtenerIndicador(String nombre) throws NoExisteElementoException, DatosInvalidosException;
	public Indicador obtenerIndicadorParaPredio(String nombre, int idPredio) throws NoExisteElementoException, DatosInvalidosException;
	
	//No se definir�n mas listados hasta que se necesiten

}
