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

		Desastre recibido = (Desastre)enviarRespuestaObjeto("ack_aviso_geriatrico", "Aviso recibido");
		env.printout("CC central: Ack mandado",0);
		
		Integer idDes = recibido.getId();
		Disaster des = env.getEvent(idDes);

		getBeliefbase().getBelief("idEmergencia").setFact(idDes);

		env.printout("CC central: Avisando a agentes... (en espera)...", 0);

		String resultado1 = enviarObjeto("ambulanceCaronte", "aviso", recibido);
		env.printout("CC central: Respuesta recibida de la ambulancia: " + resultado1, 0);

		if (!des.getType().equals("injuredPerson") && (des.getSize().equals("big") || des.getSize().equals("huge"))) {
			String resultado2 = enviarObjeto("policeCaronte", "aviso", recibido);
			env.printout("CC central: Respuesta recibida de la policia: " + resultado2, 0);
			String resultado3 = enviarObjeto("firemenCaronte", "aviso", recibido);
			env.printout("CC central: Respuesta recibida del bombero: " + resultado3, 0);
		}

		env.printout("CC central: Agentes avisados!!", 0);

		IGoal esperaSolucion = createGoal("esperaSolucion");
		dispatchSubgoalAndWait(esperaSolucion);
	}
}
