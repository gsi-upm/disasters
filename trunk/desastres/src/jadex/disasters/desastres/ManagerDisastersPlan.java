package disasters.desastres;

import jadex.bdi.runtime.*;

/**
 *
 * @author Juan Luis Molina
 */
public class ManagerDisastersPlan extends Plan{

	public void body(){
		System.out.println("Comienza manager");
		IGoal sp1 = createGoal("cms_create_component");
		sp1.getParameter("type").setValue("disasters/desastres/Environment.agent.xml");
		dispatchSubgoalAndWait(sp1);
		System.out.println("Entorno creado");

		IGoal sp2 = createGoal("cms_create_component");
		sp2.getParameter("type").setValue("disasters/desastres/police/police.agent.xml");
		dispatchSubgoalAndWait(sp2);
		System.out.println("Policia creado");

		IGoal sp3 = createGoal("cms_create_component");
		sp3.getParameter("type").setValue("disasters/desastres/firemen/firemen.agent.xml");
		dispatchSubgoalAndWait(sp3);
		System.out.println("Bombero creado");

		IGoal sp4 = createGoal("cms_create_component");
		//sp4.getParameter("type").setValue("disasters/desastres/ambulance/ambulance.agent.xml");
		sp4.getParameter("type").setValue("disasters/desastres/sanitarios/ManagerSanitario.agent.xml");
		dispatchSubgoalAndWait(sp4);
		//System.out.println("Ambulancia creada");

		waitFor(3000);

		IGoal sp5 = createGoal("cms_create_component");
		sp5.getParameter("type").setValue("disasters/desastres/central/central.agent.xml");
		dispatchSubgoalAndWait(sp5);
		System.out.println("Central creada");

		IGoal sp = createGoal("cms_destroy_component");
		sp.getParameter("componentidentifier").setValue(getComponentIdentifier());
		System.out.println("destruyendo Manager...");
		dispatchSubgoalAndWait(sp);
	}
}