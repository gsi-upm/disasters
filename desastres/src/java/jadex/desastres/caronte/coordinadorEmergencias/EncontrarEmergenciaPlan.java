package jadex.desastres.caronte.coordinadorEmergencias;

import java.util.Iterator;
import java.util.Map;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan del coordinador de emergencias para avisar al centro de emergencias.
 *
 * @author Juan Luis Molina
 *
 */
public class EncontrarEmergenciaPlan extends EnviarMensajePlan {

	/**
	 * Cuerpo del plan.
	 */
	private Disaster desastreAtendido;
	private Disaster desastreEvaluado;

	public void body() {
		// Obtenemos un objeto de la clase entorno para poder usar sus metodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

		System.out.println("&& coordinador: Buscando desastre");

		Disaster des = null;
		while (des == null) {
			// nos devuelve en des la emergencia activa de mayor gravedad y que no
			// este borrado o null si no hay
			des = findDisaster(env);
		}

		System.out.println("&& coordinador: emergencia encontrada!!");

		getBeliefbase().getBelief("desastreActual").setFact(des.getId());
		// lo publicamos en el tablon!
		env.setTablon(des.getId());

		People herido = null;

		if (des.getSlight() != null) {
			herido = des.getSlight();
		}
		if (des.getSerious() != null) {
			herido = des.getSerious();
		}
		if (des.getDead() != null) {
			herido = des.getDead();
		}

		if (herido != null) {
			System.out.println("&& coordinador: He encontrado un herido cuya id es: " + herido.getId() + "!!");
			herido.setAtendido(true);
		} else {
			System.out.println("&& coordinador: La emergencia no tiene heridos!!");
		}

		System.out.println("&& coordinator: Avisando al enfermero...");
		String resultado1 = enviarMensaje("nurse", "aviso_geriatrico", "go");

		if ((des.getSize().equals("small") == false) ||
				((herido != null) && (herido.getType().equals("slight") == false))) {
			System.out.println("&& coordinator: Avisando la central... (en espera)...");
			String resultado = enviarMensaje("centralEmergencias", "aviso_geriatrico", "go");
			System.out.println("&& coordinador: Respuesta recibida de central: " + resultado);
		}

		//IMessageEvent req = waitForMessageEvent("terminado");
		//enviarRespuesta("ack_terminado", "Terminado recibido");
		//System.out.println("&& coordinator: Ack mandado");

		//Creamos un nuevo objetivo.
		IGoal esperaSolucion = createGoal("esperaSolucion");
		dispatchSubgoalAndWait(esperaSolucion);
	}

	private Disaster findDisaster(Environment env) {
		//System.out.println("&& coordinator: Comenzamos a buscar la emergencia mas grave...");
		Iterator it = env.disasters.entrySet().iterator();

		// waitFor(500);
		Map.Entry e = null;
		// Emergencia que va a ser procesada por los agentes
		desastreAtendido = null;
		// Emergencia con la que compite en gravedad la finalmente atendida
		desastreEvaluado = null;

		if (it.hasNext()) {
			// Cogemos el primer desastre para compararlo con el resto
			// encontrados
			System.out.println("&& coordinator: Extraemos la primera emergencia, sera la emergencia mas grave...");
			try {
				e = (Map.Entry) it.next();
				desastreAtendido = (Disaster) e.getValue();
				System.out.println("&& coordinator: La emergencia atendida es: " + desastreAtendido.getId());
			} catch (Exception exc) {
				System.out.println("&& coordinator: Error extrayendo la primera emergencia: " + exc);
			}
			while (it.hasNext()) {

				try {
					e = (Map.Entry) it.next();
					desastreEvaluado = (Disaster) e.getValue();
					System.out.println("&& coordinator: La emergencia evaluada es: " + desastreEvaluado.getId());

					if ((desastreEvaluado.getState().equals("erased"))
							|| (desastreEvaluado.getState().equals("controlled"))) {
						System.out.println("&& coordinator: La emergencia encontrada esta controlada...");
						continue;
					}
					boolean desastreMasGrave = false;
					// Comprobamos los heridos graves
					desastreMasGrave = compruebaGrave();
					if (desastreMasGrave) {
						System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave...");
						continue;
					}
					// Comprobamos los heridos atrapados
					desastreMasGrave = compruebaAtrapado();
					if (desastreMasGrave) {
						System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave...");
						continue;
					}
					// Comprobamos los heridos leves
					desastreMasGrave = compruebaLeve();
					if (desastreMasGrave) {
						System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave...");
						continue;
					}
					// Comprobamos los heridos muertos
					desastreMasGrave = compruebaMuerto();
					if (desastreMasGrave) {
						System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave...");
						continue;
					}
					// La emergencia no tiene heridos que tratar
					// Comprobamos el tamano de la emergencia
					System.out.println("&& coordinator: Comprobamos la magnitud de la emergencia...");
					String tamanoEvaluado = desastreEvaluado.getSize();
					String tamanoAtendido = desastreAtendido.getSize();
					if (tamanoAtendido.equals(tamanoEvaluado)) {
						continue;
					} else {
						if (tamanoAtendido.equals("huge")) {
							System.out.println("&& coordinator: El atendido es huge...");
							continue;
						} else {
							if (((tamanoAtendido.equals("big")) && (tamanoEvaluado.equals("huge")))
									|| ((tamanoAtendido.equals("medium")) && ((tamanoEvaluado.equals("huge")) || (tamanoEvaluado.equals("big"))))
									|| ((tamanoAtendido.equals("small")) && ((tamanoEvaluado.equals("huge"))
									|| (tamanoEvaluado.equals("big")) || (tamanoEvaluado.equals("medium"))))) {
								System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave...");
								// Hemos encontrado una emergencia mas grave
								desastreAtendido = desastreEvaluado;
							} else {
								continue;
							}
						}
					}
				} catch (Exception exc) {
					System.out.println("&& coordinator: Error extrayendo las siguientes emergencias: " + exc);
				}
			}
		}
		if(desastreAtendido != null){
			System.out.println("&& coordinator: La emergencia mas grave es la: " + desastreAtendido.getId());
		}
		return desastreAtendido;
	}

	// devuelve true si hemos encontrado otro mas grave
	private boolean compruebaGrave() {

		if (((desastreAtendido.getSerious()) != null)
				&& ((desastreEvaluado.getSerious()) != null)) {
			boolean atendActivo = desastreAtendido.getSerious().getState().equals("active");
			boolean evalActivo = desastreEvaluado.getSerious().getState().equals("active");
			int numAtend = desastreAtendido.getSerious().getQuantity();
			int numEval = desastreEvaluado.getSerious().getQuantity();
			if ((evalActivo == true) && (atendActivo == true)
					&& (numAtend < numEval)) {
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave en serious...");
				return true;
			}
			if ((evalActivo == true) && (atendActivo == false)) {
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave en serious...");
				return true;
			}
			return false;
		}
		if (((desastreAtendido.getSerious()) == null)
				&& ((desastreEvaluado.getSerious()) != null)) {
			boolean evalActivo = desastreEvaluado.getSerious().getState().equals("active");
			if (evalActivo == true) {
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave en serious...");
				return true;
			}
			return false;
		}
		return false;
	}

	// devuelve true si hemos encontrado otro mas grave
	private boolean compruebaAtrapado() {
		if (((desastreAtendido.getTrapped()) != null)
				&& ((desastreEvaluado.getTrapped()) != null)) {
			boolean atendActivo = desastreAtendido.getTrapped().getState().equals("active");
			boolean evalActivo = desastreEvaluado.getTrapped().getState().equals("active");
			int numAtend = desastreAtendido.getTrapped().getQuantity();
			int numEval = desastreEvaluado.getTrapped().getQuantity();
			if ((evalActivo == true) && (atendActivo == true)
					&& (numAtend < numEval)) {
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave en atrapado...");
				return true;
			}
			if ((evalActivo == true) && (atendActivo == false)) {
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave en atrapado...");
				return true;
			}
			return false;
		}
		if (((desastreAtendido.getTrapped()) == null)
				&& ((desastreEvaluado.getTrapped()) != null)) {
			boolean evalActivo = desastreEvaluado.getTrapped().getState().equals("active");
			if (evalActivo == true) {
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave en atrapado...");
				return true;
			}
			return false;
		}
		return false;
	}

	// devuelve true si hemos encontrado otra mas grave
	private boolean compruebaLeve() {

		if (((desastreAtendido.getSlight()) != null)
				&& ((desastreEvaluado.getSlight()) != null)) {
			boolean atendActivo = desastreAtendido.getSlight().getState().equals("active");
			boolean evalActivo = desastreEvaluado.getSlight().getState().equals("active");
			int numAtend = desastreAtendido.getSlight().getQuantity();
			int numEval = desastreEvaluado.getSlight().getQuantity();
			if ((evalActivo == true) && (atendActivo == true)
					&& (numAtend < numEval)) {
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave en leve...");
				return true;
			}
			if ((evalActivo == true) && (atendActivo == false)) {
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave en leve...");
				return true;
			}
			return false;
		}
		if (((desastreAtendido.getSlight()) == null)
				&& ((desastreEvaluado.getSlight()) != null)) {
			boolean evalActivo = desastreEvaluado.getSlight().getState().equals("active");
			if (evalActivo == true) {
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave en leve...");
				return true;
			}
			return false;
		}
		return false;
	}

	// devuelve true si hemos encontrado otra mas grave
	private boolean compruebaMuerto() {
		if (((desastreAtendido.getDead()) != null)
				&& ((desastreEvaluado.getDead()) != null)) {
			boolean atendActivo = desastreAtendido.getDead().getState().equals("active");
			boolean evalActivo = desastreEvaluado.getDead().getState().equals("active");
			int numAtend = desastreAtendido.getDead().getQuantity();
			int numEval = desastreEvaluado.getDead().getQuantity();
			if ((evalActivo == true) && (atendActivo == true)
					&& (numAtend < numEval)) {
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave en muerto...");
				return true;
			}
			if ((evalActivo == true) && (atendActivo == false)) {
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave en muerto...");
				return true;
			}
			return false;
		}
		if (((desastreAtendido.getDead()) == null)
				&& ((desastreEvaluado.getDead()) != null)) {
			boolean evalActivo = desastreEvaluado.getDead().getState().equals("active");
			if (evalActivo == true) {
				// Hemos encontrado una emergencia mas grave
				desastreAtendido = desastreEvaluado;
				System.out.println("&& coordinator: Hemos encontrado una emergencia mas grave en muerto...");
				return true;
			}
			return false;
		}
		return false;
	}

	private String giveMeAgent(Environment env, String tipo) {
		Iterator it = env.agentes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = null;
			// Hacemos este try para que no salga una excepcion de sinronizacion
			// que no nos influye realmente
			try {
				e = (Map.Entry) it.next();
			} catch (Exception exc) {
			}
			String nombreAgente;
			WorldObject agente = (WorldObject) e.getValue();
			if (agente.getType().equals(tipo)) {
				nombreAgente = agente.getName();
				return nombreAgente;
			}
		}
		return null;
	}
}