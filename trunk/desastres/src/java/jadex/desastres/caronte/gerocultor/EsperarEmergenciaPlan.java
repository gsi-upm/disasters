package jadex.desastres.caronte.gerocultor;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan de GEROCULTOR
 * 
 * @author Lorena Lopez Lebon
 * 
 */
public class EsperarEmergenciaPlan extends Plan {

	/**
	 * Cuerpo del plan
	 */
	public void body() {
		waitFor(1000);
	}
}
