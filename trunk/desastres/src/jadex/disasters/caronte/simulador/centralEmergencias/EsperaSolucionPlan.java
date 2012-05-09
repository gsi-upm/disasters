package disasters.caronte.simulador.centralEmergencias;

import disasters.EnviarMensajePlan;
import disasters.caronte.Entorno;

/**
 * Plan de la central que espera que un agente ha solucionado un desastre
 * 
 * @author Ivan y Juan Luis Molina
 * 
 */
public class EsperaSolucionPlan extends EnviarMensajePlan{

	/**
	 * Cuerpo del plan
	 */
	public void body(){
		Entorno env = (Entorno)getBeliefbase().getBelief("env").getFact();

		env.printout("CC central: esperando una solucion...", 2, 0, true);
		String recibido = esperarYEnviarRespuesta("terminado", "Terminado recibido");

		//env.printout("CC central: Ack mandado", 0);
		env.printout("CC central: Emergencia solucionada", 2, 0, true);

		env.printout("CC central: Mandando mensaje al coordinador", 2, 0, true);
		String resultado = enviarMensaje("coordinadorEmergencias", "terminado_geriatrico", "go", true);
		//env.printout("CC central: Respuesta recibida del coordinador: " + resultado, 0);
	}
}