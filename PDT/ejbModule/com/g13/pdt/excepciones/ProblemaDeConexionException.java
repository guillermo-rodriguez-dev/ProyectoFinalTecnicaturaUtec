package com.g13.pdt.excepciones;

public class ProblemaDeConexionException extends PotrerosException {
	
	private static final long serialVersionUID = 1L;

	public ProblemaDeConexionException (String mensaje, Exception e){
		super(6, mensaje , e);
	}
	
	public ProblemaDeConexionException (String mensaje){
		super(6, mensaje);
	}

}
