package gsi.simulator;

import java.util.List;

/**
 *
 * @author Luis Delgado
 */
public class Fire extends Disaster {
    private int size;
    private int strength;
    private List<Person> associateVictims;
    private List<Person> associateDead;
    private List<Person> associateTrapped;
    private List<Fireman> associateFiremen;

    public Fire(int size, int strength,
            List<Person> associateVictims, List<Person> associateDead,
            List<Person> associateTrapped, List<Fireman> associateFiremen) {
        this.size = size;
        this.strength = strength;
        this.associateFiremen = associateFiremen;
        this.associateVictims = associateVictims;
    }

    public Fire(int size, int strength) {
        this(size, strength, null, null, null, null);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

        public List<Person> getAssociateVictims() {
        return associateVictims;
    }

    public void setAssociateVictims(List<Person> associateVictims) {
        this.associateVictims = associateVictims;
    }

    public List<Person> getAssociateDead() {
        return associateDead;
    }

    public void setAssociateDead(List<Person> associateDead) {
        this.associateDead = associateDead;
    }

    public List<Person> getAssociateTrapped() {
        return associateTrapped;
    }

    public void setAssociateTrapped(List<Person> associateTrapped) {
        this.associateTrapped = associateTrapped;
    }

    public List<Fireman> getAssociateFiremen() {
        return associateFiremen;
    }

    public void setAssociateFiremen(List<Fireman> associateFiremen) {
        this.associateFiremen = associateFiremen;
    }
}
