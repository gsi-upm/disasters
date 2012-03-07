package disasters;

import jadex.base.fipa.*;
import jadex.bdi.runtime.*;
import jadex.bridge.*;
import jadex.bridge.service.*;

/**
 *
 * @author Juan Luis Molina
 */
public abstract class EnviarMensajePlan extends Plan{

	/**
	 *
	 * @param agente Agente al que se le envia el mensaje
	 * @param evento Nombre del evento de mensaje
	 * @param contenido Contenido del mensaje
	 * @return Respuesta del mensaje enviado
	 */
	protected String enviarMensaje(String agente, String evento, String contenido){
		IComponentIdentifier a = buscarAgente(agente);

		/*IGoal query = createGoal("procap.rp_initiate");
		query.getParameter("receiver").setValue(a);
		query.getParameter("conversation_id").setValue(evento);
		query.getParameter("action").setValue(contenido);
		dispatchSubgoalAndWait(query);
		return (String)query.getParameter("result").getValue();*/

		IMessageEvent msg = createMessageEvent(evento);
		msg.getParameter(SFipa.CONTENT).setValue(evento + ":" + contenido);
		msg.getParameterSet(SFipa.RECEIVERS).addValue(a);
		//IMessageEvent answer = sendMessageAndWait(msg);
		sendMessage(msg);
		IMessageEvent answer = waitForMessageEvent("ack_" + evento);
		return ((String)answer.getParameter(SFipa.CONTENT).getValue()).split(":",2)[1];
	}

	/**
	 *
	 * @param evento Nombre del evento de mensaje de la respuesta a enviar
	 * @param respuesta Contenido de la respuesta a enviar
	 * @return Respuesta del emisor
	 */
	protected String enviarRespuesta(String evento, String respuesta){
		IMessageEvent solReq = (IMessageEvent) getReason();
		String recibido = ((String)solReq.getParameter(SFipa.CONTENT).getValue()).split(":",2)[1];
		IMessageEvent msgResp = getEventbase().createReply(solReq, evento);
		msgResp.getParameter(SFipa.CONTENT).setValue(evento + ":" + respuesta);
		sendMessage(msgResp);
		return recibido;
	}

	/**
	 *
	 * @param evento Nombre del evento de mensaje que se recibe
	 * @param respuesta Contenido de la respuesta a enviar
	 * @return Respuesta del emisor
	 */
	protected String esperarYEnviarRespuesta(String evento, String respuesta){
		IMessageEvent solReq = waitForMessageEvent(evento);
		String recibido = ((String)solReq.getParameter(SFipa.CONTENT).getValue()).split(":",2)[1];
		IMessageEvent msgResp = getEventbase().createReply(solReq, "ack_" + evento);
		msgResp.getParameter(SFipa.CONTENT).setValue("ack_" + evento + ":" + respuesta);
		sendMessage(msgResp);
		return recibido;
	}

	/**
	 *
	 * @param agente Agente al que se le envia el mensaje
	 * @param evento Nombre del evento de mensaje
	 * @param contenido Contenido del mensaje
	 * @return Respuesta del mensaje enviado
	 */
	protected String enviarObjeto(String agente, String evento, Object contenido){
		IComponentIdentifier a = buscarAgente(agente);
		IMessageEvent msg = createMessageEvent(evento);
		msg.getParameter(SFipa.CONTENT).setValue(contenido);
		msg.getParameterSet(SFipa.RECEIVERS).addValue(a);
		sendMessage(msg);
		IMessageEvent answer = waitForMessageEvent("ack_" + evento);
		return ((String)answer.getParameter(SFipa.CONTENT).getValue()).split(":",2)[1];
	}

	/**
	 *
	 * @param evento Nombre del evento de mensaje de la respuesta a enviar
	 * @param respuesta Contenido de la respuesta a enviar
	 * @return Respuesta del emisor
	 */
	protected Object enviarRespuestaObjeto(String evento, String respuesta){
		IMessageEvent solReq = (IMessageEvent) getReason();
		Object recibido = solReq.getParameter(SFipa.CONTENT).getValue();
		IMessageEvent msgResp = getEventbase().createReply(solReq, evento);
		msgResp.getParameter(SFipa.CONTENT).setValue(evento + ":" + respuesta);
		sendMessage(msgResp);
		return recibido;
	}

	/**
	 *
	 * @param agente Agente a buscar
	 * @return Agente encontrado
	 */
	private IComponentIdentifier buscarAgente(String agente){
		IComponentIdentifier a = null;
		while(a == null){
			IDF	dfservice = (IDF)SServiceProvider.getService(getServiceContainer(), IDF.class, RequiredServiceInfo.SCOPE_PLATFORM).get(this);
			IDFServiceDescription sd = dfservice.createDFServiceDescription(null, agente, null);
			IDFComponentDescription ad = dfservice.createDFComponentDescription(null, sd);
			IGoal ft = createGoal("df_search");
			ft.getParameter("description").setValue(ad);
			dispatchSubgoalAndWait(ft);
			IDFComponentDescription[] result = (IDFComponentDescription[])ft.getParameterSet("result").getValues();
			if(result.length>0){
				a = result[0].getName();
			}
		}
		return a;
	}
}