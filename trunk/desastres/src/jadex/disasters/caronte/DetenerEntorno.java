package disasters.caronte;

import jadex.bdi.runtime.Plan;

/**
 *
 * @author Juan Luis Molina
 */
public class DetenerEntorno extends Plan{

	/**
	 * Cuerpo del plan
	 */
	public synchronized void body(){
		Entorno.clearInstance();
	}
}