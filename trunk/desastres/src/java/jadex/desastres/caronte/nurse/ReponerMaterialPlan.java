package jadex.desastres.caronte.nurse;

import jadex.bdi.runtime.*;

/**
 * Plan de ENFERMERO
 *
 * @author Juan Luis Molina
 */
public class ReponerMaterialPlan extends Plan {

	public void body() {
		getBeliefbase().getBelief("material").setFact(true);
	}

}