package com.g13.pdt.entidades;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.g13.pdt.enumerados.TipoDocumento;

@Entity
@Table(name="USUARIOS", 
	uniqueConstraints = {
		//Aqui podemos personalizar los UNIQUE que hagan falta
		@UniqueConstraint(name = "UK_USUARIO_DOCUMENTO", columnNames = {"TIPO_DOCUMENTO","DOCUMENTO"}),
		@UniqueConstraint(name = "UK_USUARIO_CORREO", columnNames = {"CORREO"}),
		@UniqueConstraint(name = "UK_USUARIO_USERNAME", columnNames = {"USERNAME"}),
	},
	indexes = {
		//Aqui podemos definir los �ndices que nos hagan falta, los de las FK no se crean autom�ticamente.
//		@Index(name = "IDX_ALGO",  columnList="COLUMNA"),
	}
)
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIOSG")
    @SequenceGenerator(sequenceName = "USUARIOS_SEQ", allocationSize = 1, name = "USUARIOSG")
	@Column(name="ID_USUARIO")
	private Integer id;
	
	@Size(min = 1, max = 40)
	@NotNull
	@Column(name="USERNAME", length = 40, nullable = false)
	private String username;
	
	@Size(min = 1, max = 120)
	@NotNull
	@Column(name="PASSWORD", length = 120, nullable = false)
	private String password;
	
	@Size(min = 1, max = 40)
	@NotNull
	@Column(name="NOMBRES", length = 40, nullable = false)
	private String nombres;
	
	@Size(min = 1, max = 40)
	@NotNull
	@Column(name="APELLIDOS", length = 40, nullable = false)
	private String apellidos;
	
	@Enumerated
	@Column(name = "TIPO_DOCUMENTO", nullable = false)
	private TipoDocumento tipoDocumento;
	
	@Size(min = 1, max = 40)
	@NotNull
	@Column(name="DOCUMENTO", length = 40, nullable = false)
	private String documento;
	
	@Column(name="ACTIVO", length = 40, nullable = false)
	private Boolean activo;
	
	@Size(min = 1, max = 100)
	@NotNull
	@Column(name="DIRECCION", length = 100, nullable = false)
	private String direccion;
	
	@Size(min = 1, max = 50)
	@NotNull
	@Email
	@Column(name="CORREO", length = 50, nullable = false)
	private String correo;
	
	@ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinTable(
		name="ROLESUSUARIO",
		joinColumns = {
			@JoinColumn(name="ID_USUARIO", referencedColumnName="ID_USUARIO",foreignKey = @ForeignKey(name = "FK_ROLESUSUARIO_USUARIO"))
		},
		inverseJoinColumns = {
			@JoinColumn(name="ID_ROL", referencedColumnName="ID_ROL",foreignKey = @ForeignKey(name = "FK_ROLESUSUARIO_ROL"))
		}
	)
	private Set<Rol> roles;
	
	
	
	public Usuario() {
		super();
		this.roles = new HashSet<Rol>();
		// TODO Auto-generated constructor stub
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}

	public Integer getId() {
		return id;
	}
	
	
	
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public void addRol(Rol rol) {
		if (!this.roles.contains(rol)) {
			this.roles.add(rol);
		}
	}
	
	public void removeRol(Rol rol) {
		if (this.roles.contains(rol)) {
			this.roles.remove(rol);
		}	

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public boolean tieneRol(String nombre) {
		if (this.username.equals("admin")) {
			//admin tiene todos los roles asegurados sin importar nada
			return true;
		}
		
		for (Rol rol : this.roles) {
			if (rol.getNombre().equals(nombre)) {
				return true;
			}
		}
		return false;
	}
	

	
	
	
}