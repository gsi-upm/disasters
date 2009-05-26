package gsi.simulator;

/**
 * Generates new victims and manages the refreshment of the victims
 * @author luis
 */
public class VictimManager {
    /**
     * Minimum number of health points allowed. With this amount of health points
     * a person is considered DEAD.
     */
    public static final int MIN_HEALTH_POINTS = 0;
    /**
     * Maximum number of health points allowed.
     */
    public static final int MAX_HEALTH_POINTS = 100;
    /**
     * Limit of the transition SEVERE - SLIGHT. People with this or more
     * amount of health points are considered SLIGTH.
     */
    public static final int LIMIT_SEVERE_SLIGHT = 50;

    /**
     * Calculates the InjuryDegree of a Person
     * @param person whom degree we want to calculate
     * @return the InjuryDegree
     */
    public static InjuryDegree getVictimDegree(Person person) {
        if (person.getHealthPoints() >= LIMIT_SEVERE_SLIGHT) {
            return InjuryDegree.SLIGHT;
        }
        else if (person.getHealthPoints() > MIN_HEALTH_POINTS) {
            return InjuryDegree.SEVERE;
        }
        else {
            return InjuryDegree.DEAD;
        }
    }

    /**
     * Generates a new victim
     * @return
     */
    public static Person generateVictim() {
        int healthPoints = RandomGenerator.initialHealthPoints();
        return new Person(healthPoints);
    }

    /**
     * Refreshes the status of a victim: reduces his health points and
     * refreshes his InjuryDegree.
     * @param victim
     */
    public static void refreshVictim(Person victim) {
        victim.reduceHealthPoints(RandomGenerator.healthPointsDecrease());
    }
}
