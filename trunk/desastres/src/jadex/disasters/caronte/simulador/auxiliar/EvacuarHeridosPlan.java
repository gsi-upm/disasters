package disasters.caronte.simulador.auxiliar;

import disasters.*;
import org.json.me.*;

/**
 *
 * @author Lorena Lopez Lebon
 */
public class EvacuarHeridosPlan extends EnviarMensajePlan{

	public void body(){
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();
		int idDes = (Integer) getBeliefbase().getBelief("idEmergencia").getFact();
		Disaster des = env.getEvent(idDes);
		double dif = 0.0006;

		System.out.println("XX auxiliar: evacuando a los heridos");
		try{
			String sanosAux = Connection.connect(Environment.URL + "healthy");
			getBeliefbase().getBelief("sanosEvacuacion").setFact(sanosAux);

			String heridosAux = Connection.connect(Environment.URL + "unhealthy");
			JSONArray heridos = new JSONArray(heridosAux);

			for(int i = heridos.length() - 1; i >= 0; i--){ // TEMPORAL!!
				JSONObject herido = heridos.getJSONObject(i);
				int id = herido.getInt("id");

				Position posHerido1 = new Position(new Double(herido.getString("latitud")), new Double(herido.getString("longitud")));
				Position posHerido2 = new Position(new Double(herido.getString("latitud")), new Double(herido.getString("longitud")) - dif);
				Position posAnt = (Position)getBeliefbase().getBelief("pos").getFact();

				try{
					env.andar(getComponentName(), posAnt, posHerido1, env.getAgent(getComponentName()).getId(), 0);
				}catch (InterruptedException ex){
					System.out.println("Error al andar: " + ex);
				}

				env.printout("XX auxiliar: evacuando al herido " + id, 0);

				try{
					env.andar(getComponentName(), posHerido1, posHerido2, env.getAgent(getComponentName()).getId(), id);
				}catch(InterruptedException ex){
					System.out.println("Error al andar: " + ex);
				}
			}
		}catch(Exception ex){
			System.out.println("Excepcion: " + ex);
		}
	}
}