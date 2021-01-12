package com.g13.pfrest.datatypes;

import java.util.List;

import com.g13.pdt.entidades.Potrero;
import com.g13.pdt.entidades.ZonaGeografica;

public class EdicionZonaGeograficas {
	
	List<ZonaGeografica> zonasGeograficasExistentes;
	List<ZonaGeografica> zonasGeograficasDesactivar;
	List<ZonaGeografica> zonasGeograficasNuevas;
	
	public List<ZonaGeografica> getZonasGeograficasExistentes() {
		return zonasGeograficasExistentes;
	}
	public void setZonasGeograficasExistentes(List<ZonaGeografica> zonasGeograficasExistentes) {
		this.zonasGeograficasExistentes = zonasGeograficasExistentes;
	}
	public List<ZonaGeografica> getZonasGeograficasDesactivar() {
		return zonasGeograficasDesactivar;
	}
	public void setZonasGeograficasDesactivar(List<ZonaGeografica> zonasGeograficasDesactivar) {
		this.zonasGeograficasDesactivar = zonasGeograficasDesactivar;
	}
	public List<ZonaGeografica> getZonasGeograficasNuevas() {
		return zonasGeograficasNuevas;
	}
	public void setZonasGeograficasNuevas(List<ZonaGeografica> zonasGeograficasNuevas) {
		this.zonasGeograficasNuevas = zonasGeograficasNuevas;
	}
	
}
