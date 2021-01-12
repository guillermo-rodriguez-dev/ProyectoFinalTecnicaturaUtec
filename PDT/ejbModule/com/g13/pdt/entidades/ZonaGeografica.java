package com.g13.pdt.entidades;

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
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.OneToMany;

@Entity
@Table(name="ZONAGEOGRAFICAS", 
	uniqueConstraints = {
		@UniqueConstraint(name = "UK_ZONAS_NOMBRE", columnNames = {"NOMBRE", "ID_PREDIO","ACTIVO","HASTA"})
		//Uniques con los desde hasta?
	},
	indexes = {
		@Index(name = "IDX_FK_ZONAGEOGRAFICA_PREDIO",  columnList="ID_PREDIO"),
		@Index(name = "IDX_FK_ZONAGEOGRAFICA_TIPOZONA",  columnList="ID_TIPOZONA"),
	}
)
public class ZonaGeografica implements Serializable, EntidadesConForma {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "ZONAGEOGRAFICA_SEQ", allocationSize = 1, name = "ZONAGEOGRAFICASG")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ZONAGEOGRAFICASG")
	@Column(name="ID_ZONAGEOGRAFICA")
	private Integer id;
	
	@Size(min = 1, max = 40)
	@NotNull
	@Column(name="NOMBRE", length = 40, nullable = false)
	private String nombre;
	
	@Size(min = 1, max = 120)
	@NotNull
	@Column(name="DESCRIPCION", length = 120, nullable = false)
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
	
	@ManyToOne(optional = false,fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_TIPOZONA",foreignKey = @ForeignKey(name = "FK_ZONAGEOGRAFICA_TIPOZONA"))
	private TipoZona tipoZona;

	@JsonIgnore
	@OneToMany(mappedBy = "zonaGeografica", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ZonaPotrero> zonaPotreros;
	
//	@Transient
//	@JsonProperty
//	private Integer idJs;

	public ZonaGeografica() {
		super();
		this.desde = new Date(); 	//Lo seteo con la fecha actual
		this.zonaPotreros = new ArrayList<ZonaPotrero>();
	}
	
	
	
	public ZonaGeografica(String nombre, String descripcion, Boolean activo, Date desde, Polygon forma,TipoZona tipoZona) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.activo = activo;
		this.desde = desde;
		this.forma = forma;
		this.tipoZona = tipoZona;
		this.zonaPotreros = new ArrayList<ZonaPotrero>();
	}



	public ZonaGeografica(Predio predio) {
		super();
		this.desde = new Date(); 	//Lo seteo con la fecha actual
		this.predio = predio;
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

	public TipoZona getTipoZona() {
		return tipoZona;
	}

	public void setTipoZona(TipoZona tipoZona) {
		this.tipoZona = tipoZona;
	}

	public List<ZonaPotrero> getZonaPotreros() {
		return zonaPotreros;
	}

	public void setZonaPotreros(List<ZonaPotrero> zonaPotreros) {
		this.zonaPotreros = zonaPotreros;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	public void addZonaPotrero(ZonaPotrero zp) {
		if (!this.zonaPotreros.contains(zp)) {
			this.zonaPotreros.add(zp);
			zp.setZonaGeografica(this);
		}
	}
	public void removeZonaPotrero(ZonaPotrero zp) {
		if (this.zonaPotreros.contains(zp)) {
			this.zonaPotreros.remove(zp);
			zp.setZonaGeografica(null);
		}
	}
	

//	public Integer getIdJs() {
//		return idJs;
//	}
//	public void setIdJs(Integer id) {
//		this.idJs = id;
//	}
	
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
		ZonaGeografica other = (ZonaGeografica) obj;
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



	@Override
	public String toString() {
		return "ZonaGeografica [id=" + id + ", nombre=" + nombre + ", activo=" + activo + ", tipoZona=" + tipoZona
				+ "]";
	}
	
	public void copiarDe(ZonaGeografica z) {
		this.forma = z.getForma();
		this.nombre = z.getNombre();
		this.descripcion = z.getDescripcion();
		this.tipoZona = z.getTipoZona();
	}
	
}