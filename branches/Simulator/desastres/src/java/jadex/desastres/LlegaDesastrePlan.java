package jadex.desastres;

import java.util.Iterator;
import java.util.Map;

import sun.security.acl.WorldGroupImpl;
import jadex.examples.hunterprey.WorldObjectData;
import jadex.runtime.Plan;

/**
 * Plan de la POLICIA
 * 
 * @author Nuria
 * 
 */
public class LlegaDesastrePlan extends Plan {
	
	private Environment env;
	
	/**
	 * Cuerpo del plan
	 */
	public void body(){
		
		//Obtenemos un objeto de la clase Environment para poder usar sus métodos
		env = (Environment)getBeliefbase().getBelief("env").getFact();
		//Obtengo mi posición
		Position oldPos = (Position)getBeliefbase().getBelief("pos").getFact();
		
		
		
		//id del Desastre atendiendose
		//int idDes = (Integer)getBeliefbase().getBelief("desastreActual").getFact();
		int idDes = env.getTablon();
		
		//Espero a que se borre el desastre (lo borra el bombero) para irme a otra cosa...
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());
		System.out.println("++ police: Estoy destinado al desastre "+idDes);
		try {
			env.andar(getAgentName(), oldPos, positionDesastre,env.getAgent(getAgentName()).getId(),0);
			
			
		} catch (Exception e) {
			System.out.println("++ police: Error metodo andar: "+e);
		}
		while(des!=null){
			//System.out.println("++ police: No puedo marcharme hasta que se solucione este jaleo, espero un poco más...");
			//waitFor(2000);
			des = env.getEvent(idDes);
			
			
		}
		System.out.println("++ police: Desastre solucionado: "+idDes);
		
		
			
	}
					
		
}
