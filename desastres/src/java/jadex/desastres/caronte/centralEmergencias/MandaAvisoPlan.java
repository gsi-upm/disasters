package jadex.desastres.caronte.centralEmergencias;

import jadex.bdi.runtime.*;
import jadex.desastres.Disaster;
import jadex.desastres.EnviarMensajePlan;
import jadex.desastres.Environment;

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
		
		enviarRespuesta("ack_aviso_geriatrico", "Aviso recibido");
		System.out.println("$$ central: Ack mandado");
		
		// Obtenemos un objeto de la clase entorno para poder usar sus metodos.
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

		int idDes = env.getTablon();
		Disaster des = env.getEvent(idDes);

		getBeliefbase().getBelief("desastreActual").setFact(des.getId());

		System.out.println("$$ central: Avisando a agentes... (en espera)...");

		String resultado1 = enviarMensaje("ambulanceCaronte", "aviso", "go");
		System.out.println("$$ central: Respuesta recibida de ambulance: " + resultado1);

		if (des.getSize().equals("small") == false) {
			String resultado2 = enviarMensaje("policeCaronte", "aviso", "go");
			System.out.println("$$ central: Respuesta recibida de police: " + resultado2);
			String resultado3 = enviarMensaje("firemenCaronte", "aviso", "go");
			System.out.println("$$ central: Respuesta recibida de firemen: " + resultado3);
		}

		System.out.println("$$ central: Agentes avisados!!");

		IGoal esperaAviso = createGoal("esperaAviso");
		dispatchSubgoalAndWait(esperaAviso);
	}
}
