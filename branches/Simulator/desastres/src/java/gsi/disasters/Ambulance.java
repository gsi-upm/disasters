package gsi.disasters;

import java.util.List;

/**
 * Represents an ambulance
 * @author Luis Delgado
 */
public class Ambulance extends Resource {

    /**
     * Associate Doctors
     */
    private List<Doctor> associateDoctors;

    /**
     * Constructor of the ambulance
     * @param id
     * @param name
     * @param info
     * @param description
     * @param idAssigned
     * @param associateDoctors
     */
    public Ambulance(int id, String name, String info, String description,
            int idAssigned, List<Doctor> associateDoctors) {
        super(id, ResourceType.AMBULANCE, name, info, description, idAssigned);
        this.associateDoctors = associateDoctors;
    }

    /**
     * Adds Doctors to the list
     * @param associateDoctor
     */
    public void addAssociateDoctors(List<Doctor> associateDoctor) {
        for (Doctor doctor : associateDoctor) {
            this.associateDoctors.add(doctor);
        }
    }

    /**
     * Returns the list of Doctors of the Ambulance
     * @return the list of Doctors
     */
    public List<Doctor> getAssociateDoctors() {
        return this.associateDoctors;
    }
}
