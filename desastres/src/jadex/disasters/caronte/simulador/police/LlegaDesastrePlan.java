package disasters.caronte.simulador.police;

import disasters.*;
import disasters.caronte.Entorno;

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
		//Obtenemos un objeto de la clase Entorno para poder usar sus metodos
		Entorno env = (Entorno)getBeliefbase().getBelief("env").getFact();

		Desastre recibido = (Desastre)enviarRespuestaObjeto("ack_aviso", "Aviso recibido");
		//env.printout("PP police: Ack mandado",0);

		//Obtengo mi posicion
		Position oldPos = (Position)getBeliefbase().getBelief("pos").getFact();

		Position posicionComisaria = (Position)getBeliefbase().getBelief("Comisaria").getFact();

		//id del Desastre atendiendose
		int idDes = recibido.getId();
		getBeliefbase().getBelief("idEmergencia").setFact(idDes);

		//Espero a que se borre el desastre (lo borra el bombero) para irme a otra cosa...
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());
		env.printout("PP police: Estoy destinado al desastre " + idDes, 2, 0);

		try{
			env.andar(getComponentName(), oldPos, positionDesastre, env.getAgent(getComponentName()).getId(), 0);
		}catch (Exception e){
			System.out.println("PP police: Error metodo andar: " + e);
		}

		String recibido2 = esperarYEnviarRespuesta("terminado", "Terminado recibido");

		env.printout("PP police: Desastre " + idDes + " solucionado", 2, 0);
		env.printout("PP police: Vuelvo a la comisaria", 2, 0);
		
		try{
			env.andar(getComponentName(), oldPos, posicionComisaria, env.getAgent(getComponentName()).getId(), 0);
		}catch (Exception e){
			System.out.println("PP police: Error metodo andar: " + e);
		}
		env.printout("PP police: En la comisaria", 2, 0);
	}
}