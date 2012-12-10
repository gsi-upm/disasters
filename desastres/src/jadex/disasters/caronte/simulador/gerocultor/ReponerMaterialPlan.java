package disasters.caronte.simulador.gerocultor;

import disasters.caronte.Entorno;
import jadex.bdi.runtime.Plan;

/**
 * Plan de GEROCULTOR.
 *
 * @author Lorena L&oacute;pez Leb&oacute;n
 */
public class ReponerMaterialPlan extends Plan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		Entorno env = (Entorno)getBeliefbase().getBelief("env").getFact();

		env.printout("GG gerocultor: reponiendo el material usado", 2, 0, true);
		waitFor(1000);
		getBeliefbase().getBelief("material").setFact(true);
		env.printout("GG gerocultor: material repuesto", 2, 0, true);
	}
}