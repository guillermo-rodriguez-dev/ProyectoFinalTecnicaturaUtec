package com.g13.pdt.excepciones;

public class DatosInvalidosException extends PotrerosException {
	
	private static final long serialVersionUID = 1L;

	public DatosInvalidosException (String mensaje, Exception e){
		super(1, mensaje,e);
	}
	
	public DatosInvalidosException (String mensaje){
		super(1,mensaje);
	}

}
