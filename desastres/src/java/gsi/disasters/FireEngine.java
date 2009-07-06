
package gsi.disasters;

import java.util.List;

/**
 * Represents a fire engine
 * @author Luis Delgado
 */
public class FireEngine extends Resource {

    /**
     * Associate Doctors
     */
    private List<Fireman> associateFiremen;

    /**
     * Constructor of the ambulance
     * @param id
     * @param name
     * @param info
     * @param description
     * @param idAssigned
     * @param associateFiremen
     */
    public FireEngine(int id, String name, String info, String description,
            int idAssigned, List<Fireman> associateFiremen) {
        super(id, ResourceType.FIRE_ENGINE, name, info, description, idAssigned);
        this.associateFiremen = associateFiremen;
    }

    /**
     * Adds Firemen to the list
     * @param associateFiremen
     */
    public void addAssociateFiremen(List<Fireman> associateFiremen) {
        for (Fireman fireman : associateFiremen) {
            this.associateFiremen.add(fireman);
        }
    }

    /**
     * Returns the list of Firemen of the FireEngine
     * @return the list of Firemen
     */
    public List<Fireman> getAssociateFiremen() {
        return this.associateFiremen;
    }
}
