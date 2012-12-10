package disasters.caronte.simulador.nurse;

import jadex.bdi.runtime.Plan;

/**
 * Plan de ENFERMERO.
 * 
 * @author Juan Luis Molina Nogales
 */
public class VigilarHabitacionesPlan extends Plan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		waitFor(1000);
	}
}