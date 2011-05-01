package jadex.desastres.caronte.police;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan del POLICIA para mover de forma aleatoria un agente.
 * @author Nuria Siguero y Juan Luis Molina
 */
public class PatrullaPlan extends Plan {

	/**
	 * Cuerpo del plan
	 * Falta mover el agente a esa posicion.
	 */
	public void body() {
		//Obtenemos un objeto de la clase entorno para poder usar sus metodos.
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();
		//Necesito mi posicion y la nueva posicion a la que andara si no hay desastre que atender

		//Position oldPos = (Position)getBeliefbase().getBelief("pos").getFact();
		WorldObject agente = (WorldObject)getBeliefbase().getBelief("agente").getFact();
		Position oldPos = agente.getPosition();

		//Creamos una nueva posicion aleatoria
		Position newPos = (Position)env.getRandomPosition("caronte");
		
		System.out.println("++ police: Estoy patrullando porque no hay desastres activos... ");
		try {
			env.go(getComponentName(), newPos);
			env.pinta(env.getAgent(getComponentName()).getId(), 0, newPos.getX(), newPos.getY());

		} catch (Exception e) {
			System.out.println("++ police: Error metodo andar: " + e);
		}
		waitFor(1000);
	}
}
