package jadex.desastres.caronte.nurse;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan de ENFERMERO
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

		// Posicion de la residencia que le corresponde
		WorldObject agente = (WorldObject)getBeliefbase().getBelief("agente").getFact();
		Position posicion = agente.getPosition();

		enviarRespuesta("ack_aviso_geriatrico", "Aviso recibido");
		System.out.println(">> enfermero: Ack mandado");

		int idDes = env.getTablon();
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());

		System.out.println(">> enfermero: Estoy atendiendo la emergencia: " + idDes);

		People herido = null;

		if (des.getSlight() != null) {
			herido = des.getSlight();
		}
		if (des.getSerious() != null) {
			herido = des.getSerious();
		}
		if (des.getDead() != null) {
			herido = des.getDead();
		}

		// Heridos
		if(herido != null){
			int id = herido.getId();
			if(herido.getType().equals("slight")){
				System.out.println(">> enfermero: quitando la asociacion del herido " + id);
				getBeliefbase().getBelief("material").setFact(false);
				des.setSlight();
				System.out.println(">> enfermero: eliminando herido " + id);
				String resultado = Connection.connect(Environment.URL + "delete/id/" + id);

			}else{
				System.out.println(">> enfermero: herido atendido por ambulancia");
			}
		}else{
			System.out.println(">> enfermero: emergencia sin heridos");
		}

		// Emergencia
		if(des.getSize().equals("small")){
			String resultado = Connection.connect(Environment.URL + "delete/id/" + idDes);
			getBeliefbase().getBelief("material").setFact(false);
			System.out.println(">> enfermero: Eliminado el desastre " + idDes);
			
			String respuesta = enviarMensaje("coordinadorEmergencias","terminado","done");
			System.out.println(">> enfermero: Respuesta recibida del coordinador: " + respuesta);
		}else{
			System.out.println(">> enfermero: emergencia atendida por bomberos");

			IGoal evacuarResidencia = createGoal("evacuarResidencia");
			dispatchSubgoalAndWait(evacuarResidencia);
		}

		IGoal reponerMaterial = createGoal("reponerMaterial");
		dispatchSubgoalAndWait(reponerMaterial);
	}
}