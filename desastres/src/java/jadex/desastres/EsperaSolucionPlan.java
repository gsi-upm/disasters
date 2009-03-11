package jadex.desastres;

import sun.security.acl.WorldGroupImpl;
import jadex.examples.hunterprey.WorldObjectData;
import jadex.runtime.Plan;
/**
 * Plan de la central que espera que un agente ha solucionado un desastre
 * 
 * @author Ivan
 * 
 */
public class EsperaSolucionPlan extends Plan {
	/**
	 * Cuerpo del plan
	 */
	public void body() {

		// Obtenemos un objeto de la clase Environment para poder usar sus métodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();
         waitFor(500);
		}
	}

