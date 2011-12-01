package jadex.desastres.disasters.sanitarios.grupoSanitarioOperativo;

import jadex.bdi.runtime.IGoal;
import jadex.desastres.*;

/**
 * Es fundamental la primera evaluacion de la gravedad por parte de medicos o personal sanitario avanzado
 * 
 * @author Juan Luis Molina
 */
public class TriagePlan extends EnviarMensajePlan{
	
	/**
	 * Cuerpo del plan
	 */
	public void body(){
		// Obtenemos un objeto de la clase Environment para poder usar susmetodos
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();

		enviarRespuesta("ack_aviso_coor", "Aviso recibido");
		System.out.println("** Grupo sanitario: Ack mandado");

		int idDes = env.getTablon();
		Disaster des = env.getEvent(idDes);

		// Sacamos el herido
		People herido = null;
		if (des.getSlight() != null){
			herido = des.getSlight();
		}else if (des.getSerious() != null){
			herido = des.getSerious();
		}else if (des.getDead() != null){
			herido = des.getDead();
		}
		
		if(herido != null){
			System.out.println("** Grupo sanitario: herido " + herido.getId() + " con estado " + herido.getType());

			if(herido.getType().equals("serious")){
				IGoal primerosAuxilios = createGoal("primerosAuxilios");
				dispatchSubgoalAndWait(primerosAuxilios);
			}else{
				enviarMensaje("medicoCACH","triage","go");
			}
		}else{
			System.out.println("** Grupo sanitario: desastre sin heridos");
			enviarMensaje("medicoCACH","triage","go");
		}
	}
}