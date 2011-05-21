package jadex.desastres.caronte.gerocultor;

import jadex.bdi.runtime.*;
import jadex.desastres.*;
import java.util.*;

/**
 * Plan de GEROCULTOR
 * 
 * @author Juan Luis Molina
 * 
 */
public class AtenderEmergenciaPlan extends EnviarMensajePlan {

	/**
	 * Cuerpo del plan
	 */
	public void body() {
		// Obtenemos un objeto de la clase Environment para poder usar sus metodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

		String recibido = enviarRespuesta("ack_aviso_geriatrico", "Aviso recibido");
		env.printout("GG gerocultor: Ack mandado", 0);

		int idDes =  new Integer(recibido);
		getBeliefbase().getBelief("idEmergencia").setFact(idDes);
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());

		env.printout("GG gerocultor: Estoy atendiendo la emergencia: " + idDes, 0);
                
		// Emergencia
		if (des.getSize().equals("small") || des.getSize().equals("medium")) {
			String resultado = Connection.connect(Environment.URL + "delete/id/" + idDes);
			getBeliefbase().getBelief("material").setFact(false);
			env.printout("GG gerocultor: Eliminado el desastre " + idDes, 0);

			waitFor(2000);

			String respuesta = enviarMensaje("coordinadorEmergencias", "terminado_geriatrico", "done");
			env.printout("GG gerocultor: Respuesta recibida del coordinador: " + respuesta, 0);

			String recibido2 = esperarYEnviarRespuesta("fin_emergencia", "Fin recibido");
		} else {
			env.printout("GG gerocultor: emergencia atendida por bomberos", 0);

			IGoal evacuarResidencia = createGoal("evacuarResidencia");
			dispatchSubgoalAndWait(evacuarResidencia);
		}

		IGoal reponerMaterial = createGoal("reponerMaterial");
		dispatchSubgoalAndWait(reponerMaterial);
	}
}