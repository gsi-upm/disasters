package disasters.desastres.central;

import jadex.bdi.runtime.Plan;

/**
 * Plan de la central que espera que un agente ha solucionado un desastre.
 * 
 * @author Iv&aacute;n Rojo
 * @author Juan Luis Molina Nogales
 */
public class EsperaSolucionPlan extends Plan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		System.out.println("$$ central: esperando una solucion...");
	}
}