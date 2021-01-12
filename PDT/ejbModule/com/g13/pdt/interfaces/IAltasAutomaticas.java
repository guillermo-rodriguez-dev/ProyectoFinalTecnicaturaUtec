package com.g13.pdt.interfaces;

	import com.g13.pdt.entidades.Indicador;
import com.g13.pdt.entidades.Usuario;
import com.g13.pdt.excepciones.IndicadorConPotrerosException;
import com.g13.pdt.excepciones.YaExisteElementoException;

	public interface IAltasAutomaticas {
		
		public Usuario crearAdmin() throws YaExisteElementoException;	//aca ya crea los roles
		
		public  Indicador altaAutomaticaIndicadores() throws IndicadorConPotrerosException;
		
		public  void altaAutomaticaTipoZonas();
		
		public void altaAutomaticaPropietarios();

		void altaAutomaticaPredios();

}