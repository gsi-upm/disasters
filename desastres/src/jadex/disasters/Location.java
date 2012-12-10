package disasters;

/**
 * Clase para definir localidades mediante dos puntos.
 * La esquina superior derecha e inferior izquierda.
 * 
 * @author aebeda
 */
public class Location{
	/** Nombre. */
	private String name;
	/** Esquina superior derecha. */
	private Position esd;
	/** Esquina inferior izquierda. */
	private Position eii;
	
	/**
	 * Constructor de localidad.
	 * 
	 * @param name nombre
	 * @param esd esquina superior derecha
	 * @param eii esquina inferior izquierda
	 */
	public Location(String name, Position esd, Position eii){
		this.name = name;
		this.esd = esd;
		this.eii = eii;
	}
	
	/**
	 * Getter del nombre.
	 * 
	 * @return nombre
	 */
	public String getName(){
		return name;
	}

	/**
	 * Getter de la esquina superior derecha.
	 * 
	 * @return esquina superior derecha
	 */
	public Position getESD(){
		return esd;
	}

	/**
	 * Getter de la esquina inferior izquierda.
	 * 
	 * @return esquina inferior izquierda
	 */
	public Position getEII(){
		return eii;
	}
}