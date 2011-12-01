package jadex.desastres.caronte.firemen;

import jadex.bdi.runtime.Plan;

/**
 * Plan de BOMBEROS para llevar al bombero a la estacion de bomberos.
 * 
 * @author Ivan Rojo y Juan Luis Molina
 * 
 */
public class EstacionBomberosPlan extends Plan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		waitFor(1000);
	}
}