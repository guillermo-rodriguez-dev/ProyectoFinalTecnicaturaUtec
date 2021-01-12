package managedBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import com.g13.pdt.entidades.Usuario;
import com.g13.pdt.enumerados.TipoDocumento;
import com.g13.pdt.entidades.Rol;
import com.g13.pdt.entidades.TipoZona;
import com.g13.pdt.entidades.Usuario;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IRoles;
import com.g13.pdt.interfaces.IUsuarios;
/**
 * 
 * Esta clase contiene metodos relacionados con la administracion de la entidad usuario
 * 
 * 
 * @author: G13 S.A
 * 
 * @version: 28/06/2020
 * 
 * @see <a href = "www.utec.edu.uy" />
 * 
 */
@ManagedBean(name = "usuarioBean")
@SessionScoped

public class UsuarioManagedbean implements Serializable{
	private long id; // no se debe enviar el id xq genera conflictos en la base de datos
	private String username;
	private String password;

	private Usuario usuarioLogueado;
	private String nombres;
	private String apellidos;
	private TipoDocumento tipoDocumento;
	private String documento;
	private boolean activo;
	private String direccion;
	private String correo;
	private Set<Rol> roles;
	private String message;
	private List<Integer> rolId;
	private Usuario usuarioElegido;
	private List<Usuario> listadoUsuarios;

	private List<Rol> listaTodosLosRoles;
	private List<Usuario> listadoUsuariosFiltrada;

	
	
	public UsuarioManagedbean() {
		
	}
	public List<Rol> getListaTodosLosRoles() {
		return listaTodosLosRoles;
	}

	public void setListaTodosLosRoles(List<Rol> listaTodosLosRoles) {
		this.listaTodosLosRoles = listaTodosLosRoles;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public Usuario getUsuarioElegido() {
		return usuarioElegido;
	}

	public void setUsuarioElegido(Usuario usuarioElegido) {
		this.usuarioElegido = usuarioElegido;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public List<Integer> getRolId() {
		return rolId;
	}

	public void setRolId(List<Integer> rol) {
		this.rolId = rol;
	}

	public List<Usuario> getListadoUsuarios() {
		return listadoUsuarios;
	}

	public void setListadoUsuarios(List<Usuario> listadoUsuarios) {
		this.listadoUsuarios = listadoUsuarios;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Usuario getUsuarioLogueado() {
		return usuarioLogueado;
	}

	public void setUsuarioLogueado(Usuario usuarioLogueado) {
		this.usuarioLogueado = usuarioLogueado;
	}

	public long getID() {
		return id;
	}

	public void setID(long value) {
		this.id = value;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String value) {
		this.username = value;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String value) {
		this.password = value;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String value) {
		this.nombres = value;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String value) {
		this.apellidos = value;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String value) {
		this.documento = value;
	}

	public boolean getActivo() {
		return activo;
	}

	public void setActivo(boolean value) {
		this.activo = value;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String value) {
		this.direccion = value;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String value) {
		this.correo = value;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> value) {
		this.roles = value;
	}

	public List<Usuario> getListadoUsuariosFiltrada() {
		return listadoUsuariosFiltrada;
	}

	public void setListadoUsuariosFiltrada(List<Usuario> listadoUsuariosFiltrada) {
		this.listadoUsuariosFiltrada = listadoUsuariosFiltrada;
	}

	@EJB
	private IUsuarios usuarioEJBBean;

	@EJB
	private IRoles rolesEJBbean;

	
	
	/**
	 * 
	 * Login del usuario a partir de username y contraseña
	 * 
	 */
	public String obtenerUsuario() {
		if (username == "" || password == "") {
			message = "Complete el usuario y la password ";
			return null;
		} else {
			try {
				
				usuarioLogueado = usuarioEJBBean.obtenerUsuario(username, password);
				ExternalContext  s = FacesContext.getCurrentInstance().getExternalContext();
				 s.getSessionMap().put("user", usuarioLogueado);
				return "homePage";// cambiar a main page
			} catch (PotrerosException e) {
				// TODO Auto-generated catch block
				message = e.getMessage();
				e.printStackTrace();
				return null;

			}
		}

	}
	
	
	
	
	
	public String comprobarUsuario() {
		System.out.println("comprobando usuario");
		if(usuarioLogueado == null) {
			System.out.println(false);
			
			return "loginPage.xhtml";
		}
		else {
			return null;
		}

	}
	
	
	
	
	
	
	/**
	 * 
	 * Carga la lista de usuarios y roles 
	 * 
	 */
	public void iniciarPantallaUsuario() {
		obtenerListaUsuarios();
		obtenerRoles();
	}
	/**
	 * 
	 * Obtiene la lista de usuarios
	 * 
	 */
	public List<Usuario> obtenerListaUsuarios() {
		listadoUsuarios = usuarioEJBBean.obtenerListaTodos();

		return listadoUsuarios;
	}
	/**
	 * 
	 * Activa o desactiva un usuario
	 * 
	 */
	public void borrarUsuario(Usuario usuario) {

		try {
			if (usuario.getActivo()) {
				usuarioEJBBean.borrarUsuario(usuario.getId());

			} else {
				usuario.setActivo(true);
				usuarioEJBBean.editarUsuario(usuario);
			}
			obtenerListaUsuarios();

		} catch (PotrerosException e) {
			// TODO Auto-generated catch block
			message = e.getMessage();
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * Abre formulario para crear un usuario
	 * 
	 */
	public void botonCrear() {
		borrarValores();
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('crear').show();");
		current.executeScript("resetearFormulario('crearUsuarioForm');");

	}
	/**
	 * 
	 * Confirma la creacion de un usuario
	 * 
	 */
	public void crearUsuario() {
		Usuario usuario = new Usuario();
		usuario.setActivo(true);
		Set<Rol> rolUsuario = new HashSet<Rol>();

		Rol rolElejido = null;
		try {
			for (Integer integer : rolId) {
				rolElejido = rolesEJBbean.obtenerPorId(integer);
				rolUsuario.add(rolElejido);

			}
		} catch (PotrerosException e1) {
			setMessage(e1.getMessage());

			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		usuario.setRoles(rolUsuario);
		usuario.setCorreo(correo);
		usuario.setDireccion(direccion);
		usuario.setNombres(nombres);
		usuario.setApellidos(apellidos);
		usuario.setDocumento(documento);
		usuario.setPassword(password);
		usuario.setTipoDocumento(tipoDocumento);
		usuario.setUsername(username);
		message = "Usuario Creado Exitosamente";

		try {
			usuarioEJBBean.altaUsuario(usuario);
			obtenerListaUsuarios();

			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('crear').hide();");
		} catch (PotrerosException e) {
			// TODO Auto-generated catch block
			setMessage(e.getMessage());
			e.printStackTrace();
		}

		mostrarMensaje();
	}
	/**
	 * 
	 * Confirma la modificacion de un usuario
	 * 
	 */
	public void modificarUsuario() {
		try {
			usuarioElegido.setActivo(true);
			Set<Rol> rolUsuario = new HashSet<Rol>();

			Rol rolElejido = null;
			try {
				for (Integer integer : rolId) {
					rolElejido = rolesEJBbean.obtenerPorId(integer);
					rolUsuario.add(rolElejido);

				}
			} catch (PotrerosException e1) {
				setMessage(e1.getMessage());

				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			rolUsuario.add(rolElejido);
			usuarioElegido.setRoles(rolUsuario);
			usuarioElegido.setCorreo(correo);
			usuarioElegido.setDireccion(direccion);
			usuarioElegido.setNombres(nombres);
			usuarioElegido.setApellidos(apellidos);
			usuarioElegido.setDocumento(documento);
			usuarioElegido.setPassword(password);
			
			usuarioElegido.setTipoDocumento(tipoDocumento);
			usuarioEJBBean.editarUsuario(usuarioElegido);
			message = "Usuario Modificado Correctamente";
			PrimeFaces current = PrimeFaces.current();

			current.executeScript("PF('modificar').hide();");

		} catch (PotrerosException e) {
			message = e.getMessage();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		mostrarMensaje();
	}
	/**
	 * 
	 * Abre formulario para crear Usuario
	 * 
	 */
	public void iniciarModificacionUsuario(Usuario usuario) {
		usuarioElegido = usuario;

		activo = usuario.getActivo();
		roles = usuario.getRoles();
		correo = usuario.getCorreo();
		direccion = usuario.getDireccion();
		nombres = usuario.getNombres();
		apellidos = usuario.getApellidos();
		documento = usuario.getDocumento();
		username = usuario.getUsername();
		password = usuario.getPassword();
		tipoDocumento = usuario.getTipoDocumento();
		rolId = new ArrayList<Integer>();
		for (Rol rol : usuario.getRoles()) {
			rolId.add(rol.getId());
		}
		System.out.println("cargando");
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('modificar').show();");

	}
	/**
	 * 
	 * Abre dialog con los detalles del usuario seleccionado
	 * 
	 */
	public void verDetalles(Usuario usuario) {
		usuarioElegido = usuario;

		activo = usuario.getActivo();
		roles = usuario.getRoles();
		correo = usuario.getCorreo();
		direccion = usuario.getDireccion();
		nombres = usuario.getNombres();
		apellidos = usuario.getApellidos();
		documento = usuario.getDocumento();
		password = usuario.getPassword();
		tipoDocumento = usuario.getTipoDocumento();
		username = usuario.getUsername();

		System.out.println("cargando");
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('detalles').show();");

	}
	/**
	 * 
	 * Muestra un mensaje de error o exito
	 * 
	 */
	public void mostrarMensaje() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('messegeDiolog').show();");
	}
	/**
	 * 
	 * Oculta un mensaje
	 * 
	 */
	public void ocultarMensaje() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('messegeDiolog').hide();");
	}

	/**
	 * 
	 * Borra los datos de managed bean
	 * 
	 */
	public void borrarValores() {

		usuarioElegido = null;
		usuarioElegido = null;

		activo = null != null;
		roles = null;
		correo = null;
		direccion = null;
		nombres = null;
		apellidos = null;
		documento = null;
		password = null;
		tipoDocumento = null;
		message = "";

	}
	/**
	 * 
	 * Obtiene todos los roles y los carga en el ejb 
	 * 
	 */
	public void obtenerRoles() {
		listaTodosLosRoles = rolesEJBbean.obtenerListaTodos();

	}
	
	
	/**
	 * 
	 * Funcion para buscar dentro de la tabla usuarios 
	 * 
	 */
	

	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		Usuario usuario = (Usuario) value;
		return usuario.getId().toString().toLowerCase().contains(filterText)
				|| usuario.getNombres().toLowerCase().contains(filterText)
				|| usuario.getApellidos().toLowerCase().contains(filterText)
				|| usuario.getDocumento().toLowerCase().contains(filterText)
				|| usuario.getUsername().toLowerCase().contains(filterText)

		;
	}

}
