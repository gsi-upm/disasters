package disasters.caronte.simulador.firemen;

import disasters.*;
import disasters.caronte.Entorno;
import disasters.caronte.simulador.ontology.Desastre;
//import roads.*;

/**
 * Plan de BOMBEROS.
 * 
 * @author Iv&aacute;n Rojo
 * @author Juan Luis Molina Nogales
 * 
 */
public class BomberoEnDesastrePlan extends EnviarMensajePlan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		// Obtenemos un objeto de la clase Entorno para poder usar sus metodos
		Entorno env = (Entorno)getBeliefbase().getBelief("env").getFact();

		Desastre recibido = (Desastre)enviarRespuestaObjeto("ack_aviso", "Aviso recibido");
		//env.printout("FF firemen: Ack mandado",0);
		
		// Posicion actual del bombero, que le permite recoger al herido.
		Position posicionActual = (Position)getBeliefbase().getBelief("pos").getFact();

		// Posicion del parque de bomberos que le corresponde
		Position posicionParque = (Position)getBeliefbase().getBelief("parqueDeBomberos").getFact();

		//id y posicion del Desastre atendiendose
		int idDes = recibido.getId();
		getBeliefbase().getBelief("idEmergencia").setFact(idDes);
		Disaster des = env.getEvent(idDes);
		env.printout("FF firemen: Estoy destinado al desastre: " + idDes, 2, 0, true);
		Position destino = new Position(des.getLatitud(), des.getLongitud());
		String estadoEmergencia = recibido.getEstadoEmergencia();

		//in case the agent hasn't an assigned disaster yet, we have to put
		//the new value for the idAssigned parameter in the DB
		/*int assignedId = ((Integer)getBeliefbase().getBelief("assignedId").getFact()).intValue();
		/if(assignedId != 0){
			DBManager.setField(env.getAgent(getComponentName()).getId(), "idAssigned", new Integer(idDes).toString()); //Heridos atrapados
		}*/
		

		// Atiendo el desastre un tiempo medio de 4 segundos. Luego, se tiene
		// que ajustar a la grado del mismo.
		try{
			env.printout("FF firemen: Estoy destinado al desastre " + idDes + " con estado " + estadoEmergencia, 2, 0, true);
			env.andar(getComponentName(), posicionActual, destino, env.getAgent(getComponentName()).getId(), 0);
			// el cuarto parametro del metodo andar es el id del bichito
			// que queremos transportar. en bombero es siempre 0
		}catch(Exception ex){
			System.out.println("FF firemen: Error metodo andar: " + ex);
		}


		int id = 0;
		People atrapados = des.getTrapped();
		if(atrapados != null){
			env.printout("FF firemen: He encontrado un herido atrapado cuya id es: " + atrapados.getId() + "!!", 2, 0, true);
			//actualizar las creencias con el id
			id = atrapados.getId();
		}
		
		env.printout("FF firemen: Solucionando desastre...", 2, 0, true);
		//waitFor(2000);
		//borro a los atrapados
		if(id != 0){
			env.printout("FF firemen: liberando atrapados " + id, 2, 0, true);
			String resultado = Connection.connect(Entorno.URL + "delete/id/" + id);
			id = 0;
		}

		// TENGO QUE COMPROBAR SI HAY HERIDOS, SI LOS HAY, NO ME PUEDO IR HASTA QUE ESTEN ATENDIDOS
		des = env.getEvent(idDes);
		while(des.getSlight() != null || des.getSerious() != null || des.getDead() != null){
			//System.out.println("FF firemen: No puedo marcharme porque quedan heridos, espero un poco mas...");
			waitFor(2000);
			des = env.getEvent(idDes);
		}

		// El bombero regresa a su parque correspondiente cuando no hay heridos.
		try{
			// HAY QUE ELIMINAR EL DESASTRE
			env.printout("FF firemen: Eliminado el desastre " + idDes, 2, 0, true);
			String resultado = Connection.connect(Entorno.URL + "delete/id/" + idDes);
			// System.out.println(resultado);
			
			//Comunicacion con la central...
			env.printout("FF firemen: Mando mensaje de terminado a la central", 2, 0, true);
			String respuesta = enviarMensaje("centralEmergencias", "terminado", "done", true);
			env.printout("FF firemen: Respuesta recibida de central: " + respuesta, 2, 0, true);

			String respuesta2 = enviarMensaje("police", "terminado", "done", true);
			env.printout("FF firemen: Respuesta recibida del policia: " + respuesta2, 2, 0, true);

			env.printout("FF firemen: Me dirijo al parque de bomberos", 2, 0, true);
			
			env.andar(getComponentName(), posicionActual, posicionParque, env.getAgent(getComponentName()).getId(), 0);
			env.printout("FF firemen: He vuelto al parque de bomberos", 2, 0, true);
		}catch (Exception e){
			System.out.println("FF firemen: Error metodo andar: " + e);
		}
	}
}