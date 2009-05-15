/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsi.simulator;

/**
 *
 * @author luis
 */
public class VictimManager {
    public static int LIMIT_SEVERE_SLIGHT = 50;

    public static InjuryDegree getVictimDegree(Person person) {
        if(person.getLifePoints() >= LIMIT_SEVERE_SLIGHT) {
            return InjuryDegree.SLIGHT;
        }
        else if(person.getLifePoints() > 0) {
            return InjuryDegree.SEVERE;
        }
        return InjuryDegree.DEAD;
    }

    public static Person generateVictim() {
        int lifePoints = (int) Math.floor(Person.MAX_LIFE_POINTS * Math.random() + 1);
        return new Person(lifePoints);
    }
}
