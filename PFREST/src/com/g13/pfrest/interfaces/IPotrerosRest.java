package com.g13.pfrest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.g13.pfrest.datatypes.EdicionPotreros;

@Path("/potrero")
public interface IPotrerosRest {

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response guardarPotreros(@PathParam(value="id") Integer idPredio, EdicionPotreros ep);
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerPotrerosActivosPredio(@PathParam(value="id") Integer id);
	
}