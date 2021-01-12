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
import com.g13.pdt.entidades.Usuario;


@Path("/usuario")
public interface IUsuariosRest {

	@GET
	@Path("/ping")
	public String ping();
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response bajarUsuario(@PathParam(value = "id") Integer id);
	
	@POST
	@Path("/obtenerUsuario/{username}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerUsuario(@PathParam(value = "username") String username, @PathParam(value = "password") String password);
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response crearUsuario(Usuario user);
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerTodosLosUsuarios();
	
	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editarUsuario(Usuario user);
	

}
