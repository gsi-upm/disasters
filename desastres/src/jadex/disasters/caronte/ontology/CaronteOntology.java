package disasters.caronte.ontology;

/**
 *  Generated Java class for ontology caronte.
 */
public class CaronteOntology{
	//-------- constants --------

	/** The name of the ontology. */
	public static final String ONTOLOGY_NAME = "caronte";

	/** The allowed java classes. */
	public static java.util.HashSet java_classes = new java.util.HashSet();

	//-------- static part --------

	static{
		String[] sp = java.beans.Introspector.getBeanInfoSearchPath();
		String[] nsp = new String[sp.length+1];
		System.arraycopy(sp, 0, nsp, 0, sp.length);
		nsp[nsp.length-1] = "disasters.caronte.ontology";
		// Use try/catch for applets / webstart, etc.
		try{
			java.beans.Introspector.setBeanInfoSearchPath(nsp);
		}catch(SecurityException e){
			System.out.println("Warning: Cannot set BeanInfo search path 'disasters.caronte.ontology'.");
		}
	}
}