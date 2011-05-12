package jadex.desastres.caronte.gerocultor;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan de GEROCULTOR
 *
 * @author Lorena Lopez Lebon
 */
public class ReponerMaterialPlan extends Plan {

	public void body() {
		Environment.printout("GG gerocultor: reponiendo el material usado", 0);
		waitFor(1000);
		getBeliefbase().getBelief("material").setFact(true);
		Environment.printout("GG gerocultor: material repuesto", 0);
	}

}
