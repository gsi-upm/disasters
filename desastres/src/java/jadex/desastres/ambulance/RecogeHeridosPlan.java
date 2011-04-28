package jadex.desastres.ambulance;

import jadex.bdi.runtime.Plan;
import jadex.desastres.Connection;
import jadex.desastres.Disaster;
import jadex.desastres.EnviarMensajePlan;
import jadex.desastres.Environment;
import jadex.desastres.People;
import jadex.desastres.Position;
import jadex.desastres.WorldObject;

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
		Position posicionHospital = (Position) getBeliefbase().getBelief("hospitalMadrid").getFact();
		int idDes = env.getTablon();

		//Espero a que se borre el desastre (lo borra el bombero) para irme a otra cosa...
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());

		enviarRespuesta("ack_aviso", "Aviso recibido");
		System.out.println("** ambulance: Ack mandado");

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

		if (herido != null) {
			System.out.println("** ambulance: Tengo herido " + herido.getId());
		} else {
			System.out.println("** ambulance: Desastre sin heridos");
		}

		try {
			env.andar(getComponentName(), posicionActual, positionDesastre, env.getAgent(getComponentName()).getId(), 0);
		} catch (Exception e) {
			System.out.println("**ambulance: Error metodo andar: " + e);
		}

		int id = 0;
		if (herido != null) {
			//deasociar los heridos del desastre
			id = herido.getId();
			System.out.println("** ambulance: quitando la asociacion del herido " + id);
			String resultado1 = Connection.connect(Environment.URL + "put/" + id + "/idAssigned/" + "0");

			if (herido.getType().equals("slight")) {
				des.setSlight();
			} else if (herido.getType().equals("serious")) {
				des.setSerious();
			} else if (herido.getType().equals("dead")) {
				des.setDead();
			}
		}

		// La ambulancia regresa a su hospital correspondiente.
		try {
			env.andar(getComponentName(), posicionActual, posicionHospital, env.getAgent(getComponentName()).getId(), id);
			if (herido != null) {
				//y deposita al herido
				System.out.println("** ambulance: depositando herido " + id);
				String resultado = Connection.connect(Environment.URL + "delete/id/" + id);
			} else {
				System.out.println("** ambulance: de vuelta en el hospital");
			}

		} catch (Exception e) {
			System.out.println("** ambulance: Error metodo andar: " + e);
		}
	}
}
