package jadex.desastres;

import java.util.*;
import org.json.me.*;
import java.sql.Timestamp;

import jadex.commons.collection.MultiCollection;
import jadex.commons.SimplePropertyChangeSupport;
import java.io.*;

/**
 * 
 * Clase para modelar el entorno, proporcionando metodos
 * para interactuar con el.
 * 
 * @author aebeda y Juan Luis Molina
 * 
 */
public class Environment {
	//------constantes ----

	/** Los nombres de los agentes */
	public static final String AMBULANCIA = "ambulance";
	public static final String BOMBERO = "firemen";
	public static final String POLICIA = "police";
	public static final String ENFERMERO = "nurse";
	public static final String GEROCULTOR = "gerocultor";
	public static final String AUXILIAR = "assistant";
	public static final String AMBULANCIA2 = "ambulancia";
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
	/** Los nombres de los eventos provocados sobre el mapa*/
	public static final String FUEGO = "fire";
	public static final String TERREMOTO = "collapse";
	public static final String INUNDACION = "flood";
	public static final String PERSONA_PERDIDA = "lostPerson";
	public static final String PERSONA_HERIDA = "injuredPerson";
	public static final String PROYECTO = "desastres";
	public static final String URL = "http://localhost:8080/" + PROYECTO + "/rest/";
	//------- atributos ---
	private final int tiempoJSON = 5000;
	private final int tiempoMove = 500;
	private TimerJSON temporizador;
	private TimerMove tempoMover;
	private String ahora;
	/** Agentes (nombre -> WorldObject)*/
	public HashMap agentes;
	/** Numero de agentes creados, no tienen xq estar activos*/
	/** No ha sido usado*/
	protected Integer numAgentes;
	/** Eventos (id -> WorldObject)*/
	public HashMap disasters;
	protected HashMap people;
	/** Numero de eventos creados, no tienen xq estar activos
	 *  Usado para poder dar un nombre distinto a los eventos en las
	 *  tablas Hash.
	 */
	protected Integer numEventos;
	/** Agentes y Eventos (Pos -> WorldObject)*/
	protected MultiCollection objetos;
	/** Posicion del centro de Calasparra*/
	Position centroCalasparra = new Position(38.229225, -1.701830);
	private final double residenciaLatMin = 38.231943;
	private final double residenciaLatMax = 38.232634;
	private final double residenciaLongMin = -1.699622;
	private final double residenciaLongMax = -1.698201;
	/** Generador aleatorio*/
	Random rnd = new Random();
	/** Objeto para notificar de cambios*/
	public SimplePropertyChangeSupport pcs;
	private int tablon;

	//---------------------
	/**
	 * Constructor
	 */
	public Environment() {
		this.agentes = new HashMap();
		this.disasters = new HashMap(); //fuegos,heridos,...
		this.people = new HashMap();
		this.objetos = new MultiCollection();
		this.pcs = new SimplePropertyChangeSupport(this);
		numAgentes = numEventos = 0;
		this.temporizador = new TimerJSON(tiempoJSON, this);
		this.tempoMover = new TimerMove(tiempoMove);

		//Esto LA PRIMERA VEZ - recibo el json
		try {
			String eventos = Connection.connect(URL + "events");
			JSONArray desastres = new JSONArray(eventos);

			String victimas = Connection.connect(URL + "people");
			JSONArray personas = new JSONArray(victimas);

			String logueados = Connection.connect(URL + "users");
			JSONArray usuarios = new JSONArray(logueados);

			ahora = new Timestamp(new Date().getTime()).toString();

			//Por cada desastre:
			for (int i = 0; i < desastres.length(); i++) {
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
				printout("## ENV: New Disaster: " + nuevo.getType() + " - " + nuevo.getName() + " (id:" + nuevo.getId() + ") ##", 0);
				disasters.put(nuevo.getId(), nuevo);
			}

			//Por cada herido:
			for (int i = 0; i < personas.length(); i++) {
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
				//si no esta asignado a nadie o a alguien que no existe pasa al siguiente
				if (nuevo.getIdAssigned() == 0 || !disasters.containsKey(nuevo.getIdAssigned())) {
					continue;
				}

				people.put(nuevo.getId(), nuevo);
				Disaster dis = (Disaster) disasters.get(nuevo.getIdAssigned());
				printout("## ENV: Updating Disaster Victims for " + dis.getName(), 0);

				if (nuevo.getType().equals("slight")) {
					dis.setSlight(nuevo);
				}
				if (nuevo.getType().equals("serious")) {
					dis.setSerious(nuevo);
				}
				if (nuevo.getType().equals("dead")) {
					dis.setDead(nuevo);
				}
				if (nuevo.getType().equals("trapped")) {
					dis.setTrapped(nuevo);
				}
			}

			// Por cada usuario logueado
			for (int i = 0; i < usuarios.length(); i++) {
				JSONObject instancia = usuarios.getJSONObject(i);
				printout("## ENV: New user: " + instancia.getString("name") + " (id:" + instancia.getInt("id") + ") en ["
						+ new Double(instancia.getString("latitud")) + "," + new Double(instancia.getString("longitud")) + "]", 0);
			}
		} catch (JSONException e) {
			System.out.println("Error with JSON **** : " + e);
		}

		//Hacer una llamada cada pocos segundos al metodo actualiza()
		temporizador.start();
	}

	public void actualiza() {
		temporizador.reset();
		//ESTO PARA ACTUALIZAR - recibo el json actualizador
		try {
			String eventos = Connection.connect(URL + "events/modified/" + ahora);
			JSONArray desastres = new JSONArray(eventos);

			String victimas = Connection.connect(URL + "people/modified/" + ahora);
			JSONArray personas = new JSONArray(victimas);

			String logueados = Connection.connect(URL + "users/modified/" + ahora);
			JSONArray usuarios = new JSONArray(logueados);

			ahora = new Timestamp(new Date().getTime()).toString();

			for (int i = 0; i < desastres.length(); i++) {
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

				if (disasters.containsKey(nuevo.getId())) {
					//si ya existia actualizo el desastre existente
					printout("## ENV: Updating Disaster... "
							+ nuevo.getName() + " - " + nuevo.getState(), 0);
					Disaster viejo = (Disaster) disasters.get(nuevo.getId());
					viejo.setType(nuevo.getType());
					viejo.setName(nuevo.getName());
					viejo.setInfo(nuevo.getInfo());
					viejo.setDescription(nuevo.getDescription());
					viejo.setAddress(nuevo.getAddress());
					viejo.setLongitud(nuevo.getLongitud());
					viejo.setLatitud(nuevo.getLatitud());
					viejo.setState(nuevo.getState());
					viejo.setSize(nuevo.getSize());
					viejo.setTraffic(nuevo.getTraffic());

					//si se ha eliminado lo borro directamente
					if (viejo.getState().equals("erased")) {
						disasters.remove(viejo.getId());
					}
				} else {
					printout("### New Disaster: " + nuevo.getType() + " - " + nuevo.getName() + " (id:" + nuevo.getId() + ") ##", 0);
					disasters.put(nuevo.getId(), nuevo);
				}
			}

			//Por cada herido:
			for (int i = 0; i < personas.length(); i++) {
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
				if (nuevo.getState().equals("erased")) {
					continue;
				}
				if (nuevo.getIdAssigned() == 0) {
					if (!people.containsKey(nuevo.getId())) {
						continue;
					} //cuando ya existia y tengo que actualizar el desastres borrando sus heridos
					else {
						People antiguo = (People) people.get(nuevo.getId());
						int idDesastre = antiguo.getIdAssigned();
						people.remove(antiguo.getId());
						printout("## ENV: Voy a desasociar el herido " + nuevo.getId() + " del desastre " + idDesastre, 0);
						//Disaster extraido = (Disaster) disasters.get(idDesastre);
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
						disasters.put(insertado.getId(), insertado);*/
					}
				} else {
					People antiguo = (People) people.get(nuevo.getId());
					Disaster dis = (Disaster) disasters.get(nuevo.getIdAssigned());
					printout("## ENV: Updating Disaster Victims for " + dis.getName() + " -- " + nuevo.getQuantity() + " " + nuevo.getType(), 0);
					people.put(nuevo.getId(), nuevo);
					if (!antiguo.getType().equals(nuevo.getType())) {
						if (nuevo.getType().equals("slight")) {
							dis.setSlight(nuevo);
						}else if (nuevo.getType().equals("serious")) {
							dis.setSerious(nuevo);
						}else if (nuevo.getType().equals("dead")) {
							dis.setDead(nuevo);
						}else if (nuevo.getType().equals("trapped")) {
							dis.setTrapped(nuevo);
						}

						if (antiguo.getType().equals("slight")) {
							dis.setSlight(antiguo);
						}else if (antiguo.getType().equals("serious")) {
							dis.setSerious(antiguo);
						}else if (antiguo.getType().equals("dead")) {
							dis.setDead(antiguo);
						}else if (antiguo.getType().equals("trapped")) {
							dis.setTrapped(antiguo);
						}
					}
				}
			}

			for (int i = 0; i < usuarios.length(); i++) {
				JSONObject instancia = usuarios.getJSONObject(i);
				printout("## ENV: Updating user " + instancia.getString("name"), 0);
			}

		} catch (JSONException e) {
			System.out.println("Error with JSON *****" + e);
		}

		//temporizador.run();
	}
	protected static Environment instance;

	/**
	 * Obtener una instancia del entorno,
	 * para asi poder interactuar sobre el.
	 * NOTA:se puede crear un parque de bomberos, desde el cual salgan, => Si es un
	 * bombero, tiene posicion inicial fija.
	 */
	public static Environment getInstance(String tipo, String nombre, Position pos) {
		//La primera vez que se llama a este metodo (el agente Environment),
		//instance vale null.
		if (instance == null) {
			instance = new Environment();
		}
		if ((tipo != null) && (nombre != null)) {
			instance.addWorldObject(tipo, nombre, pos, null);
		}
		return instance;
	}

	/**
	 * Borra la instancia (Resetea el entorno).
	 */
	public static void clearInstance() {
		instance = null;
	}

	/**
	 * Anade un objeto al entorno
	 */
	public void addWorldObject(String type, String name, Position position, String info) {
		//System.out.println("Ejecuto addWorldObject ###" + position +" type: " + type + " nombre: "+name);
		//De momento no usamos el atributo info.
		//La posicion siempre se especifica.
		WorldObject wo = new WorldObject(name, type, position, null);

		if (type.equals(AMBULANCIA) || type.equals(BOMBERO) || type.equals(POLICIA)
				|| type.equals(ENFERMERO) || type.equals(AMBULANCIA2) || type.equals(GEROCULTOR) || type.equals(AUXILIAR)) {
			//REST -> cree el recurso
			String longitud = String.valueOf(position.getY());
			String latitud = String.valueOf(position.getX());
			System.out.println("LLamada a REST creando agente...");
			String resultado = Connection.connect(URL + "post/type=" + type
					+ "&name=" + name + "&info=" + info + "&latitud=" + latitud + "&longitud=" + longitud);

			try {
				//JSON -> guardo el id
				JSONObject idJson = new JSONObject(resultado);
				int id = idJson.getInt("id");
				wo.setId(id);
				System.out.println("Id del objeto creado: " + id);
				agentes.put(name, wo); //Si es un pironamo, se anade a la tabla de agentes
				objetos.put(position, wo); //Y tambien a la de elementos del mundo
				numAgentes++;
			} catch (Exception e) {
				System.out.println("Error json: " + e);
			}
		}
		if (type.equals(CENTRAL) || type.equals(CENTRAL_EMERGENCIAS) || type.equals(COORDINADOR_EMERGENCIAS)
				|| type.equals(GSO) || type.equals(MEDICO_CACH) || type.equals(COORDINADOR_HOSPITAL) || type.equals(COORDINADOR_MEDICO)) {
			System.out.println("Creando agente de tipo " + type);
			agentes.put(name, wo); //Si es una central, se anade a la tabla de agentes
			objetos.put(position, wo); //Y tambien a la de elementos del mundo
			numAgentes++;
		}
	}

	/**
	 * Cambia la posicion de un agente.
	 * Los eventos no se mueven de posicion.
	 */
	public void go(String name, Position pos) {
		Position oldPos;

		WorldObject wo;
		synchronized (this) { //No deben varios agentes tocar las tablas Hash a la vez
			//Obtenemos el agente de la tabla Hash agentes, dado su nombre.
			wo = getAgent(name);

			oldPos = wo.getPosition(); //Obtenemos la posicion del agente antes de desplazarlo.
			objetos.remove(oldPos, wo); //Eliminamos el agente de su posicion (de la coleccion Objetos)
		}

		synchronized (this) {
			wo.setPosition(pos);//Actualizamos la posicion al objeto agente

			objetos.put(pos, wo); //Anadimos el objeto, con la posicion renovada
			removeAgent(name); //Actualizamos la tabla Hash de agentes.
			agentes.put(name, wo);
		}

		//Avisamos para el modo de evaluacion dinamico de posicion
		//de que hemos variado una poscicion.
		pcs.firePropertyChange("cambio_de_posicion", oldPos, pos);
	}

	public void andar(String name, Position inicial, Position dest, int desastre, int herido) throws InterruptedException {

		Double x1 = inicial.getX();
		Double x2 = dest.getX();
		Double y1 = inicial.getY();
		Double y2 = dest.getY();

		Double x = x2 - x1;
		Double y = y2 - y1;
		Double pendiente = Math.atan(y / x);
		int velocidad = 150; //velocidad inversa, cuanto mas grande mas despacio

		Double pasoX = 0.25 / velocidad * Math.cos(pendiente);
		Double pasoY = (0.25 / velocidad) * Math.abs(Math.sin(pendiente));

		Boolean derecha;
		Boolean arriba;

		//Tiempo entre cambios de posicion
		Integer tiempo = 200;
		Position PosMedia;

		if (x > 0) {
			arriba = true;
		} else {
			arriba = false;
		}
		if (y > 0) {
			derecha = true;
		} else {
			derecha = false;
		}

		//El punto destino esta a la derecha del origen
		if (derecha) {
			if (arriba) {
				while ((x1 < x2) || (y1 < y2)) {
					if (x2 - x1 < pasoX) {
						x1 = x2;
					} else if (x1 < x2) {
						x1 += pasoX;
					}

					if (y2 - y1 < pasoY) {
						y1 = y2;
					} else if (y1 < y2) {
						y1 += pasoY;
					}

					PosMedia = new Position(x1, y1);
					go(getAgent(name).getName(), PosMedia);
					//pinta agente
					pinta(desastre, herido, x1, y1);
				}
				/*x1 = x2;
				y1 = y2;
				PosMedia = new Position(x1, y1);
				go(getAgent(name).getName(), PosMedia);
				//pinta agente
				pinta(desastre, herido, x1, y1);*/
			} else {
				while ((x1 > x2) || (y1 < y2)) {
					if (x1 - x2 < pasoX) {
						x1 = x2;
					} else if (x1 > x2) {
						x1 -= pasoX;
					}

					if (y2 - y1 < pasoY) {
						y1 = y2;
					} else if (y1 < y2) {
						y1 += pasoY;
					}

					PosMedia = new Position(x1, y1);
					go(getAgent(name).getName(), PosMedia);
					//pinta agente
					pinta(desastre, herido, x1, y1);
				}
				x1 = x2;
				y1 = y2;
				PosMedia = new Position(x1, y1);
				go(getAgent(name).getName(), PosMedia);
				//pinta agente
				pinta(desastre, herido, x1, y1);
			}
		} else { //El punto esta a la izquierda
			if (arriba) {
				while ((x1 < x2) || (y1 > y2)) {
					if (x2 - x1 < pasoX) {
						x1 = x2;
					} else if (x1 < x2) {
						x1 += pasoX;
					}

					if (y1 - y2 < pasoY) {
						y1 = y2;
					} else if (y1 > y2) {
						y1 -= pasoY;
					}

					PosMedia = new Position(x1, y1);
					go(getAgent(name).getName(), PosMedia);
					//pinta agente
					pinta(desastre, herido, x1, y1);
				}
				x1 = x2;
				y1 = y2;
				PosMedia = new Position(x1, y1);
				go(getAgent(name).getName(), PosMedia);
				//pinta agente
				pinta(desastre, herido, x1, y1);
			} else {
				while ((x1 > x2) || (y1 > y2)) {
					if (x1 - x2 < pasoX) {
						x1 = x2;
					} else if (x1 > x2) {
						x1 -= pasoX;
					}

					if (y1 - y2 < pasoY) {
						y1 = y2;
					} else if (y1 > y2) {
						y1 -= pasoY;
					}

					PosMedia = new Position(x1, y1);
					go(getAgent(name).getName(), PosMedia);
					//pinta agente
					pinta(desastre, herido, x1, y1);
				}
				x1 = x2;
				y1 = y2;
				PosMedia = new Position(x1, y1);
				go(getAgent(name).getName(), PosMedia);
				//pinta agente
				pinta(desastre, herido, x1, y1);
			}
		}
	}

	/**
	 * Pinta el movimiento de un agente en el mapa mediante REST
	 */
	public void pinta(int id, int idHerido, Double latitud, Double longitud) throws InterruptedException {
		String resultado = Connection.connect(URL + "put/" + id + "/latlong/" + latitud + "/" + longitud);
		if (idHerido != 0) {
			String resultado2 = Connection.connect(URL + "put/" + idHerido + "/latlong/" + latitud + "/" + longitud);
		}

		Thread.sleep(1000);
	}

	/**
	 * Devuelve un agente
	 * dado su nombre (el nombre de los agentes es unico).
	 */
	public synchronized WorldObject getAgent(String name) {
		assert agentes.containsKey(name);

		return (WorldObject) agentes.get(name);
	}

	/**
	 * Elimina un agente
	 * dado su nombre (el nombre de los agentes es unico).
	 */
	public synchronized WorldObject removeAgent(String name) {
		assert agentes.containsKey(name);

		return (WorldObject) agentes.remove(name);
	}

	/**
	 * Devuelve la posicion de un agente
	 * dado su nombre (el nombre de los agentes es unico).
	 */
	public synchronized Position getAgentPosition(String name) {
		assert agentes.containsKey(name);

		return ((WorldObject) agentes.get(name)).getPosition();
	}

	/**
	 * Devuelve un evento
	 * dado su id (el id de los eventos es unico).
	 */
	public synchronized Disaster getEvent(int id) {
		assert disasters.containsKey(id);

		return (Disaster) disasters.get(id);
	}

	/**
	 * Elimina un evento
	 * dado su nombre (el nombre de los eventos es unico).
	 */
	public synchronized WorldObject removeEvent(String name) {
		assert disasters.containsKey(name);

		return (WorldObject) disasters.remove(name);
	}

	/**
	 * Devuelve la posicion de un evento
	 * dado su nombre (concretamente su id).
	 */
	public synchronized Position getEventPosition(String name) {
		assert disasters.containsKey(name);

		return ((WorldObject) disasters.get(name)).getPosition();
	}

	/**
	 * Devuelve todos los objetos que haya en una posicion
	 */
	protected WorldObject[] getWorldObjects(Position pos) {

		Collection col = (Collection) objetos.get(pos);
		return (WorldObject[]) col.toArray(new WorldObject[col.size()]);

	}

	/**
	 * Devuelve todos los agentes
	 */
	protected WorldObject[] getAgentes() {
		Collection col = (Collection) agentes.values();
		return (WorldObject[]) col.toArray(new WorldObject[col.size()]);
	}

	/**
	 * Devuelve todos los eventos
	 */
	protected WorldObject[] getEventos() {
		Collection col = (Collection) disasters.values();
		return (WorldObject[]) col.toArray(new WorldObject[col.size()]);
	}

	/**
	 * Devuelve el numero total de agentes creados.
	 * @return
	 */
	public int getNumAgentes() {
		return numAgentes;
	}

	/**
	 * Devuelve el numero total de eventos creados.
	 * @return
	 */
	public int getNumEventos() {
		return numEventos;
	}

	/**
	 * Devuelve una posicion aleatoria
	 * conociendo la ciudad.
	 * Puesto que de momento solo tenemos Calasparra en la lista,
	 * no hace falta especificar la ciudad.
	 */
	//protected Position getRandomPosition(Location loc){
	public Position getRandomPosition(String proyecto) {
		//Las dos posiciones que se crean son las esquinas superior derecha e inferior izquierda
		//del marco que contiene a Calasparra.

		Location location = new Location("Madrid", new Position(40.44, -3.66), new Position(40.39, -3.72));
		if (proyecto.equals("caronte")) {
			location = new Location("Calasparra", new Position(38.233181, -1.69724), new Position(38.231251, -1.70252));
		}

		Position esd = (Position) location.getESD(); //Esquina superior Derecha
		Position eii = (Position) location.getEII(); //Esquina inferior Izquierda.

		Double random1 = rnd.nextDouble();
		Double random2 = rnd.nextDouble();

		//Obtenemos la altura del marco, tomando la diferencia de latitudes.
		Double alturaMarco = Math.abs(esd.getY() - eii.getY());
		//Obtenemos la anchura del marco, tomando la diferencia de longitudes.
		Double anchuraMarco = Math.abs(esd.getX() - eii.getX());

		//Obtenemos la latitud y longitud menor, para sumarles una fraccion
		//aleatoria de la diferencia que las separa.
		Double marcoBottom = Math.min(esd.getY(), eii.getY());
		Double marcoLeft = Math.min(esd.getX(), eii.getX());

		Position nuevaAleatoria = new Position(marcoLeft + random1 * anchuraMarco, marcoBottom + random2 * alturaMarco);
		return nuevaAleatoria;
	}

	/**
	 * @return the tablon
	 */
	public int getTablon() {
		return tablon;
	}

	/**
	 * @param tablon the tablon to set
	 */
	public void setTablon(int tablon) {
		this.tablon = tablon;
	}

	/**
	 * Termina la hebra del temporzador
	 */
	public synchronized void terminar() throws IOException {
		System.out.println("Deteniendo entorno...");
		temporizador = null;
		System.out.println("Entorno detenido");
	}

	/**
	 * Imprime un String por pantalla y lo envia para mostrar en la web
	 *
	 * @param valor String a imprimir
	 * @param cat   nivel del mensaje (0 todos los usuarios, 1 todos los conectados,...)
	 */
	public static void printout(String valor, int nivel) {
		Connection.connect(Environment.URL + "message/" + valor + "/" + nivel);
		System.out.println(valor);
	}
}
