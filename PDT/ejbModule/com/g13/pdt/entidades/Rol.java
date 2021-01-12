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
@Table(name="ROLES", 
	uniqueConstraints = {
		//Aqui podemos personalizar los UNIQUE que hagan falta
		@UniqueConstraint(name = "UK_ROL_NOMBRE", columnNames = {"NOMBRE"})
	},
	indexes = {
		//Aqui podemos definir los �ndices que nos hagan falta, los de las FK no se crean autom�ticamente.
//		@Index(name = "IDX_ALGO",  columnList="COLUMNA"),
	}
)
public class Rol implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLSG")
    @SequenceGenerator(sequenceName = "ROLE_SEQ", allocationSize = 1, name = "ROLSG")
	@Column(name="ID_ROL")
	private Integer id;
	
	@Size(min = 1, max = 60)
	@NotNull
	@Column (name="NOMBRE", length = 60, nullable = false)
	private String nombre;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getId() {
		return id;
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
		Rol other = (Rol) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nombre;
	}

	
	
}
