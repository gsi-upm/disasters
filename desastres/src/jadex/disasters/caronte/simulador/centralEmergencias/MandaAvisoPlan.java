package disasters.caronte.simulador.centralEmergencias;

import disasters.*;
import disasters.caronte.Entorno;
import jadex.bdi.runtime.IGoal;

/**
 * Plan de la central para avisar al resto de los agentes.
 *
 * @author Juan Luis Molina
 *
 */
public class MandaAvisoPlan extends EnviarMensajePlan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		// Obtenemos un objeto de la clase entorno para poder usar sus metodos.
		Entorno env = (Entorno)getBeliefbase().getBelief("env").getFact();

		Desastre recibido = (Desastre)enviarRespuestaObjeto("ack_aviso_geriatrico", "Aviso recibido");
		//env.printout("CC central: Ack mandado",0);
		
		Integer idDes = recibido.getId();
		Disaster des = env.getEvent(idDes);

		getBeliefbase().getBelief("idEmergencia").setFact(idDes);

		env.printout("CC central: Avisando a agentes... (en espera)...", 2, 0, true);

		String resultado1 = enviarObjeto("ambulance", "aviso", recibido, true);
		//env.printout("CC central: Respuesta recibida de la ambulancia: " + resultado1, 0);

		if(!des.getType().equals("injuredPerson") && (des.getSize().equals("big") || des.getSize().equals("huge"))){
			String resultado2 = enviarObjeto("police", "aviso", recibido, true);
			//env.printout("CC central: Respuesta recibida de la policia: " + resultado2, 0);
			String resultado3 = enviarObjeto("firemen", "aviso", recibido, true);
			//env.printout("CC central: Respuesta recibida del bombero: " + resultado3, 0);
		}

		env.printout("CC central: Agentes avisados!!", 2, 0, true);

		IGoal esperaSolucion = createGoal("esperaSolucion");
		dispatchSubgoalAndWait(esperaSolucion);
	}
}