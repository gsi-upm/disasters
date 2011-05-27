package jadex.desastres.caronte.auxiliar;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 *
 * @author Lorena Lopez Lebon
 */
public class EsperarEmergenciaPlan extends Plan{
    
    /**
     * Cuerpo del plan
     */
    public void body() {
            waitFor(1000);
    }
    
}
