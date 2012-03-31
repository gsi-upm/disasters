package disasters;

/**
 * Posicion
 */
public class Position{
	private double latitud;
	private double longitud;
	
	/**
	 * Constructor de Position
	 * 
	 * @param latitud Parametro latitud
	 * @param longitud Parametro longitud
	 */
	public Position(double latitud, double longitud){
		this.latitud = latitud;
		this.longitud = longitud;
	}
	
	/**
	 * Getter de la latitud
	 * 
	 * @return Latitud de la posicion
	 */
	public double getLat(){
		return latitud;
	}
	
	/**
	 * Getter de la longitud
	 * 
	 * @retur Longitud de la posicion
	 */
	public double getLng(){
		return longitud;
	}
}