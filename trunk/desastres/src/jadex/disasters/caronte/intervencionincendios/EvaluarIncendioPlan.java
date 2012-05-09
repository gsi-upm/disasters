package disasters.caronte.intervencionincendios;

import disasters.caronte.CarontePlan;
import disasters.caronte.ontology.Incendio;
import jadex.bdi.runtime.IGoal;

/**
 *
 * @author Juan Luis Molina Nogales
 */
public class EvaluarIncendioPlan extends CarontePlan{
	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		System.out.println("Evaluando incendio");
		Incendio inc = (Incendio) enviarRespuestaObjeto("ack_atenderIncendio", "OK");
		getBeliefbase().getBelief("incendioActual").setFact(inc.getId());
		getBeliefbase().getBelief("numEpi").setFact(2);
		
		/***********************************************************************
		 * POR COMPLETAR
		 **********************************************************************/
		
		IGoal apagarIncendio = createGoal("apagarIncendio");
		dispatchSubgoalAndWait(apagarIncendio);
	}
}
