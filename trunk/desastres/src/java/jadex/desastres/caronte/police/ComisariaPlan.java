package jadex.desastres.caronte.police;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan de la POLICIA para esperar un aviso en la comisaria.
 *
 * @author Juan Luis Molina
 *
 */
public class ComisariaPlan extends Plan {

	/**
	 * Cuerpo del plan.
	 */
	public void body() {
		waitFor(1000);
	}
}
