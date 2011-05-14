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
		String recibido = enviarRespuesta("ack_aviso_geriatrico", "Aviso recibido");
		Environment.printout("GG gerocultor: Ack mandado", 0);

		// Obtenemos un objeto de la clase Environment para poder usar sus metodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

		// Posicion de la residencia que le corresponde
		//WorldObject agente = (WorldObject) getBeliefbase().getBelief("agente").getFact();
		Position posicion = (Position) getBeliefbase().getBelief("pos").getFact();
		
		int idDes =  new Integer(recibido);
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());

		Environment.printout("GG gerocultor: Estoy atendiendo la emergencia: " + idDes, 0);

		//waitFor(1000);
		String respuesta = enviarMensaje("coordinadorEmergencias", "estadoEmergencia", des.getSize());
		Environment.printout("GG gerocultor: Respuesta recibida del coordinador: " + respuesta, 0);
                
		// Emergencia
		if (des.getSize().equals("small")) {
			String resultado = Connection.connect(Environment.URL + "delete/id/" + idDes);
			getBeliefbase().getBelief("material").setFact(false);
			Environment.printout("GG gerocultor: Eliminado el desastre " + idDes, 0);

			waitFor(2000);

			respuesta = enviarMensaje("coordinadorEmergencias", "terminado_geriatrico", "done");
			Environment.printout("GG gerocultor: Respuesta recibida del coordinador: " + respuesta, 0);

			String recibido2 = esperarYEnviarRespuesta("fin_emergencia", "Fin recibido");
		} else {
			Environment.printout("GG gerocultor: emergencia atendida por bomberos", 0);

			IGoal evacuarResidencia = createGoal("evacuarResidencia");
			dispatchSubgoalAndWait(evacuarResidencia);
		}

		IGoal reponerMaterial = createGoal("reponerMaterial");
		dispatchSubgoalAndWait(reponerMaterial);
	}
}