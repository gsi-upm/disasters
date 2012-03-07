package disasters.desastres.sanitarios.ambulancia;

import jadex.bdi.runtime.Plan;

/**
 * Plan de la AMBULANCIA para llevar a la ambulancia al hospital.
 *
 * @author Olimpia Hernandez y Juan Luis Molina Nogales
 *
 */
public class HospitalPlan extends Plan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		waitFor(1000);
	}
}