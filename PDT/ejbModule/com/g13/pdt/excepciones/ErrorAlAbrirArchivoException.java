package com.g13.pdt.excepciones;

public class ErrorAlAbrirArchivoException extends PotrerosException {
	
	private static final long serialVersionUID = 1L;

	public ErrorAlAbrirArchivoException (String mensaje, Exception e){
		super(9, mensaje,e);
	}
	public ErrorAlAbrirArchivoException (String mensaje){
		super(9, mensaje);
	}

}
