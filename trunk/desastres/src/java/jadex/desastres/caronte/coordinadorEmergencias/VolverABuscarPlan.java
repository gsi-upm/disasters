package jadex.desastres.caronte.coordinadorEmergencias;

import jadex.bdi.runtime.*;
import jadex.desastres.EnviarMensajePlan;

/**
 *
 * @author Juan Luis Molina
 */
public class VolverABuscarPlan extends EnviarMensajePlan {

	public void body(){
		enviarRespuesta("ack_terminado", "Terminado recibido");
		System.out.println("&& coordinador: Ack mandado");
		System.out.println("&& coordinador: Emergencia solucionada");

		waitFor(6000); // ESPERO A QUE EL ENTORNO SE ACTUALICE!!

		//Creamos un nuevo objetivo.
		//IGoal buscaDesastre = createGoal("buscaDesastre");
		//dispatchSubgoalAndWait(buscaDesastre);
		IGoal encuentraEmergencia = createGoal("encuentraEmergencia");
		dispatchSubgoalAndWait(encuentraEmergencia);
	}
}
