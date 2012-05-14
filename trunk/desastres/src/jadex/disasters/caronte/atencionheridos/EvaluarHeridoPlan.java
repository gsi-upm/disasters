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
		Object msg = enviarRespuestaObjeto("ack_atenderHerido", "OK");
		boolean auxiliar = false;
		if(msg instanceof Herido){
			getBeliefbase().getBelief("heridoActual").setFact(((Herido)msg).getId());
			getBeliefbase().getBelief("numEpa").setFact(2);
			getBeliefbase().getBelief("tipoEmergencia").setFact("herido");
			/*******************************************************************
			 * POR COMPLETAR
			 ******************************************************************/
			auxiliar = true;
		}else if(msg instanceof Incendio){
			getBeliefbase().getBelief("numEpa").setFact(2);
			getBeliefbase().getBelief("tipoEmergencia").setFact("incendio");
			/*******************************************************************
			 * POR COMPLETAR
			 ******************************************************************/
			auxiliar = true;
		}
		
		if(auxiliar){
			IGoal auxiliarHerido = createGoal("auxiliarHerido");
			dispatchSubgoalAndWait(auxiliarHerido);
		}
	}
}
