package com.g13.pdt.servicios;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.g13.pdt.entidades.Indicador;
import com.g13.pdt.entidades.Potrero;
import com.g13.pdt.entidades.Predio;
import com.g13.pdt.entidades.Propietario;
import com.g13.pdt.entidades.Rol;
import com.g13.pdt.entidades.TipoZona;
import com.g13.pdt.entidades.Usuario;
import com.g13.pdt.entidades.ZonaGeografica;
import com.g13.pdt.entidades.ZonaPotrero;
import com.g13.pdt.enumerados.TipoDocumento;
import com.g13.pdt.excepciones.IndicadorConPotrerosException;
import com.g13.pdt.excepciones.YaExisteElementoException;
import com.g13.pdt.interfaces.EntidadesConForma;
import com.g13.pdt.interfaces.IAltasAutomaticas;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

@Stateless
@Remote
public class AltasAutomaticasBeanRemote implements IAltasAutomaticas {

	@PersistenceContext
	private EntityManager em;
	
	
    public AltasAutomaticasBeanRemote() {
        
    }

	private void autocargarRol(String nombre) {
		TypedQuery<Rol> query = this.em.createQuery("SELECT r FROM Rol r where r.nombre=:nombre",Rol.class);
		query.setParameter("nombre", nombre);
		Rol  rol = null;
		try {
			rol = query.getSingleResult();
		}catch(NoResultException e){
			//Si no existe el admin.... 
			rol = new Rol();
			rol.setNombre(nombre);
			this.em.persist(rol);
			
		}
	}

	private void autocargarRoles() {
		//Cargar datos de prueba para admin si no existen
		this.autocargarRol("admin");
		this.autocargarRol("master");
		this.autocargarRol("Usuario invitado");
		this.em.flush();	//No necesito catch ya que no deber�a existir
	}
	
	@Override
	public Usuario crearAdmin() throws YaExisteElementoException {
		
		this.autocargarRoles();
		
	/***********************************************************
	 * ************** AUTO CARGAR USERADMIN ********************
	 * *********************************************************/
		//Cargar datos de prueba para admin si no existen
		TypedQuery<Usuario> query = this.em.createQuery("SELECT u FROM Usuario u where u.username=:username",Usuario.class);
		query.setParameter("username", "admin");
		Usuario  admin = null;
		try {
			admin = query.getSingleResult();
		}catch(NoResultException e){
			//Si no existe el admin.... 
			admin = new Usuario();
			admin.setNombres("admin");
			admin.setActivo(true);
			admin.setApellidos("admin");
			admin.setDocumento("admin");
			admin.setPassword("admin");
			admin.setTipoDocumento(TipoDocumento.CI);
			admin.setUsername("admin");
			admin.setCorreo("el_admin@potreros.com");
			admin.setDireccion("Sin direccion");
			this.em.persist(admin);
			this.em.flush();	//No necesito catch ya que no deber�a existir
			return admin;
		}
		
		admin.setPassword("admin");
		this.em.flush();
		return admin;
		
		
	}

	@Override
	public Indicador altaAutomaticaIndicadores() throws IndicadorConPotrerosException {
		TypedQuery<Indicador> query = this.em.createQuery("SELECT i FROM Indicador i where i.nombre=:nombre",Indicador.class);
		query.setParameter("nombre", "TOTAL PREDIO");
		Indicador  indicadorRaiz = null;
		try {
			indicadorRaiz = query.getSingleResult();
			//Si sigue para aca es porque ya existe el indicador
		}catch(NoResultException e){
			//Si no existe el indicador raiz.
			Date fecha = new Date();
			
			//PRIMER NIVEL - INDICADOR RAIZ, no tiene padre.
			indicadorRaiz = new Indicador("TOTAL PREDIO","�rea total del predio", fecha, "darkgreen");
			
			//SEGUNDO NIVEL
			Indicador indicadorSLE = new Indicador("SLE", "Superficie lechera", fecha, "white");
			Indicador indicadorNoSLE = new Indicador("NO SLE", "No es parte de la superficie lechera", fecha,"lightgoldenrodyellow");
			
			//TERCER NIVEL
			Indicador indicadorVM = new Indicador("VM", "Superficie Vaca Masa",fecha, "lightgray");
			Indicador indicadorRecria = new Indicador("RECRIA", "Superficie dedicada a Recr�a",fecha, "orange");
			
			//CUARTO NIVEL
			Indicador indicadorSombra = new Indicador("SOMBRAS", "Superficies destinadas a sombras",fecha,"black");
			Indicador indicadorInstalaciones = new Indicador("INSTALACIONES", "Superficies destinadas a instalaciones",fecha,"darkgray");
			Indicador indicadorCamineria = new Indicador("CAMINERIA", "Superficies dedicadas a caminer�a",fecha,"mintcream");
			Indicador indicadorReservaNoPpl = new Indicador ("RESERVAS NO PPL", "Area destinada a reservas fuera de la PPL",fecha,"darkorange");
			Indicador indicadorPPL = new Indicador("PPL", "Superficie de plataforma de pastoreo lechero",fecha,"green");
			Indicador indicadorVacasSecas = new Indicador("VACAS SECAS", "Superficie total destinada al pastoreo de vacas secas", fecha, "yellowgreen");
			
			//QUINTO NIVEL
			Indicador indicadorReservasPPL = new Indicador("RESERVAS PPL", "Superficie destinada a reservas PPL", fecha, "orangered");
			Indicador indicadorNOSEPVO = new Indicador("NO SEPVO","Area PPL no SEPVO",fecha, "red");
			Indicador indicadorSEPVO = new Indicador("SEPVO","Superficie Efectiva de Pastoreo de Vacas en Orde�e",fecha,"aliceblue");
			
			//QUINTO NIVEL
			indicadorPPL.addHijo(indicadorReservasPPL);
			indicadorPPL.addHijo(indicadorNOSEPVO);
			indicadorPPL.addHijo(indicadorSEPVO);
//			this.em.persist(indicadorPPL);
//			this.em.flush();
			
			//CUARTO NIVEL
			indicadorVM.addHijo(indicadorSombra);
			indicadorVM.addHijo(indicadorInstalaciones);
			indicadorVM.addHijo(indicadorCamineria);
			indicadorVM.addHijo(indicadorReservaNoPpl);
			indicadorVM.addHijo(indicadorPPL);
			indicadorVM.addHijo(indicadorVacasSecas);
//			this.em.persist(indicadorVM);
//			this.em.flush();
			
			indicadorSLE.addHijo(indicadorVM);
			indicadorSLE.addHijo(indicadorRecria);
//			this.em.persist(indicadorSLE);
//			this.em.flush();
			
			//SEGUNDO NIVEL
			indicadorRaiz.addHijo(indicadorSLE);
			indicadorRaiz.addHijo(indicadorNoSLE);
			this.em.persist(indicadorRaiz);			
			this.em.flush();
		}
		
		return indicadorRaiz;
	}

	@Override
	public void altaAutomaticaTipoZonas() {
		TipoZona[] lista = {
			new TipoZona("PIEDRAS", "gray"),
			new TipoZona("MONTES", "forestgreen"),
			new TipoZona("TAJAMAR", "blue"),
			new TipoZona("LAGUNA", "blue"),
			new TipoZona("ARROYO", "blue"),
			new TipoZona("BEBEDEROS", "blue"),
			new TipoZona("PRODUCTIVA", "floralwhite"),
			new TipoZona("TIERRA COLORADA", "red"),
			new TipoZona("TIERRA NEGRA", "sienna"),
			new TipoZona("INSTALACIONES", "yellow"),
			new TipoZona("PANTANO", "black"),
			new TipoZona("DESPERDICIOS", "beige")
		};
		for (TipoZona tipoZona : lista) {
			this.em.persist(tipoZona);
		}
		try {
			this.em.flush();
		}catch (PersistenceException e) {
			//Si da exepcion no pasa nada, es xq ya existen
			System.out.println("Ya estan creados los TipoZona");
		}
		
	}

	@Override
	public void altaAutomaticaPropietarios() {
		Date fecha = new Date();
		Propietario[] lista = {
				new Propietario("Jorge Melnik", "LTDA", "Salto 1245", "jorge.melnik@utec.edu.uy", "098433390", "Pedro: 098456321", true, fecha, new Long(1234567890)),
				new Propietario("Bruno Cornelius", "LTDA", "Artigas 2143", "bruno.cornelius@utec.edu.uy", "098123456", "Jose Martinez: 095321789", true, fecha, new Long(1237894560)),
				new Propietario("Florencia Ferrari", "LTDA", "Salto 2121", "florencia.ferrari@utec.edu.uy", "098456987", "Alberto: 098652321", true, fecha, new Long(1234564560)),
				new Propietario("UTEC", "UTEC", "Montevideo 4332", "contacto@utec.edu.uy", "23056542", "Claudia: 099654321", true, fecha, new Long(1567891230)),
				new Propietario("Los amigos", "SA", "Rio Negro 4563", "losamigos@amigos.com.uy", "096321963", "Nancy: 098741258", true, fecha, new Long(1472583690)),
				
			};
			for (Propietario propietario : lista) {
				this.em.persist(propietario);
			}
			try {
				this.em.flush();
			}catch (PersistenceException e) {
				//Si da exepcion no pasa nada, es xq ya existen
				System.out.println("No se pudieron crear los propietarios");
			}
			
	}
	@Override
	public void altaAutomaticaPredios() {
		TypedQuery<Indicador> queryIndicador = this.em.createQuery("SELECT i FROM Indicador i WHERE i.nombre=:nombre",Indicador.class); 
		queryIndicador.setParameter("nombre", "CAMINERIA");
		Indicador indicadorCamineria = queryIndicador.getSingleResult();
		queryIndicador = this.em.createQuery("SELECT i FROM Indicador i WHERE i.nombre=:nombre",Indicador.class); 
		queryIndicador.setParameter("nombre", "INSTALACIONES");
		Indicador indicadorInstalaciones = queryIndicador.getSingleResult();
		queryIndicador = this.em.createQuery("SELECT i FROM Indicador i WHERE i.nombre=:nombre",Indicador.class); 
		queryIndicador.setParameter("nombre", "NO SLE");
		Indicador indicadorNoSle = queryIndicador.getSingleResult();
		queryIndicador = this.em.createQuery("SELECT i FROM Indicador i WHERE i.nombre=:nombre",Indicador.class); 
		queryIndicador.setParameter("nombre", "RESERVAS NO PPL");
		Indicador indicadorReservasNoPPL = queryIndicador.getSingleResult();
		queryIndicador = this.em.createQuery("SELECT i FROM Indicador i WHERE i.nombre=:nombre",Indicador.class); 
		queryIndicador.setParameter("nombre", "RESERVAS PPL");
		Indicador indicadorReservasPPL = queryIndicador.getSingleResult();
		queryIndicador = this.em.createQuery("SELECT i FROM Indicador i WHERE i.nombre=:nombre",Indicador.class); 
		queryIndicador.setParameter("nombre", "NO SEPVO");
		Indicador indicadorNOSEPVO = queryIndicador.getSingleResult();
		queryIndicador = this.em.createQuery("SELECT i FROM Indicador i WHERE i.nombre=:nombre",Indicador.class); 
		queryIndicador.setParameter("nombre", "SEPVO");
		Indicador indicadorSEPVO = queryIndicador.getSingleResult();
		
		TypedQuery<Propietario> queryPropietario = this.em.createQuery("SELECT p FROM Propietario p WHERE p.nombre=:nombre",Propietario.class); 
		queryPropietario.setParameter("nombre", "UTEC");
		Propietario propietario = queryPropietario.getSingleResult();
		
		TypedQuery<TipoZona> queryTipoZona = this.em.createQuery("SELECT tz FROM TipoZona tz WHERE tz.nombre=:nombre",TipoZona.class); 
		queryTipoZona.setParameter("nombre", "MONTES");
		TipoZona tipoZonaMontes = queryTipoZona.getSingleResult();
		queryTipoZona = this.em.createQuery("SELECT tz FROM TipoZona tz WHERE tz.nombre=:nombre",TipoZona.class); 
		queryTipoZona.setParameter("nombre", "BEBEDEROS");
		TipoZona tipoZonaBebederos = queryTipoZona.getSingleResult();
		queryTipoZona = this.em.createQuery("SELECT tz FROM TipoZona tz WHERE tz.nombre=:nombre",TipoZona.class); 
		queryTipoZona.setParameter("nombre", "ARROYO");
		TipoZona tipoZonaArroyos = queryTipoZona.getSingleResult();
		queryTipoZona = this.em.createQuery("SELECT tz FROM TipoZona tz WHERE tz.nombre=:nombre",TipoZona.class); 
		queryTipoZona.setParameter("nombre", "INSTALACIONES");
		TipoZona tipoZonaInstalaciones = queryTipoZona.getSingleResult();
		queryTipoZona = this.em.createQuery("SELECT tz FROM TipoZona tz WHERE tz.nombre=:nombre",TipoZona.class); 
		queryTipoZona.setParameter("nombre", "TIERRA NEGRA");
		TipoZona tipoZonaTierraNegra = queryTipoZona.getSingleResult();
		queryTipoZona = this.em.createQuery("SELECT tz FROM TipoZona tz WHERE tz.nombre=:nombre",TipoZona.class); 
		queryTipoZona.setParameter("nombre", "PRODUCTIVA");
		TipoZona tipoZonaProductiva = queryTipoZona.getSingleResult();
		
		
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), EntidadesConForma.EPSG);
		Date fecha = new Date();
		Coordinate[] coordenadasPredio = {
				new Coordinate(-34.306917866842646,-57.20876097679138),
				new Coordinate(-34.30017335290119,-57.21819162368775),
				new Coordinate(-34.293157952019364,-57.21077799797059),
				new Coordinate(-34.2964419239163,-57.20613241195679),
				new Coordinate(-34.29467807912556,-57.20430850982667),
				new Coordinate(-34.298090509180405,-57.199652194976814),
				new Coordinate(-34.306917866842646,-57.20876097679138),
		};
		
		
		Coordinate[] coordenadasZona1 = {
				new Coordinate(-34.30294473584733, -57.21431648395101),new Coordinate(-34.3020921823634, -57.213578224182136),new Coordinate(-34.30162244987298, -57.213642597198486),new Coordinate(-34.301409739956505, -57.21388936042786),new Coordinate(-34.30091341472311, -57.21395373344422),new Coordinate(-34.300780469966135, -57.21420049667359),
				new Coordinate(-34.30055003188887, -57.214382886886604),new Coordinate(-34.300709566009694, -57.214683294296265),new Coordinate(-34.300931140674805, -57.2144365310669),new Coordinate(-34.301312247731424, -57.21430778503419),new Coordinate(-34.301684490163794, -57.21495151519776),new Coordinate(-34.30190606225692, -57.21501588821412),
				new Coordinate(-34.300762743982624, -57.216571569442756),new Coordinate(-34.300443675639976, -57.21618533134461),new Coordinate(-34.29959840096469, -57.21758403496487),new Coordinate(-34.3001733529012, -57.2181916236878),new Coordinate(-34.30294473584733, -57.21431648395101),
		};
		Coordinate[] coordenadasZona2 = {
				new Coordinate(-34.30169511603863, -57.21530291683897),new Coordinate(-34.301201460974646, -57.214871048927314),new Coordinate(-34.30059434695283, -57.21577763557435),new Coordinate(-34.30100292940783, -57.21624475584607),new Coordinate(-34.30169511603863, -57.21530291683897),
		};
		Coordinate[] coordenadasZona3 = {
				new Coordinate(-34.29893694764562, -57.20644354820252),new Coordinate(-34.2990610321488, -57.20672249794007),new Coordinate(-34.29936237946478, -57.206615209579475),new Coordinate(-34.29948646333948, -57.206861972808845),new Coordinate(-34.300394928980886, -57.20772027969361),new Coordinate(-34.30059434695283, -57.20740914344788),
				new Coordinate(-34.29970804123121, -57.206642031669624),new Coordinate(-34.299477600211645, -57.20652401447297),new Coordinate(-34.29945101082255, -57.206395268440254),new Coordinate(-34.29893694764562, -57.20644354820252),
		};
		Coordinate[] coordenadasZona4 = {
				new Coordinate(-34.29555934578293, -57.20521978282643),new Coordinate(-34.29622920088033, -57.20494151115418),new Coordinate(-34.296690100110766, -57.20524728298188),new Coordinate(-34.297199745354135, -57.20518827438355),new Coordinate(-34.29727065227339, -57.20504879951478),new Coordinate(-34.298538103358176, -57.20630407333374),
				new Coordinate(-34.29893694764562, -57.20644354820252),new Coordinate(-34.29945101082255, -57.206395268440254),new Coordinate(-34.29991189237535, -57.20626652240754),new Coordinate(-34.300682977010595, -57.20647573471069),new Coordinate(-34.301232481281275, -57.20632553100587),new Coordinate(-34.301462917486134, -57.206336259841926),
				new Coordinate(-34.301781981956786, -57.20599293708802),new Coordinate(-34.30239351880418, -57.205097079277046),new Coordinate(-34.30257520642982, -57.204909324645996),new Coordinate(-34.302805638950616, -57.204882502555854),new Coordinate(-34.30301113260541, -57.20472969233053),new Coordinate(-34.30294085957916, -57.20465717893659),
				new Coordinate(-34.30278348200493, -57.20481276512147),new Coordinate(-34.30250430398853, -57.204839587211616),new Coordinate(-34.29945544238798, -57.2062236070633),new Coordinate(-34.29918511646867, -57.206100225448616),new Coordinate(-34.29904330580242, -57.20626115798951),new Coordinate(-34.2987951365604, -57.20629334449769),
				new Coordinate(-34.297252925549195, -57.204893231391914),new Coordinate(-34.296995887627816, -57.205102443695075),new Coordinate(-34.296721122083525, -57.205113172531135),new Coordinate(-34.29622476914467, -57.20480203628541),new Coordinate(-34.29542560720784, -57.20508149057234),new Coordinate(-34.29555934578293, -57.20521978282643),
		};
		Coordinate[] coordenadasZona5 = {
				new Coordinate(-34.29904741642157, -57.21700177412939),new Coordinate(-34.300168921373654, -57.215391397476196),new Coordinate(-34.3005854839419, -57.21542358398438),new Coordinate(-34.30087796280848, -57.214833498001106),new Coordinate(-34.30078785799373, -57.214596102094916),new Coordinate(-34.300709566009694, -57.214683294296265),
				new Coordinate(-34.30061029886353, -57.21449637136036),new Coordinate(-34.30009801690095, -57.21475839614869),new Coordinate(-34.29995620777599, -57.214576005935676),new Coordinate(-34.29866218845056, -57.2135353088379),new Coordinate(-34.29812153063599, -57.21456527709962),new Coordinate(-34.29852924013028, -57.21489787101746),
				new Coordinate(-34.297912533008734, -57.21580246977071),new Coordinate(-34.29904741642157, -57.21700177412939),
		};
		Coordinate[] coordenadasZona6 = {
				new Coordinate(-34.30052792100006, -57.21453850644187),	new Coordinate(-34.30023539426244, -57.21421658992768),new Coordinate(-34.30032402469899, -57.21391081809998),	new Coordinate(-34.30007142770826, -57.21369624137879),
				new Coordinate(-34.29961081328674, -57.21429822720742),	new Coordinate(-34.299956207776, -57.2145760059357),new Coordinate(-34.3000980169009, -57.2147583961487),	new Coordinate(-34.30052792100006, -57.21453850644187),
		};
		Coordinate[] coordenadasZona7 = {
				new Coordinate(-34.299598400964705, -57.21758403496489),new Coordinate(-34.30044367564, -57.2161853313446),new Coordinate(-34.3007627439826, -57.2165715694428),
				new Coordinate(-34.3010029294078, -57.2162447558461),new Coordinate(-34.3005943469528, -57.2157776355743),new Coordinate(-34.3012014609746, -57.2148710489273),
				new Coordinate(-34.30169511603859, -57.21530291683899),new Coordinate(-34.3019060622569, -57.2150158882141),new Coordinate(-34.3016844901638, -57.2149515151978),
				new Coordinate(-34.3013122477314, -57.2143077850342),new Coordinate(-34.3009311406748, -57.2144365310669),new Coordinate(-34.30078785799372, -57.21459610209496),
				new Coordinate(-34.3008779628085, -57.2148334980011),new Coordinate(-34.3005854839419, -57.2154235839844),new Coordinate(-34.3001689213737, -57.2153913974762),
				new Coordinate(-34.2990474164216, -57.2170017741294),new Coordinate(-34.299598400964705, -57.21758403496489),
		};
		Coordinate[] coordenadasZona8 = {
				new Coordinate(-34.29932692689549, -57.21321880817414),new Coordinate(-34.29945101082255, -57.213084697723396),new Coordinate(-34.298976831970215, -57.2125267982483),new Coordinate(-34.29881286295914, -57.2127252817154),new Coordinate(-34.29932692689549, -57.21321880817414),
		};
		Coordinate[] coordenadasZona9 = {
				new Coordinate(-34.298307784487264, -57.21471721591629),new Coordinate(-34.29774558882959, -57.21562604910984),new Coordinate(-34.29791253300871, -57.215802469770686),new Coordinate(-34.2985292401303, -57.2148978710175),new Coordinate(-34.298307784487264, -57.21471721591629),
		};
		Coordinate[] coordenadasZona10 = {
				new Coordinate(-34.29812596227156, -57.212575078010566),new Coordinate(-34.298538103358176, -57.21214592456818),new Coordinate(-34.29806835099083, -57.211711406707764),new Coordinate(-34.29782461051977, -57.21194207668305),new Coordinate(-34.29803289787535, -57.21217274665833),new Coordinate(-34.29792653843916, -57.21230149269105),new Coordinate(-34.29812596227156, -57.212575078010566),
		};
		Coordinate[] coordenadasZona11 = {
				new Coordinate(-34.29636055185436, -57.20201274914423),new Coordinate(-34.29659703412376, -57.20220565795899),new Coordinate(-34.29779358895453, -57.20049977302552),new Coordinate(-34.29784581956235, -57.1999860778356),new Coordinate(-34.29636055185436, -57.20201274914423),
		};
		Coordinate[] coordenadasZona12 = {
				new Coordinate(-34.29636055185436, -57.20201274914423),new Coordinate(-34.2946780791256, -57.2043085098267),new Coordinate(-34.2954256072078, -57.2050814905723),new Coordinate(-34.2962247691447, -57.2048020362854),new Coordinate(-34.2967211220835, -57.2051131725311),new Coordinate(-34.2969958876278, -57.2051024436951),new Coordinate(-34.2972529255492, -57.2048932313919),new Coordinate(-34.2987951365604, -57.2062933444977),new Coordinate(-34.2990433058024, -57.2062611579895),new Coordinate(-34.2991851164687, -57.2061002254486),new Coordinate(-34.299455442388, -57.2062236070633),new Coordinate(-34.3025043039885, -57.2048395872116),new Coordinate(-34.3027834820049, -57.2048127651215),new Coordinate(-34.30294085957918, -57.20465717893662),new Coordinate(-34.2980905091804, -57.1996521949768),new Coordinate(-34.29784581956235, -57.1999860778356),new Coordinate(-34.29779358895453, -57.20049977302552),new Coordinate(-34.29659703412376, -57.20220565795899),new Coordinate(-34.29636055185436, -57.20201274914423),
		};
		Coordinate[] coordenadasZona13 = {
				new Coordinate(-34.298121530636, -57.2145652770996),new Coordinate(-34.2986621884506, -57.2135353088379),new Coordinate(-34.2988949466342, -57.213722501379316),new Coordinate(-34.29923101961378, -57.213126732504634),new Coordinate(-34.29881286295914, -57.2127252817154),new Coordinate(-34.2988842395384, -57.212638880810566),new Coordinate(-34.298426205064445, -57.21226244179859),new Coordinate(-34.29812596227156, -57.212575078010566),new Coordinate(-34.29792653843916, -57.21230149269105),new Coordinate(-34.29803289787535, -57.21217274665833),new Coordinate(-34.29782461051977, -57.21194207668305),new Coordinate(-34.29792304767474, -57.21184891818538),new Coordinate(-34.29762518597192, -57.211604118347175),new Coordinate(-34.30023771013063, -57.207571741305955),
				new Coordinate(-34.2994864633395, -57.2068619728088),new Coordinate(-34.2993623794648, -57.2066152095795),new Coordinate(-34.2990610321488, -57.2067224979401),new Coordinate(-34.2989369476456, -57.2064435482025),new Coordinate(-34.2985381033582, -57.2063040733337),new Coordinate(-34.2972706522734, -57.2050487995148),new Coordinate(-34.2971997453541, -57.2051882743836),new Coordinate(-34.2966901001108, -57.2052472829819),new Coordinate(-34.2962292008803, -57.2049415111542),new Coordinate(-34.2955593457829, -57.2052197828264),new Coordinate(-34.2964419239163, -57.2061324119568),new Coordinate(-34.2931579520194, -57.2107779979706),new Coordinate(-34.29774558882959, -57.21562604910984),new Coordinate(-34.298307784487264, -57.21471721591629),new Coordinate(-34.298121530636, -57.2145652770996),
		};
		Coordinate[] coordenadasZona14 = {
				new Coordinate(-34.3069178668426, -57.2087609767914),new Coordinate(-34.30301113260539, -57.20472969233051),new Coordinate(-34.3028056389506, -57.2048825025559),new Coordinate(-34.3025752064298, -57.204909324646),new Coordinate(-34.3023935188042, -57.205097079277),new Coordinate(-34.3017819819568, -57.205992937088),new Coordinate(-34.3014629174861, -57.2063362598419),new Coordinate(-34.3012324812813, -57.2063255310059),new Coordinate(-34.3006829770106, -57.2064757347107),new Coordinate(-34.2999118923753, -57.2062665224075),new Coordinate(-34.2994510108226, -57.2063952684403),new Coordinate(-34.2994776002116, -57.206524014473),new Coordinate(-34.2997080412312, -57.2066420316696),new Coordinate(-34.3005943469528, -57.2074091434479),new Coordinate(-34.3003949289809, -57.2077202796936),new Coordinate(-34.30023771013063, -57.207571741305955),new Coordinate(-34.29762518597192, -57.211604118347175),new Coordinate(-34.29792304767474, -57.21184891818538),new Coordinate(-34.29806835099083, -57.211711406707764),new Coordinate(-34.298538103358176, -57.21214592456818),new Coordinate(-34.298426205064445, -57.21226244179859),new Coordinate(-34.2988842395384, -57.212638880810566),
				new Coordinate(-34.298976831970215, -57.2125267982483),new Coordinate(-34.29945101082255, -57.213084697723396),new Coordinate(-34.29932692689549, -57.21321880817414),new Coordinate(-34.29923101961378, -57.213126732504634),new Coordinate(-34.2988949466342, -57.213722501379316),new Coordinate(-34.29961081328674, -57.21429822720742),new Coordinate(-34.30007142770826, -57.21369624137879),new Coordinate(-34.30032402469899, -57.21391081809998),new Coordinate(-34.30023539426244, -57.21421658992768),new Coordinate(-34.30052792100006, -57.21453850644187),new Coordinate(-34.3005500318889, -57.2143828868866),new Coordinate(-34.3007804699661, -57.2142004966736),new Coordinate(-34.3009134147231, -57.2139537334442),new Coordinate(-34.3014097399565, -57.2138893604279),new Coordinate(-34.301622449873, -57.2136425971985),new Coordinate(-34.3020921823634, -57.2135782241821),new Coordinate(-34.3029447358473, -57.214316483951),new Coordinate(-34.3069178668426, -57.2087609767914),
		};
		
//		Coordenadas Potreros
		Coordinate[] coordenadasPot1 = {
				new Coordinate(-34.3028031740591, -57.214514425455825),
				new Coordinate(-34.30277461922503, -57.21427559852601),
				new Coordinate(-34.302654971604724, -57.21402883529664),
				new Coordinate(-34.30228273347369, -57.21372306346894),
				new Coordinate(-34.30196367090538, -57.21366405487061),
				new Coordinate(-34.301684490163794, -57.21368551254273),
				new Coordinate(-34.300975455537674, -57.212859392166145),
				new Coordinate(-34.30033731925639, -57.21377134323121),
				new Coordinate(-34.300124606085234, -57.21374452114106),
				new Coordinate(-34.29891035808538, -57.21249997615815),
				new Coordinate(-34.29784676877364, -57.21399664878846),
				new Coordinate(-34.298205731671956, -57.21431314945222),
				new Coordinate(-34.298037329515616, -57.21455991268159),
				new Coordinate(-34.298276637742035, -57.21476376056672),
				new Coordinate(-34.29773791403138, -57.215617938656514),
				new Coordinate(-34.3001733529012, -57.2181916236878),
				new Coordinate(-34.3028031740591, -57.214514425455825),
		};
		Coordinate[] coordenadasPot2 = {
				new Coordinate(-34.29481437248801, -57.21252844410925),
				new Coordinate(-34.29627794995697, -57.2105097770691),
				new Coordinate(-34.29456239952917, -57.20879123257999),
				new Coordinate(-34.2931579520194, -57.2107779979706),
				new Coordinate(-34.29481437248801, -57.21252844410925),
		};
		Coordinate[] coordenadasPot3 = {
				new Coordinate(-34.29831625215242, -57.21333599630197),
				new Coordinate(-34.296036626257234, -57.21084262736846),
				new Coordinate(-34.29481437248801, -57.21252844410925),
				new Coordinate(-34.29773791403138, -57.215617938656514),
				new Coordinate(-34.298276637742035, -57.21476376056672),
				new Coordinate(-34.298037329515616, -57.21455991268159),
				new Coordinate(-34.298205731671956, -57.21431314945222),
				new Coordinate(-34.29784676877364, -57.21399664878846),
				new Coordinate(-34.29831625215242, -57.21333599630197),
		};
		Coordinate[] coordenadasPot4 = {
				new Coordinate(-34.30439248095898, -57.21229214638383),
				new Coordinate(-34.30266826579318, -57.21049904823303),
				new Coordinate(-34.3009811036815, -57.212865973010764),
				new Coordinate(-34.301684490163794, -57.21368551254273),
				new Coordinate(-34.30196367090538, -57.21366405487061),
				new Coordinate(-34.30228273347369, -57.21372306346894),
				new Coordinate(-34.302654971604724, -57.21402883529664),
				new Coordinate(-34.30277461922503, -57.21427559852601),
				new Coordinate(-34.3028031740591, -57.214514425455825),
				new Coordinate(-34.30439248095898, -57.21229214638383),
		};
		Coordinate[] coordenadasPot5 = {
				new Coordinate(-34.30517228352514, -57.206959742697755),
				new Coordinate(-34.30266826579318, -57.21049904823303),
				new Coordinate(-34.30439248095898, -57.21229214638383),
				new Coordinate(-34.3069178668426, -57.2087609767914),
				new Coordinate(-34.30517228352514, -57.206959742697755),
		};
		Coordinate[] coordenadasPot6 = {
				new Coordinate(-34.303421599700414, -57.20929741859437),
				new Coordinate(-34.30172880462919, -57.20753788948059),
				new Coordinate(-34.30092227769942, -57.20864832401276),
				new Coordinate(-34.30261065762801, -57.21041321754456),
				new Coordinate(-34.303421599700414, -57.20929741859437),
		};
		Coordinate[] coordenadasPot7 = {
				new Coordinate(-34.30260622622907, -57.21041321754456),
				new Coordinate(-34.30091784621138, -57.20865368843079),
				new Coordinate(-34.30008915383766, -57.209866046905525),
				new Coordinate(-34.301746530408785, -57.211582660675056),
				new Coordinate(-34.30260622622907, -57.21041321754456),
		};
		Coordinate[] coordenadasPot8 = {
				new Coordinate(-34.30170664739941, -57.21166849136353),
				new Coordinate(-34.299867576951314, -57.20974802970887),
				new Coordinate(-34.2984450394186, -57.211861610412605),
				new Coordinate(-34.30017778442853, -57.21363186836243),
				new Coordinate(-34.30033731925639, -57.213615775108345),
				new Coordinate(-34.30170664739941, -57.21166849136353),
		};
		Coordinate[] coordenadasPot9 = {
				new Coordinate(-34.29627370792109, -57.210505527629806),
				new Coordinate(-34.2981303939069, -57.20792412757874),
				new Coordinate(-34.29643625095786, -57.20614043706091),
				new Coordinate(-34.29456239952917, -57.20879123257999),
				new Coordinate(-34.29627370792109, -57.210505527629806),
		};
		Coordinate[] coordenadasPot10 = {
				new Coordinate(-34.29692941217533, -57.2097533941269),
				new Coordinate(-34.29865994996625, -57.21154230018276),
				new Coordinate(-34.2998587138637, -57.2097533941269),
				new Coordinate(-34.29816584698122, -57.20797777175904),
				new Coordinate(-34.29692941217533, -57.2097533941269),
		};
		Coordinate[] coordenadasPot11 = {
				new Coordinate(-34.29823163668932, -57.2132434470958),
				new Coordinate(-34.2988817495962, -57.21230777511614),
				new Coordinate(-34.2984450394186, -57.211861610412605),
				new Coordinate(-34.29865994996625, -57.21154230018276),
				new Coordinate(-34.29699689847238, -57.20982315662854),
				new Coordinate(-34.296858269920584, -57.20969279548938),
				new Coordinate(-34.29627370792109, -57.210505527629806),
				new Coordinate(-34.29627794995697, -57.2105097770691),
				new Coordinate(-34.296036626257234, -57.21084262736846),
				new Coordinate(-34.29823163668932, -57.2132434470958),
		};
		Coordinate[] coordenadasPot12 = {
				new Coordinate(-34.295515453264294, -57.20517439580676),
				new Coordinate(-34.2962203374088, -57.204882502555854),
				new Coordinate(-34.29669896353273, -57.20519900321961),
				new Coordinate(-34.29712440668682, -57.205134630203254),
				new Coordinate(-34.297261788911754, -57.20498979091645),
				new Coordinate(-34.29810823572786, -57.20375061035157),
				new Coordinate(-34.296390825894946, -57.20197143973643),
				new Coordinate(-34.2946780791256, -57.2043085098267),
				new Coordinate(-34.295515453264294, -57.20517439580676),
		};
		Coordinate[] coordenadasPot13 = {
				new Coordinate(-34.298127056933616, -57.20792061422255),
				new Coordinate(-34.29889263170721, -57.20690488815308),
				new Coordinate(-34.29709640511884, -57.20513886706869),
				new Coordinate(-34.29669896353273, -57.20519900321961),
				new Coordinate(-34.2962203374088, -57.204882502555854),
				new Coordinate(-34.295515453264294, -57.20517439580676),
				new Coordinate(-34.29643625095786, -57.20614043706091),
				new Coordinate(-34.298127056933616, -57.20792061422255),
		};
		Coordinate[] coordenadasPot14 = {
				new Coordinate(-34.298105284715795, -57.203747553216424),
				new Coordinate(-34.29941112672312, -57.20200717449189),
				new Coordinate(-34.29951640964078, -57.201123554434524),
				new Coordinate(-34.2980905091804, -57.1996521949768),
				new Coordinate(-34.296390825894946, -57.20197143973643),
				new Coordinate(-34.298105284715795, -57.203747553216424),
		};
		Coordinate[] coordenadasPot15 = {
				new Coordinate(-34.30002268083312, -57.20978021621705),
				new Coordinate(-34.301680058715974, -57.20750033855439),
				new Coordinate(-34.29989416620853, -57.205622792243965),
				new Coordinate(-34.298227889825284, -57.20794022083283),
				new Coordinate(-34.30002268083312, -57.20978021621705),
		};
		Coordinate[] coordenadasPot16 = {
				new Coordinate(-34.29810823572786, -57.20375061035157),
				new Coordinate(-34.297261788911754, -57.20498979091645),
				new Coordinate(-34.29709640511884, -57.20513886706869),
				new Coordinate(-34.29888209476965, -57.20689452840559),
				new Coordinate(-34.29982769304969, -57.20556914806367),
				new Coordinate(-34.29835197537592, -57.20403492450714),
				new Coordinate(-34.29986878509823, -57.201487163940506),
				new Coordinate(-34.29951640964078, -57.201123554434524),
				new Coordinate(-34.29941112672312, -57.20200717449189),
				new Coordinate(-34.298105284715795, -57.203747553216424),
				new Coordinate(-34.29810823572786, -57.20375061035157),
		};
		Coordinate[] coordenadasPot17 = {
				new Coordinate(-34.3034585546041, -57.20938201398553),
				new Coordinate(-34.30517228352514, -57.206959742697755),
				new Coordinate(-34.303388445146446, -57.205119033936896),
				new Coordinate(-34.30173299214796, -57.207542242081395),
				new Coordinate(-34.303421599700414, -57.20929741859437),
				new Coordinate(-34.3034585546041, -57.20938201398553),
		};
		Coordinate[] coordenadasPot18 = {
				new Coordinate(-34.29984985077516, -57.2056120634079),
				new Coordinate(-34.30159195156537, -57.20326526651435),
				new Coordinate(-34.29986878509823, -57.201487163940506),
				new Coordinate(-34.29835197537592, -57.20403492450714),
				new Coordinate(-34.29982769304969, -57.20556914806367),
				new Coordinate(-34.29984985077516, -57.2056120634079),
		};
		Coordinate[] coordenadasPot19 = {
				new Coordinate(-34.29989416620853, -57.205622792243965),
				new Coordinate(-34.30042549640236, -57.20618139083872),
				new Coordinate(-34.30213200098752, -57.203822533200906),
				new Coordinate(-34.30159195156537, -57.20326526651435),
				new Coordinate(-34.29989416620853, -57.205622792243965),
		};
		Coordinate[] coordenadasPot20 = {
				new Coordinate(-34.30173299214796, -57.207542242081395),
				new Coordinate(-34.303388445146446, -57.205119033936896),
				new Coordinate(-34.30213200098752, -57.203822533200906),
				new Coordinate(-34.30042549640236, -57.20618139083872),
				new Coordinate(-34.301680058715974, -57.20750033855439),
				new Coordinate(-34.30173299214796, -57.207542242081395),
		};
		Coordinate[] coordenadasPot21 = {
				new Coordinate(-34.2981303939069, -57.20792412757874),
				new Coordinate(-34.296858269920584, -57.20969279548938),
				new Coordinate(-34.29692941217533, -57.2097533941269),
				new Coordinate(-34.29816584698122, -57.20797777175904),
				new Coordinate(-34.2998587138637, -57.2097533941269),
				new Coordinate(-34.299867576951314, -57.20974802970887),
				new Coordinate(-34.30170664739941, -57.21166849136353),
				new Coordinate(-34.30033731925639, -57.213615775108345),
				new Coordinate(-34.30017778442853, -57.21363186836243),
				new Coordinate(-34.2988817495962, -57.21230777511614),
				new Coordinate(-34.29823163668932, -57.2132434470958),
				new Coordinate(-34.29831625215242, -57.21333599630197),
				new Coordinate(-34.29891035808538, -57.21249997615815),
				new Coordinate(-34.300124606085234, -57.21374452114106),
				new Coordinate(-34.30033731925639, -57.21377134323121),
				new Coordinate(-34.300975455537674, -57.212859392166145),
				new Coordinate(-34.3009811036815, -57.212865973010764),
				new Coordinate(-34.3034585546041, -57.20938201398553),
				new Coordinate(-34.303421599700414, -57.20929741859437),
				new Coordinate(-34.30261065762801, -57.21041321754456),
				new Coordinate(-34.30260622622907, -57.21041321754456),
				new Coordinate(-34.301746530408785, -57.211582660675056),
				new Coordinate(-34.30008915383766, -57.209866046905525),
				new Coordinate(-34.30091784621138, -57.20865368843079),
				new Coordinate(-34.30092227769942, -57.20864832401276),
				new Coordinate(-34.30172832610555, -57.20753854831685),
				new Coordinate(-34.301680058715974, -57.20750033855439),
				new Coordinate(-34.30002268083312, -57.20978021621705),
				new Coordinate(-34.298227889825284, -57.20794022083283),
				new Coordinate(-34.30159195156537, -57.20326526651435),
				new Coordinate(-34.29984985077516, -57.2056120634079),
				new Coordinate(-34.29982769304969, -57.20556914806367),
				new Coordinate(-34.29888209476965, -57.20689452840559),
				new Coordinate(-34.298127056933616, -57.20792061422255),
				new Coordinate(-34.2981303939069, -57.20792412757874),
		};
		Potrero[] potreros = {
				new Potrero("INSTALACIONES", "Instalacion de la escuela", true, fecha, geometryFactory.createPolygon(coordenadasPot1), indicadorNoSle),
				new Potrero("POT 22", "Potrero 22", true, fecha, geometryFactory.createPolygon(coordenadasPot2), indicadorReservasPPL),
				new Potrero("POT 23", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot3), indicadorSEPVO),
				new Potrero("POT 20", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot4), indicadorSEPVO),
				new Potrero("POT 5", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot5), indicadorSEPVO),
				new Potrero("POT 10", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot6), indicadorSEPVO),
				new Potrero("POT 15", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot7), indicadorReservasPPL),
				new Potrero("POT 9", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot8), indicadorSEPVO),
				new Potrero("POT 17", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot9), indicadorSEPVO),
				new Potrero("POT 18", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot10), indicadorReservasPPL),
				new Potrero("NOCHERO", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot11), indicadorSEPVO),
				new Potrero("POT 7", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot12), indicadorSEPVO),
				new Potrero("POT 13", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot13), indicadorReservasNoPPL),
				new Potrero("POT 1", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot14), indicadorSEPVO),
				new Potrero("POT 19", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot15), indicadorInstalaciones),
				new Potrero("POT 8", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot16), indicadorSEPVO),
				new Potrero("POT 4", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot17), indicadorSEPVO),
				new Potrero("POT 2", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot18), indicadorNOSEPVO),
				new Potrero("POT 3A", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot19), indicadorNOSEPVO),
				new Potrero("POT 3B", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot20), indicadorNOSEPVO),
				new Potrero("CAMINOS", "Potrero ", true, fecha, geometryFactory.createPolygon(coordenadasPot21), indicadorCamineria),
		};
		ZonaGeografica[] zonasGeograficas = {
				new ZonaGeografica("Arboleda1", "Arboleada", true, fecha, geometryFactory.createPolygon(coordenadasZona1), tipoZonaMontes),
				new ZonaGeografica("Bebederos", "Bebederos", true, fecha, geometryFactory.createPolygon(coordenadasZona2), tipoZonaBebederos),
				new ZonaGeografica("Arboleda Arroyo", "Arboleada arroyo", true, fecha, geometryFactory.createPolygon(coordenadasZona3), tipoZonaMontes),
				new ZonaGeografica("Arroyo", "Arroyo", true, fecha, geometryFactory.createPolygon(coordenadasZona4), tipoZonaArroyos),
				new ZonaGeografica("Instalaciones", "instalaciones", true, fecha, geometryFactory.createPolygon(coordenadasZona5), tipoZonaInstalaciones),
				new ZonaGeografica("Arboleda6", "Arboleada", true, fecha, geometryFactory.createPolygon(coordenadasZona6), tipoZonaMontes),
				new ZonaGeografica("Productiva", "Productiva", true, fecha, geometryFactory.createPolygon(coordenadasZona7), tipoZonaTierraNegra),
				new ZonaGeografica("Arboleda2", "Arboleada2", true, fecha, geometryFactory.createPolygon(coordenadasZona8), tipoZonaMontes),
				new ZonaGeografica("Arboleda3", "Arboleada3", true, fecha, geometryFactory.createPolygon(coordenadasZona9), tipoZonaMontes),
				new ZonaGeografica("Arboleda4", "Arboleada4", true, fecha, geometryFactory.createPolygon(coordenadasZona10), tipoZonaMontes),
				new ZonaGeografica("Arboleda5", "Arboleada5", true, fecha, geometryFactory.createPolygon(coordenadasZona11), tipoZonaMontes),
				new ZonaGeografica("Productiva1", "Productiva1", true, fecha, geometryFactory.createPolygon(coordenadasZona12), tipoZonaProductiva),
				new ZonaGeografica("Productiva2", "Productiva2", true, fecha, geometryFactory.createPolygon(coordenadasZona13), tipoZonaProductiva),
				new ZonaGeografica("Productiva3", "Productiva3", true, fecha, geometryFactory.createPolygon(coordenadasZona14), tipoZonaProductiva),
		};
		
		Polygon formaPredio = geometryFactory.createPolygon(coordenadasPredio);
		
		Predio predio = new Predio(true, "Escuela de lecher�a", fecha, "Escuela de lecher�a de Nueva Helvecia", formaPredio);
		predio.addPropietario(propietario);
		
		for (ZonaGeografica zonaGeografica : zonasGeograficas) {
			
			predio.addZonaGeografica(zonaGeografica);
			this.em.persist(zonaGeografica);
		}
		for (Potrero potrero : potreros) {
			
			predio.addPotrero(potrero);
			this.crearZonaPotreros(potrero);
			this.em.persist(potrero);
		}
		
		this.em.persist(predio);
		try {
			this.em.flush();
		}catch (PersistenceException e) {
			//Si da exepcion no pasa nada, es xq ya existen
			System.out.println("No se pudieron crear los predios");
		}
	}
	
	private void crearZonaPotreros(Potrero nuevoPotrero) {
		//Cuando se crea un nuevo potrero:
		//Tengo que intersectar el potrero con las ZonaGeografica activas que hayan en el predio
		TypedQuery<ZonaGeografica> query = this.em.createQuery("SELECT z FROM ZonaGeografica z where z.activo=:activo and z.predio=:predio",ZonaGeografica.class);
		query.setParameter("predio", nuevoPotrero.getPredio());
		query.setParameter("activo", true);
		List<ZonaGeografica> listaZonaGeograficaActivas = query.getResultList();
		System.out.println("Hay " + listaZonaGeograficaActivas.size() + " ZonaGeografica activas en el predio " + nuevoPotrero.getPredio().getNombre());
		for (ZonaGeografica zonaGeograficaActiva : listaZonaGeograficaActivas) {
			//Cada ZonaGeografica activa, la intersecto con el potrero
			if (nuevoPotrero.getForma().intersects(zonaGeograficaActiva.getForma())) {
				//Si se intersecta la ZonaGeografica con el Potrero
				System.out.println("ZonaGeografica: " + zonaGeograficaActiva.getForma().toString());
				System.out.println("Potrero: " + nuevoPotrero.getForma().toString());
				Geometry forma = nuevoPotrero.getForma().intersection(zonaGeograficaActiva.getForma());
				if (forma instanceof MultiPolygon) {
					//Si la intersecci�n  es un multipoligono.
					int cant = forma.getNumGeometries();
					for (int i = 0; i < cant; i++) {
						ZonaPotrero nuevaZonaPotrero = new ZonaPotrero(nuevoPotrero, zonaGeograficaActiva, nuevoPotrero.getDesde(), true, (Polygon) forma.getGeometryN(i));
						nuevoPotrero.addZonaPotrero(nuevaZonaPotrero);	
						this.em.persist(nuevaZonaPotrero);
					}
					
				}else if (forma instanceof Polygon) {
					ZonaPotrero nuevaZonaPotrero = new ZonaPotrero(nuevoPotrero, zonaGeograficaActiva, nuevoPotrero.getDesde(), true, (Polygon) forma);
					nuevoPotrero.addZonaPotrero(nuevaZonaPotrero);	
					this.em.persist(nuevaZonaPotrero);
				}
				
						
			}
		}
	}
}
