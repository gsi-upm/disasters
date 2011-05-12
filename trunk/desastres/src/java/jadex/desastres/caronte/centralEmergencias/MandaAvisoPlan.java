package jadex.desastres.caronte.centralEmergencias;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan de la central para avisar al resto de los agentes.
 *
 * @author Juan Luis Molina
 *
 */
public class MandaAvisoPlan extends EnviarMensajePlan {

	/**
	 * Cuerpo del plan.
	 */
	public void body() {
		// Obtenemos un objeto de la clase entorno para poder usar sus metodos.
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

		int idDes = env.getTablon();
		Disaster des = env.getEvent(idDes);

		getBeliefbase().getBelief("desastreActual").setFact(des.getId());

		Environment.printout("CC central: Avisando a agentes... (en espera)...", 0);

		String resultado1 = enviarMensaje("ambulanceCaronte", "aviso", "go");
		Environment.printout("CC central: Respuesta recibida de la ambulancia: " + resultado1, 0);

		if (des.getSize().equals("small") == false) {
			String resultado2 = enviarMensaje("policeCaronte", "aviso", "go");
			Environment.printout("CC central: Respuesta recibida de la policia: " + resultado2, 0);
			String resultado3 = enviarMensaje("firemenCaronte", "aviso", "go");
			Environment.printout("CC central: Respuesta recibida del bombero: " + resultado3, 0);
		}

		Environment.printout("CC central: Agentes avisados!!", 0);

		IGoal esperaSolucion = createGoal("esperaSolucion");
		dispatchSubgoalAndWait(esperaSolucion);
	}
}
