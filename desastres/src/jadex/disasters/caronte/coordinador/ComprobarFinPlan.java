package disasters.caronte.coordinador;

import disasters.*;
import disasters.caronte.*;
import jadex.base.fipa.SFipa;
import jadex.bdi.runtime.IMessageEvent;
//import java.sql.Timestamp;
//import java.util.Date;
//import org.json.me.*;

/**
 * Plan para avisar a los agentes.
 * 
 * @author Juan Luis Molina
 */
public class ComprobarFinPlan extends CarontePlan{
	/**
	 * Cuerpo del plan AvisarAgentes.
	 */
	public void body(){
		IMessageEvent solReq = (IMessageEvent) getReason();
		String recibido = ((String)solReq.getParameter(SFipa.CONTENT).getValue()).split(":",2)[0];
		enviarRespuesta("ack_" + recibido, "ok");
		getBeliefbase().getBelief("emergencia_actual").setFact(0);
		Entorno env = (Entorno) getBeliefbase().getBelief("env").getFact();
		Disaster[] eventos = env.getEventsArray();
		if(eventos.length == 0){
			env.printout("No hay mas eventos!!", 2, 1, true);
			env.leaveResource((Integer) getBeliefbase().getBelief("id_director_actuacion").getFact());
		}else{
			env.printout("Faltan por solucionar: " + eventos.toString(), 2, 1, true);
		}
		
		/*int des = ((Integer) getBeliefbase().getBelief("emergencia_actual").getFact()).intValue();
		int idDA = ((Integer) getBeliefbase().getBelief("idDirector_actuacion").getFact()).intValue();
		env.printout("Comprobar que el incendio (id:" + des + ") esta apagado", 0, idDA);
		String fecha = new Timestamp(new Date().getTime()).toString();
		boolean confirmado = false;
		
		try{
			JSONArray respuesta = new JSONArray();
			while(confirmado == false){
				String respuestaAux = Connection.connect(Entorno.URL + "mensajes/coor/" + idDA + "/" + fecha);
				respuesta = new JSONArray(respuestaAux);
				if(respuesta.isNull(0) == false){
					confirmado = true;
				}else{
					waitFor(2500);
				}
			}
			
			String accion = respuesta.getJSONObject(0).getString("mensaje");
			if(accion.equals("OK")){
				env.printout("Incendio apagado", 2, 1);
			}
		}catch(JSONException ex){
			System.out.println(ex);
		}*/
	}
}