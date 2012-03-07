package disasters.caronte;

import jadex.bdi.runtime.*;

/**
 *
 * @author Juan Luis Molina
 */
public class ManagerCarontePlan extends Plan{

	public void body(){
		System.out.println("Comienza manager");
		IGoal sp1 = createGoal("cms_create_component");
		sp1.getParameter("type").setValue("jadex/desastres/Environment.agent.xml");
		dispatchSubgoalAndWait(sp1);
		System.out.println("Entorno creado");

		IGoal sp2 = createGoal("cms_create_component");
		sp2.getParameter("type").setValue("jadex/desastres/caronte/simulador/police/police.agent.xml");
		dispatchSubgoalAndWait(sp2);
		System.out.println("Policia creado");

		IGoal sp3 = createGoal("cms_create_component");
		sp3.getParameter("type").setValue("jadex/desastres/caronte/simulador/firemen/firemen.agent.xml");
		dispatchSubgoalAndWait(sp3);
		System.out.println("Bombero creado");

		IGoal sp4 = createGoal("cms_create_component");
		sp4.getParameter("type").setValue("jadex/desastres/caronte/simulador/ambulance/ambulance.agent.xml");
		dispatchSubgoalAndWait(sp4);
		System.out.println("Ambulancia creada");

		IGoal sp5 = createGoal("cms_create_component");
		sp5.getParameter("type").setValue("jadex/desastres/caronte/simulador/nurse/nurse.agent.xml");
		dispatchSubgoalAndWait(sp5);
		System.out.println("Enfermero creado");

		IGoal sp6 = createGoal("cms_create_component");
		sp6.getParameter("type").setValue("jadex/desastres/caronte/simulador/gerocultor/gerocultor.agent.xml");
		dispatchSubgoalAndWait(sp6);
		System.out.println("Gerocultor creado");

		IGoal sp9 = createGoal("cms_create_component");
		sp9.getParameter("type").setValue("jadex/desastres/caronte/simulador/auxiliar/auxiliar.agent.xml");
		dispatchSubgoalAndWait(sp9);
		System.out.println("Auxiliar creado");
		
		IGoal sp7 = createGoal("cms_create_component");
		sp7.getParameter("type").setValue("jadex/desastres/caronte/simulador/centralEmergencias/centralEmergencias.agent.xml");
		dispatchSubgoalAndWait(sp7);
		System.out.println("Central creada");
		
		IGoal sp8 = createGoal("cms_create_component");
		sp8.getParameter("type").setValue("jadex/desastres/caronte/simulador/coordinadorEmergencias/coordinadorEmergencias.agent.xml");
		dispatchSubgoalAndWait(sp8);
		System.out.println("Coordinador creado");
		

		IGoal sp = createGoal("cms_destroy_component");
		sp.getParameter("componentidentifier").setValue(getComponentIdentifier());
		System.out.println("destruyendo Manager...");
		dispatchSubgoalAndWait(sp);
	}
}