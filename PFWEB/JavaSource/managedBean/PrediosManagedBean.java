package managedBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g13.pdt.entidades.Indicador;
import com.g13.pdt.entidades.Potrero;
import com.g13.pdt.entidades.Predio;
import com.g13.pdt.entidades.Propietario;
import com.g13.pdt.entidades.ZonaGeografica;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IPotreros;
import com.g13.pdt.interfaces.IPredios;
import com.g13.pdt.interfaces.IPropietarios;
import com.g13.pdt.interfaces.IZonasGeograficas;
import com.g13.pdt.servicios.PotrerosBeanRemote;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * 
 * Esta clase contiene la gestion de predios y algunas entidades relacionas
 * 
 * @author: G13 
 * 
 * @version: 28/06/2020
 * 
 * @see <a href = "www.utec.edu.uy" />
 * 
 */

@ManagedBean(name = "prediosBean")
@SessionScoped
public class PrediosManagedBean  implements Converter<Predio>{
	FacesContext context = FacesContext.getCurrentInstance();
	PrimeFaces current = PrimeFaces.current();

	private long id;
	private boolean activo;
	private String nombre;
	private long desde;
	private long hasta;
	private String descripcion;
	private com.vividsolutions.jts.geom.Polygon forma;
	private List<Potrero> potreros;
	private List<ZonaGeografica> zonas;
	private Set<Propietario> propietarios;
	private MapModel mapaPredio;
	private double areaEnHectareas;
	private List<Predio> listaPredios;
	private List<Predio> listaPrediosFiltrada;
	private String messege;
	private Coordinate[] cor;
	private Predio predioElegido;
	private String formaJson;
	private DualListModel<Propietario> listaPropietarios;
	private List<Propietario> propietariosListaElegidos;
	private String zonasJson;
	private GeometryFactory geometryFactory = new GeometryFactory();
	private List<Potrero> potrerosEliminados = new ArrayList<Potrero>();
	GsonBuilder gsonBuilder = new GsonBuilder();
	Gson gson = gsonBuilder.serializeSpecialFloatingPointValues().create();
	private String potrerosJson;
	ObjectMapper objectMapper = new ObjectMapper();
	private List<Coordinate[]> listaPrediosForma;

	private List<ZonaGeografica> zonasEliminadas = new ArrayList<ZonaGeografica>();;

	public List<Propietario> getPropietariosListaElegidos() {
		return propietariosListaElegidos;
	}

	public void setPropietariosListaElegidos(List<Propietario> propietariosListaElegidos) {
		this.propietariosListaElegidos = propietariosListaElegidos;
	}

	public DualListModel<Propietario> getListaPropietarios() {
		return listaPropietarios;
	}

	public void setListaPropietarios(DualListModel<Propietario> listaPropietarios) {
		this.listaPropietarios = listaPropietarios;
	}

	public String getFormaJson() {
		return formaJson;
	}

	public void setFormaJson(String formaJson) {
		this.formaJson = formaJson;
	}

	public Coordinate[] getCor() {
		return cor;
	}

	public void setCor(Coordinate[] cor) {
		this.cor = cor;
	}

	public Set<Propietario> getPropietarios() {
		return propietarios;
	}

	public void setPropietarios(Set<Propietario> propietarios) {
		this.propietarios = propietarios;
	}

	public List<Predio> getListaPredios() {
		return listaPredios;
	}

	public void setListaPredios(List<Predio> listaPredios) {
		this.listaPredios = listaPredios;
	}

	public Predio getPredioElegido() {
		return predioElegido;
	}

	public void setPredioElegido(Predio predioElegido) {
		this.predioElegido = predioElegido;
	}

	public long getID() {
		return id;
	}

	public void setID(long value) {
		this.id = value;
	}

	public boolean getActivo() {
		return activo;
	}

	public void setActivo(boolean value) {
		this.activo = value;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String value) {
		this.nombre = value;
	}

	public long getDesde() {
		return desde;
	}

	public void setDesde(long value) {
		this.desde = value;
	}

	public long getHasta() {
		return hasta;
	}

	public void setHasta(long value) {
		this.hasta = value;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String value) {
		this.descripcion = value;
	}

	public com.vividsolutions.jts.geom.Polygon getForma() {
		return forma;
	}

	public void setForma(com.vividsolutions.jts.geom.Polygon value) {
		this.forma = value;
	}

	public List<Potrero> getPotreros() {
		return potreros;
	}

	public void setPotreros(List<Potrero> value) {
		this.potreros = value;
	}

	public List<ZonaGeografica> getZonas() {
		return zonas;
	}

	public void setZonas(List<ZonaGeografica> value) {
		this.zonas = value;
	}

	public double getAreaEnHectareas() {
		return areaEnHectareas;
	}

	public void setAreaEnHectareas(double value) {
		this.areaEnHectareas = value;
	}

	public String getPotrerosJson() {
		return potrerosJson;
	}

	public void setPotrerosJson(String potrerosJson) {
		this.potrerosJson = potrerosJson;
	}

	public List<Coordinate[]> getListaPrediosForma() {
		return listaPrediosForma;
	}

	public void setListaPrediosForma(List<Coordinate[]> listaPrediosForma) {
		this.listaPrediosForma = listaPrediosForma;
	}

	public String getZonasJson() {
		return zonasJson;
	}

	public void setZonasJson(String zonasJson) {
		this.zonasJson = zonasJson;
	}

	public List<ZonaGeografica> getZonasEliminadas() {
		return zonasEliminadas;
	}

	public void setZonasEliminadas(List<ZonaGeografica> zonasEliminadas) {
		this.zonasEliminadas = zonasEliminadas;
	}

	@EJB
	private IPredios prediosEJBBean;

	@EJB
	private IPropietarios propietariosEJBbean;
	@EJB
	private IPotreros potrerosEJBbean;

	@EJB
	private IZonasGeograficas zonasGeograficasEjb;

	public void obtenerListaDualPropietarios() {
		propietariosListaElegidos = new ArrayList<Propietario>();

		listaPropietarios = new DualListModel<Propietario>(propietariosEJBbean.obtenerListaTodos(),
				propietariosListaElegidos);
	}

	public List<Predio> obtenerListaPredios() {
		current = PrimeFaces.current();
		context = FacesContext.getCurrentInstance();

		listaPredios = prediosEJBBean.obtenerListaTodos();
		for (Predio p : listaPredios) {
			System.out.println(p.getActivo());

		}
		return listaPredios;
	}

	/**
	 * 
	 * Muestra un dialog en donde dibujar el predio y cargar sus demas datos
	 * 
	 */
	public void botonCrear() {
		borrarValores();
		obtenerListaDualPropietarios();
		PrimeFaces current = PrimeFaces.current();
		

		current.executeScript("PF('crear').show();");
		current.executeScript("resetearFormulario('crearPredioForm');");
		current.executeScript("crearMapaVacio('mapaCrear');");

	}

	/**
	 * 
	 * Traduce el polygono dibujado en js ha java para obtener la forma deseada
	 * 
	 */
	
	
	public Coordinate[] obtenerCordenadas() {
		JsonObject jsonFormaDibujado = gson.fromJson(formaJson, JsonObject.class);
		JsonArray jsonFormaProcesada = jsonFormaDibujado.getAsJsonArray("coordinates");
		JsonArray cordenadasEnJson = (JsonArray) jsonFormaProcesada.get(0);
		Coordinate[] cordenadasLista = new Coordinate[cordenadasEnJson.size()];
		for (int i = 0; i < cordenadasEnJson.size(); i++) {
			JsonArray jsoncordenadaEnFor = (JsonArray) cordenadasEnJson.get(i);
			Coordinate cordenadaCreada = new Coordinate(jsoncordenadaEnFor.get(1).getAsDouble(),
					jsoncordenadaEnFor.get(0).getAsDouble());
			cordenadasLista[i] = cordenadaCreada;
		}
		System.out.println("obteniendo cordenadas");
		return cordenadasLista;
	}

	public void cargarDatosEnPredio(Predio predio) {
		predio.setForma(forma);
		predio.setActivo(activo);
		predio.setDescripcion(descripcion);
		predio.setPotreros(potreros);
		predio.setNombre(nombre);
		Set<Propietario> propietariosSet = new HashSet<Propietario>();
		for (Propietario propietario : listaPropietarios.getTarget()) {
			propietariosSet.add(propietario);

		}
		predio.setPropietarios(propietariosSet);

	}

	/**
	 * 
	 * Crea el predio en la bd
	 * 
	 */
	public void crearPredio() {
		Coordinate[] cordenadasLista = obtenerCordenadas();
		Predio predio = new Predio();
		forma = geometryFactory.createPolygon(cordenadasLista);

		System.out.println(listaPropietarios.getTarget().get(0));
		cargarDatosEnPredio(predio);
		try {
			prediosEJBBean.altaPredio(predio);
			messege = "Predio Creado Exitosamente";
			current.executeScript("PF('crear').hide();");

			obtenerListaPredios();
		} catch (PotrerosException e) {
			// TODO Auto-generated catch block
			setMessege(e.getMessage());
			e.printStackTrace();
		}
		mostrarMensaje();
	}
	/**
	 * 
	 * Carga las cordenas del js 
	 * 
	 */
	public void cargarCordenadasModificar() {
		JsonObject jsonFormaDibujado = gson.fromJson(formaJson, JsonObject.class);
		JsonArray jsonFormaProcesada = jsonFormaDibujado.getAsJsonArray("coordinates");
		JsonArray cordenadasEnJson = (JsonArray) jsonFormaProcesada.get(0);
		Coordinate[] cordenadasLista = new Coordinate[cordenadasEnJson.size()];

		for (int i = 0; i < cordenadasEnJson.size(); i++) {
			JsonArray jsoncordenadaEnFor = (JsonArray) cordenadasEnJson.get(i);
			Coordinate cordenadaCreada = new Coordinate(jsoncordenadaEnFor.get(1).getAsDouble(),
					jsoncordenadaEnFor.get(0).getAsDouble());
			cordenadasLista[i] = cordenadaCreada;
		}
		GeometryFactory geometryFactory = new GeometryFactory();
		forma = geometryFactory.createPolygon(cordenadasLista);
		predioElegido.setForma(forma);

	}

	/**
	 * 
	 * Guarda las modificaciones hechas en el predio en la bd
	 * 
	 */
	public void modificarPredio() {

		try {

			cargarCordenadasModificar();

			cargarDatosEnPredio(predioElegido);

			prediosEJBBean.editarPredio(predioElegido);
			messege = "Predio Modificado Correctamente";
			current.executeScript("PF('panelModificar').hide();");

		} catch (PotrerosException e) {
			messege = e.getMessage();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mostrarMensaje();
	}

	/**
	 * 
	 * Carga los datos de predio y muestra un dialog para modificar sus datos
	 * 
	 */

	public void iniciarModificacionPredio(Predio predio) {
		borrarValores();
		predioElegido = predio;
		potreros = predio.getPotreros();
		obtenerListaDualPropietarios();
		for (Propietario propietario : predio.getPropietarios()) {
			propietariosListaElegidos.add(propietario);
			listaPropietarios.getSource().remove(propietario);

		}
		try {
			potreros = potrerosEJBbean.obtenerListaTodos(predio.getId());
			objectMapper.registerModule(new JtsModule());
			potrerosJson = objectMapper.writeValueAsString(potreros);
			System.out.println(potrerosJson);

			zonas = zonasGeograficasEjb.obtenerListaTodosActivas(predio.getId());
			objectMapper.registerModule(new JtsModule());
			zonasJson = objectMapper.writeValueAsString(zonas);
			System.out.println(zonasJson);
		} catch (PotrerosException | JsonProcessingException e) {
			messege = e.getMessage();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		activo = predio.getActivo();
		descripcion = predio.getDescripcion();
		forma = predio.getForma();
		nombre = predio.getNombre();
		cor = forma.getCoordinates();
		formaJson = gson.toJson(cor);

		current.executeScript("PF('panelModificar').show();");

		current.executeScript("crearMapaEditableConPolygones('mapaModificar');");
	}

	public void cargarEntidadesEnMapa(Predio predio) {
		try {
			potreros = potrerosEJBbean.obtenerListaTodos(predio.getId());
			objectMapper.registerModule(new JtsModule());
			potrerosJson = objectMapper.writeValueAsString(potreros);
			zonas = zonasGeograficasEjb.obtenerListaTodosActivas(predio.getId());
			zonasJson = objectMapper.writeValueAsString(zonas);
			forma = predio.getForma();
			cor = forma.getCoordinates();
			formaJson = gson.toJson(cor);
		} catch (PotrerosException | JsonProcessingException e) {
			messege = e.getMessage();
			// TODO Auto-generated catch block
			e.printStackTrace();
			mostrarMensaje();
		}
	}

	/**
	 * 
	 * Carga los datos de potreros y muestra un dialog para modificar sus datos
	 * 
	 */
	public void iniciarModificacionPotreros(Predio predio) {
		borrarValores();
		predioElegido = predio;
		potreros = predio.getPotreros();
		cargarEntidadesEnMapa(predio);
		activo = predio.getActivo();
		descripcion = predio.getDescripcion();

		nombre = predio.getNombre();

		current.executeScript("PF('panelModificarPotreros').show();");

		current.executeScript("crearMapaModificarPotreros('mapaModificarPotreros');");
	}

	/**
	 * 
	 * Carga los datos de zonas y muestra un dialog para modificar sus datos
	 * 
	 */
	public void iniciarModificacionZonas(Predio predio) {
		borrarValores();
		predioElegido = predio;
		potreros = predio.getPotreros();
		cargarEntidadesEnMapa(predio);
		activo = predio.getActivo();
		descripcion = predio.getDescripcion();
		nombre = predio.getNombre();
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('panelModificarZonas').show();");
		current.executeScript("crearMapaModificarZonas('mapaModificarZonas');");
	}

	/**
	 * 
	 * Muestra los detalles del predio pero sin poder modificarlo
	 * 
	 */
	public void verDetalles(Predio predio) {
		PrimeFaces current = PrimeFaces.current();
		predioElegido = predio;
		activo = predio.getActivo();
		descripcion = predio.getDescripcion();
		forma = predio.getForma();
		potreros = predio.getPotreros();
		nombre = predio.getNombre();
		zonas = predio.getZonas();
		areaEnHectareas = predio.getAreaEnHectareas();
		propietarios = predio.getPropietarios();
		cargarEntidadesEnMapa(predio);
		current.executeScript("PF('detalles').show();");
		current.executeScript("crearMapaNoModificable('mapid');");
	}
	/**
	 * 
	 * Muestra un dialog de exito o error
	 * 
	 */
	public void mostrarMensaje() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('messegeDiolog').show();");
	}
	/**
	 * 
	 * Abre el historico del potrero
	 * 
	 */
	public void mostrarHistorico() {
		current.executeScript("PF('historico').show();");
	}

	public void cambiarIndicador(Indicador indicador) {

		System.out.println(indicador + "********");
	}

	/**
	 * 
	 * Borra los valores temporales del bean
	 * 
	 */
	public void borrarValores() {
		//// alerta
		zonasEliminadas = new ArrayList<ZonaGeografica>();
		potrerosEliminados = new ArrayList<Potrero>();
		predioElegido = null;
		propietarios = null;
		activo = null != null;
		descripcion = null;
		forma = null;
		potreros = null;
		nombre = null;
		zonas = null;
		messege = "";
		zonasJson = "";
		formaJson = "";

	}

	public String getMessege() {
		return messege;
	}

	public void setMessege(String messege) {
		this.messege = messege;
	}

	// la siguiente funcion controla el filtrado del buscador
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		Predio predio = (Predio) value;
		return predio.getId().toString().toLowerCase().contains(filterText)
				|| predio.getNombre().toLowerCase().contains(filterText);
	}

	public List<Predio> getListaPrediosFiltrada() {
		return listaPrediosFiltrada;
	}

	public void setListaPrediosFiltrada(List<Predio> listaPrediosFiltrada) {
		this.listaPrediosFiltrada = listaPrediosFiltrada;
	}

	public MapModel getMapaPredio() {
		return mapaPredio;
	}

	public void setMapaPredio(MapModel mapaPredio) {
		this.mapaPredio = mapaPredio;
	}

	/**
	 * 
	 * Activa o desactiva un predio
	 * 
	 */
	public void borrarPredio(Predio predio) {

		try {
			if (predio.getActivo()) {

				prediosEJBBean.borrarPredio(predio);

			} else {
				prediosEJBBean.activarPredio(predio);

			}
			obtenerListaPredios();

		} catch (PotrerosException e) {
			// TODO Auto-generated catch block
			messege = e.getMessage();
			e.printStackTrace();
			mostrarMensaje();
		}
	}

	/**
	 * 
	 * Guarda la modificacion de datos de potreros en la bd
	 * 
	 */
	public void modificarPotreros() {
		boolean validado = true;
		for (Potrero potrero : potreros) {
			if (potrero.getIndicadorActual() == null) {
				messege = "Debes seleccionar un indicador para " + potrero.getNombre();
				validado = false;
			}
		}
		if (validado) {

			try {
				for (Potrero potreroDelPredio : potrerosEJBbean.obtenerListaTodos(predioElegido.getId())) {

					if (potrerosEliminados.contains(potreroDelPredio)) {
						System.out.println("true");
						try {
							potrerosEJBbean.borrarPotrero(potreroDelPredio);
						} catch (PotrerosException e) {
							// TODO Auto-generated catch block
							messege = e.getMessage();

							e.printStackTrace();
						}
					}

				}
			} catch (PotrerosException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				messege = e1.getMessage();

			}

			for (Potrero potrero : potreros) {

				try {
					if (potrero.getId() == null || potrero.getId() <= 0) {
						potrero.setId(-1);// ???
						potrerosEJBbean.altaPotrero(potrero);

					}

					else {
						potrerosEJBbean.editarPotrero(potrero);
					}
				} catch (PotrerosException e) {
					// TODO Auto-generated catch block
					messege = e.getMessage();

					e.printStackTrace();
				}
			}

			predioElegido.setPotreros(potreros);
			try {
				potrerosEJBbean.guardarTodo();
				prediosEJBBean.editarPredio(predioElegido);
				messege = "Potreros modificados Existosamente";
				potreros = potrerosEJBbean.obtenerListaTodos(predioElegido.getId());
				potrerosEliminados = new ArrayList<Potrero>();
				potrerosEJBbean.renicializar();
				current.executeScript("PF('panelModificarPotreros').hide();");

			} catch (PotrerosException   e) {
				// TODO Auto-generated catch block
				messege = e.getMessage();

				e.printStackTrace();
			}
		}
		mostrarMensaje();

	}

	/**
	 * 
	 * Guarda la modificacion de datos de zona en la bd
	 * 
	 */
	public void modificarZona() {
		boolean validado = true;
		for (ZonaGeografica zona : zonas) {
			if (zona.getTipoZona() == null) {
				messege = "Debes seleccionar un tipo de zona para: " + zona.getNombre();
				validado = false;
			}
		}
		System.out.println(validado + " valor validado");
		if (validado) {
			try {
				for (ZonaGeografica zonaDelPotrero : zonasGeograficasEjb
						.obtenerListaTodosActivas(predioElegido.getId())) {

					if (zonasEliminadas.contains(zonaDelPotrero)) {
						try {
							zonasGeograficasEjb.borrarZonaGeografica(zonaDelPotrero);
						} catch (PotrerosException e) {
							// TODO Auto-generated catch block
							messege = e.getMessage();

							e.printStackTrace();
							break;
						}
					}
				}
			} catch (PotrerosException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				messege = e1.getMessage();
			}
			for (ZonaGeografica zona : zonas) {
				System.out.println(zona.getTipoZona());
				try {
					if (zona.getId() == -1) {
						zonasGeograficasEjb.altaZonaGeografica(zona);
						
					} else {
						System.out.println(validado + "Editando");

						zonasGeograficasEjb.editarZonaGeografica(zona);
					}
				} catch (PotrerosException e) {
					// TODO Auto-generated catch block
					messege = e.getMessage();
					e.printStackTrace();
				}
			}
			try {
				zonasGeograficasEjb.guardarTodo();
				prediosEJBBean.editarPredio(predioElegido);
				zonas = new ArrayList<ZonaGeografica>();
				zonas = zonasGeograficasEjb.obtenerListaTodosActivas(predioElegido.getId());
				zonasEliminadas = new ArrayList<ZonaGeografica>();
				zonasGeograficasEjb.reinicializar();
				messege = "Zonas modificados Existosamente";
				current.executeScript("PF('panelModificarZonas').hide();");

			} catch (PotrerosException e) {
				// TODO Auto-generated catch block
				messege = e.getMessage();
				e.printStackTrace();
			}
		}
		mostrarMensaje();
	}

	/**
	 * 
	 * Crea un potrero con los datos del dibujo en js pero aun no lo guarda en la bd
	 * 
	 */
	public void crearPotreroPreeliminar(String potrero) {
		System.out.println("hola");

		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		Potrero nuevoPotrero = new Potrero();
	
		forma = obtenerFormaModificada("potrero", params);;
		nuevoPotrero.setId(Integer.parseInt(params.get("id")));
		nuevoPotrero.setForma(forma);
		nuevoPotrero.setActivo(true);
		nuevoPotrero.setNombre("Potrero-" + (potreros.size() + 1) + "-" + predioElegido.getNombre());
		nuevoPotrero.setDescripcion("Potrero-" + (potreros.size() + 1));
		potreros.add(nuevoPotrero);
		for (Potrero potreroPrueba : potreros) {
			System.out.println(potreroPrueba.getNombre());
			System.out.println(potreroPrueba.getIndicadorActual());
		}
		;
	}

	/**
	 * 
	 * Modifica un predio con los datos del dibujo en js pero aun no lo guarda en la
	 * bd
	 * 
	 */

	public void modificarPotreroPreeliminarmente(String potrero) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		Potrero potreroModificado = new Potrero();
		int id = Integer.parseInt(params.get("id"));
		for (Potrero p : potreros) {
			if (p.getId() == id) {
				potreroModificado = p;
			}
		}
		potreros.remove(potreroModificado);
		forma = obtenerFormaModificada("potrero",params );
		potreroModificado.setForma(forma);
		potreros.add(potreroModificado);

	}
	
	/**
	 * 
	 * Crea una zona en memoria pero aun no la guarda
	 * 
	 */
	
	public void modificarZonaPreeliminarmente(String zona) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		ZonaGeografica zonaModificado = new ZonaGeografica();
		int id = Integer.parseInt(params.get("id"));
		for (ZonaGeografica p : zonas) {
			if (p.getId() == id) {
				zonaModificado = p;
			}
		}
		zonas.remove(zonaModificado);
		forma = obtenerFormaModificada("zona",params );
		zonaModificado.setForma(forma);
		zonas.add(zonaModificado);

	}
	/**
	 * 
	 * Obtiene el polygono dibujado en js 
	 * 
	 */
	
	public com.vividsolutions.jts.geom.Polygon obtenerFormaModificada(String tipoEntidad,Map<String, String> params){
		JsonObject jsonZonaNuevo = gson.fromJson(params.get(tipoEntidad), JsonObject.class);
		JsonObject jsonPolygonoNuevo = gson.fromJson(jsonZonaNuevo.get("geometry"), JsonObject.class);

		JsonArray jsonZonaProcesada = jsonPolygonoNuevo.getAsJsonArray("coordinates");

		JsonArray cordenadasEnJson = (JsonArray) jsonZonaProcesada.get(0);
		Coordinate[] cordenadasLista = new Coordinate[cordenadasEnJson.size()];

		for (int i = 0; i < cordenadasEnJson.size(); i++) {
			JsonArray jsoncordenadaEnFor = (JsonArray) cordenadasEnJson.get(i);
			Coordinate cordenadaCreada = new Coordinate(jsoncordenadaEnFor.get(1).getAsDouble(),
					jsoncordenadaEnFor.get(0).getAsDouble());
			cordenadasLista[i] = cordenadaCreada;
		}
		forma = geometryFactory.createPolygon(cordenadasLista);
		return forma;
	}

	/**
	 * 
	 * Modifica un zona con los datos del dibujo en js pero aun no lo guarda en la
	 * bd
	 * 
	 */
	public void crearZonaPreeliminar(String zona) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();

		ZonaGeografica zonaGeograficaModificado = new ZonaGeografica();
		int id = Integer.parseInt(params.get("id"));

		for (ZonaGeografica p : zonas) {
			if (p.getId() == id) {
				zonaGeograficaModificado = p;
			}
		}
		zonas.remove(zonaGeograficaModificado);
		
		forma = obtenerFormaModificada("zona", params);
		zonaGeograficaModificado.setForma(forma);
		zonaGeograficaModificado.setId(Integer.parseInt(params.get("id")));
		zonaGeograficaModificado.setForma(forma);
		zonaGeograficaModificado.setActivo(true);
		zonaGeograficaModificado.setNombre("Zona-" + (zonas.size() + 1));
		zonaGeograficaModificado.setDescripcion("Zona-" + (zonas.size() + 1));
		zonas.add(zonaGeograficaModificado);
	}

	/**
	 * 
	 * Borra un potrero en la sesion pero aun no guarda el borrado en la bs
	 * 
	 */
	public void borrarPotrero(Potrero potrero) {
		potrerosEliminados.add(potrero);
		potreros.remove(potrero);
		String id = potrero.getId().toString();
		current.executeScript("removerPotrero('" + id + "');");
	}

	/**
	 * 
	 * Borra una zona en la sesion pero aun no guarda el borrado en la bs
	 * 
	 */
	public void borrarZona(ZonaGeografica zona) {
		String id;
		System.out.println(zona.getId());
		if (zona.getId() == null) {
			id = zona.getId().toString();
		} else {
			id = zona.getId().toString();
		}
		zonasEliminadas.add(zona);
		current.executeScript("removerZona('" + id + "');");
		zonas.remove(zona);
	}
	

	
	
	public void cambiar() {
		
	}
	
	
	
	
	@Override
	public Predio getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		System.out.println(submittedValue + "submited value che");

		if (submittedValue.trim().equals("")) {
			System.out.println(submittedValue + "nuleado");

			return null;
		} else {

			try {
				// int number = Integer.parseInt(submittedValue);

				for (Predio s : listaPredios) {
					if (s.getNombre().equals(submittedValue)) {
						System.out.println("convirtiendo");
						return s;
					}
				}

			} catch (NumberFormatException exception) {
				throw new ConverterException(
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));
			}
		}

		return null;
	}
	
	
	
	
	

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Predio value) {
		// TODO Auto-generated method stub
		if (value == null || value.equals("")) {
			return "";
		} else {
			return value.getNombre();
		}
	}
}
