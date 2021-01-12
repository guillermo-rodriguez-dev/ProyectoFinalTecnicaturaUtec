package com.g13.pdt.excepciones;

public class NoExisteElementoException extends PotrerosException {
	
	private static final long serialVersionUID = 1L;

	public NoExisteElementoException (String mensaje){
		super(5,mensaje);
	}
	
	public NoExisteElementoException (String mensaje, Exception e){
		super(5,mensaje,e);
	}

}
