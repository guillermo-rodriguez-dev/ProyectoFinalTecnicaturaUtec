package com.g13.pfrest.datatypes;

import java.util.List;

import com.g13.pdt.entidades.Potrero;
import com.g13.pdt.entidades.ZonaGeografica;

public class EdicionPotreros {
	
	List<Potrero> potrerosExistentes;
	List<Potrero> potrerosDesactivar;
	List<Potrero> potrerosNuevas;
	
	public List<Potrero> getPotrerosExistentes() {
		return potrerosExistentes;
	}
	public void setPotrerosExistentes(List<Potrero> potrerosExistentes) {
		this.potrerosExistentes = potrerosExistentes;
	}
	public List<Potrero> getPotrerosDesactivar() {
		return potrerosDesactivar;
	}
	public void setPotrerosDesactivar(List<Potrero> potrerosDesactivar) {
		this.potrerosDesactivar = potrerosDesactivar;
	}
	public List<Potrero> getPotrerosNuevas() {
		return potrerosNuevas;
	}
	public void setPotrerosNuevas(List<Potrero> potrerosNuevas) {
		this.potrerosNuevas = potrerosNuevas;
	}
	
	
	
	
}
