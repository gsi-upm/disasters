package disasters;

/**
 * Class that represents an activity
 * 
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
	 * Constructor de la actividad
	 * 
	 * @param id Identificador de la actividad
	 * @param idUser Identificador del usuario
	 * @param idDisaster Identificador del evento
	 * @param type Tipo de evento
	 * @param state Estado de la actividad
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
	 * Getter del ID
	 * 
	 * @return Identificador
	 */
	public int getId(){
		return id;
	}

	/**
	 * Setter del ID
	 * 
	 * @param id Identificador
	 */
	public void setId(int id){
		this.id = id;
	}

	/**
	 * Getter del ID del usuario
	 * 
	 * @return ID de usuario
	 */
	public int getIdUser(){
		return idUser;
	}

	/**
	 * Setter del ID del usuario
	 * 
	 * @param idUser ID de usuario
	 */
	public void setIdInjured(int idUser){
		this.idUser = idUser;
	}
	
	/**
	 * Getter del ID del evento
	 * 
	 * @return ID de evento
	 */
	public int getIdDisaster(){
		return idDisaster;
	}

	/**
	 * Setter del ID del evento
	 * 
	 * @param idDisaster ID de evento
	 */
	public void setIdDisaster(int idDisaster){
		this.idDisaster = idDisaster;
	}
	
	/**
	 * Getter del tipo de evento
	 * 
	 * @return Tipo de evento
	 */
	public String getType(){
		return type;
	}

	/**
	 * Setter del tipo de evento
	 * 
	 * @param type Tipo de evento
	 */
	public void setType(String type){
		this.type = type;
	}
	
	/**
	 * Getter del estado de la actividad
	 * 
	 * @return Estado de la actividad
	 */
	public String getState(){
		return state;
	}

	/**
	 * Setter del estado de la actividad
	 * 
	 * @param state Estado de la actividad
	 */
	public void setState(String state){
		this.state = state;
	}
}