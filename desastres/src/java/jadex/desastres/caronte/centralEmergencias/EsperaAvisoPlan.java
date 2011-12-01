package jadex.desastres.caronte.centralEmergencias;

import jadex.bdi.runtime.Plan;

/**
 * Plan
 *
 * @author Juan Luis Molina
 *
 */
public class EsperaAvisoPlan extends Plan{

	/**
	 * Cuerpo del plan.
	 */
	public void body() {
		waitFor(1000);
	}
}