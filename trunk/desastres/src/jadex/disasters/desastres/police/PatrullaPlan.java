package disasters.desastres.police;

import disasters.*;
import jadex.bdi.runtime.Plan;

/**
 * Plan del POLICIA para mover de forma aleatoria un agente.
 * @author Nuria Siguero y Juan Luis Molina
 */
public class PatrullaPlan extends Plan{

	/**
	 * Cuerpo del plan
	 * Falta mover el agente a esa posicion.
	 */
	public void body(){
		//Obtenemos un objeto de la clase entorno para poder usar sus metodos.
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();

		//Creamos una nueva posicion aleatoria
		Position newPos = (Position)env.getRandomPosition("disasters");
		
		System.out.println("++ police: Estoy patrullando porque no hay desastres activos... ");
		try{
			env.go(getComponentName(), newPos);
			env.pinta(env.getAgent(getComponentName()).getId(), 0, newPos.getX(), newPos.getY());
		}catch(Exception e){
			System.out.println("++ police: Error metodo andar: " + e);
		}
		
		waitFor(1000);
	}
}