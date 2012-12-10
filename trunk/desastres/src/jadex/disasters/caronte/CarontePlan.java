package disasters.caronte;

import disasters.EnviarMensajePlan;

/**
 * Plan de Caronte.
 * 
 * @author Juan Luis Molina Nogales
 */
public abstract class CarontePlan extends EnviarMensajePlan{
	/** Circunferencia ecuatorial de La Tierra (en metros). */
	private double circE = 40075017;
	/** Circunferencia polar de La Tierra (en metros). */
	private double circP = 40007863;
	
	/**
	 * Distancia en metros entre dos puntos.
	 * 
	 * @param lat1 latitud coordenada 1
	 * @param lng1 longitud coordenada 1
	 * @param lat2 latitud coordenada 2
	 * @param lng2 longitud coordenada 2
	 * @return distancia entre puntos
	 */
	public double distancia(double lat1, double lng1, double lat2, double lng2){
		double alto = circP * (lat1-lat2) / 360;
		double circLng = Math.cos((lat1+lat2)/2) * circE; // circunferencia de LONGITUDES segun la LATITUD (media)
		double ancho = circLng * (lng1-lng2) / 360;
		return Math.sqrt(Math.pow(alto, 2) + Math.pow(ancho, 2));
	}
	
	/**
	 * Comprueba si la planta del posible agente es m&aacute;s cercana que la del actual.
	 * 
	 * @param planta1 planta del posible agente
	 * @param planta2 planta del agente actual
	 * @param plantaRef planta de la emergencia
	 * @return <code>true</code> si misma planta o m&aacute;s cerca
	 */
	public boolean mejorPlanta(int planta1, int planta2, int plantaRef){
		return Math.abs(plantaRef-planta1) <= Math.abs(plantaRef-planta2);
	}
}