package managedBean;

import java.util.List;



import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.g13.pdt.enumerados.TipoDocumento;
import com.g13.pdt.servicios.TiposZonasBeanRemote;


/**
 * 
 * Esta clase contiene diferentes enumerados relacionados
 * This class contains diferent enumerators
 * 
 * @author: G13 S.A
 * 
 * @version: 28/06/2020
 * 
 * @see <a href = "www.utec.edu.uy" />
 * 
 */
@ManagedBean(name="enumeradosBean")
@SessionScoped
public class Emnumerados {


	
	public Emnumerados(){
		
	}
	
	private TipoDocumento[]  tipoDocumento=new  TipoDocumento[]{TipoDocumento.CI, TipoDocumento.Pasaporte, TipoDocumento.DNI};

	public TipoDocumento[] getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento[] tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	
	
	
	
}