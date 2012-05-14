package disasters.desastres.police;

import disasters.*;
import disasters.desastres.Environment;

/**
 * Plan de la POLICIA
 * 
 * @author Nuria y Juan Luis Molina
 * 
 */
public class LlegaDesastrePlan extends EnviarMensajePlan{

	/**
	 * Cuerpo del plan
	 */
	public void body(){

		//Obtenemos un objeto de la clase Environment para poder usar sus metodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();
		//Obtengo mi posicion
		Position oldPos; // = (Position) getBeliefbase().getBelief("pos").getFact();

		Position posicionComisaria = (Position) getBeliefbase().getBelief("Comisaria").getFact();

		//id del Desastre atendiendose
		//int idDes = (Integer)getBeliefbase().getBelief("desastreActual").getFact();
		int idDes = env.getTablon();

		enviarRespuesta("ack_aviso", "Aviso recibido");
		System.out.println("++ police: Ack mandado");

		//Espero a que se borre el desastre (lo borra el bombero) para irme a otra cosa...
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());
		System.out.println("++ police: Estoy destinado al desastre " + idDes);

		try{
			oldPos = env.getAgentPosition("police");
			env.andar(getComponentName(), oldPos, positionDesastre, env.getAgent(getComponentName()).getId(), 0);
		}catch(Exception e){
			System.out.println("++ police: Error metodo andar: " + e);
		}
		while(des != null){
			//System.out.println("++ police: No puedo marcharme hasta que se solucione este jaleo, espero un poco mas...");
			//waitFor(2000);
			des = env.getEvent(idDes);
		}

		System.out.println("++ police: Desastre solucionado: " + idDes);
		System.out.println("++ police: Vuelvo a la comisaria");
		
		try{
			oldPos = env.getAgentPosition("police");
			env.andar(getComponentName(), oldPos, posicionComisaria, env.getAgent(getComponentName()).getId(), 0);
		}catch (Exception e){
			System.out.println("++ police: Error metodo andar: " + e);
		}
		System.out.println("++ police: En la comisaria");
	}
}