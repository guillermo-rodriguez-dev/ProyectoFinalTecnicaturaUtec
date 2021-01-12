package com.g13.pdt.util;

import java.sql.BatchUpdateException;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.HibernateException;
import org.hibernate.PersistentObjectException;

import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.PotrerosException;

public final class CadenasUtil {

	public CadenasUtil() throws PotrerosException {
		// TODO Auto-generated constructor stub
		throw new PotrerosException(0, "No se puede instanciar la clase CadenasUtil");
	}
	
	public static int getNumeros(String cadena) {
		char[] array_cadena = cadena.toCharArray();
		String resultado = "";
		for (int i = 0; i < array_cadena.length; i++) {
			if (Character.isDigit(array_cadena[i])) {
				//Si es un digito
				resultado = resultado + array_cadena[i];
			}
		}
		if (resultado.equals("")) {
			//Si no hay digitos
			return 0;
		}
		return Integer.parseInt(resultado);
	}
	
	public static String getCabeceraParaError(int codigo) {
		switch (codigo) {
		case 0:
			return "Error desconocido";
		case 1:
			return "Datos invalidos. ";
		case 2:
			return "Indicador con hijos";
		case 3:
			return "Indicador con potreros";
		case 4:
			return "No autorizado";
		case 5:
			return "No existe elemento";
		case 6:
			return "Problema de conexión";
		case 7:
			return "Sin Seleccion";
		case 8:
			return "Ya existe elemento";
		case 9:
			return "Error al abrir el archivo";
		case 10:
			return "Ya existe elemento";
		default:
			return "Error desconocido";
		}
		
	}
	
	public static DatosInvalidosException crearException(ConstraintViolationException e) {
		
		String mensaje = "";
		
		for (ConstraintViolation<?> violacion : e.getConstraintViolations()) {
//			System.out.println("CONSTRAINT getRootBean().getClass().getSimpleName(): " + violacion.getRootBean().getClass().getSimpleName());
//			System.out.println("CONSTRAINT getMessage: " + violacion.getMessage());
//			System.out.println("CONSTRAINT getInvalidValue: " + violacion.getInvalidValue());
//			System.out.println("CONSTRAINT getExecutableParameters: " + violacion.getExecutableParameters());
//			System.out.println("CONSTRAINT getMessageTemplate: " + violacion.getMessageTemplate());
//			System.out.println("CONSTRAINT .getClass().getName(): " + violacion.getClass().getName());
//			System.out.println("CONSTRAINT ..getPropertyPath().toString(): " + violacion.getPropertyPath().toString());
			mensaje = mensaje + " El campo " + violacion.getPropertyPath().toString() + " " + violacion.getInvalidValue() + " no es valido. " + violacion.getMessage() + ".\n" ;
		}
		
		return new DatosInvalidosException(mensaje,e);
	}
	
	public static DatosInvalidosException crearException(NullPointerException e) {
		String mensaje = "No existe el elemento solicitado";
		return new DatosInvalidosException(mensaje,e);
	}
	public static DatosInvalidosException crearException(PersistentObjectException e) {
		String mensaje = "El id ingresado ya existe. El id debe ser vacío para el alta.";
		return new DatosInvalidosException(mensaje,e);
	}
	
	public static DatosInvalidosException crearException(PersistenceException e) {

		Throwable causaRaiz = CadenasUtil.obtenerCausaRaiz(e);

		if (causaRaiz instanceof ConstraintViolationException){
			System.out.println("CausaRaiz es instancia de ConstraintViolationException");
			return CadenasUtil.crearException((ConstraintViolationException)causaRaiz);
		}
		else if (causaRaiz instanceof PersistentObjectException) {
			System.out.println("DIO PersistentObjectException");
			return CadenasUtil.crearException((PersistentObjectException)causaRaiz);
		}
		else if (causaRaiz instanceof BatchUpdateException){
			BatchUpdateException bue = (BatchUpdateException) causaRaiz;
			System.out.println("CausaRaiz es instancia de BatchUpdateException");
			System.out.println("getErrorCode: " + bue.getErrorCode());
			System.out.println("getLocalizedMessage: " + bue.getLocalizedMessage());
			System.out.println("getMessage: " + bue.getMessage());
			System.out.println("getSQLState: " + bue.getSQLState());
			if (bue.getSQLState().equals("23000")) {
				int codigoError = bue.getErrorCode();
				switch (codigoError) {
					case 1:
						//getMessagge: ORA-00001: restricción única (POTREROS.UK_PROPIETARIO_RUT) violada
						return new DatosInvalidosException("El campo " + CadenasUtil.obtenerNombreCampoRepetido(bue.getMessage()) + " no se puede repetir");
					case 2292:
						return new DatosInvalidosException("La entidad aún es referenciada desde " + CadenasUtil.obtenerNombreCampoFkViolada(bue.getMessage()));
					case 1400:
						System.out.println("ERROR: " + bue.getMessage());
						return new DatosInvalidosException("El campo " + CadenasUtil.obtenerNombreNullViolado(bue.getMessage()) + " no puede ser nulo.");
				}
				System.out.println("No se ha considerado el codigoError: " + codigoError + " POR FAVOR CORREGIR");
			}else {
				System.out.println("FALTA Agregar el sqlState " + bue.getSQLState());
			}
			return new DatosInvalidosException("CORREGIR mensaje para getSQLState: " + bue.getSQLState() + " y codigo error " + bue.getErrorCode());
		} else if (causaRaiz instanceof HibernateException) {
			HibernateException bue = (HibernateException) causaRaiz;
			System.out.println("CausaRaiz es instancia de BatchUpdateException");
			System.out.println("getLocalizedMessage: " + bue.getLocalizedMessage());
			System.out.println("getMessage: " + bue.getMessage());
			return new DatosInvalidosException("HibernateException: No estas programando bien");
		}
		System.out.println("CONSIDERAR Causa raiz " + causaRaiz.getClass().getSimpleName() + " no ha sido considerada. Favor informar");
		return new DatosInvalidosException("CONSIDERAR Causa raíz " + causaRaiz.getClass().getSimpleName() + " no ha sido considerada. Favor informar",e);
		
	}
	
	public static String obtenerNombreCampoRepetido(String mensaje) {
		/**
		 * UK_INDICADOR_NOMBRE
		 * UK_POTRERO_INDICADOR
		 * UK_POTREROS_NOMBRE
		 * UK_PREDIO_NOMBRE
		 * UK_PROPIETARIO_NOMBRE
		 * UK_PROPIETARIO_RUT
		 * UK_ROL_NOMBRE
		 * UK_TIPOZONA_NOMBRE
		 * UK_USUARIO_DOCUMENTO
		 * UK_USUARIO_CORREO
		 * UK_ZONAS_NOMBRE
		 * UK_POTRERO_ZONA
		 * 
		 */
		if (mensaje.contains("POTREROS.UK_INDICADOR_NOMBRE")) {
			return "nombre del indicador";
		}
		if (mensaje.contains("POTREROS.UK_POTRERO_INDICADOR")) {
			return "indicador del potrero";
		}
		if (mensaje.contains("POTREROS.UK_POTREROS_NOMBRE")) {
			return "nombre del potrero";
		}
		if (mensaje.contains("POTREROS.UK_PREDIO_NOMBRE")) {
			return "nombre del predio";
		}
		if (mensaje.contains("POTREROS.UK_PROPIETARIO_NOMBRE")) {
			return "nombre del propietario";
		}
		if (mensaje.contains("POTREROS.UK_PROPIETARIO_RUT")) {
			return "rut del propietario";
		}
		if (mensaje.contains("POTREROS.UK_ROL_NOMBRE")) {
			return "nombre del rol";
		}
		if (mensaje.contains("POTREROS.UK_TIPOZONA_NOMBRE")) {
			return "nombre del tipo de zona";
		}
		if (mensaje.contains("POTREROS.UK_USUARIO_DOCUMENTO")) {
			return "documento del usuario";
		}
		if (mensaje.contains("POTREROS.UK_USUARIO_CORREO")) {
			return "correo del usuario";
		}
		if (mensaje.contains("POTREROS.UK_USUARIO_USERNAME")) {
			return "username del usuario";
		}
		if (mensaje.contains("POTREROS.UK_ZONAS_NOMBRE")) {
			return "nombre de zona";
		}
		
		if (mensaje.contains("POTREROS.UK_POTRERO_ZONA")) {
			return "zona del potrero";
		}
		if (mensaje.contains("POTREROS.UK_INDICADORPOTRERO_POTRERO_INDICADOR")) {
			return "No se puede cambiar un potrero al mismo indicador";
		}
		return "NO SE CUAL ES. FAVOR INFORMAR PARA CORREGIR";
	}
	
	public static String obtenerNombreCampoFkViolada(String mensaje) {
		/**
		 * FK_INDICADOR_PADRE
		 * FK_INDICADORPOTRERO_POTRERO
		 * FK_INDICADORPOTRERO_INDICADOR
		 * FK_POTRERO_PREDIO
		 * FK_PROPIETARIOSPREDIO_PRED
		 * FK_PROPIETARIOSPREDIO_PROP
		 * 
		 * FK_ROLESUSUARIO_USUARIO
		 * FK_ROLESUSUARIO_ROL
		 * 
		 * FK_ZONAGEOGRAFICA_PREDIO
		 * FK_ZONAGEOGRAFICA_TIPOZONA
		 * FK_ZONAPOTRERO_ZONAGEOGRAFICA
		 * FK_ZONAPOTRERO_POTRERO
		 * 
		 */
		if (mensaje.contains("FK_INDICADOR_PADRE")) {
			return "Indicador";
		}
		if (mensaje.contains("FK_INDICADORPOTRERO_POTRERO")) {
			return "Indicador potrero";
		}
		if (mensaje.contains("FK_INDICADORPOTRERO_INDICADOR")) {
			return "Indicador potrero";
		}
		if (mensaje.contains("FK_POTRERO_PREDIO")) {
			return "Potrero";
		}
		if (mensaje.contains("FK_PROPIETARIOSPREDIO_PRED")) {
			return "Propietarios";
		}
		if (mensaje.contains("FK_PROPIETARIOSPREDIO_PROP")) {
			return "Predio";
		}
		if (mensaje.contains("FK_ROLESUSUARIO_USUARIO")) {
			return "Roles";
		}
		if (mensaje.contains("FK_ROLESUSUARIO_ROL")) {
			return "Usuarios";
		}
		if (mensaje.contains("FK_ZONAGEOGRAFICA_PREDIO")) {
			return "Zona Geografica";
		}
		if (mensaje.contains("FK_ZONAGEOGRAFICA_TIPOZONA")) {
			return "Zona Geografica";
		}
		if (mensaje.contains("FK_ZONAPOTRERO_ZONAGEOGRAFICA")) {
			return "Zona Potrero";
		}
		if (mensaje.contains("FK_ZONAPOTRERO_POTRERO")) {
			return "Zona Potrero";
		}
		return "NO SE SUPO DIFERENCIA LA FK: " + mensaje + ". FAVOR INFORMAR";
	}
	public static String obtenerNombreNullViolado(String mensaje) {
		int largo = mensaje.length()-2;
		int desde = 56;
		if (largo<desde) {
			return "Largo: " + largo + ". NO SE SUPO DIFERENCIAEL CAMPO VACIO: " + mensaje + ". FAVOR INFORMAR";
		}
		return mensaje.substring(desde,largo);
		//return "NO SE SUPO DIFERENCIAEL CAMPO VACIO: " + mensaje + ". FAVOR INFORMAR";
	}

	public static Throwable obtenerCausaRaiz(Throwable throwable) {
		//Agradecimientos a Gerardo que dio la clase de repaso y le copiamos este método
	    if (throwable==null){
	    	return null;
	    }
	    Throwable rootCause = throwable;
	    while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
	        rootCause = rootCause.getCause();
	    }
	    return rootCause;
	}

}
