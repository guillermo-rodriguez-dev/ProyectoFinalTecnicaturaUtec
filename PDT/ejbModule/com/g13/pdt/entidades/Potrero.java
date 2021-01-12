package com.g13.pdt.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.g13.pdt.interfaces.EntidadesConForma;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

@Entity
@Table(name="POTREROS", 
	uniqueConstraints = {
		//Aqui podemos personalizar los UNIQUE que hagan falta
		@UniqueConstraint(name = "UK_POTREROS_NOMBRE", columnNames = {"NOMBRE", "ID_PREDIO","ACTIVO","HASTA"})
	},
	indexes = {
		//Aqui podemos definir los �ndices que nos hagan falta, los de las FK no se crean autom�ticamente.
		@Index(name = "IDX_FK_POTRERO_PREDIO",  columnList="ID_PREDIO"),		
	}
)
public class Potrero implements EntidadesConForma, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @SequenceGenerator(sequenceName = "POTREROS_SEQ", allocationSize = 1, name = "POTREROSG")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POTREROSG")
	@Column(name="ID_POTRERO")
	private Integer id;
	
	@Size(min = 1, max = 40)
	@NotNull
	@Column(name="NOMBRE", length = 40, nullable = false)
	private String nombre;
	
	@Size(min = 1, max = 120)
	@NotNull
	@Column(length = 120, nullable = false)
	private String descripcion;
	
	@Column(name="ACTIVO", nullable = false)
	private Boolean activo;
	
	@Column(name="DESDE", nullable = false)
	private Date desde;
	
	@Column(name="HASTA",nullable = true)
	private Date hasta;
	
	@Column(name="FORMA", nullable = false)
	private Geometry forma;

	@JsonIgnore
	@ManyToOne(optional = false,fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PREDIO",foreignKey = @ForeignKey(name = "FK_POTRERO_PREDIO"))
	private Predio predio;
		
	@OneToMany(mappedBy = "potrero", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<IndicadorPotrero> indicadorPotreros;

	@JsonIgnore
	@OneToMany(mappedBy = "potrero", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ZonaPotrero> zonaPotreros;

	@Transient
	@JsonProperty
	private Indicador indicadorActual;
	
	public Potrero() {
		super();
		//Inicializar colecciones
		this.indicadorPotreros = new ArrayList<IndicadorPotrero>();
		this.zonaPotreros = new ArrayList<ZonaPotrero>();
		this.desde = new Date(); 	//Lo seteo con la fecha actual
	}
	
	

	public Potrero(String nombre, String descripcion, Boolean activo, Date desde, Polygon forma,
			Indicador indicadorActual) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.activo = activo;
		this.desde = desde;
		this.forma = forma;
		
		this.indicadorPotreros = new ArrayList<IndicadorPotrero>();
		this.zonaPotreros = new ArrayList<ZonaPotrero>();
		this.desde = new Date(); 	//Lo seteo con la fecha actual
		
		this.setIndicadorPotreroActual(indicadorActual);
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public Geometry getForma() {
		return forma;
	}

	public void setForma(Geometry forma) {
		this.forma = forma;
	}

	public Predio getPredio() {
		return predio;
	}

	public void setPredio(Predio predio) {
		this.predio = predio;
	}

	

	public List<IndicadorPotrero> getIndicadorPotreros() {
		return indicadorPotreros;
	}

	public void setIndicadorPotreros(List<IndicadorPotrero> indicadorPotreros) {
		this.indicadorPotreros = indicadorPotreros;
	}

	public List<ZonaPotrero> getZonaPotreros() {
		return zonaPotreros;
	}

	public void setZonaPotreros(List<ZonaPotrero> zonaPotreros) {
		this.zonaPotreros = zonaPotreros;
	}
	
	public void addZonaPotrero(ZonaPotrero zp) {
		if (!this.zonaPotreros.contains(zp)) {
			this.zonaPotreros.add(zp);
			zp.setPotrero(this);
		}
	}
	public void removeZonaPotrero(ZonaPotrero zp) {
		if (this.zonaPotreros.contains(zp)) {
			this.zonaPotreros.remove(zp);
			zp.setPotrero(null);
		}
	}
	
	public void addIndicadorPotrero(IndicadorPotrero ip) {
		if (!this.indicadorPotreros.contains(ip)) {
			this.indicadorPotreros.add(ip);
			ip.setPotrero(this);
		}
	}
	public void removeIndicadorPotrero(IndicadorPotrero ip) {
		if (this.indicadorPotreros.contains(ip)) {
			this.indicadorPotreros.remove(ip);
			ip.setPotrero(null);
		}
	}

	
	
	
	public Indicador getIndicadorActual() {
		for (IndicadorPotrero indicadorPotrero : this.indicadorPotreros) {
			//Recorro hasta encontrar el activo. Deber�a ser uno solo el activo...
			if (indicadorPotrero.getActivo()) {
				return indicadorPotrero.getIndicador();
			}
		}
		return null;
	}

	public void setIndicadorPotreroActual(Indicador nuevoActual) {
		
		IndicadorPotrero indicadorPotreroActual = null;
		for (IndicadorPotrero indicadorPotrero : this.indicadorPotreros) {
			//Recorro hasta encontrar el activo. Deber�a ser uno solo el activo...
			if (indicadorPotrero.getActivo()) {
				indicadorPotreroActual = indicadorPotrero;
			}
		}
		Date fecha = new Date();
		if (indicadorPotreroActual != null) {
			indicadorPotreroActual.setActivo(false);	//le pongo activo false
			indicadorPotreroActual.setHasta(fecha);	//y le seteo la fecha que se desactivo
		}
		
		IndicadorPotrero indicadorPotreroNuevo = new IndicadorPotrero();
		indicadorPotreroNuevo.setDesde(fecha);		//seteo la fechadesde para el nuevoactual
		indicadorPotreroNuevo.setActivo(true);		//y le seteo el activo a true. (ahora el es el actual)
		indicadorPotreroNuevo.setIndicador(nuevoActual);
//		indicadorPotreroNuevo.setPotrero(this);		//el potrero ya se setea en el add
		
		this.addIndicadorPotrero(indicadorPotreroNuevo);	//y lo agrego a la lista de indicadores 
		
	}
	
	
public void setIndicadorActual(Indicador nuevoActual) {
		
		IndicadorPotrero indicadorPotreroActual = null;
		for (IndicadorPotrero indicadorPotrero : this.indicadorPotreros) {
			//Recorro hasta encontrar el activo. Deber�a ser uno solo el activo...
			if (indicadorPotrero.getActivo()) {
				indicadorPotreroActual = indicadorPotrero;
			}
		}
		Date fecha = new Date();
		if (indicadorPotreroActual != null) {
			indicadorPotreroActual.setActivo(false);	//le pongo activo false
			indicadorPotreroActual.setHasta(fecha);	//y le seteo la fecha que se desactivo
		}
		
		IndicadorPotrero indicadorPotreroNuevo = new IndicadorPotrero();
		indicadorPotreroNuevo.setDesde(fecha);		//seteo la fechadesde para el nuevoactual
		indicadorPotreroNuevo.setActivo(true);		//y le seteo el activo a true. (ahora el es el actual)
		indicadorPotreroNuevo.setIndicador(nuevoActual);
//		indicadorPotreroNuevo.setPotrero(this);		//el potrero ya se setea en el add
		
		this.addIndicadorPotrero(indicadorPotreroNuevo);	//y lo agrego a la lista de indicadores 
		
	}


	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desde == null) ? 0 : desde.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((predio == null) ? 0 : predio.hashCode());
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
		Potrero other = (Potrero) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	@JsonProperty
	double areaEnHectareas = 0;
	
	@Override
	public double getAreaEnHectareas() {
		return this.forma.getArea() * 1000000;
	}

	@Override
	public void setAreaEnHectareas(double a) {
		areaEnHectareas = a;
	}
	
}
