package com.g13.pdt.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="PROPIETARIOS", 
	uniqueConstraints = {
		//Aqui podemos personalizar los UNIQUE que hagan falta
		@UniqueConstraint(name = "UK_PROPIETARIO_RUT", columnNames = {"RUT"}),
		@UniqueConstraint(name = "UK_PROPIETARIO_NOMBRE", columnNames = {"NOMBRE"}),
	},
	indexes = {
		//Aqui podemos definir los �ndices que nos hagan falta, los de las FK no se crean autom�ticamente.
//		@Index(name = "IDX_ALGO",  columnList="COLUMNA"),
	}
)
public class Propietario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPIETARIOSG")
    @SequenceGenerator(sequenceName = "PROPIETARIOS_SEQ", allocationSize = 1, name = "PROPIETARIOSG")
	@Column(name="ID_PROPIETARIO")
	private Integer id;
	
	@Size(min = 1, max = 60)
	@NotNull
	@Column (name="NOMBRE", length = 60, nullable = false)
	private String nombre;
	
	@Size(min = 1, max = 60)
	@NotNull
	@Column (name="RAZON_SOCIAL", length = 60, nullable = false)
	private String razonSocial;
	
	@Size(min = 1, max = 60)
	@NotNull
	@Column (name="DIRECCION", length = 60, nullable = false)
	private String direccion;
	
	@Size(min = 1, max = 60)
	@NotNull
	@Email
	@Column (name="CORREO", length = 60, nullable = false)
	private String correo;
	
	@Size(min = 1, max = 60)
	@NotNull
	@Column (name="TELEFONO", length = 60, nullable = false)
	private String telefono;
	
	@Size(min = 1, max = 60)
	@NotNull
	@Column (name="CONTACTO_REFERENCIA", length = 60, nullable = false)
	private String contactoReferencia;
	
	@Column(name = "ACTIVO", nullable = false)
	private Boolean activo;
	
	@Column(name="DESDE", nullable = false)
	private Date desde;
	
	@Column(name="HASTA",nullable = true)
	private Date hasta;
	
	@NotNull
	@Positive
	@Column(name="RUT", nullable = false)
	private Long rut;

	@JsonIgnore
	@ManyToMany(mappedBy = "propietarios", fetch = FetchType.LAZY)
	private Set<Predio> predios;
	
	public Propietario() {
		super();
		this.predios = new HashSet<Predio>();	//HashSet. Se puede definir el tama�o inicial (x)
	}
	
	


	public Propietario(String nombre, String razonSocial, String direccion, String correo, String telefono,
			String contactoReferencia, Boolean activo, Date desde, Long rut) {
		super();
		this.nombre = nombre;
		this.razonSocial = razonSocial;
		this.direccion = direccion;
		this.correo = correo;
		this.telefono = telefono;
		this.contactoReferencia = contactoReferencia;
		this.activo = activo;
		this.desde = desde;
		this.rut = rut;
		this.predios = new HashSet<Predio>();	//HashSet. Se puede definir el tama�o inicial (x)
	}




	public Integer getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getRut() {
		return rut;
	}

	public void setRut(Long rut) {
		this.rut = rut;
	}

	public Set<Predio> getPredios() {
		return predios;
	}

	public void setPredios(Set<Predio> predios) {
		this.predios = predios;
	}
	
	public void addPredio(Predio predio) {
		if (!this.predios.contains(predio)) {
			this.predios.add(predio);
		}
	}
	
	public void removePredio(Predio predio) {
		if (this.predios.contains(predio)) {
			this.predios.remove(predio);
		}
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

	public Date getDesde() {
		return desde;
	}

	public void setDesde(Date desde) {
		this.desde = desde;
	}

	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}
	
	

	public void setId(Integer id) {
		this.id = id;
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
		Propietario other = (Propietario) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}




	@Override
	public String toString() {
		return "Propietario [nombre=" + nombre + "]";
	}


	
	

}
