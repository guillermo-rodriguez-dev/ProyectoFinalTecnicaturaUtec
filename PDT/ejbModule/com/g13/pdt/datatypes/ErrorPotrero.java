package com.g13.pdt.datatypes;

import java.io.Serializable;

public class ErrorPotrero implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Contiene el código de error del negocio
	 */
	private int codigoError=0;
	/**
	 * Contiene el título que se debe mostrar al usuario
	 */
	private String titulo="Ha ocurrido un error";
	/**
	 * Contiene la descripción general que se debe mostrar al usuario
	 */
	private String cabecera = "No sabemos que ha pasado";
	
	/**
	 * Contiene la descripción general que se debe mostrar al usuario
	 */
	private String mensaje = "No sabemos que ha pasado";
	
	
	
	public ErrorPotrero(int codigoError, String titulo, String cabecera, String mensaje) {
		super();
		this.codigoError = codigoError;
		this.titulo = titulo;
		this.cabecera = cabecera;
		this.mensaje = mensaje;
	}
	
	public int getCodigoError() {
		return codigoError;
	}
	public void setCodigoError(int codigoError) {
		this.codigoError = codigoError;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getCabecera() {
		return cabecera;
	}
	public void setCabecera(String cabecera) {
		this.cabecera = cabecera;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
	
}
