package com.g13.pdt.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(
	name = "INDICADORPOTREROS", 
	uniqueConstraints = {
		//Aqui podemos personalizar los UNIQUE que hagan falta
		//TODO Chequear que no haga falta alg�n UNIQUE con las fechas
		@UniqueConstraint(name = "UK_INDICADORPOTRERO_POTRERO_INDICADOR", columnNames = { "ID_POTRERO", "ID_INDICADOR", "DESDE" }) 
	}, 
	indexes = {
		//Aqui podemos definir los �ndices que nos hagan falta, los de las FK no se crean autom�ticamente.
		@Index(name = "IDX_FK_INDICADORPOTRERO_POTRERO", columnList = "ID_POTRERO"),
		@Index(name = "IDX_FK_INDICADORPOTRERO_INDICADOR", columnList = "ID_INDICADOR"),
	}
)
public class IndicadorPotrero implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INDICADORPOTREROSG")
	@SequenceGenerator(sequenceName = "INDICADORPOTRERO_SEQ", allocationSize = 1, name = "INDICADORPOTREROSG")
	@Column(name = "ID_INDICADORPOTRERO")
	private Integer id;
	
	@Column(name = "ACTIVO", nullable = false)
	private Boolean activo;

	@Column(name = "DESDE", nullable = false)
	private Date desde;

	@Column(name = "HASTA", nullable = true)
	private Date hasta;

	@JsonIgnore
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_POTRERO",foreignKey = @ForeignKey(name = "FK_INDICADORPOTRERO_POTRERO"))
	private Potrero potrero;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_INDICADOR",foreignKey = @ForeignKey(name = "FK_INDICADORPOTRERO_INDICADOR"))
	private Indicador indicador;

	public IndicadorPotrero() {
		super();
		this.desde= new Date(); 	//Lo seteo con la fecha actual
		//No hay coleccciones para inicializar
	}
	
	public IndicadorPotrero(Indicador indicador, Potrero potrero) {
		super();
		this.desde= new Date(); 	//Lo seteo con la fecha actual
		this.indicador = indicador;
		this.potrero = potrero;
		//No hay coleccciones para inicializar
	}

	public Integer getId() {
		return id;
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

	public Potrero getPotrero() {
		return potrero;
	}

	public void setPotrero(Potrero potrero) {
		this.potrero = potrero;
	}

	public Indicador getIndicador() {
		return indicador;
	}

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desde == null) ? 0 : desde.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((indicador == null) ? 0 : indicador.hashCode());
		result = prime * result + ((potrero == null) ? 0 : potrero.hashCode());
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
		IndicadorPotrero other = (IndicadorPotrero) obj;
		if (desde == null) {
			if (other.desde != null)
				return false;
		} else if (!desde.equals(other.desde))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (indicador == null) {
			if (other.indicador != null)
				return false;
		} else if (!indicador.equals(other.indicador))
			return false;
		if (potrero == null) {
			if (other.potrero != null)
				return false;
		} else if (!potrero.equals(other.potrero))
			return false;
		return true;
	}

}