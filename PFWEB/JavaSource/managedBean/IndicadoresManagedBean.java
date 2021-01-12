package managedBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.primefaces.PrimeFaces;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.BezierConnector;
import org.primefaces.model.diagram.connector.FlowChartConnector;
import org.primefaces.model.diagram.connector.StraightConnector;
import org.primefaces.model.diagram.endpoint.BlankEndPoint;
import org.primefaces.model.diagram.endpoint.DotEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;

import com.g13.pdt.entidades.Indicador;
import com.g13.pdt.entidades.IndicadorPotrero;
import com.g13.pdt.entidades.Potrero;
import com.g13.pdt.entidades.Predio;
import com.g13.pdt.excepciones.DatosInvalidosException;
import com.g13.pdt.excepciones.NoExisteElementoException;
import com.g13.pdt.excepciones.PotrerosException;
import com.g13.pdt.interfaces.IIndicadores;
import com.g13.pdt.interfaces.IPotreros;
import com.g13.pdt.interfaces.IPredios;

/**
 * 
 * Esta clase contiene diferentes motodos para la administracion de indicadores
 * 
 * 
 * @author: G13 S.A
 * 
 * @version: 28/06/2020
 * 
 * @see <a href = "www.utec.edu.uy" />
 * 
 */
@ManagedBean(name = "indicadoresBean")
@SessionScoped
public class IndicadoresManagedBean implements Converter<Indicador> {
	private PrimeFaces current = PrimeFaces.current();

	List<Indicador> indicadoresTodos;
	private LineChartModel model = new LineChartModel();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
	private LineChartModel modelArea = new LineChartModel();
	private Predio predioElegido;
	private DefaultDiagramModel diagramModel;

	public CartesianChartModel getModelArea() {
		return modelArea;
	}
	

	
	
	private String predioTotal = "";
	
	private String slevalue = "";
	private String noslevalue = "";
	private String vmvalue = "";
	private String recriavalue = "";
	private String sombrasvalue = "";
	private String instalacionesvalue = "";
	private String camineriavalue = "";
	private String reservanopplvalue = "";
	private String pplvalue = "";
	private String vacassecasvalue = "";
	private String reservapplvalue = "";
	private String nosepvlvalue = "";
	private String sepvovalue = "";


	public void setModelArea(LineChartModel modelArea) {
		this.modelArea = modelArea;
	}

	public List<Indicador> getIndicadoresTodos() {
		return indicadoresTodos;
	}

	public void setIndicadoresTodos(List<Indicador> indicadoresTodos) {
		this.indicadoresTodos = indicadoresTodos;
	}

	@EJB
	IIndicadores indicadoreEjb;

	public void obtenerListaIndicadores() {
		indicadoresTodos = indicadoreEjb.obtenerListaSinHijosActivos();
	}

	@EJB
	private IPotreros potrerosEJBbean;

	public LineChartModel getModel() {
		return model;
	}

	public void setModel(LineChartModel model) {
		this.model = model;
	}

	public DefaultDiagramModel getDiagramModel() {
		return diagramModel;
	}

	public void setDiagramModel(DefaultDiagramModel diagramModel) {
		this.diagramModel = diagramModel;
	}
	
	@EJB
	IPredios prediosEjb;
	
	
	
	
	
	/**
	 * 
	 * Obtiene los datos para la grafica del historico de predio 
	 * 
	 */
	public void obtenerIndicadoresPredio(Predio predio) {
		List<IndicadorPotrero> indicadores = new ArrayList<IndicadorPotrero>();
		Calendar c = Calendar.getInstance();
		model = new LineChartModel();
		indicadoresTodos = indicadoreEjb.obtenerListaSinHijosActivos();
		String colores = "";
		try {
			List<Potrero> potreros = potrerosEJBbean.obtenerListaTodos(predio.getId());
			for (Potrero potrero : potreros) {
				indicadores.addAll(potrero.getIndicadorPotreros());
			}
			Calendar fechaDesde = Calendar.getInstance();
			Calendar fechaHasta = Calendar.getInstance();
			Calendar fechaActual = Calendar.getInstance();
			for (Indicador indicador : indicadoresTodos) {
				colores = colores + indicador.getColor() + ", ";
				LineChartSeries nuevoIndicador = new LineChartSeries();
				nuevoIndicador.setLabel(indicador.getNombre());
				List<IndicadorPotrero> nuevoIndicadorLista = new ArrayList<IndicadorPotrero>();
				Date fechaMinima = new Date();
				for (IndicadorPotrero i : indicadores) {
					if (i.getIndicador().getId() == indicador.getId()) {
						nuevoIndicadorLista.add(i);
					}
					if (fechaMinima.after(i.getDesde())) {
						fechaMinima = i.getDesde();
					}
				}
				c.setTime(fechaMinima);
				c.getActualMaximum(Calendar.DAY_OF_MONTH);
				fechaActual.setTime(new Date());
				while (c.before(fechaActual)) {
					Double area = 0.0;
					for (IndicadorPotrero ind : nuevoIndicadorLista) {
						fechaDesde.setTime(ind.getDesde());
						if (ind.getHasta() != null) {
							fechaHasta.setTime(ind.getHasta());
						} else {
							fechaHasta.setTime(new Date());
						}
						if (c.before(fechaHasta) && c.after(fechaDesde)) {
							System.out.println("true");
							area = area + ind.getPotrero().getAreaEnHectareas();
						}
					}
					nuevoIndicador.set(format.format(c.getTime()), area);
					c.add(Calendar.MONTH, 1);
					c.getActualMaximum(Calendar.DAY_OF_MONTH);
				}
				model.addSeries(nuevoIndicador);

			}

			Axis yAxis = model.getAxis(AxisType.Y);

			model.setTitle("Historico del predio");
			model.setLegendPosition("e");
			model.setShowPointLabels(true);
			model.getAxes().put(AxisType.X, new CategoryAxis("Fecha"));
			yAxis = model.getAxis(AxisType.Y);
			yAxis.setLabel("Hectareas");
			current.executeScript("PF('historico').show();");

		} catch (NoExisteElementoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * Crea los datos para el diagrama de indicadores
	 * 
	 */

	public void obtenerIndicadoresPredioParaDiagrama() {

		diagramModel = new DefaultDiagramModel();
		diagramModel.setMaxConnections(-1);
		diagramModel.setConnectionsDetachable(false);
	    Element totalPredio = new Element("TOTAL PREDIO " +predioTotal , "45em", "1em");
	    totalPredio.addEndPoint(createEndPoint(EndPointAnchor.BOTTOM));
        diagramModel.addElement(totalPredio);

	        Element sle = new Element("SLE " +slevalue, "35em", "6em");
	        sle.addEndPoint(createEndPoint(EndPointAnchor.TOP));
	        sle.addEndPoint(createEndPoint(EndPointAnchor.BOTTOM));
	        diagramModel.addElement(sle);

	        Element noSle = new Element("NO SLE "+noslevalue, "55em", "6em");
	        noSle.addEndPoint(createEndPoint(EndPointAnchor.TOP));
	        noSle.addEndPoint(createEndPoint(EndPointAnchor.BOTTOM));
	        diagramModel.addElement(noSle);
	        
	        
	        Element vm = new Element("VM "+vmvalue, "28em", "12em");
	        vm.addEndPoint(createEndPoint(EndPointAnchor.TOP));
	        vm.addEndPoint(createEndPoint(EndPointAnchor.BOTTOM));
	        diagramModel.addElement(vm);

	        
	        Element recria = new Element("RECRIA " + recriavalue, "42em", "12em");
	        recria.addEndPoint(createEndPoint(EndPointAnchor.TOP));
	        recria.addEndPoint(createEndPoint(EndPointAnchor.BOTTOM));
	        diagramModel.addElement(recria);
	        
	        Element sombras = new Element("SOMBRAS " + sombrasvalue, "5em", "18em");
	        sombras.addEndPoint(createEndPoint(EndPointAnchor.TOP));
	        diagramModel.addElement(sombras);
	        
	        Element instalaciones = new Element("INSTALACIONES " + instalacionesvalue, "18em", "18em");
	        instalaciones.addEndPoint(createEndPoint(EndPointAnchor.TOP));
	        diagramModel.addElement(instalaciones);
	        
	        Element camineria = new Element("CAMINERIA " + camineriavalue, "31em", "18em");
	        camineria.addEndPoint(createEndPoint(EndPointAnchor.TOP));
	        diagramModel.addElement(camineria);
	        
	        Element reservaNoPPL = new Element("RESERVA NO PPL " + reservanopplvalue, "44em", "18em");
	        reservaNoPPL.addEndPoint(createEndPoint(EndPointAnchor.TOP));
	        diagramModel.addElement(reservaNoPPL);
	        
	        Element ppl = new Element("PPL " + pplvalue, "57em", "18em");
	        ppl.addEndPoint(createEndPoint(EndPointAnchor.TOP));
	        ppl.addEndPoint(createEndPoint(EndPointAnchor.BOTTOM));
	        diagramModel.addElement(ppl);
	        
	        Element vacasSecas = new Element("VACAS SECAS " +vacassecasvalue, "70em", "18em");
	        vacasSecas.addEndPoint(createEndPoint(EndPointAnchor.TOP));
	        diagramModel.addElement(vacasSecas);

	        
	        
	        
	        
	        
	        Element reservasPPL = new Element("RESERVAS PPL " +reservapplvalue, "35em", "24em");
	        reservasPPL.addEndPoint(createEndPoint(EndPointAnchor.TOP));
	        diagramModel.addElement(reservasPPL);   
	        Element nospvo = new Element("NO SEPVO " + nosepvlvalue, "50em", "24em");
	        nospvo.addEndPoint(createEndPoint(EndPointAnchor.TOP));
	        diagramModel.addElement(nospvo);   
	        Element sepvo = new Element("SEPVO " +sepvovalue, "65em", "24em");
	        sepvo.addEndPoint(createEndPoint(EndPointAnchor.TOP));
	        diagramModel.addElement(sepvo);
	        
	        
	        
	        
	        
	        
	        
	        
	        
	       
	        FlowChartConnector	 connector = new FlowChartConnector();
	        connector.setPaintStyle("{strokeStyle:'#00aae4', lineWidth:3}");
	        connector.setHoverPaintStyle("{strokeStyle:'#00aae4'}");

		// connections
	        diagramModel.connect(new Connection(totalPredio.getEndPoints().get(0), sle.getEndPoints().get(0), connector));        
	        diagramModel.connect(new Connection(totalPredio.getEndPoints().get(0), noSle.getEndPoints().get(0), connector));
	        diagramModel.connect(new Connection(sle.getEndPoints().get(1), vm.getEndPoints().get(0), connector));
	        diagramModel.connect(new Connection(sle.getEndPoints().get(1), recria.getEndPoints().get(0), connector));  
	        diagramModel.connect(new Connection(vm.getEndPoints().get(1), sombras.getEndPoints().get(0), connector));
	        diagramModel.connect(new Connection(vm.getEndPoints().get(1), instalaciones.getEndPoints().get(0), connector));
	        diagramModel.connect(new Connection(vm.getEndPoints().get(1), camineria.getEndPoints().get(0), connector));
	        diagramModel.connect(new Connection(vm.getEndPoints().get(1), reservaNoPPL.getEndPoints().get(0), connector));
	        diagramModel.connect(new Connection(vm.getEndPoints().get(1), ppl.getEndPoints().get(0), connector));
	        diagramModel.connect(new Connection(vm.getEndPoints().get(1), vacasSecas.getEndPoints().get(0), connector));
	        diagramModel.connect(new Connection(ppl.getEndPoints().get(1), reservasPPL.getEndPoints().get(0), connector));
	        diagramModel.connect(new Connection(ppl.getEndPoints().get(1), nospvo.getEndPoints().get(0), connector));
	        diagramModel.connect(new Connection(ppl.getEndPoints().get(1), sepvo.getEndPoints().get(0), connector));

//	        diagramModel.connect(new Connection(sle.getEndPoints().get(1), pur.getEndPoints().get(0), connector));
//	        diagramModel.connect(new Connection(noSle.getEndPoints().get(1), dev.getEndPoints().get(0), connector));
//	        diagramModel.connect(new Connection(noSle.getEndPoints().get(1), tst.getEndPoints().get(0), connector));

	}

	private EndPoint createEndPoint(EndPointAnchor anchor) {
		BlankEndPoint endPoint = new BlankEndPoint(anchor);
		endPoint.setStyle("{fillStyle:'#404a4e'}");
		endPoint.setHoverStyle("{fillStyle:'#20282b'}");

		return endPoint;
	}

	@Override
	public Indicador getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		List<Indicador> indicadoresTodos = indicadoreEjb.obtenerListaTodos();
		System.out.println(submittedValue + "submited value che");
		diagramModel = new DefaultDiagramModel();

		if (submittedValue.trim().equals("")) {
			System.out.println(submittedValue + "nuleado");

			return null;
		} else {

			try {
				// int number = Integer.parseInt(submittedValue);

				for (Indicador s : indicadoresTodos) {
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
	
	
	
	
	
	public void cargarIndicadoresPredioActuales() {
		//predioTotal = redondearArea(predio.getAreaEnHectareas()) + "ha";
		//System.out.println(redondearArea(predio.getAreaEnHectareas()));
		slevalue = "";
		noslevalue = "";
		vmvalue = "";
		recriavalue = "";
		sombrasvalue = "";
		instalacionesvalue = "";
		camineriavalue = "";
		reservanopplvalue = "";
		pplvalue = "";
		vacassecasvalue = "";
		reservapplvalue = "";
		nosepvlvalue = "";
		sepvovalue = "";
		
		
		if(predioElegido!= null){
			try {
				
				
				
				System.out.println("cargando datos");
				
				
				
				
				
				predioTotal =  redondearArea(indicadoreEjb.obtenerIndicadorParaPredio("TOTAL PREDIO", predioElegido.getId()).getArea()) + "ha";
				slevalue = redondearArea(indicadoreEjb.obtenerIndicadorParaPredio("SLE", predioElegido.getId()).getArea()) + "ha";
				noslevalue = redondearArea(indicadoreEjb.obtenerIndicadorParaPredio("NO SLE", predioElegido.getId()).getArea()) + "ha";
				vmvalue = redondearArea(indicadoreEjb.obtenerIndicadorParaPredio("VM", predioElegido.getId()).getArea()) + "ha";
				recriavalue = redondearArea(indicadoreEjb.obtenerIndicadorParaPredio("RECRIA", predioElegido.getId()).getArea()) + "ha";
				sombrasvalue = redondearArea(indicadoreEjb.obtenerIndicadorParaPredio("SOMBRAS", predioElegido.getId()).getArea()) + "ha";
				instalacionesvalue = redondearArea(indicadoreEjb.obtenerIndicadorParaPredio("INSTALACIONES", predioElegido.getId()).getArea()) + "ha";
				camineriavalue = redondearArea(indicadoreEjb.obtenerIndicadorParaPredio("CAMINERIA", predioElegido.getId()).getArea()) + "ha";
				reservanopplvalue = redondearArea(indicadoreEjb.obtenerIndicadorParaPredio("RESERVAS NO PPL", predioElegido.getId()).getArea()) + "ha";
				pplvalue = redondearArea(indicadoreEjb.obtenerIndicadorParaPredio("PPL", predioElegido.getId()).getArea()) + "ha";
				vacassecasvalue = redondearArea(indicadoreEjb.obtenerIndicadorParaPredio("VACAS SECAS", predioElegido.getId()).getArea()) + "ha";
				reservapplvalue = redondearArea(indicadoreEjb.obtenerIndicadorParaPredio("RESERVAS PPL", predioElegido.getId()).getArea()) + "ha";
				nosepvlvalue = redondearArea(indicadoreEjb.obtenerIndicadorParaPredio("NO SEPVO", predioElegido.getId()).getArea()) + "ha";
				sepvovalue = redondearArea(indicadoreEjb.obtenerIndicadorParaPredio("SEPVO", predioElegido.getId()).getArea()) + "ha";
			
			
				obtenerIndicadoresPredioParaDiagrama();
			} catch (PotrerosException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Indicador value) {
		// TODO Auto-generated method stub
		if (value == null || value.equals("")) {
			return "";
		} else {
			return value.getNombre();
		}
	}

	public Predio getPredioElegido() {
		return predioElegido;
	}

	public void setPredioElegido(Predio predioElegido) {
		this.predioElegido = predioElegido;
	}
	
	
	
	public void imprimirPredio() {
		System.out.println(predioElegido.getNombre());
	}
	
	
	public String formatearFecha(Date i) {
		//System.out.println(i);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		
		
		if( i== null) {
			return "";


		}
		return formatter.format(i);

	}
	
	
	 public String redondearArea(Double i) {
	    	
	    	BigDecimal bd = new BigDecimal(i);
	    	bd = bd.setScale(2, RoundingMode.HALF_UP);
	    	
	    	System.out.println(bd);
			return bd.toString();
	    	
	    }
}
