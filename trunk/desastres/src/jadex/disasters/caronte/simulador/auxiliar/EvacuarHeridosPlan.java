package disasters.caronte.simulador.auxiliar;

import disasters.*;
import disasters.caronte.Entorno;
import org.json.me.*;

/**
 * Plan de AUXILIAR.
 * 
 * @author Lorena L&oacute;pez Leb&oacute;n
 */
public class EvacuarHeridosPlan extends EnviarMensajePlan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		Entorno env = (Entorno)getBeliefbase().getBelief("env").getFact();
		int idDes = (Integer) getBeliefbase().getBelief("idEmergencia").getFact();
		Disaster des = env.getEvent(idDes);
		double dif = 0.0006;

		System.out.println("XX auxiliar: evacuando a los heridos");
		try{
			String sanosAux = Connection.connect(Entorno.URL + "healthy");
			getBeliefbase().getBelief("sanosEvacuacion").setFact(sanosAux);

			String heridosAux = Connection.connect(Entorno.URL + "unhealthy");
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

				env.printout("XX auxiliar: evacuando al herido " + id, 2, 0, true);

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