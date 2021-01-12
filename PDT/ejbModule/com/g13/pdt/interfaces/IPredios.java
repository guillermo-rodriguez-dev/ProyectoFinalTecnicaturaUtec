package com.g13.pdt.interfaces;

import java.util.List;
import javax.ejb.Remote;
import com.g13.pdt.entidades.Predio;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;

//Se define totalmente "limpia" la interfaz, para que no est� acoplada a nada mas que el Predio
@Remote
public interface IPredios {
	
	//ABM
	public Predio altaPredio(Predio predio) throws DatosInvalidosException;
	public Predio editarPredio(Predio predio) throws DatosInvalidosException, NoExisteElementoException;
	public Predio borrarPredio(Predio predio) throws DatosInvalidosException, NoExisteElementoException;
	public Predio activarPredio(Predio predio) throws DatosInvalidosException, NoExisteElementoException;
	public Predio obtenerPorId(Integer id) throws NoExisteElementoException;
	//Listados
	public List<Predio> obtenerListaTodos();
	//No se definiran mas listados hasta que se necesiten
	
	public boolean formaEditable(Integer idPredio) throws NoExisteElementoException;
	public boolean existeNombre(String nombre);

}
