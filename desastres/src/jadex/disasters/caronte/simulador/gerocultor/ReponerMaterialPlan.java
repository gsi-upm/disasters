package disasters.caronte.simulador.gerocultor;

import disasters.Environment;
import jadex.bdi.runtime.Plan;

/**
 * Plan de GEROCULTOR
 *
 * @author Lorena Lopez Lebon
 */
public class ReponerMaterialPlan extends Plan {

	public void body() {
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();

		env.printout("GG gerocultor: reponiendo el material usado", 0);
		waitFor(1000);
		getBeliefbase().getBelief("material").setFact(true);
		env.printout("GG gerocultor: material repuesto", 0);
	}
}