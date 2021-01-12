package managedBean;

import javax.faces.bean.ManagedBean;

import javax.faces.bean.SessionScoped;

import com.jsf2leaf.model.Layer;
import com.jsf2leaf.model.Map;
@ManagedBean(name="mapaBean")
@SessionScoped
public class MapaManagedBean {
	private Map springfieldMap = new Map();

	public Layer placesLayer = new Layer();
	
}