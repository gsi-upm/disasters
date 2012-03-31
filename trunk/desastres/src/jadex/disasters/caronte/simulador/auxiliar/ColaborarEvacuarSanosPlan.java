package disasters.caronte.simulador.auxiliar;

import disasters.*;
import disasters.caronte.Entorno;
import org.json.me.*;

/**
 *
 * @author Lorena Lopez Lebon
 */
public class ColaborarEvacuarSanosPlan extends EnviarMensajePlan{

	Entorno env;

	public void body(){
		env = (Entorno)getBeliefbase().getBelief("env").getFact();
		Position posResi = (Position)getBeliefbase().getBelief("residencia").getFact();

		int idDes = (Integer)getBeliefbase().getBelief("idEmergencia").getFact();
		Disaster des = env.getEvent(idDes);
		double dif = 0.0006;

		env.printout("XX auxiliar: evacuando la residencia", 2, 0);

		try{
			//String sanosAux = Connection.connect(Entorno.URL + "healthy");
			String sanosAux = (String) getBeliefbase().getBelief("sanosEvacuacion").getFact();
			JSONArray sanos = new JSONArray(sanosAux);

			int numeroEvacuadosSanos = Math.round(2*sanos.length()/3);
			env.printout("XX auxiliar: de " + sanos.length() + " residentes sanos me encargo de evacuar a " + (sanos.length()-numeroEvacuadosSanos), 2, 0);
			// del resto de sanos se ocupa el gerocultor

			for(int i = numeroEvacuadosSanos; i < sanos.length(); i++){
				JSONObject sano = sanos.getJSONObject(i);
				evacuar(sano);
			}

			env.printout("XX auxiliar: todos los residentes a cargo del auxiliar evacuados!!", 2, 0);
			getBeliefbase().getBelief("sanosEvacuacion").setFact("-");
		}catch(Exception ex){
			System.out.println("Excepcion en EvacuarResidenciaPlan: " + ex);
		}
	}

	/**
	 *
	 * @param persona Persona a evacuar
	 */
	private void evacuar(JSONObject persona){
		double dif = 0.0006;
		try{
			int id = persona.getInt("id");
			Position posSano1 = new Position(new Double(persona.getString("latitud")), new Double(persona.getString("longitud")));
			Position posSano2 = new Position(new Double(persona.getString("latitud")), new Double(persona.getString("longitud")) - dif);
			Position posAnt = (Position) getBeliefbase().getBelief("pos").getFact();
			env.andar(getComponentName(), posAnt, posSano1, env.getAgent(getComponentName()).getId(), 0);
			env.printout("XX auxiliar: evacuando al anciano " + id, 2, 0);
			env.andar(getComponentName(), posSano1, posSano2, env.getAgent(getComponentName()).getId(), id);
		}catch (Exception ex){
			System.out.println("Excepcion en evacuar: " + ex);
		}
	}
}