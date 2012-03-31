package disasters;

/**
 * Class that represents a disaster
 * 
 * @author julio camarero
 * @version 1.0
 */
public class People{
	/** Resource id */
	private int id;
	/** type of injured (slight, serious, dead, trapped) */
	private String type;
	/** Size of the Disaster */
	private int quantity;
	/** Name of the resource */
	private String name;
	/** Info about the resource */
	private String info;
	/** Description for the resource */
	private String description;
	/**  */
	private double latitud;
	/**  */
	private double longitud;
	/**  */
	private String address;
	/**  */
	private int floor;
	/**  */
	private String size;
	/**  */
	private String traffic;
	/** state of the resource (usually active) */
	private String state;
	/** assigned Resource id */
	private int idAssigned;
	/** id of the user who added the injured */
	private int user;
	/** true si hay alguna ambulancia yendo a por el */
	private boolean atendido;

	/**
	 * Constructor de grupo de personas
	 * 
	 * @param id Identificador
	 * @param type Tipo
	 * @param quantity Cantidad de personas
	 * @param name Nombre
	 * @param info Informacion
	 * @param description Descripcion
	 * @param latitud Latitud
	 * @param longitud Longitud
	 * @param address Direccion
	 * @param floor Planta
	 * @param size Tamanno
	 * @param traffic Trafico
	 * @param state Estado
	 * @param idAssigned Identificador de desastre asignado
	 */
	public People(int id, String type, int quantity, String name, String info, String description, double latitud,
			double longitud, String address, int floor, String size, String traffic, String state, int idAssigned){
		super();
		this.id = id;
		this.type = type;
		this.quantity = quantity;
		this.name = name;
		this.info = info;
		this.description = description;
		this.latitud = latitud;
		this.longitud = longitud;
		this.address = address;
		this.floor = floor;
		this.size = size;
		this.traffic = traffic;
		this.state = state;
		this.idAssigned = idAssigned;
		
		this.user = 1;
		this.atendido = false;
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
	 * Devuelve la cantidad
	 * 
	 * @return the quantity
	 */
	public int getQuantity(){
		return quantity;
	}

	/**
	 * Establece la cantidad
	 * 
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity){
		this.quantity = quantity;
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
	 * @return Latitud 
	 */
	public double getLatitud(){
		return latitud;
	}
	
	/**
	 * Establece la latitud
	 * 
	 * @param latitud Latitud
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
	 * @param longitud Longitud
	 */
	public void setLongitud(double longitud){
		this.longitud = longitud;
	}
	
	/**
	 * Devuelve la direccion
	 * 
	 * @return Direccion
	 */
	public String getAddres(){
		return address;
	}
	
	/**
	 * Establece la direccion
	 * 
	 * @param address Direccion
	 */
	public void setAddress(String address){
		this.address = address;
	}
	
	/**
	 * Devuelve la planta
	 * 
	 * @return Planta
	 */
	public int getFloor(){
		return floor;
	}
	
	/**
	 * Establece la planta
	 * 
	 * @param floor Planta
	 */
	public void setFloor(int floor){
		this.floor = floor;
	}
	
	/**
	 * Devuelve el tamanno
	 * @return Tamanno
	 */
	public String getSize(){
		return size;
	}
	
	/**
	 * Establece el tamanno
	 * 
	 * @param size Tamanno
	 */
	public void setSize(String size){
		this.size = size;
	}
	
	/**
	 * Devuelve el trafico
	 * 
	 * @return Trafico
	 */
	public String getTraffic(){
		return traffic;
	}
	
	/**
	 * Establece el trafico
	 * 
	 * @param traffic 
	 */
	public void setTraffic(String traffic){
		this.traffic = traffic;
	}
	
	/**
	 * Devulve el estado
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
	 * Devuelve la idAssigned
	 * 
	 * @return the idAssigned
	 */
	public int getIdAssigned(){
		return idAssigned;
	}

	/**
	 * Establece la idAssigned
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

	/**
	 * Devuelve si es atendido
	 * 
	 * @return the atendido
	 */
	public boolean isAtendido(){
		return atendido;
	}

	/**
	 * Establece si es atendido
	 * 
	 * @param atendido the atendido to set
	 */
	public void setAtendido(boolean atendido){
		this.atendido = atendido;
	}
}