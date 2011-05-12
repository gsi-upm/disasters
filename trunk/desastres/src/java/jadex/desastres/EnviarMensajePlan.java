package jadex.desastres;

import jadex.bdi.runtime.*;
import jadex.base.fipa.*;
import jadex.commons.service.SServiceProvider;
import jadex.bridge.IComponentIdentifier;

/**
 *
 * @author Juan Luis Molina
 */
public abstract class EnviarMensajePlan extends Plan {

	protected String enviarMensaje(String agente, String evento, String contenido){
		IComponentIdentifier a = null;
		while(a == null){
			IDF	dfservice = (IDF)SServiceProvider.getService(getScope().getServiceProvider(), IDF.class).get(this);
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

	protected void enviarRespuesta(String evento, String respuesta){
		IMessageEvent solReq = (IMessageEvent) getReason();
		IMessageEvent msgResp = getEventbase().createReply(solReq, evento);
		msgResp.getParameter(SFipa.CONTENT).setValue(evento + ":" + respuesta);
		sendMessage(msgResp);
	}

	protected void esperarYEnviarRespuesta(String evento, String respuesta){
		IMessageEvent solReq = waitForMessageEvent(evento);
		IMessageEvent msgResp = getEventbase().createReply(solReq, "ack_" + evento);
		msgResp.getParameter(SFipa.CONTENT).setValue("ack_" + evento + ":" + respuesta);
		sendMessage(msgResp);
	}
}