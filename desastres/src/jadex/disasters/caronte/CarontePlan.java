package disasters.caronte;

import disasters.EnviarMensajePlan;

/**
 * Plan de Caronte.
 * 
 * @author Juan Luis Molina
 */
public abstract class CarontePlan extends EnviarMensajePlan{
	/** circunferencia ecuatorial y polar de La Tierra en metros */
	private double circE = 40075017;
	private double circP = 40007863;
	
	/**
	 * Distancia en metros entre dos puntos.
	 * 
	 * @param lat1 Latitud coordenada 1
	 * @param lng1 Longitud coordenada 1
	 * @param lat2 Latitud coordenada 2
	 * @param lng2 Longitud coordenada 2
	 * @return Distancia entre puntos
	 */
	public double distancia(double lat1, double lng1, double lat2, double lng2){
		double alto = circP * (lat1-lat2) / 360;
		double circLng = Math.cos((lat1+lat2)/2) * circE; // circunferencia de LONGITUDES segun la LATITUD (media)
		double ancho = circLng * (lng1-lng2) / 360;
		return Math.sqrt(Math.pow(alto, 2) + Math.pow(ancho, 2));
	}
	
	/**
	 * Comprueba si la planta del posible agente es mas cercana que la del actual.
	 * 
	 * @param planta1 Planta del posible agente
	 * @param planta2 Planta del agente actual
	 * @param plantaRef Planta de la emergencia
	 * @return True si misma planta o mas cerca
	 */
	public boolean mejorPlanta(int planta1, int planta2, int plantaRef){
		return Math.abs(plantaRef-planta1) <= Math.abs(plantaRef-planta2);
	}
}