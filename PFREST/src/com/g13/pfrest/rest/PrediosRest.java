package com.g13.pfrest.rest;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.g13.pdt.entidades.Predio;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IPredios;
import com.g13.pfrest.interfaces.IPrediosRest;
import com.g13.pfrest.util.UtilidadesRest;
import com.vividsolutions.jts.geom.Polygon;


/**
 * Session Bean implementation class PredioBeanRest
 */
@Stateless
@LocalBean
public class PrediosRest implements IPrediosRest {

    @EJB
	private IPredios predioBean;
    
	public Polygon ping() {
		Polygon f =  predioBean.obtenerListaTodos().get(0).getForma();
		return f;
	}

	public Response crearPredio(Predio predio) {
		try {
			return Response.status(Status.CREATED).entity(predioBean.altaPredio(predio)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	public Response editarPredio(Predio predio) {
		try {
			return Response.status(Status.OK).entity(predioBean.editarPredio(predio)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	public Response bajarPredio(Predio predio) {
		try {
			return Response.status(Status.OK).entity(predioBean.borrarPredio(predio)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	public Response obtenerTodosLosPredios() {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(predioBean.obtenerListaTodos()).build();
	}

	public Response activarPredio(Predio predio) {
		try {
			return Response.status(Status.OK).entity(predioBean.activarPredio(predio)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	public Response obtenerPorId(Integer id) {
		try {
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(predioBean.obtenerPorId(id)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
		
	}

	@Override
	public Response existeNombre(String nombre) {
		return Response.status(Status.OK).entity(predioBean.existeNombre(nombre)).build();
	}

}
