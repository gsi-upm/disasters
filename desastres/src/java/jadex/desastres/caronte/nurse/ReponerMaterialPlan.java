package jadex.desastres.caronte.nurse;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan de ENFERMERO
 *
 * @author Juan Luis Molina
 */
public class ReponerMaterialPlan extends Plan {

	public void body() {
		Environment.printout("EE enfermero: reponiendo el material usado", 0);
		waitFor(1000);
		getBeliefbase().getBelief("material").setFact(true);
		Environment.printout("EE enfermero: material repuesto", 0);
	}
}