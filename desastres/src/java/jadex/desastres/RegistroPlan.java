package jadex.desastres;

import jadex.bdi.runtime.*;

/**
 *
 * @author Juan Luis Molina
 */
public class RegistroPlan extends Plan{
	Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

	public void body(){
		env.putListado(getComponentName(),getComponentIdentifier());
	}
}
