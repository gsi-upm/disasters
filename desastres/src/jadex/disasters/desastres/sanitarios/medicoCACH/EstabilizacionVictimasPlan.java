package disasters.desastres.sanitarios.medicoCACH;

import disasters.*;
import disasters.desastres.Environment;

/**
 * Primeros auxilios sobre aquellas v&iacute;ctimas que necesiten de una acci&oacute;n inmediata.</br>
 * En cuanto se estabilicen, se debe producir su traslado inmediato.
 *
 * @author Juan Luis Molina Nogales
 */
public class EstabilizacionVictimasPlan extends EnviarMensajePlan{
	
	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		// Obtenemos un objeto de la clase Environment para poder usar sus metodos
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();

		int idDes = env.getTablon();
		Disaster des = env.getEvent(idDes);

		System.out.println("** Grupo sanitario: realizando primeros auxilios");

		People herido = null;
		if (des.getSlight() != null){
			herido = des.getSlight();
		}else if(des.getSerious() != null){
			herido = des.getSerious();
		}else if(des.getDead() != null){
			herido = des.getDead();
		}

		if(herido.getType().equals("serious")){
			herido.setType("slight");
		}
		System.out.println("** Medico CACH: victimas estabilizadas");
		
		System.out.println("** Medico CACH: avisando a la ambulancia");
		enviarMensaje("ambulancia", "traslado", "go", true);
	}
}