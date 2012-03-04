package gsi.disasters;

import java.util.List;

/**
 * Class that represents a disaster
 * @author julio camarero
 * @version 1.0
 */
public class Disaster {

	/**
	 * Disaster id
	 */
	private int id;
	/**
	 * Type of disaster (fire, flood, collapse, lostPerson, injuredPerson)
	 */
	private DisasterType type;
	/**
	 * Address (to represent disasters in a map)
	 */
	private String address;
	/**
	 * Longitude
	 */
	private double longitude;
	/**
	 * Latitud
	 */
	private double latitude;
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
	 * State of the disaster (active, controlled, erased)
	 */
	private StateType state;
	/**
	 * Size of the Disaster
	 */
	private SizeType size;
	/**
	 * Strength of the disaster
	 */
	private int strength;
	/**
	 * Density of traffic (high, medium, low)
	 */
	private DensityType traffic;
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
	 * Associate healthy people
	 */
	private List<Person> healthy;
	/**
	 * Associate slight victims
	 */
	private List<Person> slight;
	/**
	 * Associate slight victims
	 */
	private List<Person> serious;
	/**
	 * Associate dead
	 */
	private List<Person> dead;
	/**
	 * Associate trapped
	 */
	private List<Person> trapped;
	/**
	 * Associate police
	 */
    private int policeNum;
	/**
	 * Associate ambulances
	 */
    private int ambulancesNum;
	/**
	 * Associate firemen
	 */
    private int firemenNum;

	/**
	 *
	 * @param id
	 * @param type
	 * @param name
	 * @param info
	 * @param description
	 * @param address
	 * @param longitud
	 * @param latitud
	 * @param state
	 * @param size
	 * @param traffic
	 * @param slight
	 * @param serious
	 * @param dead
	 * @param trapped
	 * @param policeNum
	 * @param ambulancesNum
	 * @param firemenNum
	 * @param strength
	 */
	public Disaster(int id, DisasterType type, String name, String info,
			String description, String address, double longitud, double latitud,
			StateType state, SizeType size, DensityType traffic,
			List<Person> slight, List<Person> serious, List<Person> dead,
			List<Person> trapped, int policeNum,
			int ambulancesNum, int firemenNum,
			int strength) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.info = info;
		this.description = description;
		this.address = address;
		this.longitude = longitud;
		this.latitude = latitud;
		this.state = state;
		this.size = size;
		this.traffic = traffic;

		this.strength = strength;
		this.user = 1;

		this.slight = slight;
		this.serious = serious;
		this.dead = dead;
		this.trapped = trapped;

        this.policeNum = policeNum;
        this.firemenNum = firemenNum;
        this.ambulancesNum = ambulancesNum;

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
	public DisasterType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(DisasterType type) {
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
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude The longitud to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitud
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude The latitud to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
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
	public StateType getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(StateType state) {
		this.state = state;
	}

	/**
	 * @return the size
	 */
	public SizeType getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(SizeType size) {
		this.size = size;
	}

	/**
	 * @return the strength
	 */
	public int getStrength() {
		return strength;
	}

	/**
	 * @param strength the strength to set
	 */
	public void setStrength(int strength) {
		this.strength = strength;
	}

	/**
	 * @return the traffic
	 */
	public DensityType getTraffic() {
		return traffic;
	}

	/**
	 * @param traffic the traffic to set
	 */
	public void setTraffic(DensityType traffic) {
		this.traffic = traffic;
	}

	/**
	 * Returns the number of slight victims
	 * @return the number of slight victims
	 */
	public int getSlightNum() {
		return slight.size();
	}

	/**
	 * Returns the list of slight victims
	 * @return the list of slight victims
	 */
	public List<Person> getSlight() {
		return slight;
	}

	/**
	 * Sets the list of slight victims
	 * @param slight The list of slight victims
	 */
	public void setSlight(List<Person> slight) {
		this.slight = slight;
	}

	/**
	 * Adds a new slight victim to the list
	 */
	public void addSlight(Person slight) {
		this.slight.add(slight);
	}

	/**
	 * Returns the number of serious victims
	 * @return the number of serious victims
	 */
	public int getSeriousNum() {
		return serious.size();
	}

	/**
	 * Returns the list of serious victims
	 * @return the list of serious victims
	 */
	public List<Person> getSerious() {
		return serious;
	}

	/**
	 * Sets the list of serious victims
	 * @param serious The list of serious victims
	 */
	public void setSerious(List<Person> serious) {
		this.serious = serious;
	}

	/**
	 * Adds a new serious victim to the list
	 */
	public void addSerious(Person serious) {
		this.serious.add(serious);
	}

	/**
	 * Returns the number of dead
	 * @return the number of dead
	 */
	public int getDeadNum() {
		return dead.size();
	}

	/**
	 * Returns the list of dead
	 * @return the list of dead
	 */
	public List<Person> getDead() {
		return dead;
	}

	/**
	 * Sets the list of dead
	 * @param dead The list of dead
	 */
	public void setDead(List<Person> dead) {
		this.dead = dead;
	}

	/**
	 * Adds a new dead person to the list
	 */
	public void addDead(Person dead) {
		this.dead.add(dead);
	}

	/**
	 * Returns the number of trapped
	 * @return the number of trapped
	 */
	public int getTrappedNum() {
		return trapped.size();
	}

	/**
	 * Returns the list of trapped
	 * @return the list of trapped
	 */
	public List<Person> getTrapped() {
		return trapped;
	}

	/**
	 * Sets the list of trapped
	 * @param trapped The list of trapped
	 */
	public void setTrapped(List<Person> trapped) {
		this.trapped = trapped;
	}

	/**
	 * Adds a new dead person to the list
	 */
	public void addTrapped(Person trapped) {
		this.trapped.add(trapped);
	}

	/**
	 * Returns the number of police
	 * @return the number of police
	 */
    public int getPoliceNum() {
        return policeNum;
	}

	/**
	 * Returns the number of firemen
	 * @return the number of firemen
	 */
    public int getFiremenNum() {
        return firemenNum;
	}

	/**
	 * Returns the number of ambulances
	 * @return the number of ambulances
	 */
    public int getAmbulancesNum() {
        return ambulancesNum;
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

	/**
	 * @return the number of ambulances assigned
	 */
	public int getAmbulanceMarker() {
		return ambulanceMarker;
	}

	/**
	 * @param ambulanceMarker Number of ambulances assigned
	 */
	public void setAmbulanceMarker(int ambulanceMarker) {
		this.ambulanceMarker = ambulanceMarker;
	}

	/**
	 * @return the number of firemen assigned
	 */
	public int getFiremenMarker() {
		return firemenMarker;
	}

	/**
	 * @param firemenMarker Number of firmen assigned
	 */
	public void setFiremenMarker(int firemenMarker) {
		this.firemenMarker = firemenMarker;
	}

	/**
	 * @return the number of policemen assigned
	 */
	public int getPoliceMarker() {
		return policeMarker;
	}

	/**
	 * @param policeMarker Number of policemen assigned
	 */
	public void setPoliceMarker(int policeMarker) {
		this.policeMarker = policeMarker;
	}

	/**
	 * Checks if the disaster is small
	 * @return if the disaster is small
	 */
	public boolean isSmall() {
		return this.size == SizeType.SMALL;
	}

	/**
	 * Checks if the disaster is medium
	 * @return if the disaster is medium
	 */
	public boolean isMedium() {
		return this.size == SizeType.MEDIUM;
	}

	/**
	 * Checks if the disaster is big
	 * @return if the disaster is big
	 */
	public boolean isBig() {
		return this.size == SizeType.BIG;
	}

	/**
	 * Checks if the disaster is huge
	 * @return if disaster is huge
	 */
	public boolean isHuge() {
		return this.size == SizeType.HUGE;
	}

	/**
	 * Checks if the disaster is active
	 * @return if the disaster is active
	 */
	public boolean isActive() {
		return this.state == StateType.ACTIVE;
	}

	/**
	 * Checks if the disaster is controlled
	 * @return if the disaster is controlled
	 */
	public boolean isControlled() {
		return this.state == StateType.CONTROLLED;
	}

	/**
	 * Checks if the disaster is erased
	 * @return if the disaster is erased
	 */
	public boolean isErased() {
		return this.state == StateType.ERASED;
	}

	/**
	 * Reduces the disaster's strength according to the param
	 * @param loss amount of strength to decrease
	 */
	public void reduceStrength(int loss) {
		this.strength -= loss;
		if (this.strength < 0) {
			this.strength = 0;
		}
	}

	/**
	 * Increases the disaster's strength according to the param
	 * @param rise amount of strength to increase
	 */
	public void increaseStrength(int rise) {
		this.strength += rise;
	}

	/**
	 * Calculates the number of necessary firemen to control a disaster according to
	 * its size, strength and a random value
	 * @return number of necessary firemen to control the disaster
	 */
	public int necessaryFiremen() {
		int number = 0;
		if (this.isSmall()) {
			number = 1;
		} else if (this.isMedium()) {
			number = 2;
		} else if (this.isBig()) {
			number = 3;
		} else if (this.isHuge()) {
			number = 4;
		}
		number *= ((int) this.strength / 10);
		return number;
	}

	/**
	 * Calculates the number of ambulances necessary to control people
	 * One ambulance for each two slight victims
	 * One ambulance for each serious victim
	 * @return number of ambulances required
	 */
	public int necessaryAmbulances() {
		int number = 0;
		if (this.getSlightNum() > 0) {
			number += this.getSlightNum() / 2;
		}
		if (this.getSeriousNum() > 0) {
			number += this.getSeriousNum();
		}
		return number;
	}

	/**
	 * Returns if there is enough firemen
	 * @return if there is enough firemen
	 */
	public boolean isEnoughFiremen() {
        return this.getFiremenNum() >= this.necessaryFiremen();
	}

	/**
	 * Returns if there is enough ambulances
	 * @return if there is enough ambulances
	 */
	public boolean isEnoughAmbulances() {
        return this.getAmbulancesNum() >= this.necessaryAmbulances();
	}
}
