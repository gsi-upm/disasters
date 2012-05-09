package disasters.desastres.sanitarios.medicoCACH;

import disasters.*;
import disasters.desastres.Environment;
import jadex.bdi.runtime.IGoal;

/**
 * Se comprueba que es correcto el triage realizado sobre las victimas trasladadas
 * desde la zona de Rescate y se asigna la tarjeta de triage a cada victima
 *
 * @author Juan Luis Molina
 */
public class RevisionTriagePlan extends EnviarMensajePlan{

	/**
	 * Cuerpo del plan
	 */
	public void body(){
		// Obtenemos un objeto de la clase Environment para poder usar susmetodos
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();

		enviarRespuesta("ack_triage", "Aviso recibido");
		System.out.println("** Medico CACH: Ack mandado");

		int idDes = env.getTablon();
		Disaster des = env.getEvent(idDes);

		// Sacamos el herido
		People herido = null;
		if (des.getSlight() != null){
			herido = des.getSlight();
		}else if(des.getSerious() != null){
			herido = des.getSerious();
		}else if(des.getDead() != null){
			herido = des.getDead();
		}

		if(herido != null){
			System.out.println("** Medico CACH: repito el triage");
			String estadoHerido = herido.getType();
			System.out.println("** Medico CACH: herido " + herido.getId() + " con estado " + estadoHerido);

			if(estadoHerido.equals("serious")) {
				IGoal estabilizarVictimas = createGoal("estabilizarVictimas");
				dispatchSubgoalAndWait(estabilizarVictimas);
			}else{
				System.out.println("** Medico CACH: avisando a la ambulancia");
				enviarMensaje("ambulancia", "traslado", "go", true);
			}
		}else{
			System.out.println("** Medico CACH: desastre sin heridos");
		}
	}
}