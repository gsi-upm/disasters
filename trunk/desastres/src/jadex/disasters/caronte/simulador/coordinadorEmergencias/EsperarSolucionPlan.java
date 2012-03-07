package disasters.caronte.simulador.coordinadorEmergencias;

import disasters.*;

/**
 * Plan de la central que espera que un agente ha solucionado un desastre
 * 
 * @author Ivan y Juan Luis Molina
 * 
 */
public class EsperarSolucionPlan extends EnviarMensajePlan{

	/**
	 * Cuerpo del plan
	 */
	public void body(){
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();

		env.printout("OO coordinador: esperando una solucion...",3);

		String recibido = esperarYEnviarRespuesta("terminado_geriatrico", "Terminado recibido");

		env.printout("OO coordinador: Ack mandado",3);
		env.printout("OO coordinador: Emergencia solucionada",3);

		env.printout("OO coordinador: informando al gerocultor...",3);
		String resultado2 = enviarMensaje("gerocultor", "fin_emergencia", "ok");

		env.printout("OO coordinador: informando al enfermero...",3);
		String resultado1 = enviarMensaje("nurse", "fin_emergencia", "ok");

		env.printout("OO coordinador: informando al auxiliar...",3);
		String resultado3 = enviarMensaje("auxiliar", "fin_emergencia", "ok");

		waitFor(8000); // ESPERO A QUE EL ENTORNO SE ACTUALICE!!

		//Creamos un nuevo objetivo
		//IGoal encuentraEmergencia = createGoal("encuentraEmergencia");
		//dispatchSubgoalAndWait(encuentraEmergencia);
	}
}