package gsi.simulator;

import gsi.disasters.DensityType;
import gsi.disasters.Disaster;
import gsi.disasters.SizeType;
import java.util.Random;

/**
 * This class provides methods for getting random-generated numbers
 * @author al.lopezf
 * @author Miguel
 */
public class RandomGenerator {
    /**
     * Random object
     */
     Random random;
     /**
      * Parameters object
      */
     Parameters params;

    /**
     * Constructor with parameters.
     * @param Parameters object, in order to get the value of the seed.
     * @throws java.lang.IllegalArgumentException if parameters are null
     */
    public RandomGenerator(Parameters params) throws IllegalArgumentException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters could not load correctly");
        }
        this.params= params;
        long seed = params.SEED;
        if (seed == 0) {
            random = new Random();
        } else {
            random = new Random(seed);
        }
    }

    /*
     * Generates gaussian numbers with a mean and a standard deviation
     * @param mean The mean of the Gaussian
     * @param standardDeviation The standard deviation of the Gaussian
     * @return The result of the execution of a Gaussian variable.
     */
    public int randomGaussian(double mean, double standardDeviation){
        return (int) Math.round((random.nextGaussian()*standardDeviation) + mean);
    }

    /**
     * @param min minimum number wanted
     * @param max maximum number wanted
     * @return a random number between min and max, both included
     */
    public static int randomInteger(int min, int max) throws IllegalArgumentException {
        if (min > max) {
            throw new IllegalArgumentException("Maximum value cannot be greater than the minimum one");
        }
        return min + (int)((max - min)*Math.random());
    }

    /**
     * @param min minimum number wanted
     * @param max maximum number wanted
     * @return a random number between min and max, both included
     */
    public static double randomDouble(double min, double max)
            throws IllegalArgumentException {
        if (min > max) {
            throw new IllegalArgumentException("Maximum value cannot be greater than the minimum one");
        }
        return min + (max - min) * Math.random();
    }

    /**
     * Returns the time between two refreshes of the simulator
     * @return
     */
    public int refreshPeriod(){
        if(params.IS_CONSTANT){
            return params.PERIOD;
        }
        else{
            return randomInteger(0,10);
        }
    }

    /**
     * Returns the time between two fire generations
     * @return
     */
    public int fireGeneratePeriod(){
        double mean = params.TIME_BETWEEN_FIRES;
        double standardDeviation = params.DEVIATION_FOR_FIRES;
        return randomGaussian(mean, standardDeviation);
    }

    /**
     * Defines a size for a fire
     * @return random size for a new fire
     */
    public SizeType fireDefineSize() {
        double size = Math.random();
        if (size < 0.25) {
            return SizeType.SMALL;
        } else if (size < 0.5) {
            return SizeType.MEDIUM;
        } else if (size < 0.75) {
            return SizeType.BIG;
        } else {
            return SizeType.HUGE;
        }
    }

    /**
     * Defines the strength of fires
     * @return random number of strength for a new fire
     */
    public int fireDefineStrength() {
        return randomInteger(params.MIN_FIRE_STRENGTH,params.MAX_FIRE_STRENGTH);
    }

    /**
     * Defines the density of traffic
     * @return random traffic density around the fire
     */
    public DensityType trafficDefineDensity() {
        double density = (Math.random() * 3);
        if (density < 1.0) {
            return DensityType.LOW;
        } else if (density < 2.0) {
            return DensityType.MEDIUM;
        } else {
            return DensityType.HIGH;
        }
    }

    /**
     * Returns an integer random value of the health points that a person
     * looses in a refresh depending on the strength of the disaster
     * @param strength strength of the disaster affecting the people
     * @return the number of healthpoints lost
     */
    public int healthPointsDecrease(int strength) {
        if (strength >= 80)
            return randomInteger(4,6);
        else if (strength >=40)
            return randomInteger(3,4);
        else
            return randomInteger(0,2);
    }

    /**
     * Same method than previous, but without strength. Used to compile,
     * should be deleted later.
     * @return the number of healthpoints lost
     */
    public int healthPointsDecrease() {
        return randomInteger(2,6);
    }

    /**
     * Generates the number of initial trapped victims
     * @return the number of initial trapped victims
     */
    public int initialTrappedVictims() {
        return randomInteger(params.MIN_TRAPPED_VICTIMS, params.MAX_TRAPPED_VICTIMS);
    }
    /**
     * Generates the number of initial slight victims
     * @return the number of initial slight victims
     */
    public int initialSlightVictims() {
        return randomInteger(params.MIN_SLIGHT_VICTIMS, params.MAX_SLIGHT_VICTIMS);
    }
    /**
     * Generates the number of initial serious victims
     * @return the number of initial serious victims
     */
    public int initialSeriousVictims() {
        return randomInteger(params.MIN_SERIOUS_VICTIMS, params.MAX_SERIOUS_VICTIMS);
    }
    /**
     * Generates the number of initial dead
     * @return the number of initial dead
     */
    public int initialDeadVictims() {
        return randomInteger(params.MIN_DEAD_VICTIMS, params.MAX_DEAD_VICTIMS);
    }

    /**
     * Initial random number of health points for slight victims
     * @return the number of health points
     */
    public int initialHealthPointsSlight() {
        return randomInteger(VictimManager.LIMIT_SERIOUS_SLIGHT,
                VictimManager.MAX_HEALTH_POINTS - 1);
    }

    /**
     * Initial random number of health points for serious victims
     * @return the number of health points
     */
    public int initialHealthPointsSerious() {
        return randomInteger(VictimManager.MIN_HEALTH_POINTS,
                VictimManager.LIMIT_SERIOUS_SLIGHT - 1);
    }

    /**
     * Returns a random value for the latitude with the specified limits
     * @return
     */
    public double randomLatitude() {
        return randomDouble(params.MIN_LATITUDE, params.MAX_LATITUDE);
    }

    /**
     * Returns a random value for the longitude with the specified limits
     * @return
     */
    public double randomLongitude() {
        return randomDouble(params.MIN_LONGITUDE, params.MAX_LONGITUDE);
    }

    /**
     * Reduces a random value of a disaster's strenght
     * @param disaster
     */
    public void reduceRandomStrength(Disaster disaster) {
        disaster.reduceStrength(RandomGenerator.randomInteger(0, 3));
    }

    /**
     * Increases a random value of a disaster's strenght
     * @param disaster
     */
    public void increaseRandomStrength(Disaster disaster) {
        disaster.increaseStrength(RandomGenerator.randomInteger(0, 5));
    }

    /**
     * Returns if a trapped gets into a victim
     * @param strength strength of the disaster
     * @return if a trapped gets into a victim
     */
    public boolean doesTrappedPassToVictim (int strength) {
        return Math.random() < (strength / 10) * params.TRAPPED_TO_VICTIM;
    }
}
