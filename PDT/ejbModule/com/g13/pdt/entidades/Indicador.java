package com.g13.pdt.entidades;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
import com.g13.pdt.excepciones.IndicadorConPotrerosException;


@Entity
@Table(name="INDICADORES", 
	uniqueConstraints = {
		//Aqui podemos personalizar los UNIQUE que hagan falta
		@UniqueConstraint(name = "UK_INDICADOR_NOMBRE", columnNames = {"NOMBRE"})
	},
	indexes = {
		//Aqui podemos definir los �ndices que nos hagan falta, los de las FK no se crean automaticamente.
		@Index(name = "IDX_FK_INDICADORPADRE",  columnList="ID_INDICADORPADRE"),
	}
)
public class Indicador implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INDICADORSG")
    @SequenceGenerator(sequenceName = "INDICADORES_SEQ", allocationSize = 1, name = "INDICADORSG")
	@Column(name="ID_INDICADOR")
	private Integer id;
	
	@Size(min = 1, max = 40)
	@NotNull
	@Column(name="NOMBRE", length = 40, nullable = false)
	private String nombre;
	
	@Size(min = 1, max = 220)
	@NotNull
	@Column(name="DESCRIPCION", length = 220, nullable = false)
	private String descripcion;
	
	@Column(name = "ACTIVO", nullable = false)
	private Boolean activo;
	
	@Column(name = "DESDE", nullable = false)
	private Date desde;

	@Column(name = "HASTA", nullable = true)
	private Date hasta;
	
	@Size(min = 1, max = 40)
	@NotNull
	@Column(name = "COLOR", length = 40, nullable = false)
	private String color;
	
	//Solo un Indicador puede no tener padre
	@JsonIgnore
	@ManyToOne(optional = true, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_INDICADORPADRE",foreignKey = @ForeignKey(name = "FK_INDICADOR_PADRE"))
	private Indicador padre;
	
	@JsonIgnore
	@OneToMany(mappedBy = "indicador", cascade = {CascadeType.PERSIST}, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<IndicadorPotrero> indicadorPotreros;

	@OneToMany(mappedBy = "padre", cascade = {CascadeType.PERSIST}, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Indicador> hijos;
	
	@Transient
	@JsonProperty
	double area=0;

	public Indicador() {
		super();
		this.color = "white";
		this.desde= new Date();	//Lo seteo con la fecha actual
		//Inicializar coleeciones
		this.hijos = new ArrayList<Indicador>();
		this.indicadorPotreros = new ArrayList<IndicadorPotrero>();
	}
	
	public Indicador(String nombre, String descripcion, Date desde, String color) {
		super();
		this.desde= desde;	//Lo seteo con la fecha actual
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.activo = true;
		//Inicializar coleeciones
		this.hijos = new ArrayList<Indicador>();
		this.indicadorPotreros = new ArrayList<IndicadorPotrero>();
		this.color = color;
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

	public List<IndicadorPotrero> getIndicadorPotreros() {
		return indicadorPotreros;
	}

	public void setIndicadorPotreros(List<IndicadorPotrero> indicadorPotreros) {
		this.indicadorPotreros = indicadorPotreros;
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


	public Indicador getPadre() {
		return padre;
	}


	public void setPadre(Indicador padre) {
		this.padre = padre;
	}

	public List<Indicador> getHijos() {
		return hijos;
	}


	public void setHijos(List<Indicador> hijos) {
		this.hijos = hijos;
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
	

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public boolean tieneIndicadorPotrerosActivos() {
		if (this.indicadorPotreros.isEmpty()) {
			return false;
		}
		//Si tiene IndicadorPotreros asociados
		//Verificar si hay activos
		for (IndicadorPotrero indicadorPotrero : this.indicadorPotreros) {
			if (indicadorPotrero.getActivo()) {
				//Si existe un IndicadorPotrero activo en el indicador
				return true;
			}
		}
		return false;
	}
	
	public void addHijo(Indicador hijo) throws IndicadorConPotrerosException {
		if (this.tieneIndicadorPotrerosActivos()) {
			throw new IndicadorConPotrerosException("El indicador " + this.nombre + " actualmente tiene potreros asignados");
		}
		if (!this.hijos.contains(hijo)) {
			//Si a�n no contiene ese hijo, lo agrego
			this.hijos.add(hijo);	//Agrego el hijo
			hijo.setPadre(this);	//Seteo el padre del hijo
		}
		
	}
	public void removeHijo(Indicador hijo) {
		if (this.hijos.contains(hijo)) {
			//Si contiene el hijo
			this.hijos.remove(hijo);	//Saco el hijo de la lista de hijos
			hijo.setPadre(null);		//El "hijo" queda sin padre �Es necesaria esta l�nea???
		}
	}
	
	public boolean tieneHijosActivos() {
		if (this.hijos.isEmpty()) {
			return false;
		}
		//Si tiene hijos, chequear si hay activos
		for (Indicador indicador : hijos) {
			if (indicador.getActivo()) {
				//Si existe indicador hijo activo actualmente
				return true;
			}
		}
		return false;
	}
	public void addIndicadorPotrero(IndicadorPotrero ip) {
		if (this.tieneHijosActivos()) {
			
		}
		if (!this.indicadorPotreros.contains(ip)) {
			//Si a�n no contiene ese IndicadorPotrero, lo agrego
			this.indicadorPotreros.add(ip);	//Agrego el IndicadorPotrero
			ip.setIndicador(this);			//Seteo el Indicador del IndicadorPotrero
		}
		
	}
	public void removeHijo(IndicadorPotrero ip) {
		if (this.indicadorPotreros.contains(ip)) {
			//Si contiene el IndicadorPotrero
			this.indicadorPotreros.remove(ip);	//Saco el hijo de la lista de hijos
			ip.setIndicador(null);				//El IndicadoPotrero queda sin Indicador
		}
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
		Indicador other = (Indicador) obj;
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
		if (this.area == 0) {
			return this.nombre;
		}
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		return this.nombre + ": " + df.format(this.area) + "ha";
	}

	public double getArea() {
		return area;
		
	}

	public void setArea(double area) {
		this.area = area;
	}
	
}