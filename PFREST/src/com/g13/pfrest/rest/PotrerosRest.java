package com.g13.pfrest.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.g13.pdt.entidades.IndicadorPotrero;
import com.g13.pdt.entidades.Potrero;
import com.g13.pdt.entidades.Predio;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IPotreros;
import com.g13.pdt.interfaces.IPredios;
import com.g13.pfrest.datatypes.EdicionPotreros;
import com.g13.pfrest.interfaces.IPotrerosRest;
import com.g13.pfrest.util.UtilidadesRest;

@Stateless
@LocalBean
@RequestScoped
public class PotrerosRest implements IPotrerosRest {

	@EJB
	IPotreros potreroBean;
	
	@EJB
	private IPredios prediosBean;
	
    public PotrerosRest() {
    }


	@Override
	public Response obtenerPotrerosActivosPredio(Integer id) {
		try {
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(potreroBean.obtenerListaTodos(id)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}


	@Override
	public Response guardarPotreros(Integer idPredio, EdicionPotreros ep) {
		
		try {	
			Predio predio = prediosBean.obtenerPorId(idPredio);
			potreroBean.setPredio(idPredio);
			for (Potrero potrero : ep.getPotrerosDesactivar()) {
				potrero.setPredio(predio);
				for (IndicadorPotrero ip : potrero.getIndicadorPotreros()) {
					ip.setPotrero(potrero);
				}
				potreroBean.borrarPotrero(potrero);
			}
			
			for (Potrero potrero : ep.getPotrerosExistentes()) {
				for (IndicadorPotrero ip : potrero.getIndicadorPotreros()) {
					ip.setPotrero(potrero);
				}
				potrero.setPredio(predio);
				potreroBean.editarPotrero(potrero);
			}
			
			for (Potrero potrero : ep.getPotrerosNuevas()) {
				for (IndicadorPotrero ip : potrero.getIndicadorPotreros()) {
					ip.setPotrero(potrero);
				}
				potrero.setPredio(predio);
				potreroBean.altaPotrero(potrero);
			}
			
			if (!potreroBean.guardarTodo()) {
				throw new DatosInvalidosException("No se pudieron guardar los potreros ");
			}
			
			List<Potrero> lista = potreroBean.obtenerListaTodos(idPredio);
//			potreroBean.finalizarBean();
			potreroBean.Inicializar();
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(lista).build();
			
		} catch (PotrerosException e) {
//			potreroBean.finalizarBean();
			potreroBean.Inicializar();
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
		
		
	}



}
