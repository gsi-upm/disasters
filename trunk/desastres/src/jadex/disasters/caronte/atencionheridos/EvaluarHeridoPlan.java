package disasters.caronte.atencionheridos;

import disasters.caronte.CarontePlan;
import disasters.caronte.ontology.Herido;
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
		Herido her = (Herido) enviarRespuestaObjeto("ack_atenderHerido", "OK");
		getBeliefbase().getBelief("heridoActual").setFact(her.getId());
		getBeliefbase().getBelief("numEpa").setFact(2);
		
		/***********************************************************************
		 * POR COMPLETAR
		 **********************************************************************/
		
		IGoal auxiliarHerido = createGoal("auxiliarHerido");
		dispatchSubgoalAndWait(auxiliarHerido);
	}
}
