package disasters.caronte.simulador.coordinadorEmergencias;

import disasters.EnviarMensajePlan;
import disasters.caronte.Entorno;

/**
 * Plan de la central que espera que un agente ha solucionado un desastre.
 * 
 * @author Iv&aacute;n Rojo
 * @author Juan Luis Molina Nogales
 */
public class EsperarSolucionPlan extends EnviarMensajePlan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		Entorno env = (Entorno)getBeliefbase().getBelief("env").getFact();

		env.printout("OO coordinador: esperando una solucion...", 2, 3, true);

		String recibido = esperarYEnviarRespuesta("terminado_geriatrico", "Terminado recibido");

		env.printout("OO coordinador: Ack mandado", 2, 3, true);
		env.printout("OO coordinador: Emergencia solucionada", 2, 3, true);

		env.printout("OO coordinador: informando al gerocultor...", 2, 3, true);
		String resultado2 = enviarMensaje("gerocultor", "fin_emergencia", "ok", true);

		env.printout("OO coordinador: informando al enfermero...", 2, 3, true);
		String resultado1 = enviarMensaje("nurse", "fin_emergencia", "ok", true);

		env.printout("OO coordinador: informando al auxiliar...", 2, 3, true);
		String resultado3 = enviarMensaje("auxiliar", "fin_emergencia", "ok", true);

		waitFor(8000); // ESPERO A QUE EL ENTORNO SE ACTUALICE!!

		//Creamos un nuevo objetivo
		//IGoal encuentraEmergencia = createGoal("encuentraEmergencia");
		//dispatchSubgoalAndWait(encuentraEmergencia);
	}
}