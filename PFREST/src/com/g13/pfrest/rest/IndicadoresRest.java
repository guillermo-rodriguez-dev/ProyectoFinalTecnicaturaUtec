package com.g13.pfrest.rest;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import com.g13.pdt.entidades.Indicador;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IIndicadores;
import com.g13.pfrest.interfaces.IIndicadoresRest;
import com.g13.pfrest.util.UtilidadesRest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Stateless
@LocalBean
public class IndicadoresRest implements IIndicadoresRest {
	
	@EJB
    private IIndicadores indicadorService;

	@Override
	public Response altaIndicador(Indicador indicador) {
		try {
			return Response.status(Status.CREATED).entity(indicadorService.altaIndicador(indicador)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	@Override
	public Response editarIndicador(Indicador indicador) {
		try {
			return Response.status(Status.OK).entity(indicadorService.editarIndicador(indicador)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	@Override
	public Response borrarIndicador(int id) {
		try {
			return Response.status(Status.OK).entity(indicadorService.borrarIndicador(id)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
		
	}

	@Override
	public Response obtenerListaTodos() {
		return Response.status(Status.OK).entity(indicadorService.obtenerListaTodos()).build();
	}

	@Override
	public Response obtenerListaSinHijosActivos() {
		return Response.status(Status.OK).entity(indicadorService.obtenerListaSinHijosActivos()).build();
	}

	@Override
	public Response obtenerIndicador(String nombre) {
		try {
			return Response.status(Status.OK).entity(indicadorService.obtenerIndicador(nombre)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		} 
	}

	@Override
	public Response obtenerIndicadorParaPredio(String nombre, int id) {
		try {
			return Response.status(Status.OK).entity(indicadorService.obtenerIndicadorParaPredio(nombre,id)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		} 
	}

	


}
