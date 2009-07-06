/**
package gsi.simulator;

import gsi.simulator.VictimManager;
import gsi.disasters.Fireman;
import gsi.disasters.Person;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Luis Delgado


//TODO now there are methods for generating random numbers of persons in different health status
//at class RandomGenerator
public class FireGenerator {
    public static int MIN_INITIAL_SIZE = 3;
    public static int MAX_INITIAL_SIZE = 7;
    public static int MIN_STRENGTH = 1;
    public static int MAX_STRENGTH = 5;
    public static int MIN_INITIAL_VICTIMS = 3;
    public static int MAX_INITIAL_VICTIMS = 10;
    public static int MIN_INITIAL_DEAD = 0;
    public static int MAX_INITIAL_DEAD = 4;
    public static int MIN_INITIAL_TRAPPED = 3;
    public static int MAX_INITIAL_TRAPPED = 10;
    public static int MIN_INITIAL_FIREMEN = 0;
    public static int MAX_INITIAL_FIREMEN = 5;
    
    public static Fire generateFire() {
        /* We'll have to insert random values. This also generates a Fire with
         * size 5, strength 3, 2 firemen and 5 victims.
        //Size and Strength
        int size = RandomGenerator.randomInteger(MIN_INITIAL_SIZE, MAX_INITIAL_SIZE);
        int strength = RandomGenerator.randomInteger(MIN_STRENGTH, MAX_STRENGTH);

        //Victims
        List<Person> victims = new ArrayList<Person>();
        for(int i = 0; i < RandomGenerator.randomInteger(MIN_INITIAL_VICTIMS,
                MAX_INITIAL_VICTIMS); i++) {
            victims.add(VictimManager.generateVictim());
        }

        //Deads
        List<Person> dead = new ArrayList<Person>();
        for(int i = 0; i < RandomGenerator.randomInteger(MIN_INITIAL_DEAD,
                MAX_INITIAL_DEAD); i++) {
            victims.add(new Person(0));
        }

        //Trapped
        List<Person> trapped = new ArrayList<Person>();
        for(int i = 0; i < RandomGenerator.randomInteger(MIN_INITIAL_TRAPPED,
                MAX_INITIAL_TRAPPED); i++) {
            victims.add(new Person());
        }

        //Firemen
        List<Fireman> firemen = new ArrayList<Fireman>();
        for(int i = 0; i < RandomGenerator.randomInteger(MIN_INITIAL_FIREMEN,
                MAX_INITIAL_FIREMEN); i++) {
            victims.add(new Fireman());
        }
        return new Fire(size, strength, victims, dead, trapped, firemen);
    }
}
*/