package jadex.desastres.caronte.auxiliar;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 *
 * @author Lorena Lopez Lebon
 */
public class ReponerMaterialPlan extends Plan{
    
    public void body() {
        Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

        env.printout("XX auxiliar: reponiendo el material usado", 0);
        waitFor(1000);
        getBeliefbase().getBelief("material").setFact(true);
        env.printout("XX auxiliar: material repuesto", 0);
    }
    
}
