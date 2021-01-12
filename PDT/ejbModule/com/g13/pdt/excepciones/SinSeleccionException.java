package com.g13.pdt.excepciones;

public class SinSeleccionException extends PotrerosException {
	
	private static final long serialVersionUID = 1L;

	public SinSeleccionException (String mensaje, Exception e){
		super(7, mensaje,e);
	}
	public SinSeleccionException (String mensaje){
		super(7, mensaje);
	}

}
