package com.g13.pdt.excepciones;

public class IndicadorConPotrerosException extends PotrerosException {
	
	private static final long serialVersionUID = 1L;

	public IndicadorConPotrerosException (String mensaje, Exception e){
		super(3,mensaje,e);
	}
	
	public IndicadorConPotrerosException (String mensaje){
		super(3,mensaje);
	}

}
