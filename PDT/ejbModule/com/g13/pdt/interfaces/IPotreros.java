package com.g13.pdt.interfaces;

import java.util.List;
import javax.ejb.Remote;
import com.g13.pdt.entidades.Potrero;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;
import com.g13.pdt.excepciones.PotrerosException;

@Remote
public interface IPotreros {
	
	//Listados
	public List<Potrero> obtenerListaTodos(int idPredio) throws NoExisteElementoException;
	//ABM
	public int altaPotrero(Potrero potrero) throws DatosInvalidosException, PotrerosException;
	public int editarPotrero(Potrero potrero) throws DatosInvalidosException, PotrerosException;
	public boolean borrarPotrero(Potrero potreroEditado) throws DatosInvalidosException;
	public Potrero obtenerPorId(Integer id) throws NoExisteElementoException;
	public void renicializar();
	public boolean guardarTodo() throws DatosInvalidosException;
	public boolean setPredio(int idPredio) throws DatosInvalidosException, NoExisteElementoException;
	public boolean finalizarBean ();
	public void Inicializar();

}

