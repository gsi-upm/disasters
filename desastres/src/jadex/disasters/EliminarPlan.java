package disasters;

import disasters.caronte.Entorno;
import disasters.desastres.Environment;
import jadex.bdi.runtime.Plan;

/**
 *
 * @author Juan Luis Molina
 */
public class EliminarPlan extends Plan{
	public void body(){
		Object env = getBeliefbase().getBelief("env").getFact();
		String tipo = (String)getParameter("tipo").getValue();
		String clase = "";
		try{
			Class.forName("disasters.caronte.Entorno");
			clase = "Entorno"; // Solo llega aqui si Entorno existe
		}catch(ClassNotFoundException ex){}
		try{
			Class.forName("disasters.desastres.Environment");
			clase = "Environment"; // Solo llega aqui si Environment existe
		}catch(ClassNotFoundException ex){}
		
		if(clase.equals("Entorno")){ // env instanceof Entorno
			((Entorno)env).removeWorldObject(tipo, getComponentName());
		}else if(clase.equals("Environment")){ // env instanceof Environment
			((Environment)env).removeWorldObject(tipo, getComponentName());
		}
	}
}