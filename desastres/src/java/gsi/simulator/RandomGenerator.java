package gsi.simulator;

import java.util.Random;

/**
 * This class provides methods for getting random-generated numbers
 * @author al.lopezf
 */
public class RandomGenerator {

     Random random;

    /**
     * Default constructor.
     */
    public RandomGenerator() {
        random = new Random();
    }

    /**
     * Constructor with parameters.
     * @param Parameters object, in order to get the value of the seed.
     */
    public RandomGenerator(Parameters params) {

        if (params == null) {
            random = new Random();
            throw new IllegalArgumentException("Parameters could not load correctly");
        }

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
    public  int randomGaussian(double mean, double standardDeviation){

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
    public int refreshPeriod(Parameters params){
        if(params.isConstant()){
            return params.getFrequency();
        }
        else{
            // TODO:  What should return? Maybe queue(1) time - queue(0) time...
            return 60;
        }
    }

        //TODO : Return a random value to generate random fires.
    public int fireGeneratePeriod(Parameters params){
        double mean = params.getTimeBeetwenFires();
        double standardDeviation = params.getDeviationForFires();
        return this.randomGaussian(mean, standardDeviation);
    }
}
