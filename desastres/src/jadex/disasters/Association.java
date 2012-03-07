package disasters;

/**
 * Class that represents an association
 * @author Juan Luis Molina
 * @version 1.0
 */
public class Association{
	/** Association id */
	private int id;
	
	/** Person injured id */
	private int idInjured;
	
	/** Disaster or emergency id */
	private int idDisaster;
	
	/** State of the association */
	private String state;

	/**
	 * @param id
	 * @param idInjured
	 * @param idDisaster
	 * @param state
	 */
	public Association(int id, int idInjured, int idDisaster, String state){
		super();
		this.id = id;
		this.idInjured = idInjured;
		this.idDisaster = idDisaster;
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
	 * @return the idInjured
	 */
	public int getIdInjured(){
		return idInjured;
	}

	/**
	 * @param idInjured the idInjured to set
	 */
	public void setIdInjured(int idInjured){
		this.idInjured = idInjured;
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