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
import com.g13.pdt.entidades.TipoZona;

@Path("/tiposdezona")
public interface ITiposDeZonaRest {

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response altaTipoZona(TipoZona tz);
    
    @PUT
    @Path("/")		
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editarTipoZona(TipoZona tz);
    
    @DELETE
    @Path("/{id}")	
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response borrarTipoZona(@PathParam(value = "id") int id);
	
	@GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
	public Response obtenerListaTodos();
    
}
