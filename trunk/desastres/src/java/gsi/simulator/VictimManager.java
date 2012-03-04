package gsi.simulator;

import gsi.disasters.InjuryDegree;
import gsi.disasters.Person;



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
     * Limit of the transition SERIOUS - SLIGHT. People with this or more
     * amount of health points are considered SLIGTH.
     */
    public static final int LIMIT_SERIOUS_SLIGHT = 50;

    /**
     * Calculates the InjuryDegree of a Person
     * @param person whom degree we want to calculate
     * @return the InjuryDegree
     */
    public static InjuryDegree getVictimDegree(Person person) {
        if (person.getHealthPoints() >= LIMIT_SERIOUS_SLIGHT) {
            return InjuryDegree.SLIGHT;
        }
        else if (person.getHealthPoints() > MIN_HEALTH_POINTS) {
            return InjuryDegree.SERIOUS;
        }
        else {
            return InjuryDegree.DEAD;
        }
    }

    /**
     * Generates a new slight victim
     * @return Slight victim
     */
    public static Person generateSlightVictim(int id, Parameters param) {
        RandomGenerator generator = new RandomGenerator(param);
        return new Person(id, generator.initialHealthPointsSlight());
    }

    /**
     * Generates a new serious victim
     * @return Serious victim
     */
    public static Person generateSeriousVictim(int id, Parameters param) {
        RandomGenerator generator = new RandomGenerator(param);
        return new Person(id, generator.initialHealthPointsSerious());
    }

    /**
     * Refreshes the status of a victim: reduces his health points and
     * refreshes his InjuryDegree.
     * @param victim
     */
    public static void refreshVictim(Person victim, Parameters param) {
        RandomGenerator generator = new RandomGenerator(param);
        victim.reduceHealthPoints(generator.healthPointsDecrease());
    }

    /**
     * Generates a default trapped
     * @param id identifier of the person
     * @return the created trapped person
     */
    public static Person generateDefaultTrapped(int id) {
        return new Person(id, Person.MAX_HEALTH_POINTS);
    }

    /**
     * Generates a default dead
     * @param id identifier dead
     * @return the created dead
     */
    public static Person generateDefaultDead(int id) {
        return new Person(id, Person.MIN_HEALTH_POINTS);
    }
}
