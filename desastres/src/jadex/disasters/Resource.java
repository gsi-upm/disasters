package disasters;

/**
 * Class that represents a disaster
 * 
 * @author julio camarero
 * @version 1.0
 */
public class Resource{
	/** Resource id */
	private int id;
	/** type of resource (firemen, policemen, ambulance) */
	private String type;
	/** Name of the resource */
	private String name;
	/** Info about the resource */
	private String info;
	/** Description for the resource */
	private String description;
	/** Longitud */
	private double longitud;
	/** Latitud */
	private double latitud;
	/** Address (to represent resources in a map) */
	private String address;
	/** Floor of the building */
	private int floor;
	/** state of the resource (usually active) */
	private String state;
	/** assigned Resource id */
	private int idAssigned;
	/** id of the user who added the resource */
	private int user;

	/**
	 * Constructor de un recurso
	 * @param id Identificador
	 * @param type Tipo
	 * @param name Nombre
	 * @param info Informacion
	 * @param description Descripcion
	 * @param latitud Latitud
	 * @param longitud Longitud
	 * @param address Direccion
	 * @param floor Planta
	 * @param state Estado
	 * @param idAssigned Identificador de la emergencia atendida
	 */
	public Resource(int id, String type, String name, String info, String description, double latitud,
			double longitud, String address, int floor, String state, int idAssigned){
		this.id = id;
		this.type = type;
		this.name = name;
		this.info = info;
		this.description = description;
		this.latitud = latitud;
		this.longitud = longitud;
		this.address = address;
		this.floor = floor;
		this.state = state;
		this.idAssigned = idAssigned;
		
		this.user = 1;
	}

	/**
	 * Devuelve el id
	 * 
	 * @return the id
	 */
	public int getId(){
		return id;
	}

	/**
	 * Establece el id
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
	public String getDescription() {
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
	 * @return Latitud
	 */
	public double getLatitud(){
		return latitud;
	}
	
	/**
	 * Establece la latitud
	 * 
	 * @param latitud 
	 */
	public void setLatitud(double latitud){
		this.latitud = latitud;
	}
	
	/**
	 * Devuelve la longitud
	 * 
	 * @return Longitud
	 */
	public double getLongitud(){
		return longitud;
	}
	
	/**
	 * Establece la longitud
	 * 
	 * @param longitud 
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
	 * @param address 
	 */
	public void setAddress(String address){
		this.address = address;
	}

	/**
	 * Devuelve la planta
	 * 
	 * @return the floor
	 */
	public int getFloor(){
		return floor;
	}

	/**
	 * Establece la planta
	 * 
	 * @param floor the floor to set
	 */
	public void setFloor(int floor){
		this.floor = floor;
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
	 * Devuelve el id asignado
	 * 
	 * @return the idAssigned
	 */
	public int getIdAssigned(){
		return idAssigned;
	}

	/**
	 * Establece el id asignado
	 * 
	 * @param idAssigned the idAssigned to set
	 */
	public void setIdAssigned(int idAssigned){
		this.idAssigned = idAssigned;
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
}