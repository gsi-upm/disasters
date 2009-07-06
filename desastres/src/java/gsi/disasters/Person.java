package gsi.disasters;

import gsi.simulator.VictimManager;
import gsi.simulator.*;

/**
 *
 * @author Luis Delgado
 */
public class Person {

    /**
     * Identifier
     */
    private int id;

    /**
     * Persons' health points
     */
    private int healthPoints;

    /**
     * Persons' injury degree (life status)
     */
    private  InjuryDegree injuryDegree;

    /**
     * Minimum life points
     */
    public static final int MIN_HEALTH_POINTS = 0;
    /**
     * Maximum life points
     */
    public static final int MAX_HEALTH_POINTS = 100;

    /**
     * Default constructor. Intact person
     * @param id identifier of the person
     */
    public Person(int id) {
        this(id, MAX_HEALTH_POINTS);
    }

    /**
     * Creates a new person from health points
     * @param id identifier of the person
     * @param healthPoints person's health points
     */
    public Person(int id, int healthPoints) {
        this.healthPoints = healthPoints;
        this.setInjuryDegree();
    }

    /**
     * Returns the person's identifier
     * @return the person's identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Setter. It updates the injury degree
     *
     * @param healthPoints health points to assign
     */
    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
        this.setInjuryDegree();
    }

    /**
     * Getter
     *
     * @return number of health points
     */
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * This sets the injury degree from the person's health points
     */
    public void setInjuryDegree() {
        this.injuryDegree = VictimManager.getVictimDegree(this);
    }

    /**
     * Getter
     *
     * @return injury degree
     */
    public InjuryDegree getInjuryDegree() {
        return this.injuryDegree;
    }

    /**
     * Reduces the person's number of health points passed as parameter.
     * @param loss
     */
    public void reduceHealthPoints(int loss) {
        this.healthPoints -= loss;
        this.setInjuryDegree();
    }

    /**
     * Check if the person is a slight victim
     * @return if the person is a slight victim
     */
    public boolean isSlight() {
        return this.injuryDegree == InjuryDegree.SLIGHT;
    }

    /**
     * Check if the person is a severe victim
     * @return if the person is a severe victim
     */
    public boolean isSerious() {
        return this.injuryDegree == InjuryDegree.SERIOUS;
    }

    /**
     * Check if the person is dead
     * @return if the person is dead
     */
    public boolean isDead() {
        return this.injuryDegree == InjuryDegree.DEAD;
    }
}
