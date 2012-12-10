package disasters.caronte;

import disasters.*;
import jadex.bridge.IComponentIdentifier;
import jadex.commons.SimplePropertyChangeSupport;
import java.util.*;
import org.json.me.*;

/**
 * Clase para modelar el entorno, proporcionando m&eacute;todos para interactuar con &eacute;l.
 * 
 * @author aebeda
 * @author Juan Luis Molina Nogales
 */
public class Entorno{
	/** URL para REST. */
	public static final String URL = "http://localhost:8080/caronte/rest/";
	/** Agente coordinador. */
	public static final String COORDINADOR = "Coordinador";
	/** Agente intervenci&oacute;n en incendios. */
	public static final String INTERVENCION_INCENDIOS = "IntervencionIncendios";
	/** Agente atenci&oacute;n de heridos. */
	public static final String ATENCION_HERIDOS = "AtencionHeridos";
	/** Agente evacuaci&oacute;n. */
	public static final String EVACUACION = "EvacuacionResidencia";
	/** Agente apoyo externo. */
	public static final String APOYO_EXTERNO = "ApoyoExterno";
	/** Listado de agentes. */
	public static final List<String> COMPONENTES = Arrays.asList(new String[]{
		COORDINADOR, INTERVENCION_INCENDIOS, ATENCION_HERIDOS, EVACUACION});
	
	// Eventos (id -> Disaster)
	private HashMap<Integer,Disaster> disasters;
	private HashMap<Integer,People> people;
	private HashMap<Integer,Resource> resources;
	private HashMap<Integer,Association> associations;
	private HashMap<Integer,Activity> activities;
	private HashMap<Integer,String> usedResources;
	private ArrayList<Integer> inactiveResources;
	// Emergencias que se deben atender
	private ArrayList<Integer> tablonEventos;
	private ArrayList<Integer> tablonHeridos;
	
	// Agentes (nombre -> WorldObject)
	private HashMap<String,WorldObject> agentes;
	// Numero de agentes creados, no tienen por que estar activos
	private int numAgentes;
	// Objeto para notificar cambios
	private SimplePropertyChangeSupport pcs;
	
	private static Entorno instance;
	
	// Los nombres de los agentes
	/** Agente coordinador. */
	public static final String COORDINADOR_EMERGENCIAS = "coordinadorEmergencias";
	/** Agente enfermero. */
	public static final String ENFERMERO = "nurse";
	/** Agente celador. */
	public static final String CELADOR = "orderly";
	/** Agente gerocultor. */
	public static final String GEROCULTOR = "gerocultor";
	/** Agente auxiliar. */
	public static final String AUXILIAR = "assistant";
	/** Agente jefe. */
	public static final String JEFE = "chief";
	/** Agente recepcionista. */
	public static final String RECEPCIONISTA = "receptionist";
	/** Agente otro personal. */
	public static final String OTRO_PERSONAL = "otherStaff";
	/** Agente ciudadano. */
	public static final String CIUDADANO = "citizen";
	/** Agente central. */
	public static final String CENTRAL_EMERGENCIAS = "centralEmergencias";
	/** Agente ambulancia. */
	public static final String AMBULANCIA = "ambulance";
	/** Agente bombero. */
	public static final String BOMBERO = "firemen";
	/** Agente polic&iacute;a. */
	public static final String POLICIA = "police";
	/** Listado de agentes (simulaci&oacute;n). */
	public static final List<String> AGENTES = Arrays.asList(new String[]{
		ENFERMERO, CELADOR, GEROCULTOR, AUXILIAR, RECEPCIONISTA,
		OTRO_PERSONAL, CIUDADANO, AMBULANCIA, BOMBERO, POLICIA});
	/** Listado de agentes (simulaci&oacute;n - no mapa). */
	public static final List<String> COMPONENTES2 = Arrays.asList(new String[]{
		COORDINADOR_EMERGENCIAS, CENTRAL_EMERGENCIAS});

	/**
	 * Constructor del entorno.
	 */
	public Entorno(){
		disasters = new HashMap<Integer,Disaster>();
		people = new HashMap<Integer,People>();
		resources = new HashMap<Integer,Resource>();
		associations = new HashMap<Integer,Association>();
		activities = new HashMap<Integer,Activity>();
		usedResources = new HashMap<Integer,String>();
		inactiveResources = new ArrayList<Integer>();
		tablonEventos = new ArrayList<Integer>();
		tablonHeridos = new ArrayList<Integer>();
		
		agentes = new HashMap<String,WorldObject>();
		numAgentes = 0;
		pcs = new SimplePropertyChangeSupport(this);
	}

	/**
	 * Obtener una instancia del entorno, para as&iacute; poder interactuar sobre &eacute;l.
	 * 
	 * @param tipo tipo
	 * @param nombre nombre
	 * @param pos posici&oacute;n
	 * @param agentId ID del agente
	 * @return instancia de entorno
	 */
	public static Entorno getInstance(String tipo, String nombre, Position pos, IComponentIdentifier agentId){
		// La primera vez que se llama a este metodo (el agente Entorno), instance vale null
		if(instance == null){
			instance = new Entorno();
		}
		if(tipo != null && nombre != null){
			instance.addWorldObject(tipo, nombre, pos, null, agentId);
		}
		return instance;
	}

	/**
	 * Borra la instancia del entorno (y para el temporizador).
	 */
	public static void clearInstance(){
		instance = null;
		System.out.println("Entorno borrado");
	}
	
	/**
	 * A&ntilde;ade un objeto al entorno.
	 * 
	 * @param type tipo
	 * @param name nombre
	 * @param position posici&oacute;n
	 * @param info informaci&oacute;n
	 * @param agentId identificador del agente
	 */
	public void addWorldObject(String type, String name, Position position, String info, IComponentIdentifier agentId){
		WorldObject wo = new WorldObject(name, type, position, info, agentId);

		if(AGENTES.contains(type)){
			// REST -> cree el recurso
			System.out.println("LLamada a REST creando agente...");
			if(info == null){
				info = "";
			}
			String resultado = Connection.connect(URL + "post/type=" + type + "&name=" + name +
				"&info=" + info + "&latitud=" + position.getLat() + "&longitud=" + position.getLng());
			try{
				// JSON -> guardo el id
				JSONObject idJson = new JSONObject(resultado);
				int id = idJson.getInt("id");
				wo.setId(id);
				System.out.println("Id del objeto creado: " + id);
				System.out.println("Creando agente del tipo " + type);
				agentes.put(name, wo); // Si es un pironamo, se annade a la tabla de agentes
				numAgentes++;
			}catch(JSONException e){
				System.out.println("Error con JSON: " + e);
			}
		}else if(COMPONENTES.contains(type) || COMPONENTES2.contains(type)){
			System.out.println("Creando componente del tipo " + type);
			agentes.put(name, wo);
			numAgentes++;
		}
	}
	
	/**
	 * Elimina un objeto del entorno.
	 * 
	 * @param type tipo
	 * @param name nombre
	 */
	public void removeWorldObject(String type, String name){
		if(AGENTES.contains(type)){
			System.out.println("Llamada a REST eliminando agente...");
			Connection.connect(URL + "delete/resource/" + name);
		}
		System.out.println("Eliminando agente de tipo " + type);
		agentes.remove(name);
		numAgentes--;
	}
	
	/**
	 * Devuelve todos los agentes.
	 * 
	 * @return todos los agentes
	 */
	public HashMap<String,WorldObject> getAgents(){
		return agentes;
	}
	
	/**
	 * Devuelve un agente dado su nombre (el nombre de los agentes es &uacute;nico).
	 * 
	 * @param name nombre
	 * @return agente
	 */
	public synchronized WorldObject getAgent(String name){
		assert agentes.containsKey(name);
		return agentes.get(name);
	}

	/**
	 * Elimina un agente dado su nombre (el nombre de los agentes es &uacute;nico).
	 * 
	 * @param name nombre
	 * @return agente eliminado
	 */
	public synchronized WorldObject removeAgent(String name){
		assert agentes.containsKey(name);
		return agentes.remove(name);
	}

	/**
	 * Devuelve todos los agentes.
	 * 
	 * @return todos los agentes
	 */
	public WorldObject[] getAgentes(){
		Collection<WorldObject> col = agentes.values();
		return col.toArray(new WorldObject[col.size()]);
	}

	/**
	 * Devuelve el n&uacute;mero total de agentes creados.
	 * 
	 * @return n&uacute;mero total de agentes creados
	 */
	public int getNumAgentes(){
		return numAgentes;
	}
	
	/**
	 * Devuelve todos los desastres.
	 * 
	 * @return todos los desastres
	 */
	public HashMap<Integer,Disaster> getEvents(){
		return disasters;
	}
	
	/**
	 * Devuelve todos los heridos.
	 * 
	 * @return todos los heridos
	 */
	public HashMap<Integer,People> getPeople(){
		return people;
	}
	
	/**
	 * Devuelve todos los recursos.
	 * 
	 * @return todos los recursos
	 */
	public HashMap<Integer,Resource> getResources(){
		return resources;
	}
	
	/**
	 * Devuelve todas las asociaciones.
	 * 
	 * @return todas las asociaciones
	 */
	public HashMap<Integer,Association> getAssociations(){
		return associations;
	}
	
	/**
	 * Devuelve todas las actividades.
	 * 
	 * @return todas las actividades
	 */
	public HashMap<Integer,Activity> getActivities(){
		return activities;
	}

	/**
	 * Devuelve un evento dado su id.
	 * 
	 * @param id identificador
	 * @return desastre
	 */
	public synchronized Disaster getEvent(int id){
		assert disasters.containsKey(id);
		return disasters.get(id);
	}

	/**
	 * Elimina un evento dado su id.
	 * 
	 * @param id identificador
	 * @return evento eliminado
	 */
	public synchronized Disaster removeEvent(int id){
		assert disasters.containsKey(id);
		return disasters.remove(id);
	}
	
	/**
	 * A&ntilde;ade un evento.
	 * 
	 * @param id identificador
	 * @param event evento
	 */
	public synchronized void putEvent(int id, Disaster event){
		disasters.put(id, event);
	}

	/**
	 * Devuelve un herido dado su id.
	 * 
	 * @param id identificador
	 * @return herido
	 */
	public synchronized People getPeople(int id){
		assert people.containsKey(id);
		return people.get(id);
	}

	/**
	 * Elimina un herido dado su id.
	 * 
	 * @param id identificador
	 * @return herido eliminado
	 */
	public synchronized People removePeople(int id){
		assert people.containsKey(id);
		return people.remove(id);
	}
	
	/**
	 * A&ntilde;ade un herido.
	 * 
	 * @param id identificador
	 * @param person herido
	 */
	public synchronized void putPeople(int id, People person){
		people.put(id, person);
	}

	/**
	 * Devuelve un recurso dado su id.
	 * 
	 * @param id identificador
	 * @return recurso
	 */
	public synchronized Resource getResource(int id){
		assert resources.containsKey(id);
		return resources.get(id);
	}

	/**
	 * Elimina un recurso dado su id.
	 * 
	 * @param id identificador
	 * @return recurso eliminado
	 */
	public synchronized Resource removeResource(int id){
		assert resources.containsKey(id);
		return resources.remove(id);
	}
	
	/**
	 * A&ntilde;ade un recurso.
	 * 
	 * @param id identificador
	 * @param resource recurso
	 */
	public synchronized void putResource(int id, Resource resource){
		resources.put(id, resource);
	}

	/**
	 * Devuelve una asociaci&oacute;n dada su id.
	 * 
	 * @param id identificador
	 * @return asociaci&oacute;n
	 */
	public synchronized Association getAssociation(int id){
		assert associations.containsKey(id);
		return associations.get(id);
	}

	/**
	 * Elimina una asociaci&oacute;n dada su id.
	 * 
	 * @param id identificador
	 * @return asociaci&oacute;n eliminada
	 */
	public synchronized Association removeAssociation(int id){
		assert associations.containsKey(id);
		return associations.remove(id);
	}
	
	/**
	 * A&ntilde;ade una asociaci&oacute;n.
	 * 
	 * @param id identificador
	 * @param association asociaci&oacute;n
	 */
	public synchronized void putAssociation(int id, Association association){
		associations.put(id, association);
	}

	/**
	 * Devuelve una actividad dada su id.
	 * 
	 * @param id identificador
	 * @return actividad
	 */
	public synchronized Activity getActivity(int id){
		assert activities.containsKey(id);
		return activities.get(id);
	}

	/**
	 * Elimina una actividad dada su id.
	 * 
	 * @param id identificador
	 * @return actividad eliminado
	 */
	public synchronized Activity removeActivity(int id){
		assert activities.containsKey(id);
		return activities.remove(id);
	}
	
	/**
	 * A&ntilde;ade una actividad.
	 * 
	 * @param id identificador
	 * @param activity actividad
	 */
	public synchronized void putActivity(int id, Activity activity){
		activities.put(id, activity);
	}
	
	/**
	 * Devuelve todos los eventos.
	 * 
	 * @return todos los eventos
	 */
	public Disaster[] getEventsArray(){
		Collection<Disaster> col = disasters.values();
		return col.toArray(new Disaster[col.size()]);
	}
	
	/**
	 * Devuelve todos los heridos.
	 * 
	 * @return todos los heridos
	 */
	public People[] getPeopleArray(){
		Collection<People> col = people.values();
		return col.toArray(new People[col.size()]);
	}
	
	/**
	 * Devuelve todos los recursos.
	 * 
	 * @return todos los recursos
	 */
	public Resource[] getResourcesArray(){
		Collection<Resource> col = resources.values();
		return col.toArray(new Resource[col.size()]);
	}
	
	/**
	 * Devuelve todos los eventos.
	 * 
	 * @return todos los eventos
	 */
	public Association[] getAssociationsArray(){
		Collection<Association> col = associations.values();
		return col.toArray(new Association[col.size()]);
	}
	
	/**
	 * Devuelve todos los eventos.
	 * 
	 * @return todos los eventos
	 */
	public Activity[] getActivitiesArray(){
		Collection<Activity> col = activities.values();
		return col.toArray(new Activity[col.size()]);
	}

	/**
	 * Devuelve la lista de emergencias que se deben atender.
	 * 
	 * @return el tabl&oacute;n
	 */
	public ArrayList<Integer> getTablonEventos(){
		return tablonEventos;
	}

	/**
	 * Establece la emergencia que se debe atender.
	 * 
	 * @param idEmergencia emergencia a atender
	 */
	public void setTablonEventos(int idEmergencia){
		tablonEventos.add(idEmergencia);
	}
	
	/**
	 * Elimina la emergencia atendida.
	 * 
	 * @param idEmergencia emergencia a eliminar
	 */
	public void removeTablonEventos(Integer idEmergencia){
		tablonEventos.remove(idEmergencia);
		tablonEventos.trimToSize();
	}
	
	/**
	 * Devuelve la lista de emergencias que se deben atender.
	 * 
	 * @return el tabl&oacute;n
	 */
	public ArrayList<Integer> getTablonHeridos(){
		return tablonHeridos;
	}

	/**
	 * Establece la emergencia que se debe atender.
	 * 
	 * @param idEmergencia emergencia a atender
	 */
	public void setTablonHeridos(int idEmergencia){
		tablonHeridos.add(idEmergencia);
	}
	
	/**
	 * Elimina la emergencia atendida.
	 * 
	 * @param idEmergencia emergencia a eliminar
	 */
	public void removeTablonHeridos(Integer idEmergencia){
		tablonHeridos.remove(idEmergencia);
		tablonHeridos.trimToSize();
	}
	
	/**
	 * Devuelve los eventos sin atender.
	 * 
	 * @return eventos sin atender
	 */
	public Disaster[] getEventosSin(){
		HashMap<Integer,Disaster> disastersAux = (HashMap<Integer,Disaster>) disasters.clone();
		ArrayList<Integer> tablonAux = (ArrayList<Integer>) tablonEventos.clone();
		for(int i = 0; i < tablonAux.size(); i++){
			disastersAux.remove(tablonAux.get(i));
		}
		Collection<Disaster> col = disastersAux.values();
		return col.toArray(new Disaster[col.size()]);
	}
	
	/**
	 * Devuelve los heridos sin atender.
	 * 
	 * @return heridos sin atender
	 */
	public People[] getHeridosSin(){
		HashMap<Integer,People> peopleAux = (HashMap<Integer,People>) people.clone();
		ArrayList<Integer> tablonAux = (ArrayList<Integer>) tablonHeridos.clone();
		for(int i = 0; i < tablonAux.size(); i++){
			peopleAux.remove(i);
		}
		Collection<People> col = peopleAux.values();
		return col.toArray(new People[col.size()]);
	}
	
	/**
	 * Coge un recurso si no est&aacute; siendo usado.
	 * 
	 * @param id
	 * @param equipo
	 * @return <code>true</code> si es usado
	 */
	public synchronized boolean useResource(int id, String equipo){
		boolean usado = usedResources.containsKey(id);
		if(usado == false){
			usedResources.put(id, equipo);
			if(inactiveResources.contains(id)){
				removeInactiveResource(id);
			}
		}
		return !usado;
	}
	
	/**
	 * Deja de usar un recurso.
	 * 
	 * @param id identificador del recurso
	 */
	public synchronized void leaveResource(int id){
		usedResources.remove(id);
	}
	
	/**
	 * Pone un recurso como inactivo.
	 * 
	 * @param id identificador del recurso
	 */
	public synchronized void addInactiveResource(Integer id){
		inactiveResources.add(id);
	}
	
	/**
	 * Quita un recurso como inactivo.
	 * 
	 * @param id identificador del recurso
	 */
	public synchronized void removeInactiveResource(Integer id){
		inactiveResources.remove(id);
	}
	
	/**
	 * Devuelve los recursos libres.
	 * 
	 * @return recursos libres
	 */
	public Resource[] getFreeResources(){
		HashMap<Integer,Resource> recursosLibres = (HashMap<Integer,Resource>) resources.clone();
		Integer[] keySet = usedResources.keySet().toArray(new Integer[usedResources.size()]);
		for(int i = 0; i < keySet.length; i++){
			recursosLibres.remove(keySet[i]);
		}
		if(recursosLibres.size() > inactiveResources.size()){ // Si hay recursos libres que no estan inactivos
			for(Integer i : inactiveResources){               // entonces elimina a los inactivos
				recursosLibres.remove(i);
			}
		}
		Collection<Resource> col = recursosLibres.values();
		return col.toArray(new Resource[col.size()]);
	}
	
	/**
	 * Imprime un String por pantalla y lo env&iacute;a para mostrar en la web.
	 *
	 * @param valor String a imprimir
	 * @param tipo tipo de receptor (0 oculto, 1 directo y 2 a grupo)
	 * @param receptor ID del receptor (Si tipo = 2 --> 0 todos los usuarios, 1 todos los conectados,...)
	 * @param println <code>true</code> para mostrarlo en el terminal
	 */
	public final void printout(String valor, int tipo, int receptor, boolean println){
		Connection.connect(Entorno.URL + "put/message/" + tipo + "/" + receptor + "/" + valor);
		if(println){
			System.out.println(valor);
		}
	}
	
	// SIMULADOR ************************************************************ //
	
	/**
	 * Modifica la posici&oacute;n de un agente.
	 * 
	 * @param name nombre
	 * @param inicial posici&oacute;n inicial
	 * @param dest destino
	 * @param desastre identificador del desastre
	 * @param herido identificador del herido
	 * @throws InterruptedException 
	 */
	public void andar(String name, Position inicial, Position dest, int desastre, int herido) throws InterruptedException{
		double x1 = inicial.getLat();
		double x2 = dest.getLat();
		double y1 = inicial.getLng();
		double y2 = dest.getLng();

		double pendiente = Math.atan((y2-y1)/(x2-x1));
		int velocidad = 150; // velocidad inversa, cuanto mas grande mas despacio

		double pasoX = (0.25 / velocidad) * Math.cos(pendiente);
		double pasoY = (0.25 / velocidad) * Math.abs(Math.sin(pendiente));

		boolean arriba = (x2-x1 > 0);
		boolean derecha = (y2-y1 > 0);

		// El punto destino esta a la derecha del origen
		if(derecha && arriba){
			while(x1 < x2 || y1 < y2){
				if(x2 - x1 < pasoX){
					x1 = x2;
				}else if(x1 < x2){
					x1 += pasoX;
				}
				if(y2 - y1 < pasoY){
					y1 = y2;
				}else if(y1 < y2){
					y1 += pasoY;
				}
				go(getAgent(name).getName(), new Position(x1, y1));
				pinta(desastre, herido, x1, y1);
			}
		}else if(derecha && !arriba){
			while(x1 > x2 || y1 < y2){
				if(x1 - x2 < pasoX){
					x1 = x2;
				}else if(x1 > x2){
					x1 -= pasoX;
				}
				if(y2 - y1 < pasoY){
					y1 = y2;
				}else if(y1 < y2){
					y1 += pasoY;
				}
				go(getAgent(name).getName(), new Position(x1, y1));
				pinta(desastre, herido, x1, y1);
			}
		}else if(!derecha && arriba){
			while(x1 < x2 || y1 > y2){
				if(x2 - x1 < pasoX){
					x1 = x2;
				}else if(x1 < x2){
					x1 += pasoX;
				}
				if(y1 - y2 < pasoY){
					y1 = y2;
				}else if(y1 > y2){
					y1 -= pasoY;
				}
				go(getAgent(name).getName(), new Position(x1, y1));
				pinta(desastre, herido, x1, y1);
			}
		}else{
			while(x1 > x2 || y1 > y2){
				if(x1 - x2 < pasoX){
					x1 = x2;
				}else if(x1 > x2){
					x1 -= pasoX;
				}
				if(y1 - y2 < pasoY){
					y1 = y2;
				}else if(y1 > y2){
					y1 -= pasoY;
				}
				go(getAgent(name).getName(), new Position(x1, y1));
				pinta(desastre, herido, x1, y1);
			}
		}
	}

	/**
	 * Cambia la posici&oacute;n de un agente. Los eventos no se mueven de posici&oacute;n.
	 * 
	 * @param name nombre
	 * @param pos posici&oacute;n
	 */
	public void go(String name, Position pos){
		Position oldPos;
		WorldObject wo;
		
		// No deben varios agentes tocar las tablas hash a la vez
		synchronized(this){
			wo = getAgent(name);       // Obtenemos el agente de la tabla Hash agentes, dado su nombre
			oldPos = wo.getPosition(); // Obtenemos la posicion del agente antes de desplazarlo
			
			wo.setPosition(pos); // Actualizamos la posicion al objeto agente  
			agentes.put(name, wo); // Actualizamos la tabla Hash de agentes
		}

		// Avisamos para el modo de evaluacion dinamico de posicion de que hemos variado una poscicion
		pcs.firePropertyChange("cambio_de_posicion", oldPos, pos);
	}

	/**
	 * Pinta el movimiento de un agente en el mapa mediante REST.
	 * 
	 * @param id identificador del agente
	 * @param idHerido identificador del herido
	 * @param latitud latitud
	 * @param longitud longitud
	 * @throws InterruptedException 
	 */
	public void pinta(int id, int idHerido, double latitud, double longitud) throws InterruptedException{
		Connection.connect(URL + "put/" + id + "/latlong/" + latitud + "/" + longitud);
		if(idHerido != 0){
			Connection.connect(URL + "put/" + idHerido + "/latlong/" + latitud + "/" + longitud);
		}
		Thread.sleep(1000);
	}

	/**
	 * Devuelve una posici&oacute;n aleatoria conociendo la ciudad.
	 * Puesto que de momento s&oacute;lo tenemos Calasparra en la lista, no hace falta especificar la ciudad.
	 * 
	 * @return posici&oacute;n aleatoria
	 */
	public Position getRandomPosition(){
		// Las dos posiciones que se crean son las esquinas superior derecha e inferior izquierda del marco que contiene a Calasparra
		Position esd = new Position(38.233181, -1.69724); // Esquina superior derecha
		Position eii = new Position(38.231251, -1.70252); // Esquina inferior izquierda
		//Location location = new Location("Calasparra", esd, eii);
		
		// Obtenemos la altura y anchura del marco, tomando la diferencia de latitudes y longitudes respectivamente
		double alturaMarco = Math.abs(esd.getLat() - eii.getLat());
		double anchuraMarco = Math.abs(esd.getLng() - eii.getLng());

		// Obtenemos la latitud y longitud menor, para sumarles una fraccion aleatoria de la diferencia que las separa
		double marcoInferior = Math.min(esd.getLat(), eii.getLat());
		double marcoIzquierdo = Math.min(esd.getLng(), eii.getLng());

		Position nuevaAleatoria = new Position(marcoInferior + Math.random() * alturaMarco, marcoIzquierdo + Math.random() * anchuraMarco);
		return nuevaAleatoria;
	}
}