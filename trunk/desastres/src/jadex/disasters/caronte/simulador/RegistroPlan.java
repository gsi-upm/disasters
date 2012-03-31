package disasters.caronte.simulador;

import disasters.caronte.Entorno;
import jadex.bdi.runtime.Plan;

/**
 *
 * @author Juan Luis Molina
 */
public class RegistroPlan extends Plan{
	public void body(){
		Entorno env = (Entorno)getBeliefbase().getBelief("env").getFact();
		env.putListado(getComponentName(), getComponentIdentifier());
	}
}