package com.g13.pfrest.util;

import javax.ws.rs.core.Response.Status;

import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.ErrorAlAbrirArchivoException;
import com.g13.pdt.excepciones.IndicadorConHijosException;
import com.g13.pdt.excepciones.IndicadorConPotrerosException;
import com.g13.pdt.excepciones.NoAutorizadoException;
import com.g13.pdt.excepciones.NoExisteElementoException;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.excepciones.ProblemaDeConexionException;
import com.g13.pdt.excepciones.SinSeleccionException;
import com.g13.pdt.excepciones.YaExisteElementoException;

public final class UtilidadesRest {

	public static Status obtenerEstadoHttp(PotrerosException e) {
		
		if (e instanceof DatosInvalidosException) {
			return Status.BAD_REQUEST;
		}
		
		if (e instanceof ErrorAlAbrirArchivoException) {
			return Status.BAD_REQUEST;
		}
		if (e instanceof IndicadorConHijosException) {
			return Status.BAD_REQUEST;
		}
		if (e instanceof IndicadorConPotrerosException) {
			return Status.BAD_REQUEST;
		}
		if (e instanceof NoAutorizadoException) {
			return Status.BAD_REQUEST;
		}
		if (e instanceof NoExisteElementoException) {
			return Status.BAD_REQUEST;
		}
		if (e instanceof ProblemaDeConexionException) {
			return Status.BAD_REQUEST;
		}
		if (e instanceof SinSeleccionException) {
			return Status.BAD_REQUEST;
		}
		if (e instanceof YaExisteElementoException) {
			return Status.BAD_REQUEST;
		}
		
		//Si no es niguna de las excepciones personalizadas...
		System.out.println(" AGREGAR " + e.getClass().getSimpleName() + " al obtenerEstadoHttp()");
		return Status.INTERNAL_SERVER_ERROR;
	}
}
