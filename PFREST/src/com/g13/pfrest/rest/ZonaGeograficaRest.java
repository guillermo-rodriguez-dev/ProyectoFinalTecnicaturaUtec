package com.g13.pfrest.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.g13.pdt.entidades.Potrero;
import com.g13.pdt.entidades.Predio;
import com.g13.pdt.entidades.ZonaGeografica;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IPredios;
import com.g13.pdt.interfaces.IZonasGeograficas;
import com.g13.pfrest.datatypes.EdicionZonaGeograficas;
import com.g13.pfrest.interfaces.IZonaGeograficasRest;
import com.g13.pfrest.util.UtilidadesRest;


@Stateless
@LocalBean
@RequestScoped
public class ZonaGeograficaRest implements IZonaGeograficasRest {

    @EJB
	private IZonasGeograficas zonaGeograficasBean;
    
    @EJB
	private IPredios prediosBean;
	
	public Response obtenerZonasGeograficasActivasPredio(Integer id) {
		try {
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(zonaGeograficasBean.obtenerListaTodosActivas(id)).build();
		} catch (PotrerosException e) {
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
	}

	@Override
	public Response guardarZonasGeograficas(Integer idPredio, EdicionZonaGeograficas ez) {
		
		try {
			Predio predio = prediosBean.obtenerPorId(idPredio);
			zonaGeograficasBean.setPredio(idPredio);
			System.out.println("Zonas a desactivar: ");
			for (ZonaGeografica zona : ez.getZonasGeograficasDesactivar()) {
				System.out.println(zona);
				ZonaGeografica zgbd = zonaGeograficasBean.obtenerPorId(zona.getId());
				zgbd.copiarDe(zona);
				if (!zonaGeograficasBean.borrarZonaGeografica(zgbd)) {
						throw new DatosInvalidosException("No se pudo borrar la zona: " + zona.getNombre());
				}
			}
			
			System.out.println("Zonas a editar: ");
			for (ZonaGeografica zona : ez.getZonasGeograficasExistentes()) {
				System.out.println(zona);
				zona.setPredio(predio);
				zonaGeograficasBean.editarZonaGeografica(zona);
			}
			
			System.out.println("Zonas nuevas: ");
			for (ZonaGeografica zona : ez.getZonasGeograficasNuevas()) {
				System.out.println(zona);
				zona.setPredio(predio);
				zonaGeograficasBean.altaZonaGeografica(zona);
			}
			zonaGeograficasBean.guardarTodo();
			List<ZonaGeografica> lista = zonaGeograficasBean.obtenerListaTodosActivas(idPredio);
			//zonaGeograficasBean.finalizarBean();
			zonaGeograficasBean.Inicializar();
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(lista).build();
		} catch (PotrerosException e) {
//			zonaGeograficasBean.finalizarBean();
			zonaGeograficasBean.Inicializar();
			return Response.status(UtilidadesRest.obtenerEstadoHttp(e)).entity(e.getErrorPotrero()).build();
		}
		
		
	}

}
