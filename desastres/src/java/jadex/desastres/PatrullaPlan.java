package jadex.desastres;
import java.util.Iterator;
import java.util.Map;

import jadex.runtime.*;


/**
 * Plan del POLICIA para mover de forma aleatoria un agente.
 * @author Nuria Siguero
 *
 */
public class PatrullaPlan extends Plan{
		
	/**
	 * Cuerpo del plan
	 * Falta mover el agente a esa posición.
	 */
	public void body(){
		//System.out.println("++ police: Hello world!  :D ");
		//Obtenemos un objeto de la clase entorno para poder usar sus métodos.
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();
		//Necesito mi posición y la nueva posición a la que andaré si no hay desastre que atender
		
		Position oldPos = (Position)getBeliefbase().getBelief("pos").getFact();
		//Creamos una nueva posición aleatoria
		Position newPos = (Position)env.getRandomPosition();
		
		//Miramos si hay desastres
		//boolean hayDesastres = !env.disasters.isEmpty();

		//Mientras haya desastres que atender vamos a los desastres;
		//while (!hayDesastres){
			System.out.println("++ police: Estoy patrullando porque no hay desastres activos... ");
			try {
				//env.andar(getAgentName(), oldPos, newPos,env.getAgent(getAgentName()).getId(),0);
				env.go(getAgentName(), newPos);
				env.pinta(env.getAgent(getAgentName()).getId(), 0, newPos.getX(), newPos.getY());
				
			} catch (Exception e) {
				System.out.println("++ police: Error metodo andar: "+e);
			}
			
			
	}
}