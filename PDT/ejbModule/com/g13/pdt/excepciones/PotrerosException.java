package com.g13.pdt.excepciones;

import com.g13.pdt.datatypes.ErrorPotrero;
import com.g13.pdt.util.CadenasUtil;

/**
 * Excepción general para el negocio.
 * 
 */
public class PotrerosException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private ErrorPotrero errorPotrero;

	/**
	 * 
	 * @param codigoError	contiene el código de error del negocio
	 * @param titulo		título a usar cuando se muestre la excepción
	 * @param cabecera		Descripción general del error
	 * @param mensaje		mensaje detallado de la excepcion para el constructor de Exception y para mostrar
	 */
	public PotrerosException (int codigoError, String mensaje){
		super(mensaje);
		this.errorPotrero = new ErrorPotrero(codigoError, "Ha ocurrido un error", CadenasUtil.getCabeceraParaError(codigoError),mensaje);
	}
	
	/**
	 * @param codigoError	contiene el código de error del negocio
	 * @param titulo		título a usar cuando se muestre la excepción
	 * @param cabecera		Descripción general del error
	 * @param mensaje		mensaje detallado de la excepcion para el constructor de Exception y para mostrar
	 * @param e				Excepción que produjo el error
	 */
	public PotrerosException (int codigoError, String mensaje, Throwable e){
		super(mensaje,e);
		this.errorPotrero = new ErrorPotrero(codigoError, "Ha ocurrido un error", CadenasUtil.getCabeceraParaError(codigoError),mensaje);
	}

	public ErrorPotrero getErrorPotrero() {
		return errorPotrero;
	}

	public void setErrorPotrero(ErrorPotrero errorPotrero) {
		this.errorPotrero = errorPotrero;
	}

	public String getCabecera() {
		return this.errorPotrero.getCabecera();
	}
	
	public int getCodigoError() {
		return this.errorPotrero.getCodigoError();
	}
		
	public String getTitulo() {
		return this.errorPotrero.getTitulo();
	}

}
