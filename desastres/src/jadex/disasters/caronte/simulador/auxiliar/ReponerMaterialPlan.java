package disasters.caronte.simulador.auxiliar;

import disasters.*;

/**
 *
 * @author Lorena Lopez Lebon
 */
public class ReponerMaterialPlan extends EnviarMensajePlan{

	public void body(){
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();

		String tipoMaterial = (String) enviarRespuesta("ack_reponer_material", "Material repuesto");
		//env.printout("XX auxiliar: Ack mandado", 0);

		if(tipoMaterial.equals("incendio")){
			getBeliefbase().getBelief("material-incendio").setFact(true);
			env.printout("XX auxiliar: material de incendios repuesto", 0);
		}else if(tipoMaterial.equals("medico")){
			getBeliefbase().getBelief("material-medico").setFact(true);
			env.printout("XX auxiliar: material medico repuesto", 0);
		}
	}
}