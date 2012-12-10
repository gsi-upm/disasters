package disasters.caronte.simulador.firemen;

import jadex.bdi.runtime.Plan;

/**
 * Plan de BOMBEROS para llevar al bombero a la estaci&oacute;n de bomberos.
 * 
 * @author Iv&aacute;n Rojo
 * @author Juan Luis Molina Nogales
 * 
 */
public class EstacionBomberosPlan extends Plan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		waitFor(1000);
	}
}