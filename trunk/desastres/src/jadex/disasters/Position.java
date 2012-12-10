package disasters;

/**
 * Posici&oacute;n.
 */
public class Position{
	private double latitud;
	private double longitud;
	
	/**
	 * Constructor de Position.
	 * 
	 * @param latitud par&aacute;metro latitud
	 * @param longitud par&aacute;metro longitud
	 */
	public Position(double latitud, double longitud){
		this.latitud = latitud;
		this.longitud = longitud;
	}
	
	/**
	 * Getter de la latitud.
	 * 
	 * @return latitud de la posici&oacute;n
	 */
	public double getLat(){
		return latitud;
	}
	
	/**
	 * Getter de la longitud.
	 * 
	 * @return longitud de la posici&oacute;n
	 */
	public double getLng(){
		return longitud;
	}
}