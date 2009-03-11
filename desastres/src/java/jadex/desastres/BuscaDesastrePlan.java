package jadex.desastres;

import java.util.Iterator;
import java.util.Map;

import jadex.adapter.fipa.AgentIdentifier;
import jadex.adapter.fipa.SFipa;
import jadex.runtime.*;

/**
 * Plan de la central para avisar al resto de los agentes.
 *
 * @author Iván Rojo
 *
 */
public class BuscaDesastrePlan extends Plan {

    /**
     * Cuerpo del plan.
     */
    
	private Disaster desastreAtendido;
	private Disaster desastreEvaluado;
	
	
	public void body() {
        // Obtenemos un objeto de la clase entorno para poder usar sus métodos.
        // System.out.println("** firemen: Hello world! :D ");
        Environment env = (Environment) getBeliefbase().getBelief("env")
                .getFact();
        
       

        Disaster des = null;
        while (des == null) {

            // nos devuelve en des el desastre activo de mayor gravedad y que no
            // esté borrado o null si no hay
            des = findDisaster(env);

           
        }
        
        System.out.println("$$ central: desastre encontrado");
       
        getBeliefbase().getBelief("desastreActual").setFact(des.getId());
        // lo publicamos en el tablon!
        env.setTablon(des.getId());

        // Lo guardamos en una variable accesible del Environment

        IMessageEvent me = createMessageEvent("aviso");
        String nombreBombero = giveMeAgent(env, Environment.BOMBERO);
        String nombrePolicia = giveMeAgent(env, Environment.POLICIA);

        if (nombreBombero != null) {
            AgentIdentifier bombero = new AgentIdentifier(nombreBombero, true);
            me.getParameterSet(SFipa.RECEIVERS).addValue(bombero);
        }
        if (nombrePolicia != null) {
            AgentIdentifier policia = new AgentIdentifier(nombrePolicia, true);
            me.getParameterSet(SFipa.RECEIVERS).addValue(policia);
        }

        
        People herido = null;

        if (des.getSlight() != null) {
            herido = des.getSlight();
        }
        if (des.getSerious() != null) {
            herido = des.getSerious();
        }
        if (des.getDead() != null) {
            herido = des.getDead();
        }
        
        if (herido != null) {
            System.out
                    .println("$$ central: He encontrado un herido !! cuya id es: "
                            + herido.getId());
            
            // pongo al herido como atendido
            herido.setAtendido(true);
            // Avisamos a la ambulancia
            String nombreAmbulancia = giveMeAgent(env, Environment.AMBULANCIA);

            if (nombreAmbulancia != null) {
                AgentIdentifier ambulancia = new AgentIdentifier(nombreAmbulancia, true);
                me.getParameterSet(SFipa.RECEIVERS).addValue(ambulancia);

            }

        }
        System.out.println("$$ central: Avisando a agentes... (en espera)...");
        sendMessage(me);
        IMessageEvent req = waitForMessageEvent("terminado");
        System.out.println("$$ central: Mensaje recibido");
       waitFor(5000);
        //Creamos un nuevo objetivo.
        IGoal esperaSolucion = createGoal("esperaSolucion");

        // Damos prioridad a este subobjetivo, y esperamos a que se cumpla para devolvernos el control.
        dispatchSubgoalAndWait(esperaSolucion);

        // }

    }

    private Disaster findDisaster(Environment env) {

        System.out.println("$$ central: Comenzamos a buscar el desastre mas grave... ");
        Iterator it = env.disasters.entrySet().iterator();
        // waitFor(500);
        Map.Entry e = null;
        // Desastre que va a ser procesado por los agentes
        desastreAtendido = null;
        // Desastre con el que compite en gravedad el finalmente atendido
        desastreEvaluado = null;

        if (it.hasNext()) {

            // Cogemos el primer desastre para compararlo con el resto
            // encontrados
	    System.out.println("$$ central: Extraemos el primer desastre, sera el desastre mas grave... ");
            try {
                e = (Map.Entry) it.next();
                desastreAtendido = (Disaster) e.getValue();
                System.out.println("$$ central: El desastre atendido es: " + desastreAtendido.getId());
            } catch (Exception exc) {
                System.out.println("$$ central: Error extrayendo el primer desastre:"  + exc);
            }

            while (it.hasNext()) {

                try {
                    e = (Map.Entry) it.next();
                    desastreEvaluado = (Disaster) e.getValue();
		    System.out.println("$$ central: El desastre evaluado es: " + desastreEvaluado.getId());

                    if ((desastreEvaluado.getState().equals("erased"))
                            | (desastreEvaluado.getState().equals("controlled"))) {
                    			System.out.println("$$ central: El desastre encontrado esta controlado... ");                        
                    			continue;
                    }
                    
                    boolean desastreMasGrave = false;
                    // Comprobamos los heridos graves
                    desastreMasGrave = compruebaGrave();
                    if (desastreMasGrave){
                        System.out.println("$$ central: Hemos encontrado un desastre mas grave... ");
			continue;			
			}
                    // Comprobamos los heridos atrapados
                    desastreMasGrave = compruebaAtrapado();
                    if (desastreMasGrave){
			System.out.println("$$ central: Hemos encontrado un desastre mas grave... ");                        
			continue;
		    }
                    // Comprobamos los heridos leves
                    desastreMasGrave = compruebaLeve();
                    if (desastreMasGrave){
			System.out.println("$$ central: Hemos encontrado un desastre mas grave... ");                        
			continue;}
                    // Comprobamos los heridos muertos
                    desastreMasGrave = compruebaMuerto();
                    if (desastreMasGrave){
			System.out.println("$$ central: Hemos encontrado un desastre mas grave... ");                        
			continue;
		    }
                    // El desastre no tiene heridos que tratar
                    // Comprobamos el tamaño del desastre
		    System.out.println("$$ central: Comprobamos la magnitud del desastre... ");
                    String tamanoEvaluado = desastreEvaluado.getSize();
                    String tamanoAtendido = desastreAtendido.getSize();
                    if (tamanoAtendido.equals(tamanoEvaluado)) {
                        continue;
                    } else {
                        if (tamanoAtendido.equals("huge")) {
			    System.out.println("$$ central: El atendido es huge... ");
                            continue;
                        } else {
                            if (((tamanoAtendido.equals("big")) & (tamanoEvaluado
                                    .equals("huge")))
                                    | ((tamanoAtendido.equals("medium")) & ((tamanoEvaluado
                                            .equals("huge")) | (tamanoEvaluado
                                            .equals("big"))))
                                    | ((tamanoAtendido.equals("small")) & ((tamanoEvaluado
                                            .equals("huge"))
                                            | (tamanoEvaluado.equals("big")) | (tamanoEvaluado
                                            .equals("medium"))))) {
				System.out
                .println("$$ central: Hemos encontrado un desastre mas grave... ");
                                // Hemos encontrado un desastre mas grave
                                desastreAtendido = desastreEvaluado;
                            } else {
                                continue;
                            }
                        }
                    }
                } catch (Exception exc) {
                    System.out
                            .println("$$ central: Error extrayendo los siguientes desastres:"
                                    + exc);
                }
            }

        }
        System.out.println("$$ central: El desastre mas grave es el: "
                + desastreAtendido.getId());
        return desastreAtendido;

    }

    // devuelve true si hemos encontrado otro mas grave
    private boolean compruebaGrave() {

        if (((desastreAtendido.getSerious()) != null)
                & ((desastreEvaluado.getSerious()) != null)) {

            boolean atendActivo = desastreAtendido.getSerious().getState()
                    .equals("active");
            boolean evalActivo = desastreEvaluado.getSerious().getState()
                    .equals("active");
            int numAtend = desastreAtendido.getSerious().getQuantity();
            int numEval = desastreEvaluado.getSerious().getQuantity();
            if ((evalActivo == true) & (atendActivo == true)
                    & (numAtend < numEval)) {
                // Hemos encontrado un desastre mas grave
                desastreAtendido = desastreEvaluado;
		System.out.println("$$ central: Hemos encontrado un desastre mas grave en serious... ");
                return true;
            }
            if ((evalActivo == true) & (atendActivo == false)) {
                // Hemos encontrado un desastre mas grave
                desastreAtendido = desastreEvaluado;
		System.out
                .println("$$ central: Hemos encontrado un desastre mas grave en serious... ");
                return true;
            }
            return false;
        }
        if (((desastreAtendido.getSerious()) == null)
                & ((desastreEvaluado.getSerious()) != null)) {
            boolean evalActivo = desastreEvaluado.getSerious().getState()
                    .equals("active");
            if (evalActivo == true) {
                // Hemos encontrado un desastre mas grave
                desastreAtendido = desastreEvaluado;
		System.out
                .println("$$ central: Hemos encontrado un desastre mas grave en serious... ");
                return true;
            }
            return false;
        }
        return false;
    }

    // devuelve true si hemos encontrado otro mas grave
    private boolean compruebaAtrapado() {

        if (((desastreAtendido.getTrapped()) != null)
                & ((desastreEvaluado.getTrapped()) != null)) {
            boolean atendActivo = desastreAtendido.getTrapped().getState()
                    .equals("active");
            boolean evalActivo = desastreEvaluado.getTrapped().getState()
                    .equals("active");
            int numAtend = desastreAtendido.getTrapped().getQuantity();
            int numEval = desastreEvaluado.getTrapped().getQuantity();
            if ((evalActivo == true) & (atendActivo == true)
                    & (numAtend < numEval)) {
                // Hemos encontrado un desastre mas grave
                desastreAtendido = desastreEvaluado;
		System.out
                .println("$$ central: Hemos encontrado un desastre mas grave en atrapado... ");
                return true;
            }
            if ((evalActivo == true) & (atendActivo == false)) {
                // Hemos encontrado un desastre mas grave
                desastreAtendido = desastreEvaluado;
		System.out
                .println("$$ central: Hemos encontrado un desastre mas grave en atrapado... ");
                return true;
            }
            return false;
        }
        if (((desastreAtendido.getTrapped()) == null)
                & ((desastreEvaluado.getTrapped()) != null)) {
            boolean evalActivo = desastreEvaluado.getTrapped().getState()
                    .equals("active");
            if (evalActivo == true) {
                // Hemos encontrado un desastre mas grave
                desastreAtendido = desastreEvaluado;
		System.out
                .println("$$ central: Hemos encontrado un desastre mas grave en atrapado... ");
                return true;
            }
            return false;
        }
        return false;
    }

    // devuelve true si hemos encontrado otro mas grave
    private boolean compruebaLeve() {

        if (((desastreAtendido.getSlight()) != null)
                & ((desastreEvaluado.getSlight()) != null)) {
            boolean atendActivo = desastreAtendido.getSlight().getState()
                    .equals("active");
            boolean evalActivo = desastreEvaluado.getSlight().getState()
                    .equals("active");
            int numAtend = desastreAtendido.getSlight().getQuantity();
            int numEval = desastreEvaluado.getSlight().getQuantity();
            if ((evalActivo == true) & (atendActivo == true)
                    & (numAtend < numEval)) {
                // Hemos encontrado un desastre mas grave
                desastreAtendido = desastreEvaluado;
		System.out
                .println("$$ central: Hemos encontrado un desastre mas grave en leve... ");
                return true;
            }
            if ((evalActivo == true) & (atendActivo == false)) {
                // Hemos encontrado un desastre mas grave
                desastreAtendido = desastreEvaluado;
		System.out
                .println("$$ central: Hemos encontrado un desastre mas grave en leve... ");
                return true;
            }
            return false;
        }
        if (((desastreAtendido.getSlight()) == null)
                & ((desastreEvaluado.getSlight()) != null)) {
            boolean evalActivo = desastreEvaluado.getSlight().getState()
                    .equals("active");
            if (evalActivo == true) {
                // Hemos encontrado un desastre mas grave
                desastreAtendido = desastreEvaluado;
		System.out
                .println("$$ central: Hemos encontrado un desastre mas grave en leve... ");
                return true;
            }
            return false;
        }
        return false;
    }

    // devuelve true si hemos encontrado otro mas grave
    private boolean compruebaMuerto() {

        if (((desastreAtendido.getDead()) != null)
                & ((desastreEvaluado.getDead()) != null)) {
            boolean atendActivo = desastreAtendido.getDead().getState().equals(
                    "active");
            boolean evalActivo = desastreEvaluado.getDead().getState().equals(
                    "active");
            int numAtend = desastreAtendido.getDead().getQuantity();
            int numEval = desastreEvaluado.getDead().getQuantity();
            if ((evalActivo == true) & (atendActivo == true)
                    & (numAtend < numEval)) {
                // Hemos encontrado un desastre mas grave
                desastreAtendido = desastreEvaluado;
		System.out
                .println("$$ central: Hemos encontrado un desastre mas grave en muerto... ");
                return true;
            }
            if ((evalActivo == true) & (atendActivo == false)) {
                // Hemos encontrado un desastre mas grave
                desastreAtendido = desastreEvaluado;
		System.out
                .println("$$ central: Hemos encontrado un desastre mas grave en muerto... ");
                return true;
            }
            return false;
        }
        if (((desastreAtendido.getDead()) == null)
                & ((desastreEvaluado.getDead()) != null)) {
            boolean evalActivo = desastreEvaluado.getDead().getState().equals(
                    "active");
            if (evalActivo == true) {
                // Hemos encontrado un desastre mas grave
                desastreAtendido = desastreEvaluado;
		System.out
                .println("$$ central: Hemos encontrado un desastre mas grave en muerto... ");
                return true;
            }
            return false;
        }
        return false;
    }

    private String giveMeAgent(Environment env, String tipo) {

        Iterator it = env.agentes.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry e = null;
            // Hacemos este try para que no salga una excepcion de sinronizacion
            // que no nos influye realmente
            try {
                e = (Map.Entry) it.next();
            } catch (Exception exc) {
            }
            String nombreAgente;
            WorldObject agente = (WorldObject) e.getValue();
            if (agente.getType().equals(tipo)) {
                nombreAgente = agente.getName();
                return nombreAgente;
            }
        }
        return null;

    }

}
