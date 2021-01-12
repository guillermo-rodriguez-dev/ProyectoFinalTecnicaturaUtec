

package com.g13.pfrest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.g13.pdt.entidades.Predio;
import com.vividsolutions.jts.geom.Polygon;

@Path("/predio")

public interface IPrediosRest {

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Polygon ping();
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response crearPredio(Predio predio);
	
	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editarPredio(Predio predio);
	
	
	@DELETE
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response bajarPredio(Predio predio);
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerTodosLosPredios();
	
	@PUT
	@Path("/activarPredio")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response activarPredio(Predio predio);
	
	
	@GET
	@Path("/existeNombrePredio/{nombre}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response existeNombre(@PathParam(value = "nombre") String nombre);
	
	@GET
	@Path("/obtenerporid/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerPorId(@PathParam(value = "id") Integer id);
	
	
}
