package jadex.desastres;

/**
 * Class that represents an activity
 * @author Juan Luis Molina
 * @version 1.0
 */
public class Activity{
	/** Activity id */
	private int id;
	
	/** User id */
	private int idUser;
	
	/** Disaster or emergency id */
	private int idDisaster;
	
	/** Type of activity */
	private String type;
	
	/** State of the activity */
	private String state;

	/**
	 * @param id
	 * @param idUser
	 * @param idDisaster
	 * @param type
	 * @param state
	 */
	public Activity(int id, int idUser, int idDisaster, String type, String state){
		super();
		this.id = id;
		this.idUser = idUser;
		this.idDisaster = idDisaster;
		this.type = type;
		this.state = state;
	}

	/**
	 * @return the id
	 */
	public int getId(){
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id){
		this.id = id;
	}

	/**
	 * @return the idUser
	 */
	public int getIdUser(){
		return idUser;
	}

	/**
	 * @param idUser the idUser to set
	 */
	public void setIdInjured(int idUser){
		this.idUser = idUser;
	}
	
	/**
	 * @return the idDisaster
	 */
	public int getIdDisaster(){
		return idDisaster;
	}

	/**
	 * @param idDisaster the idDisaster to set
	 */
	public void setIdDisaster(int idDisaster){
		this.idDisaster = idDisaster;
	}
	
	/**
	 * @return the type
	 */
	public String getType(){
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type){
		this.type = type;
	}
	
	/**
	 * @return the state
	 */
	public String getState(){
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state){
		this.state = state;
	}
}