package disasters.caronte.simulador.gerocultor;

import jadex.bdi.runtime.Plan;

/**
 * Plan de GEROCULTOR.
 * 
 * @author Lorena L&oacute;pez Leb&oacute;n
 */
public class EsperarEmergenciaPlan extends Plan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		waitFor(1000);
	}
}