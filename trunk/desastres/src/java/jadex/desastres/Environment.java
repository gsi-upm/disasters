package jadex.desastres;

import jadex.bridge.IComponentIdentifier;
import jadex.commons.collection.MultiCollection;
import jadex.commons.SimplePropertyChangeSupport;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import org.json.me.*;

/**
 * 
 * Clase para modelar el entorno, proporcionando metodos para interactuar con el.
 * 
 * @author aebeda y Juan Luis Molina
 * 
 */
public class Environment{
	//------constantes ----

	/** Los nombres de los agentes */
	public static final String AMBULANCIA = "ambulance";
	public static final String BOMBERO = "firemen";
	public static final String POLICIA = "police";
	public static final String ENFERMERO = "nurse";
	public static final String GEROCULTOR = "gerocultor";
	public static final String AUXILIAR = "assistant";
	public static final String OTRO_PERSONAL = "otherStaff";
	public static final String CIUDADANO = "citizen";
	public static final String AMBULANCIA2 = "ambulance";
	public static final String GSO = "grupoSanitarioOperativo";
	public static final String MEDICO_CACH = "medicoCACH";
	public static final String COORDINADOR_HOSPITAL = "coordinadorHospital";
	public static final String COORDINADOR_MEDICO = "coordinadorMedico";
	public static final String CENTRAL = "central";
	public static final String CENTRAL_EMERGENCIAS = "centralEmergencias";
	public static final String COORDINADOR_EMERGENCIAS = "coordinadorEmergencias";
	public static final String APOCALIPSIS = "apocalypse";
	public static final String HERIDO_LEVE = "slight";
	public static final String HERIDO_GRAVE = "serious";
	public static final String HERIDO_MUERTO = "dead";
	public static final String HERIDO_ATRAPADO = "trapped";
	public static final String PERSONA_SANA = "healthy";
	/** Los nombres de los eventos provocados sobre el mapa */
	public static final String FUEGO = "fire";
	public static final String TERREMOTO = "collapse";
	public static final String INUNDACION = "flood";
	public static final String PERSONA_PERDIDA = "lostPerson";
	public static final String PERSONA_HERIDA = "injuredPerson";
	public static final String PROYECTO = "caronte"; // "desastres"
	public static final String URL = "http://localhost:8080/" + PROYECTO + "/rest/";
	//------- atributos ---
	private final int tiempoJSON = 5000;
	private final int tiempoMove = 500;
	private TimerJSON temporizador;
	private TimerMove tempoMover;
	private String ahora;
	/** Agentes (nombre -> WorldObject) */
	public HashMap<String, WorldObject> agentes;
	/**
	 * Numero de agentes creados, no tienen por que estar activos
	 * No ha sido usado
	 */
	protected Integer numAgentes;
	/** Eventos (id -> WorldObject) */
	public HashMap<Integer, Disaster> disasters;
	protected HashMap<Integer, People> people;
	protected HashMap<Integer, Resource> resources;
	protected HashMap<Integer, Association> associations;
	protected HashMap<Integer, Activity> activities;
	/**
	 * Numero de eventos creados, no tienen por que estar activos
	 * Usado para poder dar un nombre distinto a los eventos en las tablas Hash
	 */
	protected Integer numEventos;
	/** Agentes y Eventos (Pos -> WorldObject) */
	protected MultiCollection objetos;
	/** Generador aleatorio */
	private Random rnd = new Random();
	/** Objeto para notificar de cambios */
	public SimplePropertyChangeSupport pcs;
	private int tablon;

	private HashMap<String, IComponentIdentifier> listado;
	
	protected static Environment instance;

	//---------------------
	/**
	 * Constructor
	 */
	public Environment(){
		this.agentes = new HashMap<String, WorldObject>();
		this.disasters = new HashMap<Integer, Disaster>();
		this.people = new HashMap<Integer, People>();
		this.resources = new HashMap<Integer, Resource>();
		this.associations = new HashMap<Integer, Association>();
		this.activities = new HashMap<Integer, Activity>();
		this.objetos = new MultiCollection();
		this.pcs = new SimplePropertyChangeSupport(this);
		this.numAgentes = 0;
		this.numEventos = 0;
		this.temporizador = new TimerJSON(tiempoJSON, this);
		this.tempoMover = new TimerMove(tiempoMove);

		this.listado = new HashMap<String, IComponentIdentifier>();

		// Esto LA PRIMERA VEZ - recibo el json
		try{
			String eventos = Connection.connect(URL + "events");
			JSONArray desastres = new JSONArray(eventos);

			String victimas = Connection.connect(URL + "people");
			JSONArray personas = new JSONArray(victimas);
			
			String recursos = Connection.connect(URL + "resources");
			JSONArray usuarios = new JSONArray(recursos);
			
			String asocs = Connection.connect(URL + "associations");
			JSONArray asociaciones = new JSONArray(asocs);
			
			String actis = Connection.connect(URL + "activities");
			JSONArray actividades = new JSONArray(actis);

			ahora = new Timestamp(new Date().getTime()).toString();

			// Por cada desastre:
			for(int i = 0; i < desastres.length(); i++){
				JSONObject instancia = desastres.getJSONObject(i);
				Disaster nuevo = new Disaster(
					instancia.getInt("id"),
					instancia.getString("type"),
					instancia.getString("name"),
					instancia.getString("info"),
					instancia.getString("description"),
					instancia.getString("address"),
					new Double(instancia.getString("longitud")),
					new Double(instancia.getString("latitud")),
					instancia.getString("state"),
					instancia.getString("size"),
					instancia.getString("traffic"));
				/*
					<json:property name="floor" value="${evento.planta}"/>
					<json:property name="date" value="${evento.fecha}"/>
					<json:property name="modified" value="${evento.modificado}"/>
					<json:property name="user" value="${evento.usuario}"/>
				 */
				System.out.println("- Nueva emergencia: " + nuevo.getType() + " - " + nuevo.getName() + " (id:" + nuevo.getId() + ")");
				disasters.put(nuevo.getId(), nuevo);
			}

			// Por cada herido:
			for(int i = 0; i < personas.length(); i++){
				JSONObject instancia = personas.getJSONObject(i);
				People nuevo = new People(
					instancia.getInt("id"),
					instancia.getString("type"),
					instancia.getString("name"),
					instancia.getString("info"),
					instancia.getString("description"),
					instancia.getInt("idAssigned"),
					instancia.getInt("quantity"),
					instancia.getString("state"));
				/*
					<json:property name="latitud" value="${evento.latitud}"/>
					<json:property name="longitud" value="${evento.longitud}"/>
					<json:property name="address" value="${evento.direccion}"/>
					<json:property name="size" value="${evento.size}"/>
					<json:property name="traffic" value="${evento.traffic}"/>
					<json:property name="floor" value="${evento.planta}"/>
					<json:property name="date" value="${evento.fecha}"/>
					<json:property name="modified" value="${evento.modificado}"/>
					<json:property name="user" value="${evento.usuario}"/> 
				 */
				
				// si no esta asignado a nadie o a alguien que no existe pasa al siguiente
				if(nuevo.getIdAssigned() == 0 || disasters.containsKey(nuevo.getIdAssigned()) == false){
					continue;
				}

				people.put(nuevo.getId(), nuevo);
				Disaster dis = disasters.get(nuevo.getIdAssigned());
				if(instancia.getString("type").equals("healthy") == false){
					System.out.println("- Herido: " + nuevo.getName() + " con estado " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
				}
				
				/*if(nuevo.getType().equals("slight")){
					dis.setSlight(nuevo);
				}else if(nuevo.getType().equals("serious")){
					dis.setSerious(nuevo);
				}else if(nuevo.getType().equals("dead")){
					dis.setDead(nuevo);
				}else if(nuevo.getType().equals("trapped")){
					dis.setTrapped(nuevo);
				}*/
			
				/* --- ANTIGUO ---
				if(nuevo.getState().equals("erased")){
					System.out.println("- Herido " + nuevo.getName() + " curado (id:" + nuevo.getId() + ")");
					//continue;
				//}
				/*
				if(nuevo.getIdAssigned() == 0){
					if(!people.containsKey(nuevo.getId())){
						continue;
					} // cuando ya existia y tengo que actualizar el desastres borrando sus heridos
					else{
						People antiguo = people.get(nuevo.getId());
						int idDesastre = antiguo.getIdAssigned();
						people.remove(antiguo.getId());
						System.out.println("- ENV: Voy a desasociar el herido " + nuevo.getId() + " del desastre " + idDesastre);
						//Disaster extraido = disasters.get(idDesastre);
						//disasters.remove(idDesastre);

						/*Disaster insertado = new Disaster(
							extraido.getId(),
							extraido.getType(),
							extraido.getName(),
							extraido.getInfo(),
							extraido.getDescription(),
							extraido.getAddress(),
							extraido.getLongitud(),
							extraido.getLatitud(),
							extraido.getState(),
							extraido.getSize(),
							extraido.getTraffic());
						disasters.put(insertado.getId(), insertado);
					} * /
				}else{
					People antiguo = people.get(nuevo.getId());
					Disaster dis = disasters.get(nuevo.getIdAssigned());
					if(!people.containsKey(nuevo.getId())){
						System.out.println("- Herido " + nuevo.getName() + " con estado " + nuevo.getType());
					}else{
						System.out.println("- Herido " + nuevo.getName() + " actualizado con estado " + nuevo.getType());
					}
					people.put(nuevo.getId(), nuevo);
					if(antiguo.getType().equals(nuevo.getType()) == false && nuevo.getIdAssigned() != 0){
						if(nuevo.getType().equals("slight")){
							dis.setSlight(nuevo);
						}else if(nuevo.getType().equals("serious")){
							dis.setSerious(nuevo);
						}else if(nuevo.getType().equals("dead")){
							dis.setDead(nuevo);
						}else if(nuevo.getType().equals("trapped")){
							dis.setTrapped(nuevo);
						}

						if(antiguo.getType().equals("slight")){
							dis.setSlight(antiguo);
						}else if(antiguo.getType().equals("serious")){
							dis.setSerious(antiguo);
						}else if(antiguo.getType().equals("dead")){
							dis.setDead(antiguo);
						}else if(antiguo.getType().equals("trapped")){
							dis.setTrapped(antiguo);
						}
					}
				}
				*/
			}
			
			// Por cada usuario logueado
			for(int i = 0; i < usuarios.length(); i++){
				JSONObject instancia = usuarios.getJSONObject(i);
				Resource nuevo = new Resource(
					instancia.getInt("id"),
					instancia.getString("type"),
					instancia.getString("name"),
					instancia.getString("info"),
					instancia.getString("description"),
					new Double(instancia.getString("latitud")),
					new Double(instancia.getString("longitud")),
					instancia.getString("address"),
					instancia.getInt("idAssigned"));
				resources.put(nuevo.getId(), nuevo);
				System.out.println("- Nuevo usuario: " + nuevo.getName() + " - " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
			}
			
			// Por cada asociacion:
			for(int i = 0; i < asociaciones.length(); i++){
				JSONObject instancia = asociaciones.getJSONObject(i);
				Association nuevo = new Association(
					instancia.getInt("id"),
					instancia.getInt("idInjured"),
					instancia.getInt("idDisaster"),
					instancia.getString("state"));
				People herido = people.get(nuevo.getIdInjured());
				Disaster emergencia = disasters.get(nuevo.getIdDisaster());
				associations.put(nuevo.getId(), nuevo);
				
				if(herido.getType().equals("slight")){
					emergencia.setSlight(herido);
				}else if(herido.getType().equals("serious")){
					emergencia.setSerious(herido);
				}else if(herido.getType().equals("dead")){
					emergencia.setDead(herido);
				}else if(herido.getType().equals("trapped")){
					emergencia.setTrapped(herido);
				}
				System.out.println("- Nueva asociacion: herido '" + herido.getName() + "' con emergencia '" + emergencia.getName() + "' (id:" + nuevo.getId() + ")");
			}
			
			// Por cada actividad:
			for(int i = 0; i < actividades.length(); i++){
				JSONObject instancia = actividades.getJSONObject(i);
				Activity nuevo = new Activity(
					instancia.getInt("id"),
					instancia.getInt("idUser"),
					instancia.getInt("idDisaster"),
					instancia.getString("type"),
					instancia.getString("state"));
				Disaster emergencia = disasters.get(nuevo.getIdDisaster());
				activities.put(nuevo.getId(), nuevo);
				System.out.println("- Nueva aactividad '" + nuevo.getType() + "' en emergencia '" + emergencia.getName() + "' (id:" + nuevo.getId() + ")");
			}
		}catch(JSONException e){
			System.out.println("Error with JSON **** : " + e);
		}

		// Hacer una llamada cada pocos segundos al metodo actualiza()
		this.temporizador.start();
	}

	/**
	 * 
	 */
	public void actualiza(){
		temporizador.reset();
		// ESTO PARA ACTUALIZAR - recibo el json actualizador
		try{
			String eventos = Connection.connect(URL + "events/modified/" + ahora);
			JSONArray desastres = new JSONArray(eventos);

			String victimas = Connection.connect(URL + "people/modified/" + ahora);
			JSONArray personas = new JSONArray(victimas);
			
			String recursos = Connection.connect(URL + "resources/modified/" + ahora);
			JSONArray usuarios = new JSONArray(recursos);
			
			String asocs = Connection.connect(URL + "associations/modified/" + ahora);
			JSONArray asociaciones = new JSONArray(asocs);
			
			String actis = Connection.connect(URL + "activities/modified/" + ahora);
			JSONArray actividades = new JSONArray(actis);

			ahora = new Timestamp(new Date().getTime()).toString();

			// Por cada desastre:
			for(int i = 0; i < desastres.length(); i++){
				JSONObject instancia = desastres.getJSONObject(i);
				Disaster nuevo = new Disaster(
					instancia.getInt("id"),
					instancia.getString("type"),
					instancia.getString("name"),
					instancia.getString("info"),
					instancia.getString("description"),
					instancia.getString("address"),
					new Double(instancia.getString("longitud")),
					new Double(instancia.getString("latitud")),
					instancia.getString("state"),
					instancia.getString("size"),
					instancia.getString("traffic"));

				if(disasters.containsKey(nuevo.getId())){
					// si ya existia actualizo el desastre existente
					//Disaster viejo = disasters.get(nuevo.getId());

					// si se ha eliminado lo borro directamente
					if(nuevo.getState().equals("erased")){
						disasters.remove(nuevo.getId());
						System.out.println("- Emergencia eliminada: " + nuevo.getName() + " (id:" + nuevo.getId() + ")");
					}else{
						disasters.put(nuevo.getId(), nuevo);
						System.out.println("- Emergencia actualizada... " + nuevo.getName() + " - " + nuevo.getState() + " (id:" + nuevo.getId() + ")");
					}
				}else{
					disasters.put(nuevo.getId(), nuevo);
					System.out.println("- Nueva emergencia: " + nuevo.getType() + " - " + nuevo.getName() + " (id:" + nuevo.getId() + ")");
				}
			}

			// Por cada herido:
			for(int i = 0; i < personas.length(); i++){
				JSONObject instancia = personas.getJSONObject(i);
				People nuevo = new People(
					instancia.getInt("id"),
					instancia.getString("type"),
					instancia.getString("name"),
					instancia.getString("info"),
					instancia.getString("description"),
					instancia.getInt("idAssigned"),
					instancia.getInt("quantity"),
					instancia.getString("state"));
				
				if(disasters.containsKey(nuevo.getId())){
					//People antiguo = people.get(nuevo.getId());
					
					if(nuevo.getState().equals("erased")){
						people.remove(nuevo.getId());
						System.out.println("- Herido " + nuevo.getName() + " curado (id:" + nuevo.getId() + ")");
					}else{
						people.put(nuevo.getId(), nuevo);
						System.out.println("- Herido " + nuevo.getName() + " actualizado con estado " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
					}
				}else{
					people.put(nuevo.getId(), nuevo);
					System.out.println("- Herido " + nuevo.getName() + " con estado " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
				}
				
				/*if(nuevo.getIdAssigned() == 0){
					if(!people.containsKey(nuevo.getId())){
						continue;
					} // cuando ya existia y tengo que actualizar el desastres borrando sus heridos
					else{
						People antiguo = people.get(nuevo.getId());
						int idDesastre = antiguo.getIdAssigned();
						people.remove(antiguo.getId());
						System.out.println("- ENV: Voy a desasociar el herido " + nuevo.getId() + " del desastre " + idDesastre);
						Disaster extraido = disasters.get(idDesastre);
						disasters.remove(idDesastre);

						Disaster insertado = new Disaster(
							extraido.getId(),
							extraido.getType(),
							extraido.getName(),
							extraido.getInfo(),
							extraido.getDescription(),
							extraido.getAddress(),
							extraido.getLongitud(),
							extraido.getLatitud(),
							extraido.getState(),
							extraido.getSize(),
							extraido.getTraffic());
						disasters.put(insertado.getId(), insertado);
					}
				}else{
					People antiguo = people.get(nuevo.getId());
					Disaster dis = disasters.get(nuevo.getIdAssigned());
					if(!people.containsKey(nuevo.getId())){
						System.out.println("- Herido " + nuevo.getName() + " con estado " + nuevo.getType());
					}else{
						System.out.println("- Herido " + nuevo.getName() + " actualizado con estado " + nuevo.getType());
					}
					people.put(nuevo.getId(), nuevo);
					if(antiguo.getType().equals(nuevo.getType()) == false && nuevo.getIdAssigned() != 0){
						if(nuevo.getType().equals("slight")){
							dis.setSlight(nuevo);
						}else if(nuevo.getType().equals("serious")){
							dis.setSerious(nuevo);
						}else if(nuevo.getType().equals("dead")){
							dis.setDead(nuevo);
						}else if(nuevo.getType().equals("trapped")){
							dis.setTrapped(nuevo);
						}

						if(antiguo.getType().equals("slight")){
							dis.setSlight(antiguo);
						}else if(antiguo.getType().equals("serious")){
							dis.setSerious(antiguo);
						}else if(antiguo.getType().equals("dead")){
							dis.setDead(antiguo);
						}else if(antiguo.getType().equals("trapped")){
							dis.setTrapped(antiguo);
						}
					}
				}*/
			}
			
			// Por casa usuario:
			for(int i = 0; i < usuarios.length(); i++){
				JSONObject instancia = usuarios.getJSONObject(i);
				Resource nuevo = new Resource(
					instancia.getInt("id"),
					instancia.getString("type"),
					instancia.getString("name"),
					instancia.getString("info"),
					instancia.getString("description"),
					new Double(instancia.getString("latitud")),
					new Double(instancia.getString("longitud")),
					instancia.getString("address"),
					instancia.getInt("idAssigned"));
				if(resources.containsKey(nuevo.getId())){
					if(instancia.getString("state").equals("erased")){
						resources.remove(nuevo.getId());
						System.out.println("- El usuario " + instancia.getString("name") + " ha cerrado sesion");
					}
				}else{
					resources.put(nuevo.getId(), nuevo);
					System.out.println("- Nuevo usuario: " + nuevo.getName() + " - " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
					//String tipoUsuario = instancia.getString("info");
					//IGoal sp = createGoal("cms_create_component");
					//sp.getParameter("type").setValue("jadex/desastres/caronte/" + tipoUsuario + "/" + tipoUsuario + ".agent.xml");
					//dispatchSubgoalAndWait(sp);
				}
			}
			
			// Por cada asociacion:
			for(int i = 0; i < asociaciones.length(); i++){
				JSONObject instancia = asociaciones.getJSONObject(i);
				Association nuevo = new Association(
					instancia.getInt("id"),
					instancia.getInt("idInjured"),
					instancia.getInt("idDisaster"),
					instancia.getString("state"));
				People herido = people.get(nuevo.getIdInjured());
				Disaster emergencia = disasters.get(nuevo.getIdDisaster());
				if(nuevo.getState().equals("erased")){
					associations.remove(nuevo.getId());
					System.out.println("- Asociacion eliminada entre herido '" + herido.getName() + "' y emergencia '" + emergencia.getName() + "' (id:" + nuevo.getId() + ")");
				}else{
					associations.put(nuevo.getId(), nuevo);
					System.out.println("- Nueva asociacion: herido '" + herido.getName() + "' con emergencia '" + emergencia.getName() + "' (id:" + nuevo.getId() + ")");
				}
				
				if(herido != null && emergencia != null){
					if(herido.getType().equals("slight")){
						emergencia.setSlight(herido);
					}else if(herido.getType().equals("serious")){
						emergencia.setSerious(herido);
					}else if(herido.getType().equals("dead")){
						emergencia.setDead(herido);
					}else if(herido.getType().equals("trapped")){
						emergencia.setTrapped(herido);
					}
				}
			}
			
			// Por cada actividad:
			for(int i = 0; i < actividades.length(); i++){
				JSONObject instancia = actividades.getJSONObject(i);
				Activity nuevo = new Activity(
					instancia.getInt("id"),
					instancia.getInt("idUser"),
					instancia.getInt("idDisaster"),
					instancia.getString("type"),
					instancia.getString("state"));
				String nombre = "";
				if(disasters.containsKey(nuevo.getIdDisaster())){
					nombre = disasters.get(nuevo.getIdDisaster()).getName();
				}else if(people.containsKey(nuevo.getIdDisaster())){
					nombre = people.get(nuevo.getIdDisaster()).getName();
				}
				if(nuevo.getState().equals("erased")){
					activities.remove(nuevo.getId());
					System.out.println("- Actividades terminadas en emergencia '" + nombre + "' (id:" + nuevo.getId() + ")");
				}else{
					activities.put(nuevo.getId(), nuevo);
					System.out.println("- Nueva actividad '" + nuevo.getType() + "' en emergencia '" + nombre + "' (id:" + nuevo.getId() + ")");
				}
			}
		}catch(JSONException e){
			System.out.println("Error with JSON *****" + e);
		}

		//temporizador.run();
	}

	/**
	 * Obtener una instancia del entorno, para asi poder interactuar sobre el.
	 * NOTA: se puede crear un parque de bomberos, desde el cual salgan, => Si es un bombero, tiene posicion inicial fija.
	 * 
	 * @param tipo
	 * @param nombre
	 * @param pos
	 * @return Instancia de entorno
	 */
	public static Environment getInstance(String tipo, String nombre, Position pos){
		// La primera vez que se llama a este metodo (el agente Environment), instance vale null
		if(instance == null){
			instance = new Environment();
		}
		if(tipo != null && nombre != null){
			instance.addWorldObject(tipo, nombre, pos, null);
		}
		return instance;
	}

	/**
	 * Borra la instancia (resetea el entorno).
	 */
	public static void clearInstance(){
		instance = null;
	}

	/**
	 * Annade un objeto al entorno.
	 * 
	 * @param type
	 * @param name
	 * @param position
	 * @param info 
	 */
	public void addWorldObject(String type, String name, Position position, String info){
		//System.out.println("Ejecuto addWorldObject ###" + position +" type: " + type + " nombre: "+name);
		// De momento no usamos el atributo info
		// La posicion siempre se especifica
		WorldObject wo = new WorldObject(name, type, position, null);

		if(type.equals(AMBULANCIA) || type.equals(BOMBERO) || type.equals(POLICIA)
				|| type.equals(ENFERMERO) || type.equals(AMBULANCIA2) || type.equals(GEROCULTOR) || type.equals(AUXILIAR) || type.equals(OTRO_PERSONAL)){
			// REST -> cree el recurso
			String longitud = String.valueOf(position.getY());
			String latitud = String.valueOf(position.getX());
			System.out.println("LLamada a REST creando agente...");
			String resultado = Connection.connect(URL + "post/type=" + type +
				"&name=" + name + "&info=" + info + "&latitud=" + latitud + "&longitud=" + longitud);

			try{
				//JSON -> guardo el id
				JSONObject idJson = new JSONObject(resultado);
				int id = idJson.getInt("id");
				wo.setId(id);
				System.out.println("Id del objeto creado: " + id);
				System.out.println("Creando agente del tipo " + type);
				agentes.put(name, wo); // Si es un pironamo, se annade a la tabla de agentes
				objetos.put(position, wo); // Y tambien a la de elementos del mundo
				numAgentes++;
			}catch(Exception e){
				System.out.println("Error json: " + e);
			}
		}
		if(type.equals(CENTRAL) || type.equals(CIUDADANO) || type.equals(CENTRAL_EMERGENCIAS) || type.equals(COORDINADOR_EMERGENCIAS)
				|| type.equals(GSO) || type.equals(MEDICO_CACH) || type.equals(COORDINADOR_HOSPITAL) || type.equals(COORDINADOR_MEDICO)){
			System.out.println("Creando agente de tipo " + type);
			agentes.put(name, wo); // Si es una central, se annade a la tabla de agentes
			objetos.put(position, wo); // Y tambien a la de elementos del mundo
			numAgentes++;
		}
	}

	/**
	 * Cambia la posicion de un agente.
	 * Los eventos no se mueven de posicion.
	 * 
	 * @param name
	 * @param pos
	 */
	public void go(String name, Position pos){
		Position oldPos;

		WorldObject wo;
		synchronized(this){ // No deben varios agentes tocar las tablas Hash a la vez
			// Obtenemos el agente de la tabla Hash agentes, dado su nombre
			wo = getAgent(name);
			oldPos = wo.getPosition(); // Obtenemos la posicion del agente antes de desplazarlo
			objetos.remove(oldPos, wo); // Eliminamos el agente de su posicion (de la coleccion Objetos)
		}

		synchronized(this){
			wo.setPosition(pos);// Actualizamos la posicion al objeto agente
			objetos.put(pos, wo); // Annadimos el objeto, con la posicion renovada
			removeAgent(name); // Actualizamos la tabla Hash de agentes
			agentes.put(name, wo);
		}

		// Avisamos para el modo de evaluacion dinamico de posicion de que hemos variado una poscicion
		pcs.firePropertyChange("cambio_de_posicion", oldPos, pos);
	}

	/**
	 * 
	 * @param name
	 * @param inicial
	 * @param dest
	 * @param desastre
	 * @param herido
	 * @throws InterruptedException 
	 */
	public void andar(String name, Position inicial, Position dest, int desastre, int herido) throws InterruptedException{
		Double x1 = inicial.getX();
		Double x2 = dest.getX();
		Double y1 = inicial.getY();
		Double y2 = dest.getY();

		Double pendiente = Math.atan((y2-y1)/(x2-x1));
		int velocidad = 150; // velocidad inversa, cuanto mas grande mas despacio

		Double pasoX = (0.25 / velocidad) * Math.cos(pendiente);
		Double pasoY = (0.25 / velocidad) * Math.abs(Math.sin(pendiente));

		Boolean arriba = (x2-x1 > 0);
		Boolean derecha = (y2-y1 > 0);

		// El punto destino esta a la derecha del origen
		if(derecha){
			if(arriba){
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
			}else{
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
			}
		}else{ // El punto esta a la izquierda
			if(arriba){
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
	}

	/**
	 * Pinta el movimiento de un agente en el mapa mediante REST.
	 * 
	 * @param id
	 * @param idHerido
	 * @param latitud
	 * @param longitud
	 * @throws InterruptedException 
	 */
	public void pinta(int id, int idHerido, Double latitud, Double longitud) throws InterruptedException{
		String resultado = Connection.connect(URL + "put/" + id + "/latlong/" + latitud + "/" + longitud);
		if(idHerido != 0){
			String resultado2 = Connection.connect(URL + "put/" + idHerido + "/latlong/" + latitud + "/" + longitud);
		}
		Thread.sleep(1000);
	}

	/**
	 * Devuelve un agente dado su nombre (el nombre de los agentes es unico).
	 * 
	 * @param name
	 * @return Agente
	 */
	public synchronized WorldObject getAgent(String name){
		assert agentes.containsKey(name);
		return agentes.get(name);
	}

	/**
	 * Elimina un agente dado su nombre (el nombre de los agentes es unico).
	 * 
	 * @param name
	 * @return Agente eliminado
	 */
	public synchronized WorldObject removeAgent(String name){
		assert agentes.containsKey(name);
		return agentes.remove(name);
	}

	/**
	 * Devuelve la posicion de un agente dado su nombre (el nombre de los agentes es unico).
	 * 
	 * @param name
	 * @return Posicion del agente
	 */
	public synchronized Position getAgentPosition(String name){
		assert agentes.containsKey(name);
		return agentes.get(name).getPosition();
	}

	/**
	 * Devuelve un evento dado su id (el id de los eventos es unico).
	 * 
	 * @param id
	 * @return Desastre
	 */
	public synchronized Disaster getEvent(int id){
		assert disasters.containsKey(id);
		return disasters.get(id);
	}

	/**
	 * Elimina un evento dado su nombre (el nombre de los eventos es unico).
	 * 
	 * @param id
	 * @return Evento eliminado
	 */
	public synchronized Disaster removeEvent(int id){
		assert disasters.containsKey(id);
		return disasters.remove(id);
	}

	/**
	 * Devuelve la posicion de un evento dado su nombre (concretamente su id).
	 * 
	 * @param id
	 * @return Posicion del evento
	 */
	public synchronized Position getEventPosition(int id){
		assert disasters.containsKey(id);
		return new Position(disasters.get(id).getLatitud(), disasters.get(id).getLongitud());
	}

	/**
	 * Devuelve todos los objetos que haya en una posicion.
	 * 
	 * @param pos
	 * @return Todos los objetos de la posicion
	 */
	protected WorldObject[] getWorldObjects(Position pos){
		Collection col = objetos.getCollection(pos);
		return (WorldObject[])col.toArray();

	}

	/**
	 * Devuelve todos los agentes.
	 * @return Todos los agentes
	 */
	protected WorldObject[] getAgentes(){
		Collection<WorldObject> col = (Collection<WorldObject>) agentes.values();
		return col.toArray(new WorldObject[col.size()]);
	}

	/**
	 * Devuelve todos los eventos.
	 * @return Todos los eventos
	 */
	protected WorldObject[] getEventos(){
		Collection<Disaster> col = (Collection<Disaster>) disasters.values();
		return col.toArray(new WorldObject[col.size()]);
	}

	/**
	 * Devuelve el numero total de agentes creados.
	 * @return numero total de agentes creados.
	 */
	public int getNumAgentes(){
		return numAgentes;
	}

	/**
	 * Devuelve el numero total de eventos creados.
	 * @return numero total de eventos creados.
	 */
	public int getNumEventos(){
		return numEventos;
	}

	/**
	 * Devuelve una posicion aleatoria conociendo la ciudad.
	 * Puesto que de momento solo tenemos Calasparra en la lista, no hace falta especificar la ciudad.
	 * 
	 * @param proyecto
	 * @return Posicion aleatoria
	 */
	//protected Position getRandomPosition(Location loc){
	public Position getRandomPosition(String proyecto){
		// Las dos posiciones que se crean son las esquinas superior derecha e inferior izquierda del marco que contiene a Calasparra
		Location location = new Location("Madrid", new Position(40.44, -3.66), new Position(40.39, -3.72));
		if(proyecto.equals("caronte")){
			location = new Location("Calasparra", new Position(38.233181, -1.69724), new Position(38.231251, -1.70252));
		}

		Position esd = (Position)location.getESD(); // Esquina superior Derecha
		Position eii = (Position)location.getEII(); // Esquina inferior Izquierda

		Double random1 = rnd.nextDouble();
		Double random2 = rnd.nextDouble();

		// Obtenemos la altura del marco, tomando la diferencia de latitudes
		Double alturaMarco = Math.abs(esd.getY() - eii.getY());
		// Obtenemos la anchura del marco, tomando la diferencia de longitudes
		Double anchuraMarco = Math.abs(esd.getX() - eii.getX());

		// Obtenemos la latitud y longitud menor, para sumarles una fraccion aleatoria de la diferencia que las separa
		Double marcoBottom = Math.min(esd.getY(), eii.getY());
		Double marcoLeft = Math.min(esd.getX(), eii.getX());

		Position nuevaAleatoria = new Position(marcoLeft + random1 * anchuraMarco, marcoBottom + random2 * alturaMarco);
		return nuevaAleatoria;
	}

	/**
	 * @return the tablon.
	 */
	public int getTablon(){
		return tablon;
	}

	/**
	 * @param tablon the tablon to set.
	 */
	public void setTablon(int tablon){
		this.tablon = tablon;
	}

	/**
	 * Termina la hebra del temporzador.
	 * 
	 * @throws IOException 
	 */
	public synchronized void terminar() throws IOException{
		System.out.println("Deteniendo entorno...");
		temporizador = null;
		System.out.println("Entorno detenido");
	}

	/**
	 * Imprime un String por pantalla y lo envia para mostrar en la web.
	 *
	 * @param valor String a imprimir.
	 * @param nivel Nivel del mensaje (0 todos los usuarios, 1 todos los conectados,...).
	 */
	public final void printout(String valor, int nivel){
		Connection.connect(Environment.URL + "message/" + valor + "/" + nivel);
		System.out.println(valor);
	}

	//*****************
	// LISTADO
	//*****************

	public void putListado(String nombre, IComponentIdentifier id){
		//HashMap listado = (HashMap) getBeliefbase().getBelief("listado").getFact();
		listado.put(nombre,id);
		//getBeliefbase().getBelief("listado").setFact(listado);
	}

	public IComponentIdentifier getListado(String nombre){
		//HashMap<String,IComponentIdentifier> listado = (HashMap) getBeliefbase().getBelief("listado").getFact();
		return listado.get(nombre);
	}

	public void removeListado(String usuario){
		//HashMap<String,IComponentIdentifier> listado = (HashMap) getBeliefbase().getBelief("listado").getFact();
		listado.remove(usuario);
	}

	public boolean containsListado(String nombre){
		return listado.containsKey(nombre);
	}
}