package disasters;

/**
 * Class that represents a disaster.
 * 
 * @author Julio Camarero
 * @version 1.0
 */
public class Resource{
	/** Resource id. */
	private int id;
	/** Type of resource (firemen, policemen, ambulance). */
	private String type;
	/** Name of the resource. */
	private String name;
	/** Info about the resource. */
	private String info;
	/** Description for the resource. */
	private String description;
	/** Longitude. */
	private double longitud;
	/** Latitude. */
	private double latitud;
	/** Address (to represent resources in a map). */
	private String address;
	/** Floor of the building. */
	private int floor;
	/** State of the resource (usually active). */
	private String state;
	/** Assigned Resource id. */
	private int idAssigned;
	/** ID of the user who added the resource. */
	private int user;

	/**
	 * Constructor de un recurso.
	 * 
	 * @param id identificador
	 * @param type tipo
	 * @param name nombre
	 * @param info informaci&oacute;n
	 * @param description descripci&oacute;n
	 * @param latitud latitud
	 * @param longitud longitud
	 * @param address direcci&oacute;n
	 * @param floor planta
	 * @param state estado
	 * @param idAssigned identificador de la emergencia atendida
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
	 * Devuelve el id.
	 * 
	 * @return the id
	 */
	public int getId(){
		return id;
	}

	/**
	 * Establece el id.
	 * 
	 * @param id the id to set
	 */
	public void setId(int id){
		this.id = id;
	}

	/**
	 * Devuelve el tipo.
	 * 
	 * @return the type
	 */
	public String getType(){
		return type;
	}

	/**
	 * Establece el tipo.
	 * 
	 * @param type the type to set
	 */
	public void setType(String type){
		this.type = type;
	}

	/**
	 * Devuelve el nombre.
	 * 
	 * @return the name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Establece el nombre.
	 * 
	 * @param name the name to set
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * Devuelve la informaci&oacute;n.
	 * 
	 * @return the info
	 */
	public String getInfo(){
		return info;
	}

	/**
	 * Establece la informaci&oacute;n.
	 * 
	 * @param info the info to set
	 */
	public void setInfo(String info){
		this.info = info;
	}

	/**
	 * Devuelve la descripci&oacute;n.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Establece la descripci&oacute;n.
	 * 
	 * @param description the description to set
	 */
	public void setDescription(String description){
		this.description = description;
	}
	
	/**
	 * Devuelve la latitud.
	 * 
	 * @return the latitude
	 */
	public double getLatitud(){
		return latitud;
	}
	
	/**
	 * Establece la latitud.
	 * 
	 * @param latitud the latitude to set
	 */
	public void setLatitud(double latitud){
		this.latitud = latitud;
	}
	
	/**
	 * Devuelve la longitud.
	 * 
	 * @return the longitude
	 */
	public double getLongitud(){
		return longitud;
	}
	
	/**
	 * Establece la longitud.
	 * 
	 * @param longitud the longitude to set
	 */
	public void setLongitud(double longitud){
		this.longitud = longitud;
	}

	/**
	 * Devuelve la direcci&oacute;n.
	 * 
	 * @return the address
	 */
	public String getAddress(){
		return address;
	}
	
	/**
	 * Establece la direcci&oacute;n.
	 * 
	 * @param address the address to set
	 */
	public void setAddress(String address){
		this.address = address;
	}

	/**
	 * Devuelve la planta.
	 * 
	 * @return the floor
	 */
	public int getFloor(){
		return floor;
	}

	/**
	 * Establece la planta.
	 * 
	 * @param floor the floor to set
	 */
	public void setFloor(int floor){
		this.floor = floor;
	}

	/**
	 * Devuelve el estado.
	 * 
	 * @return the state
	 */
	public String getState(){
		return state;
	}

	/**
	 * Establece el estado.
	 * 
	 * @param state the state to set
	 */
	public void setState(String state){
		this.state = state;
	}

	/**
	 * Devuelve el id asignado.
	 * 
	 * @return the idAssigned
	 */
	public int getIdAssigned(){
		return idAssigned;
	}

	/**
	 * Establece el id asignado.
	 * 
	 * @param idAssigned the idAssigned to set
	 */
	public void setIdAssigned(int idAssigned){
		this.idAssigned = idAssigned;
	}

	/**
	 * Devuelve el usuario.
	 * 
	 * @return the user
	 */
	public int getUser(){
		return user;
	}

	/**
	 * Establece el usuario.
	 * 
	 * @param user the user to set
	 */
	public void setUser(int user){
		this.user = user;
	}
}