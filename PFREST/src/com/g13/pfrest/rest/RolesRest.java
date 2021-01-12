package com.g13.pfrest.rest;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.g13.pdt.entidades.Rol;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IRoles;
import com.g13.pfrest.interfaces.IRolesRest;
import com.g13.pfrest.util.UtilidadesRest;

@Stateless
@LocalBean
public class RolesRest implements IRolesRest{
	
	@EJB
    private IRoles us;
	
	public Response altaRol(Rol rol) {

		try {
			return Response.status(Status.CREATED).entity(us.altaRol(rol)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
		
	}

	public Response editarRol(Rol rol) {
		try {
			return Response.status(Status.OK).entity(us.editarRol(rol)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	public Response borrarRol(Integer id) {
		try {
			return Response.status(Status.OK).entity(us.borrarRol(id)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
		
	}

	public Response obtenerListaTodos(){
		return Response.status(Status.OK).entity(us.obtenerListaTodos()).build();
	}
}
