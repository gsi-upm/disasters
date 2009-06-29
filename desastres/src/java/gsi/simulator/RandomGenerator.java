package gsi.simulator;

import java.util.Random;

/**
 * This class provides methods for getting random-generated numbers
 * @author al.lopezf
 * @author Miguel
 */
public class RandomGenerator {

     Random random;
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

        long seed = params.getSeed();

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
        return min + (int) Math.floor((max - min + 1) * Math.random());
    }

    //TODO : Genearate random value for refresh period of the simulator.
    public int refreshPeriod(){
        if(params.isConstant()){
            return params.getFrequency();
        }
        else{
            // TODO:  What should return? Maybe queue(1) time - queue(0) time...
            return 60;
        }
    }

        //TODO : Return a random value to generate random fires.
    public int fireGeneratePeriod(){
        double mean = params.getTimeBeetwenFires();
        double standardDeviation = params.getDeviationForFires();
        return randomGaussian(mean, standardDeviation);
    }

    public String fireDefineSize() {
        double size = Math.random() * 3.0;
        if (size < 1.0) {
            return "small";
        } else if (size < 2.0) {
            return "medium";
        } else {
            return "large";
        }
    }
    public int fireDefineStrength() {
        return randomInteger(params.getMinFireStrength(),params.getMaxFireStrength());
    }
        //TODO: What is density? Determine density degrees
    public String fireDefineDensity() {
        return "";
    }

    /**
     * Returns an integer random value of the health points that a person
     * looses in a refresh.
     * @return the number of healthpoints lost
     */
    public static int healthPointsDecrease() {
        return randomInteger(2,6);
    }

    /**
     * Generates a random value of health points for a victim to get initialized.
     * @return the number of health points
     */
    public int initialTrappedVictims() {
        return randomInteger(params.getMinTrappedVictims(),params.getMaxTrappedVictims());
    }
    public int initialSlightVictims() {
        return randomInteger(params.getMinSlightVictims(),params.getMaxSlightVictims());
    }
    public int initialSevereVictims() {
        return randomInteger(params.getMinSevereVictims(),params.getMaxSevereVictims());
    }
    public int initialDeadVictims() {
        return randomInteger(params.getMinDeadVictims(),params.getMaxDeadVictims());
    }

    public static int initialHealthPoints() {
        return randomInteger(VictimManager.MIN_HEALTH_POINTS,
                VictimManager.MAX_HEALTH_POINTS);
    }

}
