package jadex.desastres.disasters.sanitarios.coordinadorHospital;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Los camilleros trasladan a las victimas mas urgentes al Area de Socorro
 * 
 * @author Juan Luis Molina
 */
public class GestionAccesoHospitalPlan extends EnviarMensajePlan {
	
	/**
	 * Cuerpo del plan
	 */
	public void body(){
		// Obtenemos un objeto de la clase entorno para poder usar sus metodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();
		// Posicion actual de la ambulancia, que le permite recoger al herido.
		WorldObject agente = (WorldObject) getBeliefbase().getBelief("agente").getFact();
		Position posicionActual = agente.getPosition();
		// Posicion del hospital que le corresponde
		Position posicionHospital = (Position) getBeliefbase().getBelief("hospitalMadrid").getFact();

		enviarRespuesta("ack_hospital", "Aviso recibido");
		System.out.println("** coordinadorHospital: Ack mandado");

		System.out.println("** coordinadorHospital: ambulancia de vuelta en el hospital");
	}
}
