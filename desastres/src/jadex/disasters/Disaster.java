package disasters;

import java.util.ArrayList;

/**
 * Class that represents a disaster
 * 
 * @author julio camarero
 * @version 1.0
 */
public class Disaster{
	/** Disaster id */
	private int id;
	/** type of disaster (fire, flood, collapse, lostPerson, injuredPerson) */
	private String type;
	/** Name of the Disaster */
	private String name;
	/** Name of the Information */
	private String info;
	/** Name of the Description */
	private String description;
	/** Longitude */
	private double longitud;
	/** Latitude */
	private double latitud;
	/** Address (to represent disasters in a map) */
	private String address;
	/** Floor */
	private int floor;
	/** Size of the Disaster */
	private String size;
	/** Density of traffic (high, medium, low) */
	private String traffic;
	/** state of the disaster (active, controlled, erased) */
	private String state;
	/** id of the user who added the disaster */
	private int user;
	/** Number of policemen cars assigned */
	private int policemen;
	/** Number of firemen cars assigned */
	private int firemen;
	/** Number of ambulances assigned */
	private int ambulances;
	/** id police marker already in the map */
	private int policeMarker;
	/** id police marker already in the map */
	private int firemenMarker;
	/** id police marker already in the map */
	private int ambulanceMarker;
	/** Number of slight injuries */
	private int numSlight;
	/** Number of serious injuries */
	private int numSerious;
	/** Number of dead people */
	private int numDead;
	/** Number of trapped people */
	private int numTrapped;
	/** List of slight injuries */
	private ArrayList<People> slight;
	/** List of serious injuries */
	private ArrayList<People> serious;
	/** List of dead people */
	private ArrayList<People> dead;
	/** List of trapped people */
	private ArrayList<People> trapped;

	/**
	 * Constructor de desastre
	 * 
	 * @param id Identificador
	 * @param type Tipo
	 * @param name Nombre
	 * @param info Informacion
	 * @param description Descripcion
	 * @param longitud Longitud
	 * @param latitud Latitud
	 * @param address Direccion
	 * @param floor Planta
	 * @param size Tamanno
	 * @param traffic Traffico
	 * @param state Estado
	 */
	public Disaster(int id, String type, String name, String info, String description, double latitud,
			 double longitud, String address, int floor, String size, String traffic, String state){
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.info = info;
		this.description = description;
		this.longitud = longitud;
		this.latitud = latitud;
		this.address = address;
		this.floor = floor;
		this.size = size;
		this.traffic = traffic;
		this.state = state;

		this.user = 1;
		this.policemen = 0;
		this.firemen = 0;
		this.ambulances = 0;
		this.policeMarker = 0;
		this.firemenMarker = 0;
		this.ambulanceMarker = 0;

		numSlight = 0;
		numSerious = 0;
		numDead = 0;
		numTrapped = 0;
		slight = new ArrayList<People>();
		slight.add(null);
		serious = new ArrayList<People>();
		serious.add(null);
		dead = new ArrayList<People>();
		dead.add(null);
		trapped = new ArrayList<People>();
		trapped.add(null);
	}

	/**
	 * Devuelve el identificador
	 * 
	 * @return the id
	 */
	public int getId(){
		return id;
	}

	/**
	 * Establece el identificador
	 * 
	 * @param id the id to set
	 */
	public void setId(int id){
		this.id = id;
	}

	/**
	 * Devuelve el tipo
	 * 
	 * @return the type
	 */
	public String getType(){
		return type;
	}

	/**
	 * Establece el tipo
	 * 
	 * @param type the type to set
	 */
	public void setType(String type){
		this.type = type;
	}

	/**
	 * Devuelve el nombre
	 * 
	 * @return the name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Establece el nombre
	 * 
	 * @param name the name to set
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * Devuelve la informacion
	 * 
	 * @return the info
	 */
	public String getInfo(){
		return info;
	}

	/**
	 * Establece la informacion
	 * 
	 * @param info the info to set
	 */
	public void setInfo(String info){
		this.info = info;
	}

	/**
	 * Devuelve la descripcion
	 * 
	 * @return the description
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * Establece la descripcion
	 * 
	 * @param description the description to set
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * Devuelve la latitud
	 * 
	 * @return the latitud
	 */
	public double getLatitud(){
		return latitud;
	}

	/**
	 * Establece la latitud
	 * 
	 * @param latitud the latitud to set
	 */
	public void setLatitud(double latitud){
		this.latitud = latitud;
	}

	/**
	 * Devuelve la longitud
	 * 
	 * @return the longitud
	 */
	public double getLongitud(){
		return longitud;
	}

	/**
	 * Establece la longitud
	 * 
	 * @param longitud the longitud to set
	 */
	public void setLongitud(double longitud){
		this.longitud = longitud;
	}

	/**
	 * Devuelve la direccion
	 * 
	 * @return the address
	 */
	public String getAddress(){
		return address;
	}

	/**
	 * Establece la direccion
	 * 
	 * @param address the address to set
	 */
	public void setAddress(String address){
		this.address = address;
	}
	
	/**
	 * 
	 * @return 
	 */
	public int getFloor(){
		return floor;
	}
	
	/**
	 * 
	 * @param floor 
	 */
	public void setFloor(int floor){
		this.floor = floor;
	}

	/**
	 * Devuelve el tamanno
	 * 
	 * @return the size
	 */
	public String getSize(){
		return size;
	}

	/**
	 * Establece el tamanno
	 * 
	 * @param size the size to set
	 */
	public void setSize(String size){
		this.size = size;
	}

	/**
	 * Devuelve el trafico
	 * 
	 * @return the traffic
	 */
	public String getTraffic(){
		return traffic;
	}

	/**
	 * Establece el trafico
	 * 
	 * @param traffic the traffic to set
	 */
	public void setTraffic(String traffic){
		this.traffic = traffic;
	}

	/**
	 * Devuelve el estado
	 * 
	 * @return the state
	 */
	public String getState(){
		return state;
	}

	/**
	 * Establece el estado
	 * 
	 * @param state the state to set
	 */
	public void setState(String state){
		this.state = state;
	}

	/**
	 * Devuelve el usuario
	 * 
	 * @return the user
	 */
	public int getUser(){
		return user;
	}

	/**
	 * Establece el usuario
	 * 
	 * @param user the user to set
	 */
	public void setUser(int user){
		this.user = user;
	}

	/**
	 * Devuelve los policias
	 * 
	 * @return the policemen
	 */
	public int getPolicemen(){
		return policemen;
	}

	/**
	 * Establece los policias
	 * 
	 * @param policemen the policemen to set
	 */
	public void setPolicemen(int policemen){
		this.policemen = policemen;
	}

	/**
	 * Devuelve los bomberos
	 * 
	 * @return the firemen
	 */
	public int getFiremen(){
		return firemen;
	}

	/**
	 * Establece los bomberos
	 * 
	 * @param firemen the firemen to set
	 */
	public void setFiremen(int firemen){
		this.firemen = firemen;
	}

	/**
	 * Devuelve las ambulancias
	 * 
	 * @return the ambulances
	 */
	public int getAmbulances(){
		return ambulances;
	}

	/**
	 * Establece las ambulancias
	 * 
	 * @param ambulances the ambulances to set
	 */
	public void setAmbulances(int ambulances){
		this.ambulances = ambulances;
	}

	/**
	 * Devuelve el marcador de la policia
	 * 
	 * @return the police marker
	 */
	public int getPoliceMarker(){
		return policeMarker;
	}

	/**
	 * Establece el marcador de la policia
	 * 
	 * @param policeMarker the police marker to ser
	 */
	public void setPoliceMarker(int policeMarker){
		this.policeMarker = policeMarker;
	}

	/**
	 * Devuelve el marcador de los bomberos
	 * 
	 * @return the firemen marker
	 */
	public int getFiremenMarker(){
		return firemenMarker;
	}

	/**
	 * Establece el marcador de los bomberos
	 * 
	 * @param firemenMarker the firemen marker to set
	 */
	public void setFiremenMarker(int firemenMarker){
		this.firemenMarker = firemenMarker;
	}

	/**
	 * Devuelve el marcador de la ambulancia
	 * 
	 * @return the ambulance marker
	 */
	public int getAmbulanceMarker(){
		return ambulanceMarker;
	}

	/**
	 * Establece el marcador de la ambulancia
	 * 
	 * @param ambulanceMarker the ambulance marker to set
	 */
	public void setAmbulanceMarker(int ambulanceMarker){
		this.ambulanceMarker = ambulanceMarker;
	}

	/**
	 * Devuelve el ultimo herido leve
	 * 
	 * @return the slight
	 */
	public People getSlight(){
		return slight.get(numSlight);
	}

	/**
	 * Elimina el ultimo herido leve
	 */
	public void setSlight(){
		slight.remove(numSlight);
		if(numSlight > 0){
			numSlight--;
		}
	}

	/**
	 * Establece un herido leve
	 * 
	 * @param people the slight to set
	 */
	public void addSlight(People people){
		boolean existe = false;
		int index = 0;
		for(int i = 0; i < numSlight; i++){
			if(slight.get(i).getId() == people.getId()){
				existe = true;
				index = i;
			}
		}
		if(existe){
			slight.add(index, people);
		}else{
			slight.add(people);
			numSlight++;
		}
	}
	
	/**
	 * Elimina un herido leve
	 * 
	 * @param people the slight to remove
	 */
	public void removeSlight(People people){
		int index = -1; // indexOf() devuelve -1 si no existe
		if(people != null){
			index = slight.indexOf(people);
		}
		if(index != -1){
			slight.remove(index);
			if(numSlight > 0){
				numSlight--;
			}
			while(index < slight.size()){
				slight.set(index, slight.get(index+1));
				slight.set(index+1, null);
				index++;
			}
		}
	} 

	/**
	 * Devuelve el ultimo herido grave
	 * 
	 * @return the serious
	 */
	public People getSerious(){
		return serious.get(numSerious);
	}

	/**
	 * Elimina el ultimo herido grave
	 */
	public void setSerious(){
		serious.remove(numSerious);
		if(numSerious > 0){
			numSerious--;
		}
	}

	/**
	 * Establece un herido grave
	 * 
	 * @param people the serious to set
	 */
	public void addSerious(People people){
		boolean existe = false;
		int index = 0;
		for(int i = 0; i < numSerious; i++){
			if(serious.get(i).getId() == people.getId()){
				existe = true;
				index = i;
			}
		}
		if(existe){
			serious.add(index, people);
		}else{
			serious.add(people);
			numSerious++;
		}
	}
	
	/**
	 * Elimina un herido grave
	 * 
	 * @param people the serious to remove
	 */
	public void removeSerious(People people){
		int index = -1; // indexOf() devuelve -1 si no existe
		if(people != null){
			index = serious.indexOf(people);
		}
		if(index != -1){
			serious.remove(index);
			if(numSerious > 0){
				numSerious--;
			}
			while(index < serious.size()){
				serious.set(index, serious.get(index+1));
				serious.set(index+1, null);
				index++;
			}
		}
	} 

	/**
	 * Devuelve el ultimo muerto
	 * 
	 * @return the dead
	 */
	public People getDead(){
		return dead.get(numDead);
	}

	/**
	 * Elimina el ultimo muerto
	 */
	public void setDead(){
		dead.remove(numDead);
		if(numDead > 0){
			numDead--;
		}
	}

	/**
	 * Establece un muerto
	 * 
	 * @param people the dead to set
	 */
	public void addDead(People people){
		boolean existe = false;
		int index = 0;
		for(int i = 0; i < numDead; i++){
			if(dead.get(i).getId() == people.getId()){
				existe = true;
				index = i;
			}
		}
		if(existe){
			dead.add(index, people);
		}else{
			dead.add(people);
			numDead++;
		}
	}
	
	/**
	 * Elimina un muerto
	 * 
	 * @param people the dead to remove
	 */
	public void removeDead(People people){
		int index = -1; // indexOf() devuelve -1 si no existe
		if(people != null){
			index = dead.indexOf(people);
		}
		if(index != -1){
			dead.remove(index);
			if(numDead > 0){
				numDead--;
			}
			while(index < dead.size()){
				dead.set(index, dead.get(index+1));
				dead.set(index+1, null);
				index++;
			}
		}
	} 

	/**
	 * Devuelve el ultimo atrapado
	 * 
	 * @return the trapped
	 */
	public People getTrapped(){
		return trapped.get(numTrapped);
	}

	/**
	 * Elimina el ultimo atrapado
	 */
	public void setTrapped(){
		trapped.remove(numTrapped);
		if(numTrapped > 0){
			numTrapped--;
		}
	}

	/**
	 * Establece un atrapado
	 * 
	 * @param people the trapped to set
	 */
	public void addTrapped(People people){
		boolean existe = false;
		int index = 0;
		for(int i = 0; i < numTrapped; i++){
			if(this.trapped.get(i).getId() == people.getId()){
				existe = true;
				index = i;
			}
		}
		if(existe){
			this.trapped.add(index, people);
		}else{
			this.trapped.add(people);
			numTrapped++;
		}
	}
	
	/**
	 * Elimina un atrapado
	 * 
	 * @param people the trapped to remove
	 */
	public void removeTrapped(People people){
		int index = -1; // indexOf() devuelve -1 si no existe
		if(people != null){
			index = trapped.indexOf(people);
		}
		if(index != -1){
			trapped.remove(index);
			if(numTrapped > 0){
				numTrapped--;
			}
			while(index < trapped.size()){
				trapped.set(index, trapped.get(index+1));
				trapped.set(index+1, null);
				index++;
			}
		}
	}
	
	public boolean hasInjured(){
		boolean has = slight.size() > 0 || serious.size() > 0 ||
				dead.size() > 0 || trapped.size() > 0;
		return has;
	}
}