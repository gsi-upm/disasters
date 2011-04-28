package jadex.desastres.sanitarios;

import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;

/**
 *
 * @author juanluis
 */
public class ManagerSanitarioPlan extends Plan {

	public void body(){
		System.out.println("Comienza manager sanitario");

		IGoal sp1 = createGoal("cms_create_component");
		sp1.getParameter("type").setValue("jadex/desastres/sanitarios/coordinadorMedico/coordinadorMedico.agent.xml");
		dispatchSubgoalAndWait(sp1);
		System.out.println("Coordinador medico creado");

		IGoal sp2 = createGoal("cms_create_component");
		sp2.getParameter("type").setValue("jadex/desastres/sanitarios/ambulancia/ambulancia.agent.xml");
		dispatchSubgoalAndWait(sp2);
		System.out.println("Ambulancia creada");

		IGoal sp3 = createGoal("cms_create_component");
		sp3.getParameter("type").setValue("jadex/desastres/sanitarios/grupoSanitarioOperativo/grupoSanitarioOperativo.agent.xml");
		dispatchSubgoalAndWait(sp3);
		System.out.println("GSO creado");

		IGoal sp4 = createGoal("cms_create_component");
		sp4.getParameter("type").setValue("jadex/desastres/sanitarios/medicoCACH/medicoCACH.agent.xml");
		dispatchSubgoalAndWait(sp4);
		System.out.println("Medico CACH creado");

		IGoal sp5 = createGoal("cms_create_component");
		sp5.getParameter("type").setValue("jadex/desastres/sanitarios/coordinadorHospital/coordinadorHospital.agent.xml");
		dispatchSubgoalAndWait(sp5);
		System.out.println("Coordinador hospital creado");

		IGoal sp = createGoal("cms_destroy_component");
		sp.getParameter("componentidentifier").setValue(getComponentIdentifier());
		System.out.println("destruyendo manager sanitario...");
		dispatchSubgoalAndWait(sp);
	}
}