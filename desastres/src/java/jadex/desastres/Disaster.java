package jadex.desastres;

/**
 * Class that represents a disaster
 * @author julio camarero
 * @version 1.0
 */
public class Disaster {

	/**
	 * Disaster id
	 **/
	private int id;
	/**
	 * type of disaster (fire, flood, collapse, lostPerson, injuredPerson)
	 **/
	private String type;
	/**
	 * Address (to represent disasters in a map) 
	 */
	private String address;
	/**
	 * Longitude
	 */
	private double longitud;
	/**
	 * Latitud
	 */
	private double latitud;
	/**
	 * Name of the Disaster
	 */
	private String name;
	/**
	 * Name of the Information
	 */
	private String info;
	/**
	 * Name of the Description
	 */
	private String description;
	/**
	 * state of the disaster (active, controlled, erased)
	 */
	private String state;
	/**
	 * Size of the Disaster
	 */
	private String size;
	/**
	 * Density of traffic (high, medium, low) 
	 */
	private String traffic;
	/**
	 * Number of slight injuries
	 */
	private People slight;
	/**
	 * Number of serious injuries
	 */
	private People serious;
	/**
	 * Number of dead people
	 */
	private People dead;
	/**
	 * Number of trapped people
	 */
	private People trapped;
	/**
	 * Number of policemen cars assigned
	 */
	private int policemen;
	/**
	 * Number of firemen cars assigned
	 */
	private int firemen;
	/**
	 * Number of ambulances assigned
	 */
	private int ambulances;
	/**
	 * id of the user who added the disaster
	 */
	private int user;
	/**
	 * id police marker already in the map
	 */
	private int policeMarker;
	/**
	 * id police marker already in the map
	 */
	private int ambulanceMarker;
	/**
	 * id police marker already in the map
	 */
	private int firemenMarker;

	/**
	 * @param id

	 * @param type
	 * @param address
	 * @param longitud
	 * @param latitud
	 * @param name
	 * @param info
	 * @param description
	 * @param state
	 * @param size
	 * @param traffic
	 * @param slight
	 * @param serious
	 * @param dead
	 * @param trapped
	 * Constructor sin idAsignada
	 */
	public Disaster(int id, String type, String name, String info, String description, String address, double longitud,
			double latitud, String state, String size, String traffic) {
		super();
		this.id = id;

		this.type = type;
		this.address = address;
		this.longitud = longitud;
		this.latitud = latitud;
		this.name = name;
		this.info = info;
		this.description = description;
		this.state = state;
		this.size = size;
		this.traffic = traffic;

		this.user = 1;
		this.policemen = 0;
		this.firemen = 0;
		this.ambulances = 0;
		this.policeMarker = 0;
		this.ambulanceMarker = 0;
		this.firemenMarker = 0;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the longitud
	 */
	public double getLongitud() {
		return longitud;
	}

	/**
	 * @param longitud the longitud to set
	 */
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	/**
	 * @return the latitud
	 */
	public double getLatitud() {
		return latitud;
	}

	/**
	 * @param latitud the latitud to set
	 */
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the traffic
	 */
	public String getTraffic() {
		return traffic;
	}

	/**
	 * @param traffic the traffic to set
	 */
	public void setTraffic(String traffic) {
		this.traffic = traffic;
	}

	/**
	 * @return the policemen
	 */
	public int getPolicemen() {
		return policemen;
	}

	/**
	 * @param policemen the policemen to set
	 */
	public void setPolicemen(int policemen) {
		this.policemen = policemen;
	}

	/**
	 * @return the firemen
	 */
	public int getFiremen() {
		return firemen;
	}

	/**
	 * @param firemen the firemen to set
	 */
	public void setFiremen(int firemen) {
		this.firemen = firemen;
	}

	/**
	 * @return the ambulances
	 */
	public int getAmbulances() {
		return ambulances;
	}

	/**
	 * @param ambulances the ambulances to set
	 */
	public void setAmbulances(int ambulances) {
		this.ambulances = ambulances;
	}

	/**
	 * @return the user
	 */
	public int getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(int user) {
		this.user = user;
	}

	public int getAmbulanceMarker() {
		return ambulanceMarker;
	}

	public void setAmbulanceMarker(int ambulanceMarker) {
		this.ambulanceMarker = ambulanceMarker;
	}

	public int getFiremenMarker() {
		return firemenMarker;
	}

	public void setFiremenMarker(int firemenMarker) {
		this.firemenMarker = firemenMarker;
	}

	public int getPoliceMarker() {
		return policeMarker;
	}

	public void setPoliceMarker(int policeMarker) {
		this.policeMarker = policeMarker;
	}

	/**
	 * @return the slight
	 */
	public People getSlight() {
		return slight;
	}

	/**
	 * @param slight the slight to set
	 */
	public void setSlight(People slight) {
		this.slight = slight;
	}

	public void setSlight() {
		this.slight = null;
	}

	/**
	 * @return the serious
	 */
	public People getSerious() {
		return serious;
	}

	/**
	 * @param serious the serious to set
	 */
	public void setSerious(People serious) {
		this.serious = serious;
	}

	public void setSerious() {
		this.serious = null;
	}

	/**
	 * @return the dead
	 */
	public People getDead() {
		return dead;
	}

	/**
	 * @param dead the dead to set
	 */
	public void setDead(People dead) {
		this.dead = dead;
	}

	public void setDead() {
		this.dead = null;
	}

	/**
	 * @return the trapped
	 */
	public People getTrapped() {
		return trapped;
	}

	/**
	 * @param trapped the trapped to set
	 */
	public void setTrapped(People trapped) {
		this.trapped = trapped;
	}

	public void setTrapped() {
		this.trapped = null;
	}
}
