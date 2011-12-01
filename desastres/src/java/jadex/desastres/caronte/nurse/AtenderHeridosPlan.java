package jadex.desastres.caronte.nurse;

import jadex.desastres.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.me.*;

/**
 * Plan de ENFERMERO
 * 
 * @author Juan Luis Molina
 * 
 */
public class AtenderHeridosPlan extends EnviarMensajePlan{

	Environment env;

	/**
	 * Cuerpo del plan
	 */
	public void body(){
		// Obtenemos un objeto de la clase Environment para poder usar sus metodos
		env = (Environment)getBeliefbase().getBelief("env").getFact();

		Desastre recibido = (Desastre)enviarRespuestaObjeto("ack_aviso_geriatrico", "Aviso recibido");
		//env.printout("EE enfermero: Ack mandado", 0);

		// Posicion de la residencia que le corresponde
		Position posResi = (Position)getBeliefbase().getBelief("residencia").getFact();
		Position posicion = (Position)getBeliefbase().getBelief("pos").getFact();

		int idDes = recibido.getId();
		getBeliefbase().getBelief("idEmergencia").setFact(idDes);
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());

		try{
			env.andar(getComponentName(), posicion, positionDesastre, env.getAgent(getComponentName()).getId(), 0);
		}catch(Exception ex){
			System.out.println("Error al andar: " + ex);
		}

		env.printout("EE enfermero: Estoy atendiendo la emergencia: " + idDes, 0);

		People herido = getHerido(des);
		String desSize = des.getSize();

		// Heridos
		if(herido != null){
			try{
				int id = herido.getId();
				String herAux = Connection.connect(Environment.URL + "person/" + id);
				String sintomasAux = (new JSONArray(herAux)).getJSONObject(0).getString("sintomas");

				if(sintomasAux.equals("") || sintomasAux == null){
					System.out.println("EE enfermero: No tiene sintomas con los que diagnosticar");
				}else{
					ArrayList<String> sintomas = new ArrayList(Arrays.asList(sintomasAux.split(",", 0)));
					String listaSintomas = "";
					for(int i = 0; i < sintomas.size(); i++){
						listaSintomas += sintomas.get(i);
						if(i < sintomas.size() - 2){
							listaSintomas += ", ";
						}else if (i == sintomas.size() - 2){
							listaSintomas += " y ";
						}
					}
					env.printout("EE enfermero: Sintomas: " + listaSintomas, 0);
					// FREEBASE!!! *****************************************
					freebase(sintomas);
					//******************************************************
				}
			}catch(JSONException ex){
				System.out.println("Error al andar: " + ex);
			}

			/*if(desSize.equals("big") || desSize.equals("huge")){
				IGoal evacuarHeridos = createGoal("evacuarHeridos");
				dispatchSubgoalAndWait(evacuarHeridos);
			}*/

			if(herido.getType().equals("slight")){
				while((herido = des.getSlight()) != null){
					int id = herido.getId();
					try{
						Position miPos = (Position)getBeliefbase().getBelief("pos").getFact();
						String herAux = Connection.connect(Environment.URL + "person/" + id);
						JSONObject her = (new JSONArray(herAux)).getJSONObject(0);
						Position posHerido = new Position(new Double(her.getString("latitud")), new Double(her.getString("longitud")));
						env.andar(getComponentName(), miPos, posHerido, env.getAgent(getComponentName()).getId(), 0);
					}catch(Exception ex){
						System.out.println("Error al andar: " + ex);
					}
					env.printout("EE enfermero: quitando la asociacion del herido " + id, 0);
					herido.setAtendido(true);
					String resultado1 = Connection.connect(Environment.URL + "put/" + id + "/idAssigned/0");
					des.setSlight();

					env.printout("EE enfermero: curando herido " + id, 0);
					//String resultado = Connection.connect(Environment.URL + "delete/id/" + id);
					String resultado = Connection.connect(Environment.URL + "healthy/id/" + id);
				}

				if(des.getType().equals("injuredPerson")){
					env.printout("EE enfermero: Eliminado la emergencia " + idDes, 0);
					String resultado = Connection.connect(Environment.URL + "delete/id/" + idDes);
					getBeliefbase().getBelief("material").setFact(false);

					env.printout("EE enfermero: informando al coordinador de que he terminado...", 0);
					String recibido2 = enviarMensaje("coordinadorEmergencias", "terminado_geriatrico", "Fin");
				}
			}else{
				env.printout("EE enfermero: herido " + herido.getId() + " atendido por ambulancia", 0);
			}
		}else{
			env.printout("EE enfermero: emergencia sin heridos...", 0);
			if(des.getType().equals("injuredPerson")){
				env.printout("EE enfermero: Eliminado la emergencia " + idDes, 0);
				String resultado = Connection.connect(Environment.URL + "delete/id/" + idDes);
				getBeliefbase().getBelief("material").setFact(false);

				env.printout("EE enfermero: informando al coordinador de que he terminado...", 0);
				String recibido2 = enviarMensaje("coordinadorEmergencias", "terminado_geriatrico", "Fin");
			}
		}

		String recibido3 = esperarYEnviarRespuesta("fin_emergencia", "Fin recibido");

		if(!des.getType().equals("injuredPerson") && (desSize.equals("big") || desSize.equals("huge"))){
			// Vuelve a su posicion de la residencia
			try{
				env.printout("EE enfermero: vuelvo a la residencia", 0);
				env.andar(getComponentName(), (Position)getBeliefbase().getBelief("pos").getFact(), posResi, env.getAgent(getComponentName()).getId(), 0);
			}catch (Exception ex){
				System.out.println("Error al andar: " + ex);
			}
		}

		env.printout("EE enfermero: Solicitar reposicion de material al auxiliar...",0);
		String resultado2 = enviarMensaje("auxiliar", "reponer_material", "medico");
                
		//IGoal reponerMaterial = createGoal("reponerMaterial");
		//dispatchSubgoalAndWait(reponerMaterial);
	}

	/**
	 *
	 * @param des Desastre
	 * @return Herido
	 */
	private People getHerido(Disaster des){
		People herido = null;

		if(des.getSlight() != null){
			herido = des.getSlight();
		}
		if(des.getSerious() != null){
			herido = des.getSerious();
		}
		if(des.getDead() != null){
			herido = des.getDead();
		}

		return herido;
	}

	/**
	 *
	 * @param sintomas Sintomas
	 */
	private void freebase(ArrayList<String> sintomas){
		String queryText = "[{\"/common/topic/article\":{\"guid\":null,\"limit\":1,\"optional\":true},"
				+ "\"/common/topic/image\":{\"id\":null,\"limit\":1,\"optional\":true},"
				+ "\"FBID117:risk_factors\":[{\"id\":\"/en/old_age\",\"name\":null,\"type\":\"/medicine/risk_factor\"}]";
		if(!sintomas.isEmpty()){
			for(int i = 0; i < sintomas.size(); i++){
				queryText += ",\"FBID17" + i + ":symptoms\":[{\"id\":\"/en/" + sintomas.get(i) + "\",\"name\":null,\"type\":\"/medicine/symptom\"}]";
			}
		}
		queryText += ",\"id\":null,\"ENFERMEDAD:name\":null,"
				//+ "\"risk_factors\":[{\"id\":null,\"index\":null,\"limit\":6,\"name\":null,\"optional\":true,\"sort\":\"index\",\"type\":\"/medicine/risk_factor\"}],"
				//+ "\"s0:type\":[{\"id\":\"/medicine/disease\",\"link\":[{\"timestamp\":[{\"optional\":true,\"type\":\"/type/datetime\",\"value\":null}],\"type\":\"/type/link\"}],\"type\":\"/type/type\"}],"
				//+ "\"sort\":\"s0:type.link.timestamp.value\",\"symptoms\":[{\"id\":null,\"index\":null,\"limit\":6,\"name\":null,\"optional\":true,\"sort\":\"index\",\"type\":\"/medicine/symptom\"}],"
				+ "\"treatments\":[{\"id\":null,\"index\":null,\"TRATAMIENTO:name\":null,\"optional\":true,\"sort\":\"index\",\"type\":\"/medicine/medical_treatment\"}],"
				+ "\"type\":\"/medicine/disease\"}]";

		try{
			URL url = new URL("http://api.freebase.com/api/service/mqlread?query={\"query\":" + queryText + "}");
			URLConnection urlCon = url.openConnection();
			InputStreamReader inStream = new InputStreamReader(urlCon.getInputStream());
			BufferedReader buffer = new BufferedReader(inStream);

			String texto1 = "";
			String nextLine;
			while((nextLine = buffer.readLine()) != null){
				texto1 += nextLine;
			}

			String texto2[] = texto1.split("\"ENFERMEDAD:name\": ", -1);
			String enfermedades[] = new String[texto2.length - 1];
			String listaEnfermedades = "";
			for(int i = 1; i < texto2.length; i++){ // desecho el primero
				ArrayList<String> tratamientos = new ArrayList();
				enfermedades[i - 1] = texto2[i].substring(1, texto2[i].indexOf("\"", 1)); // el 0 es ", asi que mira desde el 1
				listaEnfermedades += enfermedades[i - 1];

				/*System.out.print(enfermedades[i - 1] + ": ");
				String texto3[] = texto2[i].split("\"TRATAMIENTO:name\": ", -1);
				for (int j = 1; j < texto3.length; j++) {
					tratamientos.add(texto3[j].substring(1, texto3[j].indexOf("\"", 1)));
					System.out.print(texto3[j].substring(1, texto3[j].indexOf("\"", 1)) + ", ");
				}
				System.out.println("");*/

				if(i < texto2.length - 2){
					listaEnfermedades += ", ";
				}else if(i == texto2.length - 2){
					listaEnfermedades += " o ";
				}

			}
			env.printout("EE enfermero: Posibles enfermedades: " + listaEnfermedades, 0);
		}catch (Exception ex){
			System.out.println("Excepcion en AtenderEmergenciaPlan.freebase(): " + ex);
		}
	}
}