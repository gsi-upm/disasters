package disasters.desastres.sanitarios.coordinadorHospital;

import disasters.EnviarMensajePlan;

/**
 * Los camilleros trasladan a las v&iacute;ctimas m&aacute;s urgentes al &Aacute;rea de Socorro.
 * 
 * @author Juan Luis Molina Nogales
 */
public class GestionAccesoHospitalPlan extends EnviarMensajePlan{
	
	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		enviarRespuesta("ack_hospital", "Aviso recibido");
		System.out.println("** coordinadorHospital: Ack mandado");

		System.out.println("** coordinadorHospital: ambulancia de vuelta en el hospital");
	}
}
