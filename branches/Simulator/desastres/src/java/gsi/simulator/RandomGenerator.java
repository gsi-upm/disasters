package gsi.simulator;

import java.util.Random;

/**
 * This class provides methods for getting random-generated numbers
 * @author al.lopezf
 */
public class RandomGenerator {

     Random random;
     Parameters params;

    /**
     * Constructor with parameters.
     * @param Parameters object, in order to get the value of the seed.
     */
    public RandomGenerator(Parameters params) {

        if (params == null) {
            throw new IllegalArgumentException("Parameters could not load correctly");
        }

        this.params= params;

        long seed = params.getSeed();

        if (seed == 0) {
            random = new Random();
        }else{
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
    public static int randomInteger(int min, int max) {
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
    public static int initialHealthPoints() {
        return randomInteger(VictimManager.MIN_HEALTH_POINTS, 
                VictimManager.MAX_HEALTH_POINTS);
    }

}
