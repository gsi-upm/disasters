package disasters.desastres.sanitarios.grupoSanitarioOperativo;

import disasters.*;
import disasters.desastres.Environment;

/**
 * Primeros auxilios sobre aquellas v&iacute;ctimas que necesiten de una acci&oacute;n
 * inmediata antes de poder ser trasladados al &Acute;rea de Rescate.
 * 
 * @author Juan Luis Molina Nogales
 */
public class PrimerosAuxiliosPlan extends EnviarMensajePlan{
	
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
		if(des.getSlight() != null){
			herido = des.getSlight();
		}else if(des.getSerious() != null){
			herido = des.getSerious();
		}else if(des.getDead() != null){
			herido = des.getDead();
		}
		
		if(herido.getType().equals("serious")){
			herido.setType("slight");
		}

		System.out.println("** Grupo sanitario: heridos estabilizados");
		enviarMensaje("medicoCACH", "triage", "go", true);
	}
}