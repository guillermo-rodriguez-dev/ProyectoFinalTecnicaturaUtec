package com.g13.pdt.excepciones;

public class NoAutorizadoException extends PotrerosException {
	
	private static final long serialVersionUID = 1L;

	public NoAutorizadoException (String mensaje, Exception e){
		super(4,mensaje,e);
	}
	
	public NoAutorizadoException (String mensaje){
		super(4,mensaje);
	}

}
