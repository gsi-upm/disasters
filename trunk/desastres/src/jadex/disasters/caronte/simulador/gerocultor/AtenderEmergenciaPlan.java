package disasters.caronte.simulador.gerocultor;

import disasters.*;
import disasters.caronte.Entorno;
import jadex.bdi.runtime.IGoal;

/**
 * Plan de GEROCULTOR
 * 
 * @author Juan Luis Molina
 * 
 */
public class AtenderEmergenciaPlan extends EnviarMensajePlan{

	/**
	 * Cuerpo del plan
	 */
	public void body() {
		// Obtenemos un objeto de la clase Entorno para poder usar sus metodos
		Entorno env = (Entorno)getBeliefbase().getBelief("env").getFact();
		Position posicion = (Position)getBeliefbase().getBelief("pos").getFact();

		Desastre recibido = (Desastre)enviarRespuestaObjeto("ack_aviso_geriatrico", "Aviso recibido");
		//env.printout("GG gerocultor: Ack mandado", 0);

		int idDes = recibido.getId();
		getBeliefbase().getBelief("idEmergencia").setFact(idDes);
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());

		try{
			env.andar(getComponentName(), posicion, positionDesastre, env.getAgent(getComponentName()).getId(), 0);
		}catch(Exception ex) {
			System.out.println("Error al andar: " + ex);
		}

		env.printout("GG gerocultor: Estoy atendiendo la emergencia: " + idDes, 2, 0);

		if(!des.getType().equals("injuredPerson")){
			if(des.getSize().equals("small") || des.getSize().equals("medium")){
				String resultado = Connection.connect(Entorno.URL + "delete/id/" + idDes);
				env.printout("GG gerocultor: Eliminado el desastre " + idDes, 2, 0);

				waitFor(2000);

				String respuesta = enviarMensaje("coordinadorEmergencias", "terminado_geriatrico", "done");
				//env.printout("GG gerocultor: Respuesta recibida del coordinador: " + respuesta, 0);

				String recibido2 = esperarYEnviarRespuesta("fin_emergencia", "Fin recibido");
			}else{
				env.printout("GG gerocultor: emergencia atendida por bomberos", 2, 0);

				IGoal evacuarResidencia = createGoal("evacuarResidencia");
				dispatchSubgoalAndWait(evacuarResidencia);
			}
		}else{
			env.printout("GG gerocultor: emergencia de caracter medico", 2, 0);
			String recibido2 = esperarYEnviarRespuesta("fin_emergencia", "Fin recibido");
		}

		env.printout("GG gerocultor: Solicitar reposicion de material al auxiliar...", 2, 0);
		String resultado2 = enviarMensaje("auxiliar", "reponer_material", "incendio");
                
		//IGoal reponerMaterial = createGoal("reponerMaterial");
		//dispatchSubgoalAndWait(reponerMaterial);
	}
}