package jadex.desastres.caronte.centralEmergencias;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan de la central que espera que un agente ha solucionado un desastre
 * 
 * @author Ivan y Juan Luis Molina
 * 
 */
public class EsperaSolucionPlan extends EnviarMensajePlan {

	/**
	 * Cuerpo del plan
	 */
	public void body() {
		Environment.printout("CC central: esperando una solucion...", 0);

		esperarYEnviarRespuesta("terminado", "Terminado recibido");

		Environment.printout("CC central: Ack mandado", 0);
		Environment.printout("CC central: Emergencia solucionada", 0);

		Environment.printout("CC central: Mandando mensaje al coordinador", 0);
		String resultado = enviarMensaje("coordinadorEmergencias", "terminado_geriatrico", "go");
		Environment.printout("CC central: Respuesta recibida del coordinador: " + resultado, 0);
	}
}
