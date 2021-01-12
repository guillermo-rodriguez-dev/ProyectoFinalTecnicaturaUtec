package com.g13.pfrest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.g13.pdt.entidades.Propietario;

@Path("/propietarios")
public interface IPropietariosRest {
    
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response altaPropietario(Propietario propietario);
    
    @PUT
    @Path("/")		//Hay que poner TODOS los atributos en el json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response editarPropietario(Propietario propietario);
    
    @PUT
    @Path("desactivar")	//Se puede mandar solo el id en el json objeto propietario
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response desactivarPropietario(Propietario propietario);
    
    @PUT
    @Path("activar")	//Se puede mandar solo el id en el json objeto propietario
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response activarPropietario(Propietario propietario);
	
	@GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
	public Response obtenerListaTodos();
    
}
