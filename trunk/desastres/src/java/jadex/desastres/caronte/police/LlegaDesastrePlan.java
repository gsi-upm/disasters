package jadex.desastres.caronte.police;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

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
		Environment.printout("PP police: Ack mandado",0);

		//Espero a que se borre el desastre (lo borra el bombero) para irme a otra cosa...
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());
		Environment.printout("PP police: Estoy destinado al desastre " + idDes,0);

		try {
			env.andar(getComponentName(), oldPos, positionDesastre, env.getAgent(getComponentName()).getId(), 0);
		} catch (Exception e) {
			System.out.println("PP police: Error metodo andar: " + e);
		}
		while (des != null) {
			//System.out.println("PP police: No puedo marcharme hasta que se solucione este jaleo, espero un poco mas...");
			//waitFor(2000);
			des = env.getEvent(idDes);
		}

		Environment.printout("PP police: Desastre solucionado: " + idDes,0);
		Environment.printout("PP police: Vuelvo a la comisaria",0);
		
		try {
			env.andar(getComponentName(), oldPos, posicionComisaria, env.getAgent(getComponentName()).getId(), 0);
		} catch (Exception e) {
			System.out.println("PP police: Error metodo andar: " + e);
		}
		Environment.printout("PP police: En la comisaria",0);
	}
}
