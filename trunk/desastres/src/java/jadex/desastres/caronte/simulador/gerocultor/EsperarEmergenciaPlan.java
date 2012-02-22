package jadex.desastres.caronte.simulador.gerocultor;

import jadex.bdi.runtime.Plan;

/**
 * Plan de GEROCULTOR
 * 
 * @author Lorena Lopez Lebon
 * 
 */
public class EsperarEmergenciaPlan extends Plan{

	/**
	 * Cuerpo del plan
	 */
	public void body(){
		waitFor(1000);
	}
}