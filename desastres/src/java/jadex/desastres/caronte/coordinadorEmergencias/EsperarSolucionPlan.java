package jadex.desastres.caronte.coordinadorEmergencias;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan de la central que espera que un agente ha solucionado un desastre
 * 
 * @author Ivan y Juan Luis Molina
 * 
 */
public class EsperarSolucionPlan extends EnviarMensajePlan {

	/**
	 * Cuerpo del plan
	 */
	public void body() {
		Environment.printout("OO coordinador: esperando una solucion...",0);

		esperarYEnviarRespuesta("terminado_geriatrico", "Terminado recibido");

		Environment.printout("OO coordinador: Ack mandado",0);
		Environment.printout("OO coordinador: Emergencia solucionada",0);

		waitFor(7000); // ESPERO A QUE EL ENTORNO SE ACTUALICE!!

		//Creamos un nuevo objetivo
		//IGoal encuentraEmergencia = createGoal("encuentraEmergencia");
		//dispatchSubgoalAndWait(encuentraEmergencia);
	}
}
