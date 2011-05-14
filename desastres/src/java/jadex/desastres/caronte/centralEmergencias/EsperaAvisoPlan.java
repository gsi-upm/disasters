package jadex.desastres.caronte.centralEmergencias;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan
 *
 * @author Juan Luis Molina
 *
 */
public class EsperaAvisoPlan extends EnviarMensajePlan {

	/**
	 * Cuerpo del plan.
	 */

	public void body() {

		waitFor(1000);

	}
}
