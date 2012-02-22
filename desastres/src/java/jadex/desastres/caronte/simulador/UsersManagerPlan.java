package jadex.desastres.caronte.simulador;

import jadex.bdi.runtime.*;
import jadex.bridge.IComponentIdentifier;
import jadex.desastres.*;
import java.sql.Timestamp;
import java.util.Date;
import org.json.me.*;

/**
 *
 * @author Juan Luis Molina
 */
public class UsersManagerPlan extends Plan{

	public void body(){
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();
		boolean primera = (Boolean)getBeliefbase().getBelief("primera").getFact();
		String ahora = (String)getBeliefbase().getBelief("ahora").getFact();

		try{
			String logueados;
			if(primera){
				logueados = Connection.connect(Environment.URL + "users");
				getBeliefbase().getBelief("primera").setFact(false);
			}else{
				logueados = Connection.connect(Environment.URL + "users/modified/" + ahora);
			}

			ahora = new Timestamp(new Date().getTime()).toString();
			getBeliefbase().getBelief("ahora").setFact(ahora);

			JSONArray usuarios = new JSONArray(logueados);
			for(int i = 0; i < usuarios.length(); i++){
				JSONObject instancia = usuarios.getJSONObject(i);
				String nombre = instancia.getString("name");
				IComponentIdentifier id = env.getListado(nombre);
				if(instancia.getString("state").equals("active") && !env.containsListado(nombre)){
					env.printout("- ENV: New user " + instancia.getString("name"), 5);
					String tipoUsuario = instancia.getString("type");
					env.putListado(nombre,null); // se vuelve a llamar por el agente para introducir el id

					IGoal sp = createGoal("cms_create_component");
					if(!tipoUsuario.equals("citizen")){
						sp.getParameter("type").setValue("jadex/desastres/caronte/simulador/" + tipoUsuario + "/" + tipoUsuario + ".agent.xml");
					}else{
						sp.getParameter("type").setValue("jadex/desastres/caronte/simulador/usuarios/ciudadano.agent.xml");
					}
					sp.getParameter("name").setValue(nombre);
					dispatchSubgoalAndWait(sp);
				}else if (instancia.getString("state").equals("erased") && env.containsListado(nombre)){
					env.printout("- ENV: User " + instancia.getString("name") + " has logged out", 5);

					IGoal sp = createGoal("cms_destroy_component");
					sp.getParameter("componentidentifier").setValue(id);
					dispatchSubgoalAndWait(sp);
					
					env.removeListado(nombre);
				}
			}
		}catch (JSONException ex){
			System.out.println("Excepcion: " + ex);
		}
		waitFor(5000);
	}
}