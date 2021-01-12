package com.g13.pdt.excepciones;

public class YaExisteElementoException extends PotrerosException {
	
	private static final long serialVersionUID = 1L;

	public YaExisteElementoException (String mensaje, Exception e){
		super(8,mensaje,e);
	}
	public YaExisteElementoException (String mensaje){
		super(8,mensaje);
	}

}
