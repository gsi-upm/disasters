package jadex.desastres;

import java.util.Iterator;
import java.util.Map;

import sun.security.acl.WorldGroupImpl;
import jadex.adapter.fipa.AgentIdentifier;
import jadex.adapter.fipa.SFipa;
import jadex.examples.hunterprey.WorldObjectData;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import maps.*;

/**
 * Plan de BOMBEROS
 * 
 * @author Ivan
 * 
 */
public class BomberoEnDesastrePlan extends Plan {

    /**
     * Cuerpo del plan
     */
    public void body() {

        // Obtenemos un objeto de la clase Environment para poder usar sus
        // m�todos
        Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

        // Posici�n actual del bombero, que le permite recoger al herido.
        Position posicionActual = (Position) getBeliefbase().getBelief("pos").getFact();

        // Posicion del parque de bomberos que le corresponde
        Position posicionParque = (Position) getBeliefbase().getBelief("parqueDeBomberos").getFact();

        //id y posicion del Desastre atendiendose
        int idDes = env.getTablon();
        Disaster des = env.getEvent(idDes);
        System.out.println("^^ firemen: Estoy destinado al desastre: " + idDes);
        Position destino = new Position(des.getLatitud(), des.getLongitud());

        //in case the agent hasn't an assigned disaster yet, we have to put 
        //the new value for the idAssigned parameter in the DB
        int assignedId = ((Integer) getBeliefbase().getBelief("assignedId").getFact()).intValue();
        if (assignedId != 0) {
            DBManager.setField(env.getAgent(getAgentName()).getId(), "idAssigned", new Integer(idDes).toString());        //Heridos atrapados
        }
        int id = 0;
        People atrapados = des.getTrapped();
        if (atrapados != null) {
            System.out.println("^^ firemen: He encontrado un herido atrapado!! cuya id es: " + atrapados.getId());
            //actualizar las creencias con el id 
            id = atrapados.getId();

        }
        // Atiendo el desastre un tiempo medio de 4 segundos. Luego, se tiene
        // que ajustar a la grado del mismo.
        try {
            env.andar(getAgentName(), posicionActual, destino,
                    env.getAgent(getAgentName()).getId(), 0);
        // el cuarto par�metro del m�todo andar es el id del bichito
        // que queremos transportar. en bombero es siempre 0
        } catch (Exception ex) {
            System.out.println("^^ firemen: Error m�todo andar: " + ex);
        }
        System.out.println("^^ firemen:: Solucionando desastre...");
        //waitFor(2000);
        //borro a los atrapados
        if (id != 0) {

            System.out.println("^^ firemen: liberando atrapados " + id);
            String resultado = Connection.connect(Environment.DISASTERS + "delete/id/" + id);
            id = 0;
        }


        // TENGO QUE COMPROBAR SI HAY HERIDOS, SI LOS HAY, NO ME PUEDO IR HASTA
        // QUE ESTEN ATENDIDOS
        des = env.getEvent(idDes);
        while (des.getSlight() != null || des.getSerious() != null || des.getDead() != null) {
            //System.out.println("^^ firemen: No puedo marcharme porque quedan heridos, espero un poco m�s...");
            waitFor(2000);
            des = env.getEvent(idDes);
        }




        // El bombero regresa a su parque correspondiente cuando no hay heridos.

        try {
            // HAY QUE ELIMINAR EL DESASTRE
            String resultado = Connection.connect(Environment.DISASTERS + "delete/id/" + idDes);
            System.out.println("^^ firemen: Eliminado el desastre " + idDes);
            // System.out.println(resultado);

            System.out.println("^^ firemen: se dirige al parque de bomberos");



            //Comunicaci�n con la central...
            System.out.println("^^ firemen: Mando respuesta a la central de terminado");
            IMessageEvent fin = createMessageEvent("terminado");
            String nombreCentral = giveMeAgent(env, Environment.CENTRAL);
            AgentIdentifier central = new AgentIdentifier(nombreCentral, true);
            fin.getParameterSet(SFipa.RECEIVERS).addValue(central);
            sendMessage(fin);

            env.andar(getAgentName(), posicionActual, posicionParque, env.getAgent(getAgentName()).getId(), 0);

            System.out.println("^^ firemen: He vuelto al parque de bomberos");


        } catch (Exception e) {
            System.out.println("^^ firemen: Error metodo andar: " + e);
        }

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
