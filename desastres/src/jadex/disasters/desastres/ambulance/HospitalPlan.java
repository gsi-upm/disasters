package disasters.desastres.ambulance;

import jadex.bdi.runtime.Plan;

/**
 * Plan de la AMBULANCIA para llevar a la ambulancia al hospital.
 *
 * @author Olimpia Hernandez y Juan Luis Molina
 *
 */
public class HospitalPlan extends Plan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		waitFor(2500);
	}
}