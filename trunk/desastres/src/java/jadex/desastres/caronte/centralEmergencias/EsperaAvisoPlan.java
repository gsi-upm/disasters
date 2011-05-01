package jadex.desastres.caronte.centralEmergencias;

import jadex.bdi.runtime.*;
import jadex.desastres.Environment;

/**
 * Plan
 *
 * @author Juan Luis Molina
 *
 */
public class EsperaAvisoPlan extends Plan {

	/**
	 * Cuerpo del plan.
	 */

	public void body() {
		// Obtenemos un objeto de la clase entorno para poder usar sus metodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();
		System.out.println("$$ central: Espero un aviso...");
		waitFor(1000);
	}
}
