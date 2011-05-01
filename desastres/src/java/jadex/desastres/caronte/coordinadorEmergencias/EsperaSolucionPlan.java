package jadex.desastres.caronte.coordinadorEmergencias;

import jadex.bdi.runtime.Plan;
import jadex.desastres.Environment;

/**
 * Plan de la central que espera que un agente ha solucionado un desastre
 * 
 * @author Ivan y Juan Luis Molina
 * 
 */
public class EsperaSolucionPlan extends Plan {

	/**
	 * Cuerpo del plan
	 */
	public void body() {
		// Obtenemos un objeto de la clase Environment para poder usar sus metodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();
		//System.out.println("$$ central: esperando una solucion...");
		System.out.println("&& coordinador: esperando una solucion...");
	}
}
