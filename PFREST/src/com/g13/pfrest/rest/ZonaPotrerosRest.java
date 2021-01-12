package com.g13.pfrest.rest;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IZonasPotreros;
import com.g13.pfrest.interfaces.IZonaPotrerosRest;
import com.g13.pfrest.util.UtilidadesRest;

@Stateless
@LocalBean
public class ZonaPotrerosRest implements IZonaPotrerosRest {

	@EJB
	IZonasPotreros zonaPotreroBean;
    /**
     * Default constructor. 
     */
    public ZonaPotrerosRest() {
    }


	@Override
	public Response obtenerZonaPotrerosActivasPredio(Integer id) {
		try {
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(zonaPotreroBean.obtenerListaTodosActivas(id)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}



}
