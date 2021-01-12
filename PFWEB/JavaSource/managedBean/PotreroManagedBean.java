package managedBean;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import com.g13.pdt.entidades.Potrero;
import com.g13.pdt.entidades.TipoZona;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IPotreros;
import com.vividsolutions.jts.geom.Polygon;

@ManagedBean(name = "potreroBean")
@RequestScoped
public class PotreroManagedBean {
	 private long id;
	    private String nombre;
	    private String descripcion;
	    private boolean activo;
	    private long desde;
	    private Object hasta;
	    private Polygon forma;
	    private TipoZona tipoZona;
	    private long idJS;
	    private double areaEnHectareas;
	    
	    private Potrero selectedPotrero;
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
		public boolean isActivo() {
			return activo;
		}
		public void setActivo(boolean activo) {
			this.activo = activo;
		}
		public long getDesde() {
			return desde;
		}
		public void setDesde(long desde) {
			this.desde = desde;
		}
		public Object getHasta() {
			return hasta;
		}
		public void setHasta(Object hasta) {
			this.hasta = hasta;
		}
		public Polygon getForma() {
			return forma;
		}
		public void setForma(Polygon forma) {
			this.forma = forma;
		}
		public TipoZona getTipoZona() {
			return tipoZona;
		}
		public void setTipoZona(TipoZona tipoZona) {
			this.tipoZona = tipoZona;
		}
		public double getAreaEnHectareas() {
			return areaEnHectareas;
		}
		public void setAreaEnHectareas(double areaEnHectareas) {
			this.areaEnHectareas = areaEnHectareas;
		}
	    
	    
	    
	    @EJB
	    IPotreros potreroejb;
	    public void eliminarPotrero(Potrero potrero) {
	    	try {
				potreroejb.borrarPotrero(potrero);
			} catch (PotrerosException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    public String redondearArea(Potrero potrero) {
	    	
	    	Double i = potrero.getAreaEnHectareas();
	    	BigDecimal bd = new BigDecimal(i);
	    	bd = bd.setScale(2, RoundingMode.HALF_UP);
	    	
	    	
			return bd.toString();
	    	
	    }
		public Potrero getSelectedPotrero() {
			return selectedPotrero;
		}
		public void setSelectedPotrero(Potrero selectedPotrero) {
			this.selectedPotrero = selectedPotrero;
		}
		
		public void verHistorico(Potrero selectedPotrero) {
			this.selectedPotrero = selectedPotrero;
		}
}
