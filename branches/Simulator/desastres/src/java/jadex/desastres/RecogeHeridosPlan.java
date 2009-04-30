package jadex.desastres;

import jadex.runtime.Plan;

/**
 * Plan de la AMBULANCIA para recoger heridos.
 * 
 * @author Olimpia Hernandez
 * 
 */
public class RecogeHeridosPlan extends Plan {

	/**
	 * Cuerpo del plan
	 */
	public void body() {
		// Obtenemos un objeto de la clase Environment para poder usar sus
		// métodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

		// Posición actual de la ambulancia, que le permite recoger al herido.
		Position posicionActual = (Position) getBeliefbase().getBelief("pos").getFact();

		
		// Posicion del hospital que le corresponde
		Position posicionHospital = (Position) getBeliefbase().getBelief("hospitalMadrid").getFact();
		int idDes = env.getTablon();
		
		//Espero a que se borre el desastre (lo borra el bombero) para irme a otra cosa...
		Disaster des = env.getEvent(idDes);
		Position positionDesastre = new Position(des.getLatitud(), des.getLongitud());
		
		//sacamos el herido
		People herido=null;
		
		if (des.getSlight() != null){herido=des.getSlight(); }
		if( des.getSerious() != null) {herido=des.getSerious();}
		if(des.getDead() != null){herido=des.getDead();}
		
		System.out.println("** ambulance: Tengo herido "+herido.getId());
		
		
		try {
			env.andar(getAgentName(), posicionActual, positionDesastre,env.getAgent(getAgentName()).getId(),0);
			
			
		} catch (Exception e) {
			System.out.println("**ambulance: Error metodo andar: "+e);
		}
					
		//deasociar los heridos del desastre
		int id = herido.getId();
		System.out.println("** ambulance: quitando la asociacion del herido "+id);
		String resultado1 = Connection.connect(Environment.DISASTERS+"put/"+id+"/idAssigned/"+"0");
		
		
		// La ambulancia regresa a su hospital correspondiente.
		try {
			env.andar(getAgentName(), posicionActual, posicionHospital,env.getAgent(getAgentName()).getId(),id);
			//y deposita al herido
			System.out.println("** ambulance: depositando herido "+id);
			String resultado = Connection.connect(Environment.DISASTERS+"delete/id/"+id);
			//System.out.println(resultado);
			
		} catch (Exception e) {
			System.out.println("** ambulance: Error metodo andar: "+e);
		}
		
	}

}
