package com.g13.pdt.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="TIPOZONAS", 
	uniqueConstraints = {
		//Aqui podemos personalizar los UNIQUE que hagan falta
		@UniqueConstraint(name = "UK_TIPOZONA_NOMBRE", columnNames = {"NOMBRE"})
	},
	indexes = {
		//Aqui podemos definir los �ndices que nos hagan falta, los de las FK no se crean autom�ticamente.
//		@Index(name = "IDX_ALGO",  columnList="COLUMNA"),
	}
)
public class TipoZona implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPOZONASG")
    @SequenceGenerator(sequenceName = "TIPOZONA_SEQ", allocationSize = 1, name = "TIPOZONASG")
	@Column(name="ID_TIPO_ZONA")
	private Integer id;
	
	@Size(min = 1, max = 40)
	@NotNull
	@Column(name="NOMBRE",length = 40, nullable = false)
	private String nombre;
	
	@Size(min = 1, max = 40)
	@NotNull
	@Column(name = "COLOR",length = 40, nullable = false)
	private String color;

	public TipoZona() {
		super();
		this.color = "white";
	}

	public TipoZona(String nombre, String color) {
		super();
		this.nombre = nombre;
		this.color = color;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		TipoZona other = (TipoZona) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.nombre;
	}

	
	
	
}

