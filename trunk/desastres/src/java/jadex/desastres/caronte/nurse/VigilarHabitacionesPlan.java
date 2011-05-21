package jadex.desastres.caronte.nurse;

import jadex.bdi.runtime.Plan;
import jadex.desastres.*;

/**
 * Plan de ENFERMERO
 * 
 * @author Juan Luis Molina
 * 
 */
public class VigilarHabitacionesPlan extends Plan {

	/**
	 * Cuerpo del plan
	 */
	public void body() {
		waitFor(1000);
	}
}
