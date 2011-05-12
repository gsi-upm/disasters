package jadex.desastres.caronte.centralEmergencias;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan
 *
 * @author Juan Luis Molina
 *
 */
public class EsperaAvisoPlan extends EnviarMensajePlan {

	/**
	 * Cuerpo del plan.
	 */

	public void body() {
		// Obtenemos un objeto de la clase entorno para poder usar sus metodos
		Environment.printout("CC central: Espero un aviso...",0);

		esperarYEnviarRespuesta("aviso_geriatrico", "Aviso recibido");
		Environment.printout("CC central: Ack mandado",0);

		IGoal mandaAviso = createGoal("mandaAviso");
		dispatchSubgoalAndWait(mandaAviso);
	}
}
