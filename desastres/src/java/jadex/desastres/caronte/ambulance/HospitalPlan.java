package jadex.desastres.caronte.ambulance;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan de la AMBULANCIA para llevar a la ambulancia al hospital.
 *
 * @author Olimpia Hernandez y Juan Luis Molina
 *
 */
public class HospitalPlan extends Plan {

	/**
	 * Cuerpo del plan.
	 */
	public void body() {
		waitFor(1000);
	}
}
