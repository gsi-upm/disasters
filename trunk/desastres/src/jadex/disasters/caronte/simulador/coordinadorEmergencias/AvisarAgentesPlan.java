package disasters.caronte.simulador.coordinadorEmergencias;

import disasters.*;
import disasters.caronte.Entorno;
import disasters.caronte.simulador.ontology.Desastre;
import jadex.bdi.runtime.IGoal;

/**
 *
 * @author Juan Luis Molina Nogales
 */
public class AvisarAgentesPlan extends EnviarMensajePlan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		// Obtenemos un objeto de la clase Entorno para poder usar sus metodos
		Entorno env = (Entorno) getBeliefbase().getBelief("env").getFact();
		
		int id = (Integer) getBeliefbase().getBelief("desastreActual").getFact();
		String idDes = new Integer(id).toString();
		Disaster des = env.getEvent(id);

		String emergencia = des.getSize();
		People heridoAux = getHerido(des);
		String herido = "null";
		if(heridoAux != null){
			herido = heridoAux.getType();
		}

		Desastre desastre = new Desastre(id,emergencia,herido);

		env.printout("OO coordinador: Avisando al gerocultor...", 2, 3, true);
		String resultado2 = enviarObjeto("gerocultor", "aviso_geriatrico", desastre, true);

		env.printout("OO coordinador: Avisando al enfermero...", 2, 3, true);
		String resultado1 = enviarObjeto("nurse", "aviso_geriatrico", desastre, true);

		env.printout("OO coordinador: Avisando al auxiliar...", 2, 3, true);
		String resultado3 = enviarObjeto("auxiliar", "aviso_geriatrico", desastre, true);

		/*String emergencia = esperarYEnviarRespuesta("estadoEmergencia","ok");
		Entorno.printout("OO coordinador: estado de la emergencia: " + emergencia, 0);

		String heridos = esperarYEnviarRespuesta("estadoHeridos","ok");
		if (!heridos.equals("null")) {
			Entorno.printout("OO coordinador: estado del herido: " + heridos, 0);
		} else {
			Entorno.printout("OO coordinador: La emergencia no tiene heridos!!",0);
		}*/

		if(((emergencia.equals("big") || emergencia.equals("huge")) && !des.getType().equals("injuredPerson")) ||
				(!herido.equals("null") && !herido.equals("slight"))){
			env.printout("OO coordinador: Avisando la central... (en espera)...", 2, 3, true);
			String resultado = enviarObjeto("centralEmergencias", "aviso_geriatrico", desastre, true);
			env.printout("OO coordinador: Respuesta recibida de central: " + resultado, 2, 3, true);
		}

		// Creamos un nuevo objetivo.
		IGoal esperaSolucion = createGoal("esperarSolucion");
		dispatchSubgoalAndWait(esperaSolucion);
	}

	/**
	 * Busca un herido.
	 * 
	 * @param des desastre
	 * @return herido
	 */
	private People getHerido(Disaster des){
		People herido = null;

		if(des.getSlight() != null){
			herido = des.getSlight();
		}
		if(des.getSerious() != null){
			herido = des.getSerious();
		}
		if(des.getDead() != null){
			herido = des.getDead();
		}

		return herido;
	}
}