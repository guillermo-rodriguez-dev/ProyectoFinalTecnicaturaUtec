package com.g13.pdt.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
import com.vividsolutions.jts.geom.Polygon;
import javax.persistence.ManyToMany;

@Entity
@Table(name="PREDIOS", 
	uniqueConstraints = {
		//Aqui podemos personalizar los UNIQUE que hagan falta
		@UniqueConstraint(name = "UK_PREDIO_NOMBRE", columnNames = {"NOMBRE"})
	},
	indexes = {
		//Aqui podemos definir los �ndices que nos hagan falta, los de las FK no se crean autom�ticamente.
		//@Index(name = "IDX_ALGO",  columnList="LA_COLUMNA"),
	}
)
public class Predio implements EntidadesConForma, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PREDIOSG")
    @SequenceGenerator(sequenceName = "PREDIOS_SEQ", allocationSize = 1, name = "PREDIOSG")
	@Column(name="ID_PREDIO")
	private Integer id;
	
	@Column(name = "ACTIVO")
	private Boolean activo;
	
	@Size(min = 1, max = 40)
	@NotNull
	@Column(name="NOMBRE",length = 40, nullable = false)
	private String nombre;
	
	@Column(name="DESDE", nullable = false)
	private Date desde;
	
	@Column(name="HASTA",nullable = true)
	private Date hasta;
	
	@Size(min = 1, max = 120)
	@NotNull
	@Column(name="DESCRIPCION",length = 120, nullable = false)
	private String descripcion;

	@Column(name="FORMA", nullable = false)
	private Polygon forma;
	
	@JsonIgnore
	@OneToMany(mappedBy = "predio", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Potrero> potreros;
	
	@JsonIgnore
	@OneToMany(mappedBy = "predio", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ZonaGeografica> zonas;

	@ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinTable(
		name="PROPIETARIOSPREDIO",
		joinColumns = {
				@JoinColumn(name="ID_PREDIO", referencedColumnName="ID_PREDIO",foreignKey = @ForeignKey(name = "FK_PROPIETARIOSPREDIO_PRED"))
		},
		inverseJoinColumns = {
				@JoinColumn(name="ID_PROPIETARIO", referencedColumnName="ID_PROPIETARIO",foreignKey = @ForeignKey(name = "FK_PROPIETARIOSPREDIO_PROP"))
		}
		)
	private Set<Propietario> propietarios;
	
	public Predio() {
		super();
		this.desde= new Date(); 	//Lo seteo con la fecha actual
		//Inicializar colecciones
		this.potreros = new ArrayList<Potrero>();
		this.zonas = new ArrayList<ZonaGeografica>();
		this.propietarios = new HashSet<Propietario>();	//Set porque en predio nos vemos obligados a usar set para que genere la clave primaria compuesta
	}
	
	

	public Predio(Boolean activo, String nombre, Date desde, String descripcion, Polygon forma) {
		super();
		this.activo = activo;
		this.nombre = nombre;
		this.desde = desde;
		this.descripcion = descripcion;
		this.forma = forma;
		
		
		this.potreros = new ArrayList<Potrero>();
		this.zonas = new ArrayList<ZonaGeografica>();
		this.propietarios = new HashSet<Propietario>();	//Set porque en predio nos vemos obligados a usar set para que genere la clave primaria compuesta
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	public Polygon getForma() {
		return forma;
	}	
	 
	 

	
	public void setForma(Polygon forma) {
		this.forma = forma;
	}
	
	

	public List<Potrero> getPotreros() {
		return potreros;
	}

	public void setPotreros(List<Potrero> potreros) {
		this.potreros = potreros;
	}

	public List<ZonaGeografica> getZonas() {
		return zonas;
	}

	public void setZonas(List<ZonaGeografica> zonas) {
		this.zonas = zonas;
	}
	
	public void addPotrero(Potrero potrero) {
		if (!this.potreros.contains(potrero)) {
			//Si a�n no existe el potrero en la lista, lo agrego
			this.potreros.add(potrero);
			potrero.setPredio(this);
		}
	}
	
	public void removePotrero(Potrero potrero) {
		if (this.potreros.contains(potrero)) {
			//Si contiene el potrero lo saco
			this.potreros.remove(potrero);
			potrero.setPredio(null);			//El potrero se queda sin predio
		}
	}
	
	public void addZonaGeografica(ZonaGeografica zg) {
		if (!this.zonas.contains(zg)) {
			this.zonas.add(zg);
			zg.setPredio(this);
		}
	}
	
	public void removeZonaGeografica(ZonaGeografica zg) {
		if (this.zonas.contains(zg)) {
			this.zonas.remove(zg);
			zg.setPredio(null);
		}
	}
	
	public void addPropietario(Propietario propietario) {
		if (!this.propietarios.contains(propietario)) {
			this.propietarios.add(propietario);
		}
	}
	
	public void removePropietario(Propietario propietario) {
		if (this.propietarios.contains(propietario)) {
			this.propietarios.remove(propietario);
		}
	}
	
	

	public Set<Propietario> getPropietarios() {
		return propietarios;
	}

	public void setPropietarios(Set<Propietario> propietarios) {
		this.propietarios = propietarios;
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
		Predio other = (Predio) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public double cargarAreaIndicador(Indicador indicador) {
		
		double area = 0;
		
		if (indicador.tieneHijosActivos()) {
			//Si el indicador es padre de otros indicadores
			for (Indicador hijo : indicador.getHijos()) {
				if (hijo.getActivo()) {
					area = area + cargarAreaIndicador(hijo);
				}
			}
		}else {
			//Si el indicador no tiene hijos, me fijo solo en los potreros
			for (Potrero potrero : potreros) {
				if (potrero.getActivo()) {
					//Si el potrero est� activo suma. sino no.
					if (indicador.equals(potrero.getIndicadorActual())) {
						area = area + potrero.getAreaEnHectareas();
					}
				}
				
			}
		}
		
		indicador.setArea(area);
		return area;
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

	public void copiarDesde(Predio p) {
		this.nombre = p.getNombre();
		this.descripcion = p.getDescripcion();
		this.forma = p.getForma();
	}



	@Override
	public String toString() {
		return "Predio [nombre=" + nombre + ", propietarios=" + propietarios + "]";
	}
	
	
	
}
