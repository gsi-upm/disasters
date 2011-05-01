package jadex.desastres;

import jadex.bdi.runtime.*;

/**
 *
 * @author Juan Luis Molina
 */
public class DetenerEntorno extends Plan {

	/**
	 * Cuerpo del plan
	 */
	public synchronized void body() {
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();
		env.terminar();
	}
}
