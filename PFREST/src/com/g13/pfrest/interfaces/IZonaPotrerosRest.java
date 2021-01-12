package com.g13.pfrest.interfaces;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/zonapotrero")
public interface IZonaPotrerosRest {
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerZonaPotrerosActivasPredio(@PathParam(value="id") Integer id);

	
}
