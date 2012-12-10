package disasters.caronte;

import jadex.bdi.runtime.Plan;

/**
 * Detiene el entorno.
 * 
 * @author Juan Luis Molina Nogales
 */
public class DetenerEntorno extends Plan{

	/**
	 * Cuerpo del plan.
	 */
	public synchronized void body(){
		Entorno.clearInstance();
	}
}