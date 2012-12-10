package disasters.caronte.simulador.nurse;

import disasters.caronte.Entorno;
import jadex.bdi.runtime.Plan;

/**
 * Plan de ENFERMERO.
 *
 * @author Juan Luis Molina Nogales
 */
public class ReponerMaterialPlan extends Plan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		Entorno env = (Entorno)getBeliefbase().getBelief("env").getFact();
		
		env.printout("EE enfermero: reponiendo el material usado", 2, 0, true);
		waitFor(1000);
		getBeliefbase().getBelief("material").setFact(true);
		env.printout("EE enfermero: material repuesto", 2, 0, true);
	}
}