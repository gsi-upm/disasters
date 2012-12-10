package disasters;

import disasters.caronte.Entorno;
import disasters.desastres.Environment;
import jadex.bdi.runtime.IMessageEvent;
import jadex.bdi.runtime.Plan;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.fipa.SFipa;

/**
 * Plan que permite enviar mensajes a otros agentes.
 * 
 * @author Juan Luis Molina Nogales
 */
public abstract class EnviarMensajePlan extends Plan{

	/**
	 * Env&iacute;a un mensaje de tipo string y recibe una respuesta.
	 * 
	 * @param agente agente al que se le env&iacute;a el mensaje
	 * @param evento nombre del evento de mensaje
	 * @param contenido contenido del mensaje
	 * @param respuesta <code>true</code> si espera una respuesta
	 * @return respuesta del mensaje enviado
	 */
	protected String enviarMensaje(String agente, String evento, String contenido, boolean respuesta){
		IComponentIdentifier a = buscarAgente(agente);
		IMessageEvent msg = createMessageEvent(evento);
		msg.getParameter(SFipa.CONTENT).setValue(evento + ":" + contenido);
		msg.getParameterSet(SFipa.RECEIVERS).addValue(a);
		//IMessageEvent answer = sendMessageAndWait(msg);
		sendMessage(msg);
		
		String respondido = null;
		if(respuesta){
			IMessageEvent answer = waitForMessageEvent("ack_" + evento);
			respondido = ((String)answer.getParameter(SFipa.CONTENT).getValue()).split(":",2)[1];
		}
		return respondido;
	}

	/**
	 * Env&iacute;a una respuesta a un mensaje previamente recibido y devuelve el posible ack.
	 * 
	 * @param evento nombre del evento de mensaje de la respuesta a enviar
	 * @param respuesta contenido de la respuesta a enviar
	 * @return respuesta del emisor
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
	 * Espera la llegada de un determinado tipo de mensaje, env&iacute;a una repuesta y muestra el posible ack.
	 * 
	 * @param evento nombre del evento de mensaje que se recibe
	 * @param respuesta contenido de la respuesta a enviar
	 * @return respuesta del emisor
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
	 * Env&iacute;a un mensaje con un objeto y recibe una respuesta.
	 * 
	 * @param agente agente al que se le env&iacute;a el mensaje
	 * @param evento nombre del evento de mensaje
	 * @param contenido contenido del mensaje
	 * @param respuesta <code>true</code> si espera una respuesta
	 * @return respuesta del mensaje enviado
	 */
	protected String enviarObjeto(String agente, String evento, Object contenido, boolean respuesta){
		IComponentIdentifier a = buscarAgente(agente);
		IMessageEvent msg = createMessageEvent(evento);
		msg.getParameter(SFipa.CONTENT).setValue(contenido);
		msg.getParameterSet(SFipa.RECEIVERS).addValue(a);
		sendMessage(msg);
		
		String respondido = null;
		if(respuesta){
			IMessageEvent answer = waitForMessageEvent("ack_" + evento);
			respondido = ((String)answer.getParameter(SFipa.CONTENT).getValue()).split(":",2)[1];
		}
		return respondido;
	}

	/**
	 * Env&iacute;a una respuesta a un mensaje con objeto previamente recibido y devuelve el posible ack.
	 * 
	 * @param evento nombre del evento de mensaje de la respuesta a enviar
	 * @param respuesta contenido de la respuesta a enviar
	 * @return respuesta del emisor
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
	 * Busca un agente.
	 * 
	 * @param agente agente a buscar
	 * @return identificador del agente
	 */
	private IComponentIdentifier buscarAgente(String agente){
		Object env = getBeliefbase().getBelief("env").getFact();
		String clase = "";
		try{
			Class.forName("disasters.caronte.Entorno");
			clase = "Entorno"; // Solo llega aqui si Entorno existe
		}catch(ClassNotFoundException ex){}
		try{
			Class.forName("disasters.desastres.Environment");
			clase = "Environment"; // Solo llega aqui si Environment existe
		}catch(ClassNotFoundException ex){}
		
		IComponentIdentifier a = null;
		if(clase.equals("Entorno")){ // env instanceof Entorno
			a = ((Entorno)env).getAgent(agente).getAgentId();
		}else if(clase.equals("Environment")){ // env instanceof Environment
			a = ((Environment)env).getAgent(agente).getAgentId();
		}
		return a;
	}
}