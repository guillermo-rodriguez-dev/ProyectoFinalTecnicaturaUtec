package com.g13.pfrest.rest;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import com.g13.pdt.entidades.TipoZona;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.ITiposZonas;
import com.g13.pfrest.interfaces.ITiposDeZonaRest;
import com.g13.pfrest.util.UtilidadesRest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Stateless
@LocalBean
public class TiposDeZonaRest implements ITiposDeZonaRest {
	
	@EJB
    private ITiposZonas tzs;

	@Override
	public Response altaTipoZona(TipoZona tz) {
		try {
			return Response.status(Status.CREATED).entity(tzs.altaTipoZona(tz)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	@Override
	public Response editarTipoZona(TipoZona tz) {
		try {
			return Response.status(Status.OK).entity(tzs.editarTipoZona(tz)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	@Override
	public Response borrarTipoZona(int id) {
		try {
			return Response.status(Status.OK).entity(tzs.borrarTipoZona(id)).build();
		} catch (PotrerosException e) {
			System.out.println("Error: " + e.getErrorPotrero().getCodigoError() + " Mensaje: " + e.getErrorPotrero().getMensaje());
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	@Override
	public Response obtenerListaTodos() {
		return Response.status(Status.OK).entity(tzs.obtenerListaTodos()).build();
	}


}
