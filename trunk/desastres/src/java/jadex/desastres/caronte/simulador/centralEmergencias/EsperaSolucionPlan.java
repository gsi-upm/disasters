package jadex.desastres.caronte.simulador.centralEmergencias;

import jadex.desastres.*;

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
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();

		env.printout("CC central: esperando una solucion...", 0);
		String recibido = esperarYEnviarRespuesta("terminado", "Terminado recibido");

		//env.printout("CC central: Ack mandado", 0);
		env.printout("CC central: Emergencia solucionada", 0);

		env.printout("CC central: Mandando mensaje al coordinador", 0);
		String resultado = enviarMensaje("coordinadorEmergencias", "terminado_geriatrico", "go");
		//env.printout("CC central: Respuesta recibida del coordinador: " + resultado, 0);
	}
}