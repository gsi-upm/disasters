package disasters.desastres.sanitarios.coordinadorMedico;

import disasters.EnviarMensajePlan;

/**
 * Determinar el numero de ambulancias y personal que debe ir a la zona del desastre
 *
 * @author Juan Luis Molina
 */
public class OrganizarRecursosMedicosPlan extends EnviarMensajePlan{
	/**
	 * Cuerpo del plan
	 */
	public void body(){
		enviarRespuesta("ack_aviso", "Aviso recibido");
		System.out.println("** Coordinador medico: Ack mandado");

		System.out.println("** Coordinador medico: Desplegando agentes sanitarios...");

		//String resultado1 = enviarMensaje("ambulancia","aviso_coor","go");
		//System.out.println("** Coordinador medico: Respuesta recibida de la ambulancia: " + resultado1);
		String resultado2 = enviarMensaje("grupoSanitarioOperativo", "aviso_coor", "go", true);
		System.out.println("** Coordinador medico: Respuesta recibida del gso: " + resultado2);
		//String resultado3 = enviarMensaje("medicoCACH","go");
		//System.out.println("** coordinador medico: Respuesta recibida del medico CACH: " + resultado3);
		//String resultado4 = enviarMensaje("coordinadorHospital","go");
		//System.out.println("** coordinador medico: Respuesta recibida del coordinador hospital: " + resultado4);

		System.out.println("** Coordinador medico: Agentes avisados!!");
	}
}