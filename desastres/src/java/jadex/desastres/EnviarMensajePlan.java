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

	protected String enviarMensaje(String agente, String mensajeID, String contenido){
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

		IGoal query = createGoal("procap.rp_initiate");
		query.getParameter("receiver").setValue(a);
		query.getParameter("conversation_id").setValue(mensajeID);
		query.getParameter("action").setValue(contenido);
		dispatchSubgoalAndWait(query);
		return (String)query.getParameter("result").getValue();
	}

	protected void enviarRespuesta(String evento, String respuesta){
		IMessageEvent solReq = (IMessageEvent) getReason();
		IMessageEvent msgResp = getEventbase().createReply(solReq,evento);
		msgResp.getParameter(SFipa.CONTENT).setValue(respuesta);
		sendMessage(msgResp);
	}
}