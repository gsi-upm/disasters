package jadex.desastres.caronte.simulador.coordinadorEmergencias;

import jadex.bdi.runtime.IGoal;
import jadex.desastres.*;
import java.util.*;

/**
 * Plan del coordinador de emergencias para avisar al centro de emergencias.
 *
 * @author Juan Luis Molina
 *
 */
public class EncontrarEmergenciaPlan extends EnviarMensajePlan{

	/**
	 * Cuerpo del plan.
	 */
	private Disaster desastreAtendido;
	private Disaster desastreEvaluado;

	/**
	 * 
	 */
	public void body(){
		// Obtenemos un objeto de la clase entorno para poder usar sus metodos
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();
		Position posResi = (Position)getBeliefbase().getBelief("residencia").getFact();

		env.printout("OO coordinador: Buscando desastre",3);

		Disaster des = null;
		while(des == null){
			// nos devuelve en des la emergencia activa de mayor gravedad y que no
			// este borrado o null si no hay
			des = findDisaster(env);
		}

		//waitFor(10000); // Retardo para asociar heridos

		env.printout("OO coordinador: emergencia encontrada!!", 3);

		// EL COORDINADOR SE DESPLAZA HASTA LA EMERGENCIA PARA EVALUARLA
		/*
		try{
			env.andar(getComponentName(), (Position)getBeliefbase().getBelief("pos").getFact(), new Position(des.getLatitud(),des.getLongitud()), env.getAgent(getComponentName()).getId(), 0);
		}catch(Exception ex){
			System.out.println("Error al andar: " + ex);
		}
		*/

		getBeliefbase().getBelief("desastreActual").setFact(des.getId());
		// lo publicamos en el tablon!
		env.setTablon(des.getId());

		// EL COORDINADOR VUELVE HASTA SU POSICION EN LA RESIDENCIA
		/*
		try{
			env.andar(getComponentName(), (Position)getBeliefbase().getBelief("pos").getFact(), posResi, env.getAgent(getComponentName()).getId(), 0);
		}catch(Exception ex){
			System.out.println("Error al andar: " + ex);
		}
		*/

		// Creamos un nuevo objetivo
		IGoal avisarAgentes = createGoal("avisarAgentes");
		dispatchSubgoalAndWait(avisarAgentes);
	}

	/**
	 * 
	 * @param env
	 * @return 
	 */
	private Disaster findDisaster(Environment env){
		//System.out.println("OO coordinador: Comenzamos a buscar la emergencia mas grave...");
		Iterator it = env.disasters.entrySet().iterator();

		// waitFor(500);
		Map.Entry e = null;
		// Emergencia que va a ser procesada por los agentes
		desastreAtendido = null;
		// Emergencia con la que compite en gravedad la finalmente atendida
		desastreEvaluado = null;

		if(it.hasNext()){
			// Cogemos el primer desastre para compararlo con el resto encontrados
			System.out.println("OO coordinador: Extraemos la primera emergencia, sera la emergencia mas grave...");
			try{
				e = (Map.Entry) it.next();
				desastreAtendido = (Disaster) e.getValue();
				System.out.println("OO coordinador: La emergencia atendida es: " + desastreAtendido.getId());
			}catch(Exception exc){
				System.out.println("OO coordinador: Error extrayendo la primera emergencia: " + exc);
			}
			while(it.hasNext()){
				try{
					e = (Map.Entry) it.next();
					desastreEvaluado = (Disaster) e.getValue();
					System.out.println("OO coordinador: La emergencia evaluada es: " + desastreEvaluado.getId());

					if(desastreEvaluado.getState().equals("erased") || desastreEvaluado.getState().equals("controlled")){
						System.out.println("OO coordinador: La emergencia encontrada esta controlada...");
						continue;
					}
					boolean desastreMasGrave = false;
					// Comprobamos los heridos graves
					desastreMasGrave = compruebaGrave();
					if(desastreMasGrave){
						System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave...");
						continue;
					}
					// Comprobamos los heridos atrapados
					desastreMasGrave = compruebaAtrapado();
					if(desastreMasGrave){
						System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave...");
						continue;
					}
					// Comprobamos los heridos leves
					desastreMasGrave = compruebaLeve();
					if(desastreMasGrave){
						System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave...");
						continue;
					}
					// Comprobamos los heridos muertos
					desastreMasGrave = compruebaMuerto();
					if(desastreMasGrave){
						System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave...");
						continue;
					}
					// La emergencia no tiene heridos que tratar
					// Comprobamos el tamano de la emergencia
					System.out.println("OO coordinador: Comprobamos la magnitud de la emergencia...");
					String tamanoEvaluado = desastreEvaluado.getSize();
					String tamanoAtendido = desastreAtendido.getSize();
					if(tamanoAtendido.equals(tamanoEvaluado)){
						continue;
					}else{
						if(tamanoAtendido.equals("huge")){
							System.out.println("OO coordinador: El atendido es huge...");
							continue;
						}else{
							if((tamanoAtendido.equals("big") && tamanoEvaluado.equals("huge")) ||
									(tamanoAtendido.equals("medium") && (tamanoEvaluado.equals("huge") || tamanoEvaluado.equals("big")))||
									(tamanoAtendido.equals("small") && (tamanoEvaluado.equals("huge") || tamanoEvaluado.equals("big") || tamanoEvaluado.equals("medium")))){
								System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave...");
								// Hemos encontrado una emergencia mas grave
								desastreAtendido = desastreEvaluado;
							}else{
								continue;
							}
						}
					}
				}catch(Exception exc){
					System.out.println("OO coordinador: Error extrayendo las siguientes emergencias: " + exc);
				}
			}
		}
		if(desastreAtendido != null){
			System.out.println("OO coordinador: La emergencia mas grave es la: " + desastreAtendido.getId());
		}
		return desastreAtendido;
	}

	/**
	 * Devuelve true si hemos encontrado otro mas grave
	 * @return 
	 */
	private boolean compruebaGrave(){
		if(desastreAtendido.getSerious() != null && desastreEvaluado.getSerious() != null){
			boolean atendActivo = desastreAtendido.getSerious().getState().equals("active");
			boolean evalActivo = desastreEvaluado.getSerious().getState().equals("active");
			int numAtend = desastreAtendido.getSerious().getQuantity();
			int numEval = desastreEvaluado.getSerious().getQuantity();
			if(evalActivo == true && atendActivo == true && (numAtend < numEval)){
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave en serious...");
				return true;
			}
			if(evalActivo == true && atendActivo == false){
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave en serious...");
				return true;
			}
			return false;
		}
		if(desastreAtendido.getSerious() == null && desastreEvaluado.getSerious() != null){
			boolean evalActivo = desastreEvaluado.getSerious().getState().equals("active");
			if(evalActivo == true){
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave en serious...");
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Devuelve true si hemos encontrado otro mas grave
	 * @return 
	 */
	private boolean compruebaAtrapado(){
		if(desastreAtendido.getTrapped() != null && desastreEvaluado.getTrapped() != null){
			boolean atendActivo = desastreAtendido.getTrapped().getState().equals("active");
			boolean evalActivo = desastreEvaluado.getTrapped().getState().equals("active");
			int numAtend = desastreAtendido.getTrapped().getQuantity();
			int numEval = desastreEvaluado.getTrapped().getQuantity();
			if(evalActivo == true && atendActivo == true && numAtend < numEval){
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave en atrapado...");
				return true;
			}
			if(evalActivo == true && atendActivo == false){
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave en atrapado...");
				return true;
			}
			return false;
		}
		if(desastreAtendido.getTrapped() == null && desastreEvaluado.getTrapped() != null){
			boolean evalActivo = desastreEvaluado.getTrapped().getState().equals("active");
			if(evalActivo == true){
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave en atrapado...");
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Devuelve true si hemos encontrado otra mas grave
	 * @return 
	 */
	private boolean compruebaLeve(){
		if(desastreAtendido.getSlight() != null && desastreEvaluado.getSlight() != null){
			boolean atendActivo = desastreAtendido.getSlight().getState().equals("active");
			boolean evalActivo = desastreEvaluado.getSlight().getState().equals("active");
			int numAtend = desastreAtendido.getSlight().getQuantity();
			int numEval = desastreEvaluado.getSlight().getQuantity();
			if(evalActivo == true && atendActivo == true && numAtend < numEval){
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave en leve...");
				return true;
			}
			if(evalActivo == true && atendActivo == false){
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave en leve...");
				return true;
			}
			return false;
		}
		if(desastreAtendido.getSlight() == null && desastreEvaluado.getSlight() != null){
			boolean evalActivo = desastreEvaluado.getSlight().getState().equals("active");
			if(evalActivo == true){
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave en leve...");
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Devuelve true si hemos encontrado otra mas grave
	 * @return 
	 */
	private boolean compruebaMuerto(){
		if(desastreAtendido.getDead() != null && desastreEvaluado.getDead() != null){
			boolean atendActivo = desastreAtendido.getDead().getState().equals("active");
			boolean evalActivo = desastreEvaluado.getDead().getState().equals("active");
			int numAtend = desastreAtendido.getDead().getQuantity();
			int numEval = desastreEvaluado.getDead().getQuantity();
			if(evalActivo == true && atendActivo == true && numAtend < numEval){
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave en muerto...");
				return true;
			}
			if(evalActivo == true && atendActivo == false){
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave en muerto...");
				return true;
			}
			return false;
		}
		if(desastreAtendido.getDead() == null && desastreEvaluado.getDead() != null){
			boolean evalActivo = desastreEvaluado.getDead().getState().equals("active");
			if(evalActivo == true){
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("OO coordinador: Hemos encontrado una emergencia mas grave en muerto...");
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @param env
	 * @param tipo
	 * @return 
	 */
	private String giveMeAgent(Environment env, String tipo){
		Iterator it = env.agentes.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry e = null;
			// Hacemos este try para que no salga una excepcion de sinronizacion que no nos influye realmente
			try{
				e = (Map.Entry)it.next();
			}catch(Exception exc){}
			String nombreAgente;
			WorldObject agente = (WorldObject) e.getValue();
			if(agente.getType().equals(tipo)){
				nombreAgente = agente.getName();
				return nombreAgente;
			}
		}
		return null;
	}
}