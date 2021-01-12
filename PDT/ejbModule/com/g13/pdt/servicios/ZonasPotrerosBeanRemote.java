package com.g13.pdt.servicios;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import com.g13.pdt.entidades.Predio;
import com.g13.pdt.entidades.ZonaPotrero;
import com.g13.pdt.excepciones.NoExisteElementoException;
import com.g13.pdt.interfaces.IZonasPotreros;

/**
 * Session Bean implementation class ZonaPotrerosBeanRemote
 */
@Stateless
@LocalBean
public class ZonasPotrerosBeanRemote implements IZonasPotreros {

	@PersistenceContext
	private EntityManager em;
	
    public ZonasPotrerosBeanRemote() {
        
    }

	@Override
	public List<ZonaPotrero> obtenerListaTodosActivas(int idPredio) throws NoExisteElementoException {
		//Se pasa solo el id, porque de cualquier forma hay que buscar el predio
		Predio predio = this.em.find(Predio.class, idPredio);
		if (predio == null) {
			throw new NoExisteElementoException("No existe un Predio con id " + idPredio);
		}
		// Aca se obtienen todas las ZonaGeografica activas del predio
		TypedQuery<ZonaPotrero> query = this.em.createQuery("SELECT z FROM ZonaPotrero z JOIN z.potrero p where z.activo=:activo and p.predio=:predio",ZonaPotrero.class);
		query.setParameter("predio", predio);
		query.setParameter("activo", true);
		List<ZonaPotrero> resultado = query.getResultList();

		return resultado;
	}

	@Override
	public ZonaPotrero obtenerPorId(Integer id) throws NoExisteElementoException {
		ZonaPotrero entidadBD = this.em.find(ZonaPotrero.class, id);
		if (entidadBD == null) {
			throw new NoExisteElementoException("No existe una Zona Potrero con id " + id);
		}
		return entidadBD;
	}

}
