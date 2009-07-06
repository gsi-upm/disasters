package gsi.disasters;

import java.util.List;

/**
 * Represents a police car
 * @author Luis Delgado
 */
public class PoliceCar extends Resource {

    /**
     * Associate Policemen
     */
    private List<Policeman> associatePolicemen;

    /**
     * Constructor of the PoliceCar
     * @param id
     * @param name
     * @param info
     * @param description
     * @param idAssigned
     * @param associatePolicemen
     */
    public PoliceCar(int id, String name, String info, String description,
            int idAssigned, List<Policeman> associatePolicemen) {
        super(id, ResourceType.POLICE_CAR, name, info, description, idAssigned);
        this.associatePolicemen = associatePolicemen;
    }

    /**
     * Adds Policemen to the list
     * @param associatePolicemen
     */
    public void addAssociateDoctors(List<Policeman> associatePolicemen) {
        for (Policeman policeman : associatePolicemen) {
            this.associatePolicemen.add(policeman);
        }
    }

    /**
     * Returns the list of Policeman of the PoliceCar
     * @return the list of Policeman
     */
    public List<Policeman> getAssociatePolicemen() {
        return this.associatePolicemen;
    }
}
