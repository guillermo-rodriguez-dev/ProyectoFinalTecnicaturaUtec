package com.g13.pdt.interfaces;

import java.util.List;
import javax.ejb.Remote;
import com.g13.pdt.entidades.ZonaPotrero;
import com.g13.pdt.excepciones.NoExisteElementoException;

@Remote
public interface IZonasPotreros {
	
	public ZonaPotrero obtenerPorId(Integer id) throws NoExisteElementoException;
	
	//Listados
	public List<ZonaPotrero> obtenerListaTodosActivas(int idPredio) throws NoExisteElementoException;

}