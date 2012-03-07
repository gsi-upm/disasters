package disasters.caronte.simulador.auxiliar;

import jadex.bdi.runtime.Plan;

/**
 *
 * @author Lorena Lopez Lebon
 */
public class EsperarEmergenciaPlan extends Plan{
    
    /**
     * Cuerpo del plan
     */
    public void body(){
		waitFor(1000);
    }
}