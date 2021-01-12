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
import com.g13.pdt.entidades.Indicador;


@Path("/indicadores")
public interface IIndicadoresRest {

	@POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response altaIndicador(Indicador indicador);
	
	@PUT
    @Path("/")		
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response editarIndicador(Indicador indicador);
	
	@DELETE
    @Path("/{id}")	
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response borrarIndicador(@PathParam(value = "id") int id);
	
	@GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
	public Response obtenerListaTodos();
	
	@GET
    @Path("obtenerSinHijos")
    @Produces(MediaType.APPLICATION_JSON)
	public Response obtenerListaSinHijosActivos();
	
	@GET
    @Path("obtenerpornombre/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
	public Response obtenerIndicador(@PathParam(value = "nombre") String nombre);
	
	@GET
    @Path("obtenerpornombreypredio/{nombre}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	public Response obtenerIndicadorParaPredio(@PathParam(value = "nombre") String nombre, @PathParam(value = "id") int id);
    
}
