package disasters.caronte.atencionheridos;

import disasters.caronte.CarontePlan;
import disasters.caronte.ontology.*;
import jadex.bdi.runtime.IGoal;

/**
 *
 * @author Juan Luis Molina Nogales
 */
public class EvaluarHeridoPlan extends CarontePlan{
	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		Object msg = enviarRespuestaObjeto("ack_atender_herido", "OK");
		boolean auxiliar = false;
		if(msg instanceof Herido){
			getBeliefbase().getBelief("herido_actual").setFact(((Herido)msg).getId());
			getBeliefbase().getBelief("numero_epa").setFact(1);
			getBeliefbase().getBelief("tipo_emergencia").setFact("herido");
			/*******************************************************************
			 * POR COMPLETAR
			 ******************************************************************/
			auxiliar = true;
		}else if(msg instanceof Incendio){
			getBeliefbase().getBelief("herido_actual").setFact(((Incendio)msg).getId());
			getBeliefbase().getBelief("numero_epa").setFact(1);
			getBeliefbase().getBelief("tipo_emergencia").setFact("incendio");
			/*******************************************************************
			 * POR COMPLETAR
			 ******************************************************************/
			auxiliar = true;
		}
		
		if(auxiliar){
			IGoal auxiliarHerido = createGoal("auxiliar_herido");
			dispatchSubgoalAndWait(auxiliarHerido);
		}
	}
}
