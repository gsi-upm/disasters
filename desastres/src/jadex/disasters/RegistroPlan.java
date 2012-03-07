package disasters;

import jadex.bdi.runtime.*;

/**
 *
 * @author Juan Luis Molina
 */
public class RegistroPlan extends Plan{

	public void body(){
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();
		env.putListado(getComponentName(), getComponentIdentifier());
	}
}