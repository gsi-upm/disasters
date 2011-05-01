package jadex.desastres.caronte.firemen;

import java.util.Iterator;
import java.util.Map;

import jadex.bridge.ComponentIdentifier;
import jadex.base.fipa.SFipa;
import jadex.bdi.runtime.*;
import jadex.base.fipa.*;
import jadex.commons.service.SServiceProvider;
import jadex.bridge.IComponentIdentifier;
import jadex.desastres.*;

import roads.*;

/**
 * Plan de BOMBEROS
 * 
 * @author Ivan y Juan Luis Molina
 * 
 */
public class BomberoEnDesastrePlan extends EnviarMensajePlan {

	/**
	 * Cuerpo del plan
	 */
	public void body() {

		// Obtenemos un objeto de la clase Environment para poder usar sus metodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

		// Posicion actual del bombero, que le permite recoger al herido.
		//Position posicionActual = (Position) getBeliefbase().getBelief("pos").getFact();
		WorldObject agente = (WorldObject)getBeliefbase().getBelief("agente").getFact();
		Position posicionActual = agente.getPosition();

		// Posicion del parque de bomberos que le corresponde
		Position posicionParque = (Position) getBeliefbase().getBelief("parqueDeBomberos").getFact();
		
		enviarRespuesta("ack_aviso", "Aviso recibido");
		System.out.println("^^ firemen: Ack mandado");

		//id y posicion del Desastre atendiendose
		int idDes = env.getTablon();
		Disaster des = env.getEvent(idDes);
		System.out.println("^^ firemen: Estoy destinado al desastre: " + idDes);
		Position destino = new Position(des.getLatitud(), des.getLongitud());

		//in case the agent hasn't an assigned disaster yet, we have to put
		//the new value for the idAssigned parameter in the DB
		//int assignedId = ((Integer) getBeliefbase().getBelief("assignedId").getFact()).intValue();
		//if (assignedId != 0) {
		//	DBManager.setField(env.getAgent(getComponentName()).getId(), "idAssigned", new Integer(idDes).toString()); //Heridos atrapados
		//}
		

		// Atiendo el desastre un tiempo medio de 4 segundos. Luego, se tiene
		// que ajustar a la grado del mismo.
		try {
			env.andar(getComponentName(), posicionActual, destino, env.getAgent(getComponentName()).getId(), 0);
			// el cuarto parametro del metodo andar es el id del bichito
			// que queremos transportar. en bombero es siempre 0
		} catch (Exception ex) {
			System.out.println("^^ firemen: Error metodo andar: " + ex);
		}


		int id = 0;
		People atrapados = des.getTrapped();
		if (atrapados != null) {
			System.out.println("^^ firemen: He encontrado un herido atrapado cuya id es: " + atrapados.getId() + "!!");
			//actualizar las creencias con el id
			id = atrapados.getId();
		}
		
		System.out.println("^^ firemen: Solucionando desastre...");
		//waitFor(2000);
		//borro a los atrapados
		if (id != 0) {
			System.out.println("^^ firemen: liberando atrapados " + id);
			String resultado = Connection.connect(Environment.URL + "delete/id/" + id);
			id = 0;
		}

		// TENGO QUE COMPROBAR SI HAY HERIDOS, SI LOS HAY, NO ME PUEDO IR HASTA QUE ESTEN ATENDIDOS
		des = env.getEvent(idDes);
		while (des.getSlight() != null || des.getSerious() != null || des.getDead() != null) {
			//System.out.println("^^ firemen: No puedo marcharme porque quedan heridos, espero un poco mas...");
			waitFor(2000);
			des = env.getEvent(idDes);
		}

		// El bombero regresa a su parque correspondiente cuando no hay heridos.
		try {
			// HAY QUE ELIMINAR EL DESASTRE
			String resultado = Connection.connect(Environment.URL + "delete/id/" + idDes);
			System.out.println("^^ firemen: Eliminado el desastre " + idDes);
			// System.out.println(resultado);

			//waitFor(6000); // ESPERO A QUE EL ENTORNO SE ACTUALICE!!
			//Comunicacion con la central...
			System.out.println("^^ firemen: Mando mensaje de terminado a la central");
			
			String respuesta = enviarMensaje("coordinadorEmergencias","terminado","done");
			System.out.println("^^ firemen: Respuesta recibida de central: " + respuesta);

			System.out.println("^^ firemen: Me dirijo al parque de bomberos");
			
			env.andar(getComponentName(), posicionActual, posicionParque, env.getAgent(getComponentName()).getId(), 0);
			System.out.println("^^ firemen: He vuelto al parque de bomberos");
		} catch (Exception e) {
			System.out.println("^^ firemen: Error metodo andar: " + e);
		}
	}
}