package disasters;

/**
 * Class that represents an association.
 * 
 * @author Juan Luis Molina Nogales
 */
public class Association{
	/** Association id. */
	private int id;
	/** Person injured id. */
	private int idInjured;
	/** Disaster or emergency id. */
	private int idDisaster;
	/** State of the association. */
	private String state;

	/**
	 * Constructor de la asociacion.
	 * 
	 * @param id identificador de la asociaci&oacute;n
	 * @param idInjured identificador del herido
	 * @param idDisaster identificador de la emergencia
	 * @param state estado de la asociacion
	 */
	public Association(int id, int idInjured, int idDisaster, String state){
		super();
		this.id = id;
		this.idInjured = idInjured;
		this.idDisaster = idDisaster;
		this.state = state;
	}

	/**
	 * Getter del ID.
	 * 
	 * @return identificador
	 */
	public int getId(){
		return id;
	}

	/**
	 * Setter del ID.
	 * 
	 * @param id identificador
	 */
	public void setId(int id){
		this.id = id;
	}

	/**
	 * Getter del ID del herido.
	 * 
	 * @return ID del herido
	 */
	public int getIdInjured(){
		return idInjured;
	}

	/**
	 * Setter del ID del herido.
	 * 
	 * @param idInjured ID del herido
	 */
	public void setIdInjured(int idInjured){
		this.idInjured = idInjured;
	}
	
	/**
	 * Getter del ID de la emergencia.
	 * 
	 * @return ID de la emergencia
	 */
	public int getIdDisaster(){
		return idDisaster;
	}

	/**
	 * Setter del ID de la emergencia.
	 * 
	 * @param idDisaster ID de la emergencia
	 */
	public void setIdDisaster(int idDisaster){
		this.idDisaster = idDisaster;
	}
	
	/**
	 * Getter del estado de la asociaci&oacute;n.
	 * 
	 * @return estado de la asociaci&oacute;n
	 */
	public String getState(){
		return state;
	}

	/**
	 * Setter del estado de la asociaci&oacute;n
	 * 
	 * @param state estado de la asociaci&oacute;n
	 */
	public void setState(String state){
		this.state = state;
	}
}