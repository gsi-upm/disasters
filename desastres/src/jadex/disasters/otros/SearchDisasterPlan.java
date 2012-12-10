package disasters.otros;

import disasters.Disaster;
import disasters.desastres.Environment;
import jadex.bdi.runtime.*;
import java.util.*;

/** 
 * Search disaster plan.
 */
public class SearchDisasterPlan extends Plan{
	/** Environment. */
    private Environment env;

	/**
	 * Cuerpo del plan.
	 */
    public void body(){
        // Obtenemos un objeto de la clase entorno para poder usar sus metodos.
        env = (Environment)getBeliefbase().getBelief("env").getFact();

        checkDisasters();

        waitFor(5000);
    }

    /**
     * Iterates through the disasters, looking for unattended ones.
	 * When one is found, a new goal is created to expand it.
     */
    private synchronized void checkDisasters(){
        Iterator it = env.getEvents().entrySet().iterator();
        Map.Entry e = null;
        Disaster dis;

        while(it.hasNext()){
            try{
                e = (Map.Entry) it.next();
                dis = (Disaster) e.getValue();

                if(dis.getState().equals("erased") || dis.getState().equals("controlled")){
                    System.out.println("$$ apocalypse: controlled disaster ");
                    continue;
                }else{
                    //unattended disaster
                    System.out.println("$$ apocalypse: found disaster");
                    getBeliefbase().getBelief("unattendedDisaster").setFact(dis);
                    //Creamos un nuevo objetivo.
                    IGoal harm = createGoal("harm");

                    // Damos prioridad a este subobjetivo, y esperamos a que se cumpla para devolvernos el control.
                    dispatchSubgoalAndWait(harm);
                }
            }catch(GoalFailureException exc){
                System.out.println("$$ apocalypse: error when retrieving disaster:" + exc);
            }
        }
    }
}