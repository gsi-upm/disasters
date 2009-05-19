package gsi.simulator;

/**
 *
 * @author Luis Delgado
 */
public class Person {

    /**
     * Persons' health points
     */
    private int healthPoints;

    /**
     * Persons' injury degree (life status)
     */
    private  InjuryDegree injuryDegree;

    /**
     * Health bounds
     */
    public static final int MIN_HEALTH_POINTS = 0;
    public static final int MAX_HEALTH_POINTS = 100;

    /**
     * Default constructor. Intact person
     */
    public Person() {
        this.healthPoints = MAX_HEALTH_POINTS;
        this.setInjuryDegree();
    }

    /**
     * Creates a new person from health points
     *
     * @param healthPoints person's health points
     */
    public Person(int healthPoints) {
        this.healthPoints = healthPoints;
        this.setInjuryDegree();
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
}
