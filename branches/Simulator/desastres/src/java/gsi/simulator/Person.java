/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gsi.simulator;

/**
 *
 * @author Luis Delgado
 */
public class Person {
    private int lifePoints;
    public static int MAX_LIFE_POINTS = 100;

    public Person() {
        this.lifePoints = MAX_LIFE_POINTS;
    }

    public Person(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public int getLifePoints() {
        return lifePoints;
    }
}
