package disasters.caronte;

import disasters.*;
import jadex.bridge.IComponentIdentifier;
import jadex.commons.SimplePropertyChangeSupport;
import java.sql.Timestamp;
import java.util.*;
import org.json.me.*;

/**
 * Clase para modelar el entorno, proporcionando metodos para interactuar con el.
 * 
 * @author aebeda
 * @author Juan Luis Molina
 */
public class Entorno{
	/** URL para REST. */
	public static final String URL = "http://localhost:8080/caronte/rest/";
	/** Agente coordinador. */
	public static final String COORDINADOR = "Coordinador";
	/** Agente intervencion en incendios. */
	public static final String INTERVENCION_INCENDIOS = "IntervencionIncendios";
	/** Agente atencion de heridos. */
	public static final String ATENCION_HERIDOS = "AtencionHeridos";
	/** Agente evacuacion. */
	public static final String EVACUACION = "EvacuacionResidencia";
	/** Agente apoyo externo. */
	public static final String APOYO_EXTERNO = "ApoyoExterno";
	private static final String[] COMPONENTES_AUX = {COORDINADOR, INTERVENCION_INCENDIOS, ATENCION_HERIDOS, EVACUACION};
	public static final List<String> COMPONENTES = Arrays.asList(COMPONENTES_AUX);
	private static TimerJSON temporizador;
	private final int tiempoJSON = 5000;
	private String ahora;
	/** Eventos (id -> Disaster). */
	private HashMap<Integer,Disaster> disasters;
	private HashMap<Integer,People> people;
	private HashMap<Integer,Resource> resources;
	private HashMap<Integer,Association> associations;
	private HashMap<Integer,Activity> activities;
	private HashMap<Integer,String> usedResources;
	private ArrayList<Integer> inactiveResources;
	/** Emergencias que se deben atender. */
	private ArrayList<Integer> tablonEventos;
	private ArrayList<Integer> tablonHeridos;
	
	/** Agentes (nombre -> WorldObject). */
	private HashMap<String,WorldObject> agentes;
	/** Numero de agentes creados, no tienen por que estar activos. */
	private int numAgentes;
	/** Objeto para notificar cambios. */
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
	/** Agente policia. */
	public static final String POLICIA = "police";
	/**  */
	private static final String[] AGENTES_AUX = {ENFERMERO, CELADOR, GEROCULTOR, AUXILIAR,
		RECEPCIONISTA, OTRO_PERSONAL, CIUDADANO, AMBULANCIA, BOMBERO, POLICIA};
	public static final List<String> AGENTES = Arrays.asList(AGENTES_AUX);
	private static final String[] COMPONENTES2_AUX = {COORDINADOR_EMERGENCIAS, CENTRAL_EMERGENCIAS};
	public static final List<String> COMPONENTES2 = Arrays.asList(COMPONENTES2_AUX);

	//---------------------
	/**
	 * Constructor del entorno.
	 */
	public Entorno(){
		temporizador = new TimerJSON(tiempoJSON, this);
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
					new Double(instancia.getString("latitud")),
					new Double(instancia.getString("longitud")),
					instancia.getString("address"),
					instancia.getInt("floor"),
					instancia.getString("size"),
					instancia.getString("traffic"),
					instancia.getString("state"));
				System.out.println("- Nueva emergencia: " + nuevo.getType() + " - " + nuevo.getName() + " (id:" + nuevo.getId() + ")");
				disasters.put(nuevo.getId(), nuevo);
			}
			// Por cada herido:
			for(int i = 0; i < personas.length(); i++){
				JSONObject instancia = personas.getJSONObject(i);
				People nuevo = new People(
					instancia.getInt("id"),
					instancia.getString("type"),
					instancia.getInt("quantity"),
					instancia.getString("name"),
					instancia.getString("info"),
					instancia.getString("description"),
					new Double(instancia.getString("latitud")),
					new Double(instancia.getString("longitud")),
					instancia.getString("address"),
					instancia.getInt("floor"),
					instancia.getString("size"),
					instancia.getString("traffic"),
					instancia.getString("state"),
					instancia.getInt("idAssigned"));
				people.put(nuevo.getId(), nuevo);
				if(nuevo.getType().equals("healthy")){
					System.out.println("- Nueva persona: " + nuevo.getName() + " (id:" + nuevo.getId() + ")");
				}else{
					System.out.println("- Nuevo herido: " + nuevo.getName() + " con estado " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
				}
			}
			// Por cada usuario logueado:
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
					instancia.getInt("floor"),
					instancia.getString("state"),
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
				associations.put(nuevo.getId(), nuevo);
				People herido = people.get(nuevo.getIdInjured());
				Disaster emergencia = disasters.get(nuevo.getIdDisaster());
				System.out.println("- Nueva asociacion: herido '" + herido.getName() + "' con emergencia '" + emergencia.getName() + "' (id:" + nuevo.getId() + ")");

				if(herido.getType().equals("slight")){
					emergencia.addSlight(herido);
				}else if(herido.getType().equals("serious")){
					emergencia.addSerious(herido);
				}else if(herido.getType().equals("dead")){
					emergencia.addDead(herido);
				}else if(herido.getType().equals("trapped")){
					emergencia.addTrapped(herido);
				}
				disasters.put(emergencia.getId(), emergencia);
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
				activities.put(nuevo.getId(), nuevo);
				Resource usuario = resources.get(nuevo.getIdUser());
				Disaster emergencia = disasters.get(nuevo.getIdDisaster());
				System.out.println("- Nueva actividad '" + nuevo.getType() + "' realizada por '" + usuario.getName() +
					"' en emergencia '" + emergencia.getName() + "' (id:" + nuevo.getId() + ")");
			}
		}catch(JSONException e){
			System.out.println("Error con JSON: " + e);
		}

		// Hacer una llamada cada pocos segundos al metodo actualiza()
		temporizador.start();
	}

	/**
	 * Actualizacion del entorno.
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
					new Double(instancia.getString("latitud")),
					new Double(instancia.getString("longitud")),
					instancia.getString("address"),
					instancia.getInt("floor"),
					instancia.getString("size"),
					instancia.getString("traffic"),
					instancia.getString("state"));
				
				// si ya existia actualizo el desastre existente
				if(disasters.containsKey(nuevo.getId())){
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
					instancia.getInt("quantity"),
					instancia.getString("name"),
					instancia.getString("info"),
					instancia.getString("description"),
					new Double(instancia.getString("latitud")),
					new Double(instancia.getString("longitud")),
					instancia.getString("adreess"),
					instancia.getInt("floor"),
					instancia.getString("size"),
					instancia.getString("traffic"),
					instancia.getString("state"),
					instancia.getInt("idAssigned"));
				
				if(people.containsKey(nuevo.getId())){
					if(nuevo.getState().equals("erased")){
						people.remove(nuevo.getId());
						System.out.println("- Herido " + nuevo.getName() + " curado (id:" + nuevo.getId() + ")");
					}else{
						People antiguo = people.get(nuevo.getId());
						people.put(nuevo.getId(), nuevo);
						System.out.println("- Herido " + nuevo.getName() + " actualizado con estado " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
						
						if(nuevo.getType().equals(antiguo.getType()) == false){
							ArrayList<Disaster> emergencias = new ArrayList<Disaster>();
							for(int j = 0; j < associations.size(); j++){
								if(associations.get(j).getIdInjured() == nuevo.getId()){
									emergencias.add(disasters.get(associations.get(j).getIdDisaster()));
								}
							}
							if(emergencias.isEmpty() == false){
								for(int j = 0; j < emergencias.size(); j++){
									if(antiguo.getType().equals("slight")){
										emergencias.get(j).removeSlight(antiguo);
									}else if(antiguo.getType().equals("serious")){
										emergencias.get(j).removeSerious(antiguo);
									}else if(antiguo.getType().equals("dead")){
										emergencias.get(j).removeDead(antiguo);
									}else if(antiguo.getType().equals("trapped")){
										emergencias.get(j).removeTrapped(antiguo);
									}
									if(nuevo.getType().equals("slight")){
										emergencias.get(j).addSlight(nuevo);
									}else if(nuevo.getType().equals("serious")){
										emergencias.get(j).addSerious(nuevo);
									}else if(nuevo.getType().equals("dead")){
										emergencias.get(j).addDead(nuevo);
									}else if(nuevo.getType().equals("trapped")){
										emergencias.get(j).addTrapped(nuevo);
									}
									disasters.put(emergencias.get(j).getId(), emergencias.get(j));
								}
							}
						}
					}
				}else{
					people.put(nuevo.getId(), nuevo);
					System.out.println("- Nuevo herido " + nuevo.getName() + " con estado " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
				}
			}
			// Por cada usuario:
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
					instancia.getInt("floor"),
					instancia.getString("state"),
					instancia.getInt("idAssigned"));
				
				if(resources.containsKey(nuevo.getId())){
					if(nuevo.getState().equals("erased")){
						resources.remove(nuevo.getId());
						System.out.println("- El usuario " + nuevo.getName() + " ha cerrado sesion");
					}else{
						resources.put(nuevo.getId(), nuevo);
						System.out.println("- El usuario " + nuevo.getName() + " ha actualizado sus datos");
					}
				}else{
					resources.put(nuevo.getId(), nuevo);
					System.out.println("- Nuevo usuario: " + nuevo.getName() + " - " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
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
					if(emergencia != null){
						if(herido.getType().equals("slight")){
							emergencia.removeSlight(herido);
						}else if(herido.getType().equals("serious")){
							emergencia.removeSerious(herido);
						}else if(herido.getType().equals("dead")){
							emergencia.removeDead(herido);
						}else if(herido.getType().equals("trapped")){
							emergencia.removeTrapped(herido);
						}
						disasters.put(emergencia.getId(), emergencia);
					}
				}else{
					associations.put(nuevo.getId(), nuevo);
					System.out.println("- Nueva asociacion: herido '" + herido.getName() + "' con emergencia '" + emergencia.getName() + "' (id:" + nuevo.getId() + ")");
					if(herido.getType().equals("slight")){
						emergencia.addSlight(herido);
					}else if(herido.getType().equals("serious")){
						emergencia.addSerious(herido);
					}else if(herido.getType().equals("dead")){
						emergencia.addDead(herido);
					}else if(herido.getType().equals("trapped")){
						emergencia.addTrapped(herido);
					}
					disasters.put(emergencia.getId(), emergencia);
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
					nombre = "emergencia '" + disasters.get(nuevo.getIdDisaster()).getName() + "'";
				}else if(people.containsKey(nuevo.getIdDisaster())){
					nombre = "herido '" + people.get(nuevo.getIdDisaster()).getName() + "'";
				}
				
				if(activities.containsKey(nuevo.getId())){
					activities.remove(nuevo.getId());
					System.out.println("- Actividad '" + nuevo.getType() + "' terminada en " + nombre + " (id:" + nuevo.getId() + ")");
				}else{
					if(nuevo.getState().equals("erased")){
						activities.put(nuevo.getId(), nuevo);
						System.out.println("- Actividad '" + nuevo.getType() + "' realizada en 'id:" + nuevo.getIdDisaster() + "' (id:" + nuevo.getId() + ")");
					}else{
						activities.put(nuevo.getId(), nuevo);
						System.out.println("- Nueva actividad '" + nuevo.getType() + "' en " + nombre + " (id:" + nuevo.getId() + ")");
					}
				}
			}
		}catch(JSONException e){
			System.out.println("Error con JSON: " + e);
		}
	}

	/**
	 * Obtener una instancia del entorno, para asi poder interactuar sobre el.
	 * 
	 * @param tipo Tipo
	 * @param nombre Nombre
	 * @param pos Posicion
	 * @return Instancia de entorno
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
		//temporizador.reset();
		temporizador = null;
		instance = null;
		System.out.println("Entorno detenido");
	}
	
	/**
	 * Annade un objeto al entorno.
	 * 
	 * @param type Tipo
	 * @param name Nombre
	 * @param position Posicion
	 * @param info Informacion
	 * @param agentId Identificador del agente
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
				//JSON -> guardo el id
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
	 * Elimina un objeto del entorno
	 * 
	 * @param type Tipo
	 * @param name Nombre
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
	 * @return Todos los agentes
	 */
	public HashMap<String,WorldObject> getAgents(){
		return agentes;
	}
	
	/**
	 * Devuelve un agente dado su nombre (el nombre de los agentes es unico).
	 * 
	 * @param name Nombre
	 * @return Agente
	 */
	public synchronized WorldObject getAgent(String name){
		assert agentes.containsKey(name);
		return agentes.get(name);
	}

	/**
	 * Elimina un agente dado su nombre (el nombre de los agentes es unico).
	 * 
	 * @param name Nombre
	 * @return Agente eliminado
	 */
	public synchronized WorldObject removeAgent(String name){
		assert agentes.containsKey(name);
		return agentes.remove(name);
	}

	/**
	 * Devuelve todos los agentes.
	 * 
	 * @return Todos los agentes
	 */
	public WorldObject[] getAgentes(){
		Collection<WorldObject> col = agentes.values();
		return col.toArray(new WorldObject[col.size()]);
	}

	/**
	 * Devuelve el numero total de agentes creados.
	 * 
	 * @return Numero total de agentes creados
	 */
	public int getNumAgentes(){
		return numAgentes;
	}
	
	/**
	 * Devuelve todos los desastres.
	 * 
	 * @return Todos los desastres
	 */
	public HashMap<Integer,Disaster> getEvents(){
		return disasters;
	}
	
	/**
	 * Devuelve todos los heridos.
	 * 
	 * @return Todos los heridos
	 */
	public HashMap<Integer,People> getPeople(){
		return people;
	}
	
	/**
	 * Devuelve todos los recursos.
	 * 
	 * @return Todos los recursos
	 */
	public HashMap<Integer,Resource> getResources(){
		return resources;
	}
	
	/**
	 * Devuelve todas las asociaciones.
	 * 
	 * @return Todas las asociaciones
	 */
	public HashMap<Integer,Association> getAssociations(){
		return associations;
	}
	
	/**
	 * Devuelve todas las actividades.
	 * 
	 * @return Todas las actividades
	 */
	public HashMap<Integer,Activity> getActivities(){
		return activities;
	}

	/**
	 * Devuelve un evento dado su id.
	 * 
	 * @param id Identificador
	 * @return Desastre
	 */
	public synchronized Disaster getEvent(int id){
		assert disasters.containsKey(id);
		return disasters.get(id);
	}

	/**
	 * Elimina un evento dado su id.
	 * 
	 * @param id Identificador
	 * @return Evento eliminado
	 */
	public synchronized Disaster removeEvent(int id){
		assert disasters.containsKey(id);
		return disasters.remove(id);
	}

	/**
	 * Devuelve un herido dado su id.
	 * 
	 * @param id Identificador
	 * @return Herido
	 */
	public synchronized People getPeople(int id){
		assert people.containsKey(id);
		return people.get(id);
	}

	/**
	 * Elimina un herido dado su id.
	 * 
	 * @param id Identificador
	 * @return Herido eliminado
	 */
	public synchronized People removePeople(int id){
		assert people.containsKey(id);
		return people.remove(id);
	}

	/**
	 * Devuelve un recurso dado su id.
	 * 
	 * @param id Identificador
	 * @return Recurso
	 */
	public synchronized Resource getResource(int id){
		assert resources.containsKey(id);
		return resources.get(id);
	}

	/**
	 * Elimina un recurso dado su id.
	 * 
	 * @param id Identificador
	 * @return Recurso eliminado
	 */
	public synchronized Resource removeResource(int id){
		assert resources.containsKey(id);
		return resources.remove(id);
	}

	/**
	 * Devuelve una asociacion dada su id.
	 * 
	 * @param id Identificador
	 * @return Asociacion
	 */
	public synchronized Association getAssociation(int id){
		assert associations.containsKey(id);
		return associations.get(id);
	}

	/**
	 * Elimina una asociacion dada su id.
	 * 
	 * @param id Identificador
	 * @return Asociacion eliminada
	 */
	public synchronized Association removeAssociation(int id){
		assert associations.containsKey(id);
		return associations.remove(id);
	}

	/**
	 * Devuelve una actividad dada su id.
	 * 
	 * @param id Identificador
	 * @return Actividad
	 */
	public synchronized Activity getActivity(int id){
		assert activities.containsKey(id);
		return activities.get(id);
	}

	/**
	 * Elimina una actividad dada su id.
	 * 
	 * @param id Identificador
	 * @return Actividad eliminado
	 */
	public synchronized Activity removeActivity(int id){
		assert activities.containsKey(id);
		return activities.remove(id);
	}
	
	/**
	 * Devuelve todos los eventos.
	 * 
	 * @return Todos los eventos
	 */
	public Disaster[] getEventsArray(){
		Collection<Disaster> col = disasters.values();
		return col.toArray(new Disaster[col.size()]);
	}
	
	/**
	 * Devuelve todos los heridos.
	 * 
	 * @return Todos los heridos
	 */
	public People[] getPeopleArray(){
		Collection<People> col = people.values();
		return col.toArray(new People[col.size()]);
	}
	
	/**
	 * Devuelve todos los recursos.
	 * 
	 * @return Todos los recursos
	 */
	public Resource[] getResourcesArray(){
		Collection<Resource> col = resources.values();
		return col.toArray(new Resource[col.size()]);
	}
	
	/**
	 * Devuelve todos los eventos.
	 * 
	 * @return Todos los eventos
	 */
	public Association[] getAssociationsArray(){
		Collection<Association> col = associations.values();
		return col.toArray(new Association[col.size()]);
	}
	
	/**
	 * Devuelve todos los eventos.
	 * 
	 * @return Todos los eventos
	 */
	public Activity[] getActivitiesArray(){
		Collection<Activity> col = activities.values();
		return col.toArray(new Activity[col.size()]);
	}

	/**
	 * Devuelve la lista de emergencias que se deben atender.
	 * 
	 * @return the tablon
	 */
	public ArrayList<Integer> getTablonEventos(){
		return tablonEventos;
	}

	/**
	 * Establece la emergencia que se debe atender.
	 * 
	 * @param tablon the tablon to set
	 */
	public void setTablonEventos(int idEmergencia){
		tablonEventos.add(idEmergencia);
	}
	
	/**
	 * Elimina la emergencia atendida.
	 * 
	 * @param tablon the tablon to remove
	 */
	public void removeTablonEventos(Integer idEmergencia){
		tablonEventos.remove(idEmergencia);
		tablonEventos.trimToSize();
	}
	
	/**
	 * Devuelve la lista de emergencias que se deben atender.
	 * 
	 * @return the tablon
	 */
	public ArrayList<Integer> getTablonHeridos(){
		return tablonHeridos;
	}

	/**
	 * Establece la emergencia que se debe atender.
	 * 
	 * @param tablon the tablon to set
	 */
	public void setTablonHeridos(int idEmergencia){
		tablonHeridos.add(idEmergencia);
	}
	
	/**
	 * Elimina la emergencia atendida.
	 * 
	 * @param tablon the tablon to remove
	 */
	public void removeTablonHeridos(Integer idEmergencia){
		tablonHeridos.remove(idEmergencia);
		tablonHeridos.trimToSize();
	}
	
	public Disaster[] getEventosSin(){
		HashMap<Integer,Disaster> disastersAux = (HashMap<Integer,Disaster>) disasters.clone();
		ArrayList<Integer> tablonAux = (ArrayList<Integer>) tablonEventos.clone();
		for(int i = 0; i < tablonAux.size(); i++){
			disastersAux.remove(tablonAux.get(i));
		}
		Collection<Disaster> col = disastersAux.values();
		return col.toArray(new Disaster[col.size()]);
	}
	
	public People[] getHeridosSin(){
		HashMap<Integer,People> peopleAux = (HashMap<Integer,People>) people.clone();
		ArrayList<Integer> tablonAux = (ArrayList<Integer>) tablonHeridos.clone();
		for(int i = 0; i < tablonAux.size(); i++){
			peopleAux.remove(i);
		}
		Collection<People> col = peopleAux.values();
		return col.toArray(new People[col.size()]);
	}
	
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
	
	public synchronized void leaveResource(int id){
		usedResources.remove(id);
	}
	
	public synchronized void addInactiveResource(Integer id){
		inactiveResources.add(id);
	}
	
	public synchronized void removeInactiveResource(Integer id){
		inactiveResources.remove(id);
	}
	
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
	 * Imprime un String por pantalla y lo envia para mostrar en la web.
	 *
	 * @param valor String a imprimir
	 * @param tipo Tipo de receptor (0 oculto, 1 directo y 2 a grupo)
	 * @param receptor ID del receptor (Si tipo=2 --> 0 todos los usuarios, 1 todos los conectados,...)
	 * @param println true para mostrarlo en el terminal
	 */
	public final void printout(String valor, int tipo, int receptor, boolean println){
		Connection.connect(Entorno.URL + "message/" + tipo + "/" + receptor + "/" + valor);
		if(println){
			System.out.println(valor);
		}
	}
	
	// SIMULADOR ************************************************************ //
	
	/**
	 * Modifica la posicion de un agente.
	 * 
	 * @param name Nombre
	 * @param inicial Posicion inicial
	 * @param dest Destino
	 * @param desastre Identificador del desastre
	 * @param herido Identificador del herido
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
	 * Cambia la posicion de un agente. Los eventos no se mueven de posicion.
	 * 
	 * @param name Nombre
	 * @param pos Posicion
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
	 * @param id Identificador del agente
	 * @param idHerido Identificador del herido
	 * @param latitud latitud
	 * @param longitud Longitud
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
	 * Devuelve una posicion aleatoria conociendo la ciudad.
	 * Puesto que de momento solo tenemos Calasparra en la lista, no hace falta especificar la ciudad.
	 * 
	 * @return Posicion aleatoria
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