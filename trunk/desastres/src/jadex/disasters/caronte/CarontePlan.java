package disasters.caronte;

import disasters.EnviarMensajePlan;

/**
 * Plan de Caronte.
 * 
 * @author Juan Luis Molina
 */
public abstract class CarontePlan extends EnviarMensajePlan{
	/**
	 * Distancia entre dos puntos.
	 * 
	 * @param lat1 Latitud coordenada 1
	 * @param lng1 Longitud coordenada 1
	 * @param lat2 Latitud coordenada 2
	 * @param lng2 Longitud coordenada 2
	 * @return Distancia entre puntos
	 */
	public double distancia(double lat1, double lng1, double lat2, double lng2){
		return Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lng1 - lng2, 2));
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