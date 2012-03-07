package disasters;

import jadex.bdi.runtime.*;
import java.io.IOException;

/**
 *
 * @author Juan Luis Molina
 */
public class DetenerEntorno extends Plan{

	/**
	 * Cuerpo del plan
	 */
	public synchronized void body(){
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();
		try{
			env.terminar();
		}catch(IOException ex){
			System.out.println("Excepcion en DetenerEntorno: " + ex);
		}
	}
}