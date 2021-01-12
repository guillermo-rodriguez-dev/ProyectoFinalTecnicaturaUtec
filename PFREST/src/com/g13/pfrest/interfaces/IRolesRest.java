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
import com.g13.pdt.entidades.Rol;


@Path("/roles")
public interface IRolesRest {

	// ABM
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response altaRol(Rol rol);

	@PUT
	@Path("/") // Hay que poner TODOS los atributos en el json
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editarRol(Rol rol);

	@DELETE
	@Path("/{id}") // Se puede mandar solo el id en el json objeto rol
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response borrarRol(@PathParam(value = "id") Integer id);

	// Listados
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerListaTodos();

}
