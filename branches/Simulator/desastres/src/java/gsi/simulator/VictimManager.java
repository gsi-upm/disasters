package gsi.simulator;

/**
 *
 * @author luis
 */
public class VictimManager {
    public static final int MAX_HEALTH_POINTS = 100;
    public static final int MIN_HEALTH_POINTS = 0;
    public static final int LIMIT_SEVERE_SLIGHT = 50;

    public static InjuryDegree getVictimDegree(Person person) {
        if (person.getHealthPoints() == MAX_HEALTH_POINTS) {
            return InjuryDegree.TRAPPED;
        }
        else if (person.getHealthPoints() >= LIMIT_SEVERE_SLIGHT) {
            return InjuryDegree.SLIGHT;
        }
        else if (person.getHealthPoints() > MIN_HEALTH_POINTS) {
            return InjuryDegree.SEVERE;
        }
        else {
            return InjuryDegree.DEAD;
        }
    }

    public static Person generateVictim() {
        int healthPoints = RandomGenerator.randomInteger(MIN_HEALTH_POINTS, MAX_HEALTH_POINTS);
        return new Person(healthPoints);
    }
}
