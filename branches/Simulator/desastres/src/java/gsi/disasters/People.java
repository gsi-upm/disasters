package gsi.disasters;

/**
 * Class that represents a group of victims
 * @author julio camarero
 * @version 1.0
 */
public class People {

	/**
	 * Resource id
	 **/
	private int id;

	/**
	 * Assigned Resource id
	 **/
	private int idAssigned;
	
	/**
	 * Type of injured (slight, serious, dead, trapped)
	 **/
	private InjuryDegree type;
	
	/**
	 * Name of the resource
	 **/
	private String name;
     
    /**
	 * Info about the resource
	 */
     private String info;

    /**
	 * Description for the resource
	 */
     private String description;

	/**
	 * State of the resource (usually active)
	 */
     private StateType state;
	
    /**
 	 * Quantity of people
 	 */
 	private int quantity;
	
	/**
	 * Id of the user who added the injured
	 */
	private int user;

    /**
     * Health points of people
     */
    private int healthPoints;

	/**
	 * @param id
	 * @param idAssigned
	 * @param type
	 * @param name
	 * @param address
	 * @param longitud
	 * @param latitud
	 * @param info
	 * @param description
	 * @param quantity
     *
     * Constructor with Injury Degree and without healthPoints, they're assigned automatically
	 */
	public People(int id, InjuryDegree type, String name, String info, String description, int idAssigned, int quantity) {
		super();
		this.id = id;
		this.idAssigned = idAssigned;
		this.type = type;
		this.name = name;
		this.quantity= quantity;
		this.info = info;
		this.description = description;
		this.state = StateType.ACTIVE;
		this.user = 1;
        if (type == InjuryDegree.SLIGHT)
            this.healthPoints = 80;
        else if (type == InjuryDegree.SEVERE)
            this.healthPoints = 30;
        else if (type == InjuryDegree.TRAPPED)
            this.healthPoints = 100;
        else
            this.healthPoints = 0;
	}

    /**
	 * @param id
	 * @param idAssigned
	 * @param type
	 * @param name
	 * @param address
	 * @param longitud
	 * @param latitud
	 * @param info
	 * @param description
	 * @param quantity
     *
     * Constructor with healthPoints, Injury Degree is assigned automatically
	 */
	public People(int id, String name, String info, String description, int idAssigned, int quantity, int healthPoints) {
		super();
		this.id = id;
		this.idAssigned = idAssigned;
		this.name = name;
		this.quantity= quantity;
		this.info = info;
		this.description = description;
		this.state = StateType.ACTIVE;
		this.user = 1;
        this.healthPoints = healthPoints;
        this.type = getVictimDegree(healthPoints);
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
	 * @return the idAssigned
	 */
	public int getIdAssigned() {
		return idAssigned;
	}

	/**
	 * @param idAssigned the idAssigned to set
	 */
	public void setIdAssigned(int idAssigned) {
		this.idAssigned = idAssigned;
	}

	/**
	 * @return the type
	 */
	public InjuryDegree getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(InjuryDegree type) {
		this.type = type;
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
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

    /**
     * @return the healthPoints
     */
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * @param healthPoints the healthPoints to set
     */
    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }
	
	/**
     * Calculates the InjuryDegree associated to a number of health points
     * @param healthPoints
     * @return the InjuryDegree
     */
    public static InjuryDegree getVictimDegree(int healthPoints) {
        if (healthPoints >= 50) {
            return InjuryDegree.SLIGHT;
        }
        else if (healthPoints > 0) {
            return InjuryDegree.SEVERE;
        }
        else {
            return InjuryDegree.DEAD;
        }
    }

    /**
     * Reduces the people number of health points passed as parameter.
     * @param loss number of health points to decrease
     */
    public void reduceHealthPoints(int loss) {
        this.healthPoints -= loss;
        this.type = getVictimDegree(this.healthPoints);
    }

    /**
     * Checks if people are slight victims
     * @return if people are slight victims
     */
    public boolean areSlight() {
        return this.type == InjuryDegree.SLIGHT;
    }

    /**
     * Checks if people are severe victims
     * @return if people are severe victims
     */
    public boolean areSevere() {
        return this.type == InjuryDegree.SEVERE;
    }

    /**
     * Checks if people are dead
     * @return if people are dead
     */
    public boolean areDead() {
        return this.type == InjuryDegree.DEAD;
    }

     /**
     * Checks if people are trapped
     * @return if people are trapped
     */
    public boolean areTrapped() {
        return this.type == InjuryDegree.TRAPPED;
    }

    /**
     * Calculates the number of ambulances necessary to control people
     * @return number of ambulances required
     */
    public int necessaryAmbulances() {
        int number = this.quantity;
        if (this.areSlight()) number*=2;
        else if (this.areSevere()) number *=3;
        number += (int) 10*Math.random();
        return number;
    }


}