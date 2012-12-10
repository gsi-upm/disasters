package disasters;

/**
 * Class that represents a disaster.
 * 
 * @author Julio Camarero
 * @version 1.0
 */
public class People{
	/** Resource id. */
	private int id;
	/** Type of injured (slight, serious, dead, trapped). */
	private String type;
	/** Size of the disaster. */
	private int quantity;
	/** Name of the resource. */
	private String name;
	/** Info about the resource. */
	private String info;
	/** Description for the resource. */
	private String description;
	/** Latitude of the resource. */
	private double latitud;
	/** Longitude of the resource. */
	private double longitud;
	/** Address of the resource. */
	private String address;
	/** Floor of the resource. */
	private int floor;
	/** Size of the resource. */
	private String size;
	/** Traffic of the area. */
	private String traffic;
	/** State of the resource (usually active). */
	private String state;
	/** Assigned Resource id. */
	private int idAssigned;
	/** ID of the user who added the injured. */
	private int user;
	/** <code>true</code> si hay alguna ambulancia yendo a por &eacute;l. */
	private boolean atendido;

	/**
	 * Constructor de grupo de personas.
	 * 
	 * @param id identificador
	 * @param type tipo
	 * @param quantity cantidad de personas
	 * @param name nombre
	 * @param info informaci&oacute;n
	 * @param description descripci&oacute;n
	 * @param latitud latitud
	 * @param longitud longitud
	 * @param address direcci&oacute;n
	 * @param floor planta
	 * @param size tama&ntilde;o
	 * @param traffic tr&aacute;fico
	 * @param state estado
	 * @param idAssigned identificador de desastre asignado
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
	 * Devuelve la cantidad.
	 * 
	 * @return the quantity
	 */
	public int getQuantity(){
		return quantity;
	}

	/**
	 * Establece la cantidad.
	 * 
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity){
		this.quantity = quantity;
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
	public String getDescription(){
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
	 * @return latitud 
	 */
	public double getLatitud(){
		return latitud;
	}
	
	/**
	 * Establece la latitud.
	 * 
	 * @param latitud latitud
	 */
	public void setLatitud(double latitud){
		this.latitud = latitud;
	}
	
	/**
	 * Devuelve la longitud.
	 * 
	 * @return longitud 
	 */
	public double getLongitud(){
		return longitud;
	}
	
	/**
	 * Establece la longitud.
	 * 
	 * @param longitud longitud
	 */
	public void setLongitud(double longitud){
		this.longitud = longitud;
	}
	
	/**
	 * Devuelve la direcci&oacute;n.
	 * 
	 * @return direcci&oacute;n
	 */
	public String getAddres(){
		return address;
	}
	
	/**
	 * Establece la direcci&oacute;n.
	 * 
	 * @param address direcci&oacute;n
	 */
	public void setAddress(String address){
		this.address = address;
	}
	
	/**
	 * Devuelve la planta.
	 * 
	 * @return planta
	 */
	public int getFloor(){
		return floor;
	}
	
	/**
	 * Establece la planta.
	 * 
	 * @param floor planta
	 */
	public void setFloor(int floor){
		this.floor = floor;
	}
	
	/**
	 * Devuelve el tama&ntilde;o.
	 * @return tama&ntilde;o
	 */
	public String getSize(){
		return size;
	}
	
	/**
	 * Establece el tama&ntilde;o.
	 * 
	 * @param size tama&ntilde;o
	 */
	public void setSize(String size){
		this.size = size;
	}
	
	/**
	 * Devuelve el tr&aacute;fico.
	 * 
	 * @return tr&aacute;fico
	 */
	public String getTraffic(){
		return traffic;
	}
	
	/**
	 * Establece el tr&aacute;fico.
	 * 
	 * @param traffic tr&aacute;fico
	 */
	public void setTraffic(String traffic){
		this.traffic = traffic;
	}
	
	/**
	 * Devulve el estado.
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
	 * Devuelve la idAssigned.
	 * 
	 * @return the idAssigned
	 */
	public int getIdAssigned(){
		return idAssigned;
	}

	/**
	 * Establece la idAssigned.
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

	/**
	 * Devuelve si es atendido.
	 * 
	 * @return the atendido
	 */
	public boolean isAtendido(){
		return atendido;
	}

	/**
	 * Establece si es atendido.
	 * 
	 * @param atendido the atendido to set
	 */
	public void setAtendido(boolean atendido){
		this.atendido = atendido;
	}
}