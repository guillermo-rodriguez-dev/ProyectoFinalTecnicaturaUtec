package managedBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.PrimeFaces;

import com.g13.pdt.entidades.Propietario;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IPropietarios;


/**
 * 
 * Esta clase contiene metodos relacionados con la administracion de propietarios
 * 
 * 
 * @author: G13 S.A
 * 
 * @version: 28/06/2020
 * 
 * @see <a href = "www.utec.edu.uy" />
 * 
 */




@ManagedBean(name = "propietariosBean")
@SessionScoped

public class PropietarioManagedbean {

	private List<Propietario> listaPropietarios;
	private List<Propietario> listaPropietariosFiltrada;
	private String nombre;
	private String razonSocial;
	private String direccion;
	private String correo;
	private String telefono;
	private String contactoReferencia;
	private Boolean activo;
	// private Date desde;
	// private Date hasta;
	private Long rut;

	private String messege;

	private Propietario propietarioElegido;

	public Propietario getPropietarioElegido() {
		return propietarioElegido;
	}

	public void setPropietarioElegido(Propietario propietarioElegido) {
		this.propietarioElegido = propietarioElegido;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getContactoReferencia() {
		return contactoReferencia;
	}

	public void setContactoReferencia(String contactoReferencia) {
		this.contactoReferencia = contactoReferencia;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public Long getRut() {
		return rut;
	}

	public void setRut(Long rut) {
		this.rut = rut;
	}

	public IPropietarios getPropietariosEJBBean() {
		return propietariosEJBBean;
	}

	public void setPropietariosEJBBean(IPropietarios propietariosEJBBean) {
		this.propietariosEJBBean = propietariosEJBBean;
	}

	public List<Propietario> getListaPropietarios() {
		return listaPropietarios;
	}

	public void setListaPropietarios(List<Propietario> listaPropietarios) {
		this.listaPropietarios = listaPropietarios;
	}
	public List<Propietario> getListaPropietariosFiltrada() {
		return listaPropietariosFiltrada;
	}

	public void setListaPropietariosFiltrada(List<Propietario> listaPropietariosFiltrada) {
		this.listaPropietariosFiltrada = listaPropietariosFiltrada;
	}

	@EJB
	private IPropietarios propietariosEJBBean;

	
	
	/**
	 * 
	 * Carga la lista de propietarios
	 * 
	 */
	
	public List<Propietario> obtenerListaPropietarios() {
		listaPropietarios = propietariosEJBBean.obtenerListaTodos();
		
		
		return listaPropietarios;
	}
	/**
	 * 
	 * Activa o desactiva un propietario
	 * 
	 */
	public void borrarPropietario(Propietario propietario) {

		try {
			if(propietario.getActivo()) {
				propietariosEJBBean.desactivarPropietario(propietario);

			}else {
				propietariosEJBBean.activarPropietario(propietario);

			}
			obtenerListaPropietarios();

		} catch (PotrerosException e) {
			// TODO Auto-generated catch block
			messege = e.getMessage();
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * Borra los datos y abre el dialogo crear
	 * 
	 */
	public void botonCrear() {
		borrarValores();
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('crear').show();");
		current.executeScript("resetearFormulario('crearPropietarioForm');");

	}
	
	
	/**
	 * 
	 * Guarda los datos en un propietario
	 * 
	 */
	public void setearDatosPropietario(Propietario propietario) {
		propietario.setActivo(activo);
		propietario.setContactoReferencia(contactoReferencia);
		propietario.setCorreo(correo);
		propietario.setDireccion(direccion);
		propietario.setNombre(nombre);
		propietario.setRut(rut);
		propietario.setRazonSocial(razonSocial);
		propietario.setTelefono(telefono);
	}
	
	
	
	
	
	
	/**
	 * 
	 * Guarda los datos y crea un propietario
	 * 
	 */
	public void crearPropietario() {

		Propietario propietario = new Propietario();
		
		setearDatosPropietario(propietario);
		
		messege = "Propietario Creado Exitosamente";
		PrimeFaces current = PrimeFaces.current();
		
		try {
			propietariosEJBBean.altaPropietario(propietario);
			current.executeScript("PF('crear').hide();");
			obtenerListaPropietarios();
		} catch (PotrerosException e) {
			// TODO Auto-generated catch block
			setMessege(e.getMessage());
			e.printStackTrace();
		}

		mostrarMensaje();
	}
	/**
	 * 
	 * Guarda las modificaciones del propietario
	 * 
	 */
	public void modificarPropietario() {
		try {
			setearDatosPropietario(propietarioElegido);
			propietariosEJBBean.editarPropietario(propietarioElegido);
			messege = "Propietario Modificado Correctamente";
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('modificar').hide();");
		} catch (PotrerosException e) {
			messege = e.getMessage();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		mostrarMensaje();
	}
	/**
	 * 
	 * Carga los datos iniciales del propietario a modificar
	 * 
	 */
	public void iniciarModificacionPropietario(Propietario propietario) {
		cargarDatosPropietario(propietario);


		System.out.println("cargando");
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('modificar').show();");

	}
	/**
	 * 
	 * Carga los datos iniciales del propietario
	 * 
	 */
	public void cargarDatosPropietario(Propietario propietario) {
		propietarioElegido = propietario;

		activo = propietario.getActivo();
		contactoReferencia = propietario.getContactoReferencia();
		correo = propietario.getCorreo();
		direccion = propietario.getDireccion();
		nombre = propietario.getNombre();
		rut = propietario.getRut();
		razonSocial = propietario.getRazonSocial();
		telefono = propietario.getTelefono();
	}
	
	
	
	/**
	 * 
	 * carga los dats y abre un dialog con losdetalles
	 * 
	 */
	public void verDetalles(Propietario propietario) {
		cargarDatosPropietario(propietario);
		System.out.println("cargando");
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('detalles').show();");

	}
	/**
	 * 
	 * Muestra mensaje de exito o error
	 * 
	 */
	public void mostrarMensaje() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('messegeDiolog').show();");
	}
	public void ocultarMensaje() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('messegeDiolog').hide();");
	}
	/**
	 * 
	 * Borra Los valores
	 * 
	 */
	public void borrarValores() {

		propietarioElegido = null;

		activo = null;
		contactoReferencia = null;
		correo = null;
		direccion = null;
		nombre = null;
		rut = null;
		razonSocial = null;
		telefono = null;
		messege = "";

	}

	public String getMessege() {
		return messege;
	}

	public void setMessege(String messege) {
		this.messege = messege;
	}
	
	/**
	 * 
	 * Funcion para hacer busquedas en la tabla propietarios 
	 * 
	 */ 
	 public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
	        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
	        if (filterText == null || filterText.equals("")) {
	            return true;
	        }
	 
	        Propietario propietario = (Propietario) value;
	        return propietario.getId().toString().toLowerCase().contains(filterText)
	                || propietario.getNombre().toLowerCase().contains(filterText)
	            ;
	    }

	
}
