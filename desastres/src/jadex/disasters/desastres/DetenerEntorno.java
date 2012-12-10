package disasters.desastres;

import jadex.bdi.runtime.Plan;

/**
 * Plan para detiener el entorno.
 * 
 * @author Juan Luis Molina Nogales
 */
public class DetenerEntorno extends Plan{

	/**
	 * Cuerpo del plan.
	 */
	public synchronized void body(){
		Environment.clearInstance();
	}
}