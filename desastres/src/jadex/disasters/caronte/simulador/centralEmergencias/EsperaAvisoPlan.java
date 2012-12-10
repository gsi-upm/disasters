package disasters.caronte.simulador.centralEmergencias;

import jadex.bdi.runtime.Plan;

/**
 * Plan de la CENTRAL DE EMERGENCIAS.
 *
 * @author Juan Luis Molina Nogales
 */
public class EsperaAvisoPlan extends Plan{

	/**
	 * Cuerpo del plan.
	 */
	public void body() {
		waitFor(1000);
	}
}