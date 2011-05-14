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

		String recibido = esperarYEnviarRespuesta("terminado_geriatrico", "Terminado recibido");

		Environment.printout("OO coordinador: Ack mandado",0);
		Environment.printout("OO coordinador: Emergencia solucionada",0);

		Environment.printout("OO coordinador: informando al gerocultor...",0);
		String resultado2 = enviarMensaje("gerocultor", "fin_emergencia", "ok");

		Environment.printout("OO coordinador: informando al enfermero...",0);
		String resultado1 = enviarMensaje("nurse", "fin_emergencia", "ok");

		waitFor(8000); // ESPERO A QUE EL ENTORNO SE ACTUALICE!!

		//Creamos un nuevo objetivo
		//IGoal encuentraEmergencia = createGoal("encuentraEmergencia");
		//dispatchSubgoalAndWait(encuentraEmergencia);
	}
}
