package jadex.desastres.caronte.nurse;

import jadex.bdi.runtime.*;
import jadex.desastres.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Plan de ENFERMERO
 * 
 * @author Juan Luis Molina
 * 
 */
public class AtenderHeridosPlan extends EnviarMensajePlan {

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
		Environment.printout("EE enfermero: Ack mandado",0);

		int idDes = env.getTablon();
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());

		Environment.printout("EE enfermero: Estoy atendiendo la emergencia: " + idDes, 0);

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
				freebase(); // FREEBASE!!!

				Environment.printout("EE enfermero: quitando la asociacion del herido " + id, 0);
				String resultado1 = Connection.connect(Environment.URL + "put/" + id + "/idAssigned/0");
				getBeliefbase().getBelief("material").setFact(false);

				Environment.printout("EE enfermero: curando herido " + id, 0);
				//String resultado = Connection.connect(Environment.URL + "delete/id/" + id);
				String resultado = Connection.connect(Environment.URL + "healthy/id/" + id);
				des.setSlight();
			}else{
				Environment.printout("EE enfermero: herido atendido por ambulancia", 0);
			}
		}else{
			Environment.printout("EE enfermero: emergencia sin heridos", 0);
		}

		IGoal reponerMaterial = createGoal("reponerMaterial");
		dispatchSubgoalAndWait(reponerMaterial);
	}


	// AQUI!!!
	// Annadir ArrayList como parametro para poner varios sintomas!
	private void freebase(){
		// Esto se pasaria de parametro
		ArrayList<String> sintomas = new ArrayList(Arrays.asList("fever","pain"));
		// fin

		String queryText = "[{\"/common/topic/article\":{\"guid\":null,\"limit\":1,\"optional\":true},"
				+ "\"/common/topic/image\":{\"id\":null,\"limit\":1,\"optional\":true},"
				+ "\"FBID117:risk_factors\":[{\"id\":\"/en/old_age\",\"name\":null,\"type\":\"/medicine/risk_factor\"}]";
		if(!sintomas.isEmpty()){
			for(int i=0; i<sintomas.size(); i++){
				queryText += ",\"FBID17" + i + ":symptoms\":[{\"id\":\"/en/" + sintomas.get(i) + "\",\"name\":null,\"type\":\"/medicine/symptom\"}]";
			}
		}
		queryText += ",\"id\":null,\"ENFERMEDAD:name\":null,"
				+ "\"risk_factors\":[{\"id\":null,\"index\":null,\"limit\":6,\"name\":null,\"optional\":true,\"sort\":\"index\",\"type\":\"/medicine/risk_factor\"}],"
				+ "\"s0:type\":[{\"id\":\"/medicine/disease\",\"link\":[{\"timestamp\":[{\"optional\":true,\"type\":\"/type/datetime\",\"value\":null}],\"type\":\"/type/link\"}],\"type\":\"/type/type\"}],"
				+ "\"sort\":\"s0:type.link.timestamp.value\",\"symptoms\":[{\"id\":null,\"index\":null,\"limit\":6,\"name\":null,\"optional\":true,\"sort\":\"index\",\"type\":\"/medicine/symptom\"}],"
				+ "\"treatments\":[{\"id\":null,\"index\":null,\"limit\":6,\"name\":null,\"optional\":true,\"sort\":\"index\",\"type\":\"/medicine/medical_treatment\"}],"
				+ "\"type\":\"/medicine/disease\"}]";

		try {
			URL url = new URL("http://api.freebase.com/api/service/mqlread?query={\"query\":" + queryText + "}");
			URLConnection urlCon = url.openConnection();
			InputStreamReader inStream = new InputStreamReader(urlCon.getInputStream());
            BufferedReader buffer= new BufferedReader(inStream);

			String texto1 = "";
			String nextLine;
			while((nextLine = buffer.readLine()) != null){
				texto1 += nextLine;
			}

			String texto2[] = texto1.split("\"ENFERMEDAD:name\": ",-1);
			String enfermedades[] = new String[texto2.length-1];
			for(int i=1; i<texto2.length; i++){ // desecho el primero
				enfermedades[i-1] = texto2[i].substring(1,texto2[i].indexOf("\"",1)); // el 0 es ", asi que mira desde el 1
				System.out.println(i + ": " + enfermedades[i-1]);
			}
		} catch (Exception ex) {
			System.out.println("Excepcion en AtenderEmergenciaPlan.freebase(): " + ex);
		}
	}
}