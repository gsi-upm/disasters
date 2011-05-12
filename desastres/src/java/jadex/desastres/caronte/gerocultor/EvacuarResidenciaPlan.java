package jadex.desastres.caronte.gerocultor;

import jadex.bdi.runtime.*;
import jadex.desastres.*;
import org.json.me.*;

/**
 * Plan de ENFERMERO
 *
 * @author Lorena Lopez Lebon y Juan Luis Molina
 */
public class EvacuarResidenciaPlan extends EnviarMensajePlan {

	public void body() {
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();
		Position posResi = (Position) getBeliefbase().getBelief("residencia").getFact();
		int idDes = env.getTablon();
		Disaster des = env.getEvent(idDes);

		Environment.printout("GG gerocultor: evacuando la residencia", 0);
		try {
			String sanosAux = Connection.connect(Environment.URL + "healthy");
			JSONArray sanos = new JSONArray(sanosAux);
			double dif = 0.0006;

			for (int i = 0; i < sanos.length(); i++) {
				JSONObject sano = sanos.getJSONObject(i);
				int id = sano.getInt("id");
				Position posSano1 = new Position(new Double(sano.getString("latitud")), new Double(sano.getString("longitud")));
				Position posSano2 = new Position(new Double(sano.getString("latitud")), new Double(sano.getString("longitud")) - dif);
				Position posSanoAnt;

				if (i == 0) {
					posSanoAnt = posResi;
				} else {
					JSONObject sanoAnt = sanos.getJSONObject(i - 1);
					posSanoAnt = new Position(new Double(sanoAnt.getString("latitud")), new Double(sanoAnt.getString("longitud")) - dif);
				}
				try {
					env.andar(getComponentName(), posSanoAnt, posSano1, env.getAgent(getComponentName()).getId(), 0);
				} catch (InterruptedException ex) {
					System.out.println("Error al andar");
				}

				Environment.printout("GG gerocultor: evacuando al anciano " + id, 0);

				try {
					env.andar(getComponentName(), posSano1, posSano2, env.getAgent(getComponentName()).getId(), id);
				} catch (InterruptedException ex) {
					System.out.println("Error al andar");
				}
			}

			// ENFERMERO???
			/*String sanosAux2 = Connection.connect(Environment.URL + "healthy");
			JSONArray sanos2 = new JSONArray(sanosAux2);
			if (sanos2.length() > sanos.length()) {
				Environment.printout("GG gerocultor: volviendo a por los curados", 0);
				for (int i = sanos.length(); i < sanos2.length(); i++) {
					sanos.put(i,sanos2.getJSONObject(i));
				}
			} else {
				Environment.printout("GG gerocultor: no hay mas", 0);
			}*/

			Environment.printout("GG gerocultor: todos los residentes evacuados!!", 0);

			while (des != null) {
				des = env.getEvent(idDes);
			}

			// La posicion de los residentes es la de la residencia, no se vuelve a llamar a la BD
			for (int i = 0; i < sanos.length(); i++) {
				JSONObject sano = sanos.getJSONObject(i);
				int id = sano.getInt("id");
				Position posSano1 = new Position(new Double(sano.getString("latitud")), new Double(sano.getString("longitud")) - dif);
				Position posSano2 = new Position(new Double(sano.getString("latitud")), new Double(sano.getString("longitud")));
				Position posSanoAnt;

				if (i == 0) {
					JSONObject sanoAnt = sanos.getJSONObject(sanos.length() - 1);
					posSanoAnt = new Position(new Double(sanoAnt.getString("latitud")), new Double(sanoAnt.getString("longitud")) - dif);
				} else {
					JSONObject sanoAnt = sanos.getJSONObject(i - 1);
					posSanoAnt = new Position(new Double(sanoAnt.getString("latitud")), new Double(sanoAnt.getString("longitud")));
				}
				try {
					env.andar(getComponentName(), posSanoAnt, posSano1, env.getAgent(getComponentName()).getId(), id);
				} catch (InterruptedException ex) {
					System.out.println("Error al andar");
				}

				Environment.printout("GG gerocultor: llevando de vuelta al anciano " + id, 0);

				try {
					env.andar(getComponentName(), posSano1, posSano2, env.getAgent(getComponentName()).getId(), id);
				} catch (InterruptedException ex) {
					System.out.println("Error al andar");
				}

				// Vuelve a su posicion de la residencia
				if (i == sanos.length() - 1) {
					try {
						env.andar(getComponentName(), posSano2, posResi, env.getAgent(getComponentName()).getId(), 0);
					} catch (InterruptedException ex) {
						System.out.println("Error al andar");
					}
				}
			}

			Environment.printout("GG gerocultor: todos los residentes de vuelta en la residencia!!", 0);

		} catch (JSONException ex) {
			System.out.println("Excepcion en EvacuarResidenciaPlan: " + ex);
		}
	}
}
