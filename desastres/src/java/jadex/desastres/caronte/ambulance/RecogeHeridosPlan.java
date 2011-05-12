package jadex.desastres.caronte.ambulance;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan de la AMBULANCIA para recoger heridos.
 *
 * @author Olimpia Hernandez y Juan Luis Molina
 */
public class RecogeHeridosPlan extends EnviarMensajePlan {

	/**
	 * Cuerpo del plan
	 */
	public void body() {
		// Obtenemos un objeto de la clase Environment para poder usar sus metodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

		// Posicion actual de la ambulancia, que le permite recoger al herido.
		//Position posicionActual = (Position) getBeliefbase().getBelief("pos").getFact();
		WorldObject agente = (WorldObject) getBeliefbase().getBelief("agente").getFact();
		Position posicionActual = agente.getPosition();

		// Posicion del hospital que le corresponde
		Position posicionHospital = (Position) getBeliefbase().getBelief("hospital").getFact();
		int idDes = env.getTablon();

		//Espero a que se borre el desastre (lo borra el bombero) para irme a otra cosa...
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());

		Environment.printout("AA ambulance: Estoy destinado al desastre: " + idDes,0);

		enviarRespuesta("ack_aviso", "Aviso recibido");
		Environment.printout("AA ambulance: Ack mandado",0);

		//sacamos el herido
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

		try {
			env.andar(getComponentName(), posicionActual, positionDesastre, env.getAgent(getComponentName()).getId(), 0);
		} catch (Exception e) {
			System.out.println("**ambulance: Error metodo andar: " + e);
		}

		int id = 0;
		if ((herido != null) && !herido.getType().equals("slight")) { // Leves atendidos por el enfermero de la residencia
			id = herido.getId();
			Environment.printout("AA ambulance: Tengo herido " + id,0);
			
			//deasociar los heridos del desastre
			Environment.printout("AA ambulance: quitando la asociacion del herido " + id,0);
			String resultado1 = Connection.connect(Environment.URL + "put/" + id + "/idAssigned/0");
			
			if (herido.getType().equals("slight")) {
				des.setSlight();
			} else if (herido.getType().equals("serious")) {
				des.setSerious();
			} else if (herido.getType().equals("dead")) {
				des.setDead();
			}
		} else {
			Environment.printout("AA ambulance: Desastre sin heridos",0);
		}

		// La ambulancia regresa a su hospital correspondiente.
		try {
			env.andar(getComponentName(), posicionActual, posicionHospital, env.getAgent(getComponentName()).getId(), id);
			if ((herido != null) && !herido.getType().equals("slight")) {
				//y deposita al herido
				Environment.printout("AA ambulance: depositando herido " + id,0);
				String resultado = Connection.connect(Environment.URL + "delete/id/" + id);
			} else {
				Environment.printout("AA ambulance: de vuelta en el hospital",0);
			}

		} catch (Exception e) {
			System.out.println("AA ambulance: Error metodo andar: " + e);
		}
	}
}
