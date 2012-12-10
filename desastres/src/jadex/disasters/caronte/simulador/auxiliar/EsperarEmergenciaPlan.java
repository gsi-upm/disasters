package disasters.caronte.simulador.auxiliar;

import jadex.bdi.runtime.Plan;

/**
 * Plan del AUXILIAR.
 * 
 * @author Lorena L&oacute;pez Leb&oacute;n
 */
public class EsperarEmergenciaPlan extends Plan{
    
    /**
     * Cuerpo del plan.
     */
    public void body(){
		waitFor(1000);
    }
}