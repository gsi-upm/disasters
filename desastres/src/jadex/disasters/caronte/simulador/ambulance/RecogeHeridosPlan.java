package disasters.caronte.simulador.ambulance;

import disasters.*;
import disasters.caronte.Entorno;
import disasters.caronte.simulador.ontology.Desastre;
import org.json.me.*;

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
		// Obtenemos un objeto de la clase Entorno para poder usar sus metodos
		Entorno env = (Entorno) getBeliefbase().getBelief("env").getFact();

		Desastre recibido = (Desastre)enviarRespuestaObjeto("ack_aviso", "Aviso recibido");
		//env.printout("AA ambulance: Ack mandado", 0);

		// Posicion actual de la ambulancia, que le permite recoger al herido.
		Position posicionActual = (Position) getBeliefbase().getBelief("pos").getFact();

		// Posicion del hospital que le corresponde
		Position posicionHospital = (Position) getBeliefbase().getBelief("hospital").getFact();

		//id y posicion del Desastre atendiendose
		int idDes = recibido.getId();
		getBeliefbase().getBelief("idEmergencia").setFact(idDes);
		Disaster des = env.getEvent(idDes);
		Position posicionDesastre = new Position(des.getLatitud(), des.getLongitud());
		String estadoHerido = recibido.getEstadoHeridos();

		env.printout("AA ambulance: Estoy destinado al desastre " + idDes + " con herido " + estadoHerido, 2, 0, true);

		//sacamos el herido
		People herido = getHerido(des);

		try{
			env.andar(getComponentName(), posicionActual, posicionDesastre, env.getAgent(getComponentName()).getId(), 0);
		}catch(Exception e){
			System.out.println("AA ambulance: Error metodo andar: " + e);
		}

		int id = 0;
		if(herido != null){ // Leves atendidos por el enfermero de la residencia
			while((herido = getHerido(des)) != null){
				id = herido.getId();
				env.printout("AA ambulance: Tengo herido " + id, 2, 0, true);

				//deasociar los heridos del desastre
				env.printout("AA ambulance: quitando la asociacion del herido " + id, 2, 0, true);
				String resultado1 = Connection.connect(Entorno.URL + "put/" + id + "/idAssigned/0");
				if(herido.getType().equals("slight")){
					des.setSlight();
				}else if(herido.getType().equals("serious")){
					des.setSerious();
				}else if(herido.getType().equals("dead")){
					des.setDead();
				}

				try{
					String herAux = Connection.connect(Entorno.URL + "person/" + id);
					JSONObject her = (new JSONArray(herAux)).getJSONObject(0);
					Position posHerido1 = new Position(new Double(her.getString("latitud")), new Double(her.getString("longitud")));
					Position posHerido2 = new Position(new Double(her.getString("latitud")), new Double(her.getString("longitud"))+0.0006);
					posicionActual = (Position) getBeliefbase().getBelief("pos").getFact();

					env.andar(getComponentName(), posicionActual, posHerido1, env.getAgent(getComponentName()).getId(), 0);
					env.andar(getComponentName(), posHerido1, posicionHospital, env.getAgent(getComponentName()).getId(), id);
					if(!herido.getType().equals("dead")){
						env.printout("AA ambulance: curando herido " + id, 2, 0, true);
						String resultado = Connection.connect(Entorno.URL + "healthy/id/" + id);
						env.printout("AA ambulance: llevando de vuelta a " + id + " a la residencia", 2, 0, true);
						env.andar(getComponentName(), posicionHospital, posHerido2, env.getAgent(getComponentName()).getId(), id);
					}else{
						env.printout("AA ambulance: depositando muerto " + id, 2, 0, true);
						String resultado = Connection.connect(Entorno.URL + "delete/id/" + id);
					}
				}catch (Exception ex){
					System.out.println("AA ambulance: Error: " + ex);
				}
			}
		} else {
			env.printout("AA ambulance: Desastre sin heridos", 2, 0, true);
		}

		// La ambulancia regresa a su hospital correspondiente.
		try{
			env.andar(getComponentName(), posicionActual, posicionHospital, env.getAgent(getComponentName()).getId(), 0);
			env.printout("AA ambulance: de vuelta en el hospital", 2, 0, true);
		}catch(Exception e){
			System.out.println("AA ambulance: Error metodo andar: " + e);
		}
	}

	/**
	 * Coge un herido.
	 * 
	 * @param des desastre
	 * @return herido
	 */
	private People getHerido(Disaster des){
		People herido = null;

		// No atiende leves porque le corresponde al enfermero
		if(des.getSerious() != null){
			herido = des.getSerious();
		}else if(des.getDead() != null){
			herido = des.getDead();
		}

		return herido;
	}
}