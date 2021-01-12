package com.g13.pdt.excepciones;

public class IndicadorConHijosException extends PotrerosException {
	
	private static final long serialVersionUID = 1L;

	public IndicadorConHijosException (String mensaje, Exception e){
		super(2,mensaje,e);
	}
	
	public IndicadorConHijosException (String mensaje){
		super(2,mensaje);
	}

}
