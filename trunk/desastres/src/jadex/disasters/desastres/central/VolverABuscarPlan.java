package disasters.desastres.central;

import disasters.EnviarMensajePlan;
import jadex.bdi.runtime.IGoal;

/**
 *
 * @author Juan Luis Molina
 */
public class VolverABuscarPlan extends EnviarMensajePlan{

	public void body(){
		enviarRespuesta("ack_terminado", "Terminado recibido");
		System.out.println("$$ central: Ack mandado");
		System.out.println("$$ central: Emergencia solucionada");

		waitFor(6000); // ESPERO A QUE EL ENTORNO SE ACTUALICE!!

		//Creamos un nuevo objetivo.
		IGoal buscaDesastre = createGoal("buscaDesastre");
		dispatchSubgoalAndWait(buscaDesastre);
	}
}