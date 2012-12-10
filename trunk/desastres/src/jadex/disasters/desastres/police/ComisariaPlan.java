package disasters.desastres.police;

import jadex.bdi.runtime.Plan;

/**
 * Plan de la POLIC&Iacute;A para esperar un aviso en la comisar&iacute;a.
 *
 * @author Juan Luis Molina Nogales
 */
public class ComisariaPlan extends Plan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		waitFor(2500);
	}
}