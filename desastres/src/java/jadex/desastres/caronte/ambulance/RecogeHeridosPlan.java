package jadex.desastres.caronte.ambulance;

import jadex.bdi.runtime.*;
import jadex.desastres.*;
import org.json.me.*;

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
		String recibido = enviarRespuesta("ack_aviso", "Aviso recibido");
		Environment.printout("AA ambulance: Ack mandado", 0);

		// Obtenemos un objeto de la clase Environment para poder usar sus metodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

		// Posicion actual de la ambulancia, que le permite recoger al herido.
		Position posicionActual = (Position) getBeliefbase().getBelief("pos").getFact();

		// Posicion del hospital que le corresponde
		Position posicionHospital = (Position) getBeliefbase().getBelief("hospital").getFact();

		int idDes = new Integer(recibido);

		Disaster des = env.getEvent(idDes);
		Position posicionDesastre = new Position(des.getLatitud(), des.getLongitud());

		Environment.printout("AA ambulance: Estoy destinado al desastre " + idDes, 0);

		//sacamos el herido
		People herido = getHerido(des);

		try {
			env.andar(getComponentName(), posicionActual, posicionDesastre, env.getAgent(getComponentName()).getId(), 0);
		} catch (Exception e) {
			System.out.println("AA ambulance: Error metodo andar: " + e);
		}

		int id = 0;
		if (herido != null) { // Leves atendidos por el enfermero de la residencia
			while ((herido = getHerido(des)) != null) {
				id = herido.getId();
				Environment.printout("AA ambulance: Tengo herido " + id, 0);

				//deasociar los heridos del desastre
				Environment.printout("AA ambulance: quitando la asociacion del herido " + id, 0);
				String resultado1 = Connection.connect(Environment.URL + "put/" + id + "/idAssigned/0");
				if (herido.getType().equals("slight")) {
					des.setSlight();
				} else if (herido.getType().equals("serious")) {
					des.setSerious();
				} else if (herido.getType().equals("dead")) {
					des.setDead();
				}

				try {
					String herAux = Connection.connect(Environment.URL + "person/" + id);
					JSONObject her = (new JSONArray(herAux)).getJSONObject(0);
					Position posHerido1 = new Position(new Double(her.getString("latitud")), new Double(her.getString("longitud")));
					Position posHerido2 = new Position(new Double(her.getString("latitud")), new Double(her.getString("longitud"))+0.0006);
					posicionActual = (Position) getBeliefbase().getBelief("pos").getFact();

					env.andar(getComponentName(), posicionActual, posHerido1, env.getAgent(getComponentName()).getId(), 0);
					env.andar(getComponentName(), posHerido1, posicionHospital, env.getAgent(getComponentName()).getId(), id);
					if(!herido.getType().equals("dead")){
						Environment.printout("AA ambulance: curando herido " + id, 0);
						String resultado = Connection.connect(Environment.URL + "healthy/id/" + id);
						Environment.printout("AA ambulance: llevando de vuelta a " + id + " a la residencia", 0);
						env.andar(getComponentName(), posicionHospital, posHerido2, env.getAgent(getComponentName()).getId(), id);
					}else{
						Environment.printout("AA ambulance: depositando muerto " + id, 0);
						String resultado = Connection.connect(Environment.URL + "delete/id/" + id);
					}
				} catch (Exception ex) {
					System.out.println("AA ambulance: Error: " + ex);
				}
			}
		} else {
			Environment.printout("AA ambulance: Desastre sin heridos", 0);
		}

		// La ambulancia regresa a su hospital correspondiente.
		try {
			env.andar(getComponentName(), posicionActual, posicionHospital, env.getAgent(getComponentName()).getId(), 0);
			Environment.printout("AA ambulance: de vuelta en el hospital", 0);
		} catch (Exception e) {
			System.out.println("AA ambulance: Error metodo andar: " + e);
		}
	}

	private People getHerido(Disaster des) {
		People herido = null;

		// No atiende leves porque le corresponde al enfermero
		if (des.getSerious() != null) {
			herido = des.getSerious();
		} else if (des.getDead() != null) {
			herido = des.getDead();
		}

		return herido;
	}
}
