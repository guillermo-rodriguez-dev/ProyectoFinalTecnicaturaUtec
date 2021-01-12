package managedBean;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.g13.pdt.entidades.Potrero;
import com.g13.pdt.entidades.ZonaGeografica;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IPotreros;
import com.g13.pdt.interfaces.IZonasGeograficas;
/**
 * 
 * Esta clase contiene metodos relacionados con la administracion de zonas
 * 
 * 
 * @author: G13 S.A
 * 
 * @version: 28/06/2020
 * 
 * @see <a href = "www.utec.edu.uy" />
 * 
 */
@ManagedBean(name = "zonaGeografica")
@SessionScoped
public class ZonaManagedBean {

	@EJB
	IZonasGeograficas zonaEjb;

	public void eliminarZonaGeografica(ZonaGeografica zona) {
		try {
			zonaEjb.borrarZonaGeografica(zona);
		} catch (PotrerosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * Redondea el area para que muestr un numero con menos digitos 
	 * 
	 */
	public String redondearArea(ZonaGeografica zona) {

		Double i = zona.getAreaEnHectareas();
		BigDecimal bd = new BigDecimal(i);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return bd.toString();

	}
}
