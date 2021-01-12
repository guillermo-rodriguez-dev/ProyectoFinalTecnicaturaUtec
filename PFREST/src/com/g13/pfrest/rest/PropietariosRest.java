package com.g13.pfrest.rest;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.g13.pdt.entidades.Propietario;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IPropietarios;
import com.g13.pfrest.interfaces.IPropietariosRest;
import com.g13.pfrest.util.UtilidadesRest;

@Stateless
@LocalBean
public class PropietariosRest implements IPropietariosRest {
	
	@EJB
    private IPropietarios pb;

	@Override
	public Response altaPropietario(Propietario propietario) {
		try {
			return Response.status(Status.CREATED).entity(pb.altaPropietario(propietario)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
		
	}

	@Override
	public Response editarPropietario(Propietario propietario) {
		try {
			return Response.status(Status.OK).entity(pb.editarPropietario(propietario)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	@Override
	public Response desactivarPropietario(Propietario propietario) {
		try {
			return Response.status(Status.OK).entity(pb.desactivarPropietario(propietario)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	@Override
	public Response activarPropietario(Propietario propietario) {
		try {
			return Response.status(Status.OK).entity(pb.activarPropietario(propietario)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	@Override
	public Response obtenerListaTodos(){
		return Response.status(Status.OK).entity(pb.obtenerListaTodos()).build();
	}

}
