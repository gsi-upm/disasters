package disasters.desastres;

import disasters.*;
import jadex.commons.SimplePropertyChangeSupport;
import jadex.commons.collection.MultiCollection;
import java.sql.Timestamp;
import java.util.*;
import org.json.me.*;

/**
 * Clase para modelar el entorno, proporcionando metodos para interactuar con el.
 * 
 * @author aebeda
 * @author Juan Luis Molina
 */
public class Environment{
	//------constantes ----

	/** Los nombres de los agentes */
	public static final String AMBULANCIA = "ambulance";
	public static final String BOMBERO = "firemen";
	public static final String POLICIA = "police";
	public static final String AMBULANCIA2 = "ambulance";
	public static final String GSO = "grupoSanitarioOperativo";
	public static final String MEDICO_CACH = "medicoCACH";
	public static final String COORDINADOR_HOSPITAL = "coordinadorHospital";
	public static final String COORDINADOR_MEDICO = "coordinadorMedico";
	public static final String CENTRAL = "central";
	public static final String APOCALIPSIS = "apocalypse";
	/** Los nombres de los eventos provocados sobre el mapa */
	public static final String FUEGO = "fire";
	public static final String TERREMOTO = "collapse";
	public static final String INUNDACION = "flood";
	public static final String HERIDO_LEVE = "slight";
	public static final String HERIDO_GRAVE = "serious";
	public static final String HERIDO_MUERTO = "dead";
	public static final String HERIDO_ATRAPADO = "trapped";
	
	public static final String URL = "http://localhost:8080/desastres/rest/";
	//------- atributos ---
	private final int tiempoJSON = 5000;
	private static TimerJSON temporizador;
	private String ahora;
	/** Agentes (nombre -> WorldObject) */
	private HashMap<String,WorldObject> agentes;
	/** Eventos (id -> WorldObject) */
	private HashMap<Integer,Disaster> disasters;
	private HashMap<Integer,People> people;
	private HashMap<Integer,Resource> resources;
	/** Agentes y Eventos (Pos -> WorldObject) */
	private MultiCollection objetos;
	/**
	 * Numero de agentes creados, no tienen por que estar activos
	 * No ha sido usado
	 */
	private Integer numAgentes;
	/**
	 * Numero de eventos creados, no tienen por que estar activos
	 * Usado para poder dar un nombre distinto a los eventos en las tablas Hash
	 */
	private Integer numEventos;
	private int tablon;
	private static Environment instance;
	/** Objeto para notificar de cambios */
	private SimplePropertyChangeSupport pcs;

	//---------------------
	/**
	 * Constructor
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
					new Double(instancia.getLong("latitud")),
					new Double(instancia.getLong("longitud")),
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
					instancia.getString("adreess"),
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
	 * Actualizacion del entorno
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
					instancia.getString("adreess"),
					instancia.getInt("floor"),
					instancia.getString("size"),
					instancia.getString("traffic"),
					instancia.getString("state"),
					instancia.getInt("idAssigned"));
				
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
							extraido.getLatitud(),
							extraido.getLongitud(),
							extraido.getAddress(),
							extraido.Floor(),
							extraido.getSize(),
							extraido.getTraffic(),
							extraido.getState());
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
	 * Obtener una instancia del entorno, para asi poder interactuar sobre el.
	 * NOTA: se puede crear un parque de bomberos, desde el cual salgan, => Si es un bombero, tiene posicion inicial fija.
	 * 
	 * @param tipo Tipo
	 * @param nombre Nombre
	 * @param pos Posicion
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
	 */
	public void addWorldObject(String type, String name, Position position, String info){
		WorldObject wo = new WorldObject(name, type, position, info);

		if(type.equals(AMBULANCIA) || type.equals(BOMBERO) || type.equals(POLICIA) || type.equals(AMBULANCIA2)){
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
		}
		if(type.equals(CENTRAL) || type.equals(GSO) || type.equals(MEDICO_CACH) || type.equals(COORDINADOR_HOSPITAL) || type.equals(COORDINADOR_MEDICO)){
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
	 * @param name Nombre
	 * @param pos Posicion
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
	 * Modifica la posicion de un agente
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
	 * @param id Identificador del agente
	 * @param idHerido Identificador del herido
	 * @param latitud latitud
	 * @param longitud Longitud
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
	 * @return Todos los agentes
	 */
	public HashMap<String,WorldObject> getAgents(){
		return agentes;
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
	 * @param name nombre
	 * @return Agente eliminado
	 */
	public synchronized WorldObject removeAgent(String name){
		assert agentes.containsKey(name);
		return agentes.remove(name);
	}

	/**
	 * Devuelve la posicion de un agente dado su nombre (el nombre de los agentes es unico).
	 * 
	 * @param name Nombre
	 * @return Posicion del agente
	 */
	public synchronized Position getAgentPosition(String name){
		assert agentes.containsKey(name);
		return agentes.get(name).getPosition();
	}

	/**
	 * Devuelve un evento dado su id (el id de los eventos es unico).
	 * 
	 * @param id Identificador
	 * @return Desastre
	 */
	public synchronized Disaster getEvent(int id){
		assert disasters.containsKey(id);
		return disasters.get(id);
	}

	/**
	 * Elimina un evento dado su id (el id de los eventos es unico).
	 * 
	 * @param id Identificador
	 * @return Evento eliminado
	 */
	public synchronized Disaster removeEvent(int id){
		assert disasters.containsKey(id);
		return disasters.remove(id);
	}

	/**
	 * Devuelve la posicion de un evento dado su id.
	 * 
	 * @param id Identificador
	 * @return Posicion del evento
	 */
	public synchronized Position getEventPosition(int id){
		assert disasters.containsKey(id);
		return new Position(disasters.get(id).getLatitud(), disasters.get(id).getLongitud());
	}

	/**
	 * Devuelve todos los objetos que haya en una posicion.
	 * 
	 * @param pos Posicion
	 * @return Todos los objetos de la posicion
	 */
	public WorldObject[] getWorldObjects(Position pos){
		Collection col = objetos.getCollection(pos);
		return (WorldObject[])col.toArray();

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
	 * Devuelve todos los eventos.
	 * 
	 * @return Todos los eventos
	 */
	public Disaster[] getEventos(){
		Collection<Disaster> col = disasters.values();
		return col.toArray(new Disaster[col.size()]);
	}

	/**
	 * Devuelve el numero total de agentes creados.
	 * 
	 * @return numero total de agentes creados.
	 */
	public int getNumAgentes(){
		return numAgentes;
	}

	/**
	 * Devuelve el numero total de eventos creados.
	 * 
	 * @return numero total de eventos creados.
	 */
	public int getNumEventos(){
		return numEventos;
	}

	/**
	 * Devuelve una posicion aleatoria conociendo la ciudad.
	 * Puesto que de momento solo tenemos Calasparra en la lista, no hace falta especificar la ciudad.
	 * 
	 * @return Posicion aleatoria
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
	 * @return the tablon.
	 */
	public int getTablon(){
		return tablon;
	}

	/**
	 * Establece el desastre que se debe atender.
	 * 
	 * @param tablon the tablon to set.
	 */
	public void setTablon(int tablon){
		this.tablon = tablon;
	}

	/**
	 * Imprime un String por pantalla y lo envia para mostrar en la web.
	 *
	 * @param valor String a imprimir.
	 * @param nivel Nivel del mensaje (0 todos los usuarios, 1 todos los conectados,...).
	 */
	public final void printout(String valor, int nivel){
		Connection.connect(Environment.URL + "message/2/" + nivel + "/" + valor);
		System.out.println(valor);
	}
}