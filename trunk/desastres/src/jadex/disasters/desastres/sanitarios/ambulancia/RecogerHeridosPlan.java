package disasters.desastres.sanitarios.ambulancia;

import disasters.*;
import disasters.desastres.Environment;

/**
 * Es fundamental la primera evaluacion de la gravedad por parte de los medicos o personal sanitario
 *
 * @author Juan Luis Molina y Olimpia Hernandez
 *
 */
public class RecogerHeridosPlan extends EnviarMensajePlan{

	/**
	 * Cuerpo del plan
	 */
	public void body(){
		// Obtenemos un objeto de la clase Environment para poder usar sus metodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();
		// Posicion actual de la ambulancia, que le permite recoger al herido.
		Position posicionActual = (Position) getBeliefbase().getBelief("pos").getFact();
		// Posicion del hospital que le corresponde
		Position posicionHospital = (Position) getBeliefbase().getBelief("hospitalMadrid").getFact();

		enviarRespuesta("ack_traslado", "Aviso recibido");
		System.out.println("** Ambulancia: Ack mandado");

		int idDes = env.getTablon();
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());

		System.out.println("** Ambulancia: me dirijo al desastre " + idDes);

		try{
			env.andar(getComponentName(), posicionActual, positionDesastre, env.getAgent(getComponentName()).getId(), 0);
		}catch (Exception e){
			System.out.println("** Ambulancia: error metodo andar: " + e);
		}

		// TRASLADO!!

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

		int id = 0;
		if(herido != null){
			System.out.println("** Ambulancia: Tengo herido " + herido.getId());
			//deasociar los heridos del desastre
			id = herido.getId();
			System.out.println("** Ambulancia: quitando la asociacion del herido " + id);
			String resultado1 = Connection.connect(Environment.URL + "put/" + id + "/idAssigned/" + "0");

			if(herido.getType().equals("slight")){
				des.setSlight();
			}else if(herido.getType().equals("serious")){
				des.setSerious();
			}else if(herido.getType().equals("dead")){
				des.setDead();
			}
		}

		// La ambulancia regresa a su hospital correspondiente.
		try{
			env.andar(getComponentName(), posicionActual, posicionHospital, env.getAgent(getComponentName()).getId(), id);
			if(herido != null){
				//y deposita al herido
				System.out.println("** Ambulancia: depositando herido " + id);
				String resultado = Connection.connect(Environment.URL + "delete/id/" + id);
			}else{
				System.out.println("** Ambulancia: de vuelta en el hospital");
			}

		}catch (Exception e){
			System.out.println("** Ambulancia: Error metodo andar: " + e);
		}

		enviarMensaje("coordinadorHospital", "hospital", "hospital");
	}
}