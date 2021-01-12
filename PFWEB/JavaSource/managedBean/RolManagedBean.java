package managedBean;

import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.PrimeFaces;

import com.g13.pdt.entidades.Rol;
import com.g13.pdt.entidades.Rol;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IRoles;
/**
 * 
 * Esta clase contiene metodos relacionados con la administracion roles
 * 
 * 
 * @author: G13 S.A
 * 
 * @version: 28/06/2020
 * 
 * @see <a href = "www.utec.edu.uy" />
 * 
 */

@ManagedBean(name = "rolesBean")
@SessionScoped
public class RolManagedBean {

	private List<Rol> listaRol;
	private List<Rol> listaRolFiltrada;

	private String nombre;
	private Rol rol;
	private Rol rolElegido;
	private String messege;
	public Rol getRol() {
		return rol;
	}
	public void setRol(Rol rol) {
		this.rol = rol;
	}
	@EJB
	private IRoles rolesEJBbean;
	public void obtenerRoles() {
		setListaRol(rolesEJBbean.obtenerListaTodos());

	}
	public List<Rol> getListaRol() {
		return listaRol;
	}
	public void setListaRol(List<Rol> listaRol) {
		this.listaRol = listaRol;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getMessege() {
		return messege;
	}
	public void setMessege(String messege) {
		this.messege = messege;
	}
	public Rol getRolElegido() {
		return rolElegido;
	}
	public void setRolElegido(Rol rolElegido) {
		this.rolElegido = rolElegido;
	}
	public List<Rol> getListaRolFiltrada() {
		return listaRolFiltrada;
	}
	public void setListaRolFiltrada(List<Rol> listaRolFiltrada) {
		this.listaRolFiltrada = listaRolFiltrada;
	}

	
	/**
	 * 
	 * Este metodo borra el rol pasado
	 * 
	 */

	
	public void borrarRol(Rol rol) {

		try {
			rolesEJBbean.borrarRol(rol.getId());
			obtenerRoles();
		} catch (PotrerosException e) {
			// TODO Auto-generated catch block
			setMessege(e.getMessage());
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 * Este metodo contiene el comportamiento del metodo crear
	 * 
	 */


	public void botonCrear() {
		borrarValores();
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('crear').show();");
		current.executeScript("resetearFormulario('crearRolForm');");

	}
	
	
	/**
	 * 
	 * Confirma la creacion del rol 
	 * 
	 */


	public void crearRol() {
		Rol rol = new Rol();

		rol.setNombre(nombre);

		setMessege("Rol Creado Exitosamente");

		try {
			rolesEJBbean.altaRol(rol);

		} catch (PotrerosException e) {
			// TODO Auto-generated catch block
			setMessege(e.getMessage());
			e.printStackTrace();
		}
		 obtenerRoles();
		mostrarMensaje();
	}
	
	/**
	 * 
	 * Condirma la modificacion del rol 
	 * 
	 */


	public void modificarRol() {
		try {


			rolElegido.setNombre(nombre);

			rolesEJBbean.editarRol(rolElegido);
			setMessege("Rol Modificado Correctamente");
		} catch (PotrerosException e) {
			setMessege(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 obtenerRoles();
		mostrarMensaje();
	}
	
	
	/**
	 * 
	 * Abre el dialog modificar rol
	 * 
	 */


	public void iniciarModificacionRol(Rol rol) {
		borrarValores();
		rolElegido = rol;


		nombre = rol.getNombre();

		System.out.println("cargando");
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('modificar').show();");

	}

	/**
	 * 
	 * Funcion para buscar dentro de la tabla rol
	 * 
	 */
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		Rol rol = (Rol) value;
		return rol.getId().toString().toLowerCase().contains(filterText)
				|| rol.getNombre().toLowerCase().contains(filterText);
	}
	/**
	 * 
	 * Abre el dialog ya sea exio o error
	 * 
	 */

	public void mostrarMensaje() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('messegeDiolog').show();");
	}
	/**
	 * 
	 * Borra los valores del managed ejb 
	 * 
	 */

	public void borrarValores() {

		rolElegido = null;
		nombre = null;

		setMessege("");

	}
	
	
}
