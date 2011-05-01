package jadex.desastres.disasters.police;

import jadex.bdi.runtime.Plan;
import jadex.desastres.Disaster;
import jadex.desastres.EnviarMensajePlan;
import jadex.desastres.Environment;
import jadex.desastres.Position;
import jadex.desastres.WorldObject;

/**
 * Plan de la POLICIA
 * 
 * @author Nuria y Juan Luis Molina
 * 
 */
public class LlegaDesastrePlan extends EnviarMensajePlan {

	private Environment env;

	/**
	 * Cuerpo del plan
	 */
	public void body() {

		//Obtenemos un objeto de la clase Environment para poder usar sus metodos
		env = (Environment) getBeliefbase().getBelief("env").getFact();
		//Obtengo mi posicion
		//Position oldPos = (Position) getBeliefbase().getBelief("pos").getFact();
		WorldObject agente = (WorldObject)getBeliefbase().getBelief("agente").getFact();

		Position posicionComisaria = (Position) getBeliefbase().getBelief("Comisaria").getFact();

		Position oldPos = agente.getPosition();
		
		//id del Desastre atendiendose
		//int idDes = (Integer)getBeliefbase().getBelief("desastreActual").getFact();
		int idDes = env.getTablon();

		enviarRespuesta("ack_aviso", "Aviso recibido");
		System.out.println("++ police: Ack mandado");

		//Espero a que se borre el desastre (lo borra el bombero) para irme a otra cosa...
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());
		System.out.println("++ police: Estoy destinado al desastre " + idDes);

		try {
			env.andar(getComponentName(), oldPos, positionDesastre, env.getAgent(getComponentName()).getId(), 0);
		} catch (Exception e) {
			System.out.println("++ police: Error metodo andar: " + e);
		}
		while (des != null) {
			//System.out.println("++ police: No puedo marcharme hasta que se solucione este jaleo, espero un poco mas...");
			//waitFor(2000);
			des = env.getEvent(idDes);
		}

		System.out.println("++ police: Desastre solucionado: " + idDes);
		System.out.println("++ police: Vuelvo a la comisaria");
		
		try {
			env.andar(getComponentName(), oldPos, posicionComisaria, env.getAgent(getComponentName()).getId(), 0);
		} catch (Exception e) {
			System.out.println("++ police: Error metodo andar: " + e);
		}
		System.out.println("++ police: En la comisaria");
	}
}
