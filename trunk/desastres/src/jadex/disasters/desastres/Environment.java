package disasters.desastres;

import disasters.*;
import jadex.bridge.IComponentIdentifier;
import jadex.commons.SimplePropertyChangeSupport;
import jadex.commons.collection.MultiCollection;
import java.sql.Timestamp;
import java.util.*;
import org.json.me.*;

/**
 * Clase para modelar el entorno, proporcionando m&eacute;todos para interactuar con &eacute;l.
 * 
 * @author aebeda
 * @author Juan Luis Molina Nogales
 */
public class Environment{
	//------constantes ----
	/** URL para REST. */
	public static final String URL = "http://localhost:8080/desastres/rest/";
	
	// Los nombres de los agentes
	/** Agente ambulancia. */
	public static final String AMBULANCIA = "ambulance";
	/** Agente bombero. */
	public static final String BOMBERO = "firemen";
	/** Agente polic&iacute;a. */
	public static final String POLICIA = "police";
	/** Agente ambulancia (bis). */
	public static final String AMBULANCIA2 = "ambulance";
	/** Agente grupo sanitario operativo. */
	public static final String GSO = "grupoSanitarioOperativo";
	/** Agente m&eacute;dico del centro de atenci&oacute;n y clasificaci&oacute;n de heridos. */
	public static final String MEDICO_CACH = "medicoCACH";
	/** Agente coordinador del hospital. */
	public static final String COORDINADOR_HOSPITAL = "coordinadorHospital";
	/** Agente coordinador m&eacute;dico. */
	public static final String COORDINADOR_MEDICO = "coordinadorMedico";
	/** Agente central de emergencias. */
	public static final String CENTRAL = "central";
	/** Listado de agentes. */
	public static final List<String> AGENTES = Arrays.asList(new String[]{
		AMBULANCIA, BOMBERO, POLICIA, AMBULANCIA2});
	/** Listado de componentes. */
	public static final List<String> COMPONENTES = Arrays.asList(new String[]{
		GSO, MEDICO_CACH, COORDINADOR_HOSPITAL, COORDINADOR_MEDICO, CENTRAL});
	/** Apocalipsis. */
	public static final String APOCALIPSIS = "apocalypse";
	
	// Los nombres de los eventos provocados sobre el mapa
	/** Evento fuego. */
	public static final String FUEGO = "fire";
	/** Evento terremoto. */
	public static final String TERREMOTO = "collapse";
	/** Evento inundaci&oacute;n. */
	public static final String INUNDACION = "flood";
	/** Evento herido leve. */
	public static final String HERIDO_LEVE = "slight";
	/** Evento herido grave. */
	public static final String HERIDO_GRAVE = "serious";
	/** Evento muerto. */
	public static final String HERIDO_MUERTO = "dead";
	/** Evento atrapado. */
	public static final String HERIDO_ATRAPADO = "trapped";
	/** Listado de eventos. */
	public static final List<String> EVENTOS = Arrays.asList(new String[]{
		FUEGO, TERREMOTO, INUNDACION, HERIDO_LEVE, HERIDO_GRAVE, HERIDO_MUERTO, HERIDO_ATRAPADO});
	
	//------- atributos ---
	private final int tiempoJSON = 5000;
	private static TimerJSON temporizador;
	private String ahora;
	// Agentes (nombre -> WorldObject)
	private HashMap<String,WorldObject> agentes;
	// Eventos (id -> WorldObject)
	private HashMap<Integer,Disaster> disasters;
	private HashMap<Integer,People> people;
	private HashMap<Integer,Resource> resources;
	// Agentes y Eventos (Pos -> WorldObject)
	private MultiCollection objetos;
	// Numero de agentes creados, no tienen por que estar activos. No ha sido usado
	private Integer numAgentes;
	// Numero de eventos creados, no tienen por que estar activos.
	// Usado para poder dar un nombre distinto a los eventos en las tablas Hash
	private Integer numEventos;
	private int tablon;
	private static Environment instance;
	// Objeto para notificar de cambios
	private SimplePropertyChangeSupport pcs;
	
	//---------------------
	
	/**
	 * Constructor.
	 */
	public Environment(){
		temporizador = new TimerJSON(tiempoJSON, this);
		agentes = new HashMap<String,WorldObject>();
		disasters = new HashMap<Integer,Disaster>();
		people = new HashMap<Integer,People>();
		resources = new HashMap<Integer,Resource>();
		objetos = new MultiCollection();
		numAgentes = 0;
		numEventos = 0;
		pcs = new SimplePropertyChangeSupport(this);

		// Esto LA PRIMERA VEZ - recibo el json
		try{
			String eventos = Connection.connect(URL + "events");
			JSONArray desastres = new JSONArray(eventos);

			String victimas = Connection.connect(URL + "people");
			JSONArray personas = new JSONArray(victimas);
			
			String recursos = Connection.connect(URL + "resources");
			JSONArray usuarios = new JSONArray(recursos);

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
				
				// si no esta asignado a nadie o a alguien que no existe pasa al siguiente
				if(nuevo.getIdAssigned() != 0 && disasters.containsKey(nuevo.getIdAssigned()) == true){
					people.put(nuevo.getId(), nuevo);
					Disaster dis = disasters.get(nuevo.getIdAssigned());
					if(instancia.getString("type").equals("healthy") == false){
						System.out.println("- Herido: " + nuevo.getName() + " con estado " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
					}
					
					if(nuevo.getType().equals("slight")){
						dis.addSlight(nuevo);
					}else if(nuevo.getType().equals("serious")){
						dis.addSerious(nuevo);
					}else if(nuevo.getType().equals("dead")){
						dis.addDead(nuevo);
					}else if(nuevo.getType().equals("trapped")){
						dis.addTrapped(nuevo);
					}
					
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
								extraido.getLatitud(),
								extraido.getLongitud(),
								extraido.getAddress(),
								extraido.getFloor(),
								extraido.getSize(),
								extraido.getTraffic(),
								extraido.getState());
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
					instancia.getInt("floor"),
					instancia.getString("state"),
					instancia.getInt("idAssigned"));
				resources.put(nuevo.getId(), nuevo);
				System.out.println("- Nuevo usuario: " + nuevo.getName() + " - " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
			}
		}catch(JSONException e){
			System.out.println("Error with JSON **** : " + e);
		}

		// Hacer una llamada cada pocos segundos al metodo actualiza()
		temporizador.start();
	}

	/**
	 * Actualizaci&oacute;n del entorno.
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
				
				People antiguo = people.get(nuevo.getId());
				if(people.containsKey(nuevo.getId())){
					if(nuevo.getState().equals("erased")){
						people.remove(nuevo.getId());
						System.out.println("- Herido " + nuevo.getName() + " curado (id:" + nuevo.getId() + ")");
					}else{
						people.put(nuevo.getId(), nuevo);
						//System.out.println("- Herido " + nuevo.getName() + " actualizado con estado " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
					}
				}else{
					people.put(nuevo.getId(), nuevo);
					System.out.println("- Herido " + nuevo.getName() + " con estado " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
				}
				
				if(nuevo.getIdAssigned() != antiguo.getIdAssigned() || antiguo.getType().equals(nuevo.getType()) == false){
					int idDesastre = antiguo.getIdAssigned();
					Disaster extraido = disasters.get(idDesastre);
					System.out.println("- ENV: Desasociando el herido " + nuevo.getId() + " del desastre " + idDesastre);
					if(extraido != null){
						if(antiguo.getType().equals("slight")){
							extraido.removeSlight(antiguo);
						}else if(antiguo.getType().equals("serious")){
							extraido.removeSerious(antiguo);
						}else if(antiguo.getType().equals("dead")){
							extraido.removeDead(antiguo);
						}else if(antiguo.getType().equals("trapped")){
							extraido.removeTrapped(antiguo);
						}
					}
					
					if(nuevo.getIdAssigned() != 0 && disasters.containsKey(nuevo.getIdAssigned())){
						Disaster dis = disasters.get(nuevo.getIdAssigned());
						if(nuevo.getType().equals("slight")){
							dis.addSlight(nuevo);
						}else if(nuevo.getType().equals("serious")){
							dis.addSerious(nuevo);
						}else if(nuevo.getType().equals("dead")){
							dis.addDead(nuevo);
						}else if(nuevo.getType().equals("trapped")){
							dis.addTrapped(nuevo);
						}
					}
				}
				
				/*if(nuevo.getIdAssigned() == 0){
					if(people.containsKey(nuevo.getId())){
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
							extraido.getLatitud(),
							extraido.getLongitud(),
							extraido.getAddress(),
							extraido.getFloor(),
							extraido.getSize(),
							extraido.getTraffic(),
							extraido.getState());
						disasters.put(insertado.getId(), insertado);
					}
				}else{
					Disaster dis = disasters.get(nuevo.getIdAssigned());
					if(!people.containsKey(nuevo.getId())){
						System.out.println("- Herido " + nuevo.getName() + " con estado " + nuevo.getType());
					}else{
						System.out.println("- Herido " + nuevo.getName() + " actualizado con estado " + nuevo.getType());
					}
					people.put(nuevo.getId(), nuevo);
					if(antiguo.getType().equals(nuevo.getType()) == false && nuevo.getIdAssigned() != 0){
						if(nuevo.getType().equals("slight")){
							dis.addSlight(nuevo);
						}else if(nuevo.getType().equals("serious")){
							dis.addSerious(nuevo);
						}else if(nuevo.getType().equals("dead")){
							dis.addDead(nuevo);
						}else if(nuevo.getType().equals("trapped")){
							dis.addTrapped(nuevo);
						}

						if(antiguo.getType().equals("slight")){
							dis.removeSlight(antiguo);
						}else if(antiguo.getType().equals("serious")){
							dis.removeSerious(antiguo);
						}else if(antiguo.getType().equals("dead")){
							dis.removeDead(antiguo);
						}else if(antiguo.getType().equals("trapped")){
							dis.removeTrapped(antiguo);
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
					instancia.getInt("floor"),
					instancia.getString("state"),
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
					//sp.getParameter("type").setValue("disasters/caronte/simulador/" + tipoUsuario + "/" + tipoUsuario + ".agent.xml");
					//dispatchSubgoalAndWait(sp);
				}
			}
		}catch(JSONException e){
			System.out.println("Error with JSON *****" + e);
		}

		//temporizador.run();
	}

	/**
	 * Obtener una instancia del entorno, para as&iacute; poder interactuar sobre &eacute;l.</br>
	 * NOTA: se puede crear un parque de bomberos, desde el cual salgan => Si es un bombero, tiene posici&oacute;n inicial fija.
	 * 
	 * @param tipo tipo
	 * @param nombre nombre
	 * @param pos posici&oacute;n
	 * @param agentId identificador del agente
	 * @return instancia de entorno
	 */
	public static Environment getInstance(String tipo, String nombre, Position pos, IComponentIdentifier agentId){
		// La primera vez que se llama a este metodo (el agente Environment), instance vale null
		if(instance == null){
			instance = new Environment();
		}
		if(tipo != null && nombre != null){
			instance.addWorldObject(tipo, nombre, pos, null, agentId);
		}
		return instance;
	}

	/**
	 * Borra la instancia (resetea el entorno).
	 */
	public static void clearInstance(){
		temporizador = null;
		instance = null;
		System.out.println("Entorno detenido");
	}

	/**
	 * A&ntilde;de un objeto al entorno.
	 * 
	 * @param type yipo
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
				objetos.put(position, wo); // Y tambien a la de elementos del mundo
				numAgentes++;
			}catch(JSONException e){
				System.out.println("Error with JSON: " + e);
			}
		}else if(COMPONENTES.contains(type)){
			System.out.println("Creando agente de tipo " + type);
			agentes.put(name, wo); // Si es una central, se annade a la tabla de agentes
			objetos.put(position, wo); // Y tambien a la de elementos del mundo
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
		WorldObject wo = getAgent(name);
		if(AGENTES.contains(type)){
			System.out.println("Llamada a REST eliminando agente...");
			Connection.connect(URL + "delete/resource/" + name);
			
			System.out.println("Eliminando agente de tipo " + type);
			agentes.remove(name);
			objetos.remove(wo.getPosition(), wo);
			numAgentes--;
		}else if(COMPONENTES.contains(type)){
			System.out.println("Eliminando agente de tipo " + type);
			agentes.remove(name);
			objetos.remove(wo.getPosition(), wo);
			numAgentes--;
		}
	}
	
	/**
	 * Cambia la posici&oacute;n de un agente.
	 * Los eventos no se mueven de posici&oacute;n.
	 * 
	 * @param name nombre
	 * @param pos posici&oacute;n
	 */
	public void go(String name, Position pos){
		Position oldPos;
		WorldObject wo;
		
		// No deben varios agentes tocar las tablas Hash a la vez
		synchronized(this){ 
			wo = getAgent(name);        // Obtenemos el agente de la tabla Hash agentes, dado su nombre
			oldPos = wo.getPosition();  // Obtenemos la posicion del agente antes de desplazarlo
			objetos.remove(oldPos, wo); // Eliminamos el agente de su posicion (de la coleccion Objetos)
			
			wo.setPosition(pos);  // Actualizamos la posicion al objeto agente
			objetos.put(pos, wo); // Annadimos el objeto, con la posicion renovada
			removeAgent(name);    // Actualizamos la tabla Hash de agentes
			agentes.put(name, wo);
		}

		// Avisamos para el modo de evaluacion dinamico de posicion de que hemos variado una poscicion
		pcs.firePropertyChange("cambio_de_posicion", oldPos, pos);
	}

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
	 * @param id identificador del agente
	 * @param idHerido identificador del herido
	 * @param latitud latitud
	 * @param longitud longitud
	 * @throws InterruptedException 
	 */
	public void pinta(int id, int idHerido, double latitud, double longitud) throws InterruptedException{
		String resultado = Connection.connect(URL + "put/" + id + "/latlong/" + latitud + "/" + longitud);
		if(idHerido != 0){
			String resultado2 = Connection.connect(URL + "put/" + idHerido + "/latlong/" + latitud + "/" + longitud);
		}
		Thread.sleep(1000);
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
	 * Devuelve todos los desastres.
	 * 
	 * @return todos los desastres
	 */
	public HashMap<Integer,Disaster> getEvents(){
		return disasters;
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
	 * Devuelve la posici&oacute;n de un agente dado su nombre (el nombre de los agentes es &uacute;nico).
	 * 
	 * @param name nombre
	 * @return posici&oacute;n del agente
	 */
	public synchronized Position getAgentPosition(String name){
		assert agentes.containsKey(name);
		return agentes.get(name).getPosition();
	}

	/**
	 * Devuelve un evento dado su id (el id de los eventos es &uacute;nico).
	 * 
	 * @param id identificador
	 * @return desastre
	 */
	public synchronized Disaster getEvent(int id){
		assert disasters.containsKey(id);
		return disasters.get(id);
	}

	/**
	 * Elimina un evento dado su id (el id de los eventos es &uacute;nico).
	 * 
	 * @param id identificador
	 * @return evento eliminado
	 */
	public synchronized Disaster removeEvent(int id){
		assert disasters.containsKey(id);
		return disasters.remove(id);
	}

	/**
	 * Devuelve la posici&oacute;n de un evento dado su id.
	 * 
	 * @param id identificador
	 * @return posici&oacute;n del evento
	 */
	public synchronized Position getEventPosition(int id){
		assert disasters.containsKey(id);
		return new Position(disasters.get(id).getLatitud(), disasters.get(id).getLongitud());
	}

	/**
	 * Devuelve todos los objetos que haya en una posici&oacute;n.
	 * 
	 * @param pos posici&oacute;n
	 * @return todos los objetos de la posici&oacute;n
	 */
	public WorldObject[] getWorldObjects(Position pos){
		Collection col = objetos.getCollection(pos);
		return (WorldObject[])col.toArray();

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
	 * Devuelve todos los eventos.
	 * 
	 * @return todos los eventos
	 */
	public Disaster[] getEventos(){
		Collection<Disaster> col = disasters.values();
		return col.toArray(new Disaster[col.size()]);
	}

	/**
	 * Devuelve el n&uacute;mero total de agentes creados.
	 * 
	 * @return n&uacute;mero total de agentes creados.
	 */
	public int getNumAgentes(){
		return numAgentes;
	}

	/**
	 * Devuelve el n&uacute;mero total de eventos creados.
	 * 
	 * @return n&uacute;mero total de eventos creados.
	 */
	public int getNumEventos(){
		return numEventos;
	}

	/**
	 * Devuelve una posici&oacute;n aleatoria conociendo la ciudad.</br>
	 * Puesto que de momento solo tenemos Calasparra en la lista, no hace falta especificar la ciudad.
	 * 
	 * @return posici&oacute;n aleatoria
	 */
	public Position getRandomPosition(){
		// Las dos posiciones que se crean son las esquinas superior derecha e inferior izquierda del marco que contiene a Calasparra
		Location location = new Location("Madrid", new Position(40.44, -3.66), new Position(40.39, -3.72));

		Position esd = location.getESD(); // Esquina superior Derecha
		Position eii = location.getEII(); // Esquina inferior Izquierda

		// Obtenemos la altura y anchura del marco, tomando la diferencia de latitudes y longitudes respectivamente
		double alturaMarco = Math.abs(esd.getLat() - eii.getLat());
		double anchuraMarco = Math.abs(esd.getLng() - eii.getLng());

		// Obtenemos la latitud y longitud menor, para sumarles una fraccion aleatoria de la diferencia que las separa
		double marcoInferior = Math.min(esd.getLat(), eii.getLat());
		double marcoIzquierdo = Math.min(esd.getLng(), eii.getLng());

		Position nuevaAleatoria = new Position(marcoInferior + Math.random() * alturaMarco, marcoIzquierdo + Math.random() * anchuraMarco);
		return nuevaAleatoria;
	}

	/**
	 * Devuelve el desastre que se debe atender.
	 * 
	 * @return el tabl&oacute;n
	 */
	public int getTablon(){
		return tablon;
	}

	/**
	 * Establece el desastre que se debe atender.
	 * 
	 * @param tablon el tabl&oacute;n
	 */
	public void setTablon(int tablon){
		this.tablon = tablon;
	}
	
	/**
	 * Imprime un String por pantalla y lo env&iacute;a para mostrar en la web.
	 *
	 * @param valor String a imprimir
	 * @param nivel nivel del mensaje (0 todos los usuarios, 1 todos los conectados,...)
	 */
	public final void printout(String valor, int nivel){
		Connection.connect(Environment.URL + "message/2/" + nivel + "/" + valor);
		System.out.println(valor);
	}
}