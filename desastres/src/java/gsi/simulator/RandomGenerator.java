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
        return min + Math.floor((max - min) * Math.random());
    }

    public int refreshPeriod(){
        if(params.isConstant()){
            return params.getFrequency();
        }
        else{
            return randomInteger(0,10);
        }
    }

    public int fireGeneratePeriod(){
        double mean = params.getTimeBeetwenFires();
        double standardDeviation = params.getDeviationForFires();
        return randomGaussian(mean, standardDeviation);
    }
    /**
     * Defines a size for a fire
     * @return random size for a new fire
     */
    public String fireDefineSize() {
        double size = Math.random();
        if (size < 0.25) {
            return "small";
        } else if (size < 0.5) {
            return "medium";
        } else if (size < 0.75) {
            return "big";
        } else {
            return "huge";
        }
    }

    /**
     * Defines the strength of fires
     * @return random number of strength for a new fire
     */
    public int fireDefineStrength() {
        return randomInteger(params.getMinFireStrength(),params.getMaxFireStrength());
    }

    /**
     * Defines the density of traffic
     * @return random traffic density around the fire
     */
    public String trafficDefineDensity() {
        double density = (Math.random() * 3);
        if (density < 1.0) {
            return "low";
        } else if (density < 2.0) {
            return "medium";
        } else {
            return "high";
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
            return randomInteger(7,10);
        else if (strength >=40)
            return randomInteger(4,7);
        else
            return randomInteger(0,4);
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
     * Generates a random value of health points for a victim to get initialized.
     * @return the number of health points
     */
    public int initialTrappedVictims() {
        return randomInteger(params.getMinTrappedVictims(),params.getMaxTrappedVictims());
    }
    public int initialSlightVictims() {
        return randomInteger(params.getMinSlightVictims(),params.getMaxSlightVictims());
    }
    public int initialSeriousVictims() {
        return randomInteger(params.getMinSeriousVictims(),params.getMaxSeriousVictims());
    }
    public int initialDeadVictims() {
        return randomInteger(params.getMinDeadVictims(),params.getMaxDeadVictims());
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
     * Returns a random value for the longlitude with the specified limits
     * @return
     */
    public double randomLongitude() {
        return randomDouble(params.MIN_LONGITUDE, params.MAX_LONGITUDE);
    }

}
