package jadex.desastres.caronte.nurse;

import jadex.bdi.runtime.*;
import jadex.desastres.*;
import org.json.me.*;

/**
 * Plan de ENFERMERO
 *
 * @author Juan Luis Molina
 */
public class EvacuarResidenciaPlan extends EnviarMensajePlan {

	public void body() {
/*  |\
	| \
=====  \
=====  /  AQUI!!!
	| /
	*/
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();
		int idDes = env.getTablon();
		Disaster des = env.getEvent(idDes);

		System.out.println(">> enfermero: evacuando la residencia");
		try {
			String sanosAux = Connection.connect(Environment.URL + "healthy");
			JSONArray sanos = new JSONArray(sanosAux);

			for (int i = 0; i < sanos.length(); i++) {
				JSONObject instancia = sanos.getJSONObject(i);

				int id = instancia.getInt("id");
				double longitud = new Double(instancia.getString("longitud"));
				System.out.println(">> enfermero: evacuando al anciano " + id);
				String response = Connection.connect(Environment.URL + "put/" + id + "/" + "longitud/" + (longitud-0.0006));
			}

			while (des != null) {
				des = env.getEvent(idDes);
			}

			for (int i = 0; i < sanos.length(); i++) {
				JSONObject instancia = sanos.getJSONObject(i);

				int id = instancia.getInt("id");
				double longitud = new Double(instancia.getString("longitud"));
				System.out.println(">> enfermero: llevando de vuelta al anciano " + id);
				// longitud inicial, no se vuelve a llama a la BD
				String response = Connection.connect(Environment.URL + "put/" + id + "/" + "longitud/" + longitud);
			}
			
		} catch (JSONException ex) {
			System.out.println("Excepcion: " + ex);
		}
	}

}