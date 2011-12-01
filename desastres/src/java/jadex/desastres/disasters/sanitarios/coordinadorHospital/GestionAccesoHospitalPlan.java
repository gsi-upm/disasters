package jadex.desastres.disasters.sanitarios.coordinadorHospital;

import jadex.desastres.EnviarMensajePlan;

/**
 * Los camilleros trasladan a las victimas mas urgentes al Area de Socorro
 * 
 * @author Juan Luis Molina
 */
public class GestionAccesoHospitalPlan extends EnviarMensajePlan{
	
	/**
	 * Cuerpo del plan
	 */
	public void body(){
		enviarRespuesta("ack_hospital", "Aviso recibido");
		System.out.println("** coordinadorHospital: Ack mandado");

		System.out.println("** coordinadorHospital: ambulancia de vuelta en el hospital");
	}
}
