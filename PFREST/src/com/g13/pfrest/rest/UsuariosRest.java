package com.g13.pfrest.rest;

import com.g13.pdt.entidades.Usuario;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IUsuarios;
import com.g13.pfrest.interfaces.IUsuariosRest;
import com.g13.pfrest.util.UtilidadesRest;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Session Bean implementation class UsuarioBean
 */
@Stateless
@LocalBean
public class UsuariosRest implements IUsuariosRest {


	@EJB(lookup = "java:app/PDT/UsuariosBeanRemote!com.g13.pdt.interfaces.IUsuarios")
	private IUsuarios usuarioBean;

	
	public String ping() {
		return "pong";
	}

	public Response obtenerUsuario(String username, String password) {
		try {
			return Response.status(Status.OK).entity(usuarioBean.obtenerUsuario(username, password)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}
	

	public Response obtenerTodosLosUsuarios() {
		return Response.status(Status.OK).entity(usuarioBean.obtenerListaTodos()).build();
	}

	public Response crearUsuario(Usuario user) {
		try {
			return Response.status(Status.CREATED).entity(usuarioBean.altaUsuario(user)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	public Response editarUsuario(Usuario user) {
		try {
			return Response.status(Status.OK).entity(usuarioBean.editarUsuario(user)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	public Response bajarUsuario(Integer id) {
		try {
			return Response.status(Status.OK).entity(usuarioBean.borrarUsuario(id)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}
	
	
}
