package disasters.desastres.ambulance;

import disasters.*;
import disasters.desastres.Environment;

/**
 * Plan de la AMBULANCIA para recoger heridos.
 *
 * @author Olimpia Hern&aacute;ndez
 * @author Juan Luis Molina Nogales
 */
public class RecogeHeridosPlan extends EnviarMensajePlan{

	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		// Obtenemos un objeto de la clase Environment para poder usar sus metodos
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();

		// Posicion actual de la ambulancia, que le permite recoger al herido.
		Position posicionActual; // = (Position) getBeliefbase().getBelief("pos").getFact();

		// Posicion del hospital que le corresponde
		Position posicionHospital = (Position) getBeliefbase().getBelief("hospital").getFact();
		int idDes = env.getTablon();

		//Espero a que se borre el desastre (lo borra el bombero) para irme a otra cosa...
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());

		System.out.println("** ambulance: Estoy destinado al desastre: " + idDes);

		enviarRespuesta("ack_aviso", "Aviso recibido");
		System.out.println("** ambulance: Ack mandado");

		//sacamos el herido
		People herido = null;

		if(des.getSlight() != null){
			herido = des.getSlight();
		}
		if(des.getSerious() != null){
			herido = des.getSerious();
		}
		if(des.getDead() != null){
			herido = des.getDead();
		}

		try{
			posicionActual = env.getAgentPosition("ambulance");
			env.andar(getComponentName(), posicionActual, positionDesastre, env.getAgent(getComponentName()).getId(), 0);
		}catch(Exception e){
			System.out.println("** ambulance: Error metodo andar: " + e);
		}

		int id = 0;
		if(herido != null){
			id = herido.getId();
			System.out.println("** ambulance: Tengo herido " + id);
			
			//deasociar los heridos del desastre
			System.out.println("** ambulance: quitando la asociacion del herido " + id);
			String resultado1 = Connection.connect(Environment.URL + "put/" + id + "/idAssigned/" + "0");
			
			if(herido.getType().equals("slight")){
				des.setSlight();
			}else if(herido.getType().equals("serious")){
				des.setSerious();
			}else if(herido.getType().equals("dead")){
				des.setDead();
			}
		}else{
			System.out.println("** ambulance: Desastre sin heridos");
		}

		// La ambulancia regresa a su hospital correspondiente.
		try{
			posicionActual = env.getAgentPosition("ambulance");
			env.andar(getComponentName(), posicionActual, posicionHospital, env.getAgent(getComponentName()).getId(), id);
			if(herido != null){
				//y deposita al herido
				System.out.println("** ambulance: depositando herido " + id);
				String resultado1 = Connection.connect(Environment.URL + "put/" + "0.0" + "/idAssigned/" + "0");
				String resultado = Connection.connect(Environment.URL + "delete/id/" + id);
			}else{
				System.out.println("** ambulance: de vuelta en el hospital");
			}

		}catch(Exception e){
			System.out.println("** ambulance: Error metodo andar: " + e);
		}
	}
}