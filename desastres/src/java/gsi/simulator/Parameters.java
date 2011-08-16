package gsi.simulator;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * This class contains the diferent parameters which define a simulation
 *
 * @author Sergio
 * @author al.lopezf
 * @author Miguel
 */
public class Parameters {

    /*
     * Defines if the simulation is going to refresh constantly (is in real time).
     */
    public final boolean IS_CONSTANT;
    /*
     * Seed for the number generation.
     */
    public final long SEED;
    /*
     * If 'constant' is true, we need to know what the value of the period of simulation is.
     */
    public final int PERIOD;
    /*
     * Mean of the time beetween new and random fires.
     */
    public final double TIME_BETWEEN_FIRES;
    /*
     * Standard deviation of the gaussian generating new fires.
     */
    public final double DEVIATION_FOR_FIRES;
    /**
     * Probability in a refresh for a trapped to turn into a victim.
     * 0 <= trappedToVictim <= 1
     */
    public final double TRAPPED_TO_VICTIM;
    /**
     * Minimum and maximum values for fire strength
     */
    public final int MIN_FIRE_STRENGTH;
    public final int MAX_FIRE_STRENGTH;
    /**
     * Minimum and maximum number of persons in each status
     */
    public final int MIN_TRAPPED_VICTIMS;
    public final int MAX_TRAPPED_VICTIMS;
    public final int MIN_SLIGHT_VICTIMS;
    public final int MAX_SLIGHT_VICTIMS;
    public final int MIN_SERIOUS_VICTIMS;
    public final int MAX_SERIOUS_VICTIMS;
    public final int MIN_DEAD_VICTIMS;
    public final int MAX_DEAD_VICTIMS;
    /**
     * Length of the simulation, in seconds
     */
    public final int LENGTH;

    /*
     * Default parameters value
     */
    public final boolean DEFAULT_IS_CONSTANT = true;
    public final long DEFAULT_SEED = 1;
    public final int DEFAULT_PERIOD = 1;
    public final double DEFAULT_TIME_BETWEEN_FIRES = 50;
    public final double DEFAULT_DEVIATION_FOR_FIRES = 20;
    public final double DEFAULT_TRAPPED_TO_VICTIM = 0.005;
    public final int DEFAULT_MIN_FIRE_STRENGTH = 1;
    public final int DEFAULT_MAX_FIRE_STRENGTH = 100;
    public final int DEFAULT_MIN_TRAPPED_VICTIMS = 0;
    public final int DEFAULT_MAX_TRAPPED_VICTIMS = 10;
    public final int DEFAULT_MIN_SLIGHT_VICTIMS = 0;
    public final int DEFAULT_MAX_SLIGHT_VICTIMS = 10;
    public final int DEFAULT_MIN_SERIOUS_VICTIMS = 0;
    public final int DEFAULT_MAX_SERIOUS_VICTIMS = 10;
    public final int DEFAULT_MIN_DEAD_VICTIMS = 0;
    public final int DEFAULT_MAX_DEAD_VICTIMS = 10;
    public final int DEFAULT_LENGTH = 60;
    /*
     * Tags to read parameteres from file
     */
    private final String IS_CONSTANT_TAG = "is constant";
    private final String SEED_TAG = "seed";
    private final String PERIOD_TAG = "period";
    private final String TIME_BETWEEN_FIRES_TAG = "time between fires";
    private final String DEVIATION_FOR_FIRES_TAG = "deviation for fires";
    private final String TRAPPED_TO_VICTIM_TAG = "trapped to victim";
    private final String MIN_FIRE_STRENGTH_TAG = "minimum fire strength";
    private final String MAX_FIRE_STRENGTH_TAG = "maximum fire strength";
    private final String MIN_TRAPPED_VICTIMS_TAG = "minimum number of trapped victims";
    private final String MAX_TRAPPED_VICTIMS_TAG = "maximum number of trapped victims";
    private final String MIN_SLIGHT_VICTIMS_TAG = "minimum number of slight victims";
    private final String MAX_SLIGHT_VICTIMS_TAG = "maximum number of slight victims";
    private final String MIN_SERIOUS_VICTIMS_TAG = "minimum number of serious victims";
    private final String MAX_SERIOUS_VICTIMS_TAG = "maximum number of serious victims";
    private final String MIN_DEAD_VICTIMS_TAG = "minimum number of dead victims";
    private final String MAX_DEAD_VICTIMS_TAG = "maximum number of dead victims";
    private final String LENGTH_TAG = "length";
	/**
     * Limits for latitude and longitude in Madrid
     */
    public final double MIN_LATITUDE_DISASTERS = 40.36;
    public final double MAX_LATITUDE_DISASTERS = 40.48;
    public final double MIN_LONGITUDE_DISASTERS = -3.78;
    public final double MAX_LONGITUDE_DISASTERS = -3.57;
    /**
     * Limits for latitude and longitude in Calasparra
     */
	 public final double MIN_LATITUDE_CARONTE = 38.232272;
	 public final double MAX_LATITUDE_CARONTE = MIN_LATITUDE_CARONTE;
	 public final double MIN_LONGITUDE_CARONTE = -1.698925;
	 public final double MAX_LONGITUDE_CARONTE = MIN_LONGITUDE_CARONTE;
    //public final double MIN_LATITUDE_CARONTE = 38.225;
    //public final double MAX_LATITUDE_CARONTE = 38.233;
    //public final double MIN_LONGITUDE_CARONTE = -1.71;
    //public final double MAX_LONGITUDE_CARONTE = -1.694;
    /**
     * Differences for marker's latitudes and longitudes
     */
    public final double SLIGHT_LATITUDE_DIFFERENCE = -0.00025;
    public final double SERIOUS_LATITUDE_DIFFERENCE = -0.00025;
    public final double TRAPPED_LATITUDE_DIFFERENCE = +0.00025;
    public final double DEAD_LATITUDE_DIFFERENCE = +0.00025;
    public final double SLIGHT_LONGITUDE_DIFFERENCE = -0.00025;
    public final double SERIOUS_LONGITUDE_DIFFERENCE = +0.00025;
    public final double TRAPPED_LONGITUDE_DIFFERENCE = -0.00025;
    public final double DEAD_LONGITUDE_DIFFERENCE = +0.00025;

    /**
     * Default constructor, with default parameters.
     */
    public Parameters() {
        IS_CONSTANT = DEFAULT_IS_CONSTANT;
        SEED = DEFAULT_SEED;
        PERIOD = DEFAULT_PERIOD;
        TIME_BETWEEN_FIRES = DEFAULT_TIME_BETWEEN_FIRES;
        DEVIATION_FOR_FIRES = DEFAULT_DEVIATION_FOR_FIRES;
        TRAPPED_TO_VICTIM = DEFAULT_TRAPPED_TO_VICTIM;
        MIN_FIRE_STRENGTH = DEFAULT_MIN_FIRE_STRENGTH;
        MAX_FIRE_STRENGTH = DEFAULT_MAX_FIRE_STRENGTH;
        MIN_TRAPPED_VICTIMS = DEFAULT_MIN_TRAPPED_VICTIMS;
        MAX_TRAPPED_VICTIMS = DEFAULT_MAX_TRAPPED_VICTIMS;
        MIN_SLIGHT_VICTIMS = DEFAULT_MIN_SLIGHT_VICTIMS;
        MAX_SLIGHT_VICTIMS = DEFAULT_MAX_SLIGHT_VICTIMS;
        MIN_SERIOUS_VICTIMS = DEFAULT_MIN_SERIOUS_VICTIMS;
        MAX_SERIOUS_VICTIMS = DEFAULT_MAX_SERIOUS_VICTIMS;
        MIN_DEAD_VICTIMS = DEFAULT_MIN_DEAD_VICTIMS;
        MAX_DEAD_VICTIMS = DEFAULT_MAX_DEAD_VICTIMS;
        LENGTH = DEFAULT_LENGTH;
    }

    /**
     * Constructor from file. If it doesn't exist, default values will be used.
     * If value associated to any of the file parameters doesn't exist or has
     * wrong format, some parameters will remain with file values and others
     * will have the default ones.
     *
     * @param file Configuration file where parameters are read
     * @throws IOException If problems when opening, reading or closing the file
     * @throws NumberFormatException If value associated to any file parameter
     * doesn't exist or has wrong format.
     * @throws IllegalArgumentException If object FileInputStream has a wrong
     * format or some of the final parameters is out of range.
     */
    public Parameters(String file)
            throws IOException, NumberFormatException, IllegalArgumentException {
        FileInputStream in = null;
        // All attributes are firstly initialized to 'default'. If the value
        // read from file is right, they will get it. They will remain default
        // otherwise.
        boolean newConstant = DEFAULT_IS_CONSTANT;
        long newSeed = DEFAULT_SEED;
        int newPeriod = DEFAULT_PERIOD;
        double newTimeBeetwenFires = DEFAULT_TIME_BETWEEN_FIRES;
        double newDeviationForFires = DEFAULT_DEVIATION_FOR_FIRES;
        double newTrappedToVictim = DEFAULT_TRAPPED_TO_VICTIM;
        int newMinFireStrength = DEFAULT_MIN_FIRE_STRENGTH;
        int newMaxFireStrength = DEFAULT_MAX_FIRE_STRENGTH;
        int newMinTrappedVictims = DEFAULT_MIN_TRAPPED_VICTIMS;
        int newMaxTrappedVictims = DEFAULT_MAX_TRAPPED_VICTIMS;
        int newMinSlightVictims = DEFAULT_MIN_SLIGHT_VICTIMS;
        int newMaxSlightVictims = DEFAULT_MAX_SLIGHT_VICTIMS;
        int newMinSeriousVictims = DEFAULT_MIN_SERIOUS_VICTIMS;
        int newMaxSeriousVictims = DEFAULT_MAX_SERIOUS_VICTIMS;
        int newMinDeadVictims = DEFAULT_MIN_DEAD_VICTIMS;
        int newMaxDeadVictims = DEFAULT_MAX_DEAD_VICTIMS;
        int newLength = DEFAULT_LENGTH;

        try {

            Properties properties = new Properties();
            in = new FileInputStream(file);
            properties.load(in);

            newConstant = Boolean.parseBoolean(properties.getProperty(IS_CONSTANT_TAG));
            newSeed = Long.parseLong(properties.getProperty(SEED_TAG));
            newPeriod = Integer.parseInt(properties.getProperty(PERIOD_TAG));

            newTimeBeetwenFires = Double.parseDouble(properties.getProperty(TIME_BETWEEN_FIRES_TAG));
            newDeviationForFires = Double.parseDouble(properties.getProperty(DEVIATION_FOR_FIRES_TAG));
            newTrappedToVictim = Double.parseDouble(properties.getProperty(TRAPPED_TO_VICTIM_TAG));

            newMinFireStrength = Integer.parseInt(properties.getProperty(MIN_FIRE_STRENGTH_TAG));
            newMaxFireStrength = Integer.parseInt(properties.getProperty(MAX_FIRE_STRENGTH_TAG));

            newMinTrappedVictims = Integer.parseInt(properties.getProperty(MIN_TRAPPED_VICTIMS_TAG));
            newMaxTrappedVictims = Integer.parseInt(properties.getProperty(MAX_TRAPPED_VICTIMS_TAG));

            newMinSlightVictims = Integer.parseInt(properties.getProperty(MIN_SLIGHT_VICTIMS_TAG));
            newMaxSlightVictims = Integer.parseInt(properties.getProperty(MAX_SLIGHT_VICTIMS_TAG));

            newMinSeriousVictims = Integer.parseInt(properties.getProperty(MIN_SERIOUS_VICTIMS_TAG));
            newMaxSeriousVictims = Integer.parseInt(properties.getProperty(MAX_SERIOUS_VICTIMS_TAG));

            newMinDeadVictims = Integer.parseInt(properties.getProperty(MIN_DEAD_VICTIMS_TAG));
            newMaxDeadVictims = Integer.parseInt(properties.getProperty(MAX_DEAD_VICTIMS_TAG));

            newLength = Integer.parseInt(properties.getProperty(LENGTH_TAG));

            in.close();

        } catch (FileNotFoundException e) {
            System.out.println("File does not exist or cannot be opened. Default values " + "will be used.");
            throw new IOException("File not found.");

        } finally {
            IS_CONSTANT = newConstant;
            SEED = newSeed;
            PERIOD = newPeriod;
            TIME_BETWEEN_FIRES = newTimeBeetwenFires;
            DEVIATION_FOR_FIRES = newDeviationForFires;
            TRAPPED_TO_VICTIM = newTrappedToVictim;
            MIN_FIRE_STRENGTH = newMinFireStrength;
            MAX_FIRE_STRENGTH = newMaxFireStrength;
            MIN_TRAPPED_VICTIMS = newMinTrappedVictims;
            MAX_TRAPPED_VICTIMS = newMaxTrappedVictims;
            MIN_SLIGHT_VICTIMS = newMinSlightVictims;
            MAX_SLIGHT_VICTIMS = newMaxSlightVictims;
            MIN_SERIOUS_VICTIMS = newMinSeriousVictims;
            MAX_SERIOUS_VICTIMS = newMaxSeriousVictims;
            MIN_DEAD_VICTIMS = newMinDeadVictims;
            MAX_DEAD_VICTIMS = newMaxDeadVictims;
            LENGTH = newLength;

            checkParameters();

            try {
                in.close();
            } catch (NullPointerException e) {
                System.err.println("Error closing the lecture object.");
            }
        }
    }

    /**
     * Constructor from explicit values
     *
     * @param constant           Defines if the simulation is going to refresh constantly.
     * @param seed               to get a repetitive behaviour at random number sequences
     * @param period          how often we refesh the simulator
     * @param timeBeetwenFires   Mean of the time beetween new and random fires.
     * @param deviationForFires  Standard deviation of the gaussian generating new fires.
     * @throws IllegalArgumentException if any value is out of range
     */
    public Parameters(boolean constant, long seed, int period,
            double timeBetweenFires, double deviationForFires,
            double trappedToVictim, int minFireStrength, int maxFireStrength,
            int minTrappedVictims, int maxTrappedVictims,
            int minSlightVictims, int maxSlightVictims,
            int minSeriousVictims, int maxSeriousVictims,
            int minDeadVictims, int maxDeadVictims, int length) throws IllegalArgumentException {
        this.IS_CONSTANT = constant;
        this.SEED = seed;
        this.PERIOD = period;
        this.TIME_BETWEEN_FIRES = timeBetweenFires;
        this.DEVIATION_FOR_FIRES = deviationForFires;
        this.TRAPPED_TO_VICTIM = trappedToVictim;
        this.MIN_FIRE_STRENGTH = minFireStrength;
        this.MAX_FIRE_STRENGTH = maxFireStrength;
        this.MIN_TRAPPED_VICTIMS = minTrappedVictims;
        this.MAX_TRAPPED_VICTIMS = maxTrappedVictims;
        this.MIN_SLIGHT_VICTIMS = minSlightVictims;
        this.MAX_SLIGHT_VICTIMS = maxSlightVictims;
        this.MIN_SERIOUS_VICTIMS = minSeriousVictims;
        this.MAX_SERIOUS_VICTIMS = maxSeriousVictims;
        this.MIN_DEAD_VICTIMS = minDeadVictims;
        this.MAX_DEAD_VICTIMS = maxDeadVictims;
        this.LENGTH = length;

        checkParameters();
    }

    /**
     * Check that parameters values are correct
     *
     * @throws IllegalArgumentException if any value is out of range
     */
    private void checkParameters() {
        if (PERIOD < 0) {
            wrongParameter(Integer.toString(PERIOD), PERIOD_TAG);
        }
        if (TIME_BETWEEN_FIRES < 0) {
            wrongParameter(Double.toString(TIME_BETWEEN_FIRES), TIME_BETWEEN_FIRES_TAG);
        }
        if (DEVIATION_FOR_FIRES < 0) {
            wrongParameter(Double.toString(DEVIATION_FOR_FIRES), DEVIATION_FOR_FIRES_TAG);
        }
        if (TRAPPED_TO_VICTIM < 0 || TRAPPED_TO_VICTIM > 1) {
            wrongParameter(Double.toString(TRAPPED_TO_VICTIM), TRAPPED_TO_VICTIM_TAG);
        }
        if (MAX_FIRE_STRENGTH < 1 || MAX_FIRE_STRENGTH > 100) {
            wrongParameter(Integer.toString(MAX_FIRE_STRENGTH), MAX_FIRE_STRENGTH_TAG);
        }
        if (MIN_FIRE_STRENGTH < 1 || MIN_FIRE_STRENGTH > MAX_FIRE_STRENGTH) {
            wrongParameter(Integer.toString(MIN_FIRE_STRENGTH), MIN_FIRE_STRENGTH_TAG);
        }
        if (MAX_TRAPPED_VICTIMS < 0) {
            wrongParameter(Integer.toString(MAX_TRAPPED_VICTIMS), MAX_TRAPPED_VICTIMS_TAG);
        }
        if (MIN_TRAPPED_VICTIMS < 0 || MIN_TRAPPED_VICTIMS > MAX_TRAPPED_VICTIMS) {
            wrongParameter(Integer.toString(MIN_TRAPPED_VICTIMS), MIN_TRAPPED_VICTIMS_TAG);
        }
        if (LENGTH <= 0) {
            wrongParameter(Integer.toString(LENGTH), LENGTH_TAG);
        }

    }

    /**
     * Throws IllegalArgumentException for a wrong parameter
     *
     * @param value parameter value
     * @param key param name
     * @throws IllegalArgumentException showing the wrong parameter
     */
    private void wrongParameter(String value, String key) {
        throw new IllegalArgumentException(key + "= " + value);
    }

    /**
     * Parameters list
     *
     * @return every parameter
     */
    @Override
    public String toString() {
        String a = "\n";
        a += "\t" + IS_CONSTANT_TAG + "=" + Boolean.toString(IS_CONSTANT) + "\n";
        a += "\t" + SEED_TAG + "=" + SEED + "\n";
        a += "\t" + PERIOD_TAG + "=" + PERIOD;
        a += "\t" + TIME_BETWEEN_FIRES_TAG + "=" + TIME_BETWEEN_FIRES;
        a += "\t" + DEVIATION_FOR_FIRES_TAG + "=" + DEVIATION_FOR_FIRES;
        a += "\t" + TRAPPED_TO_VICTIM_TAG + "=" + TRAPPED_TO_VICTIM;
        a += "\t" + MIN_FIRE_STRENGTH_TAG + "=" + MIN_FIRE_STRENGTH;
        a += "\t" + MAX_FIRE_STRENGTH_TAG + "=" + MAX_FIRE_STRENGTH;
        a += "\t" + MIN_TRAPPED_VICTIMS_TAG + "=" + MIN_TRAPPED_VICTIMS;
        a += "\t" + MAX_TRAPPED_VICTIMS_TAG + "=" + MAX_TRAPPED_VICTIMS;
        a += "\t" + MIN_SLIGHT_VICTIMS_TAG + "=" + MIN_SLIGHT_VICTIMS;
        a += "\t" + MAX_SLIGHT_VICTIMS_TAG + "=" + MAX_SLIGHT_VICTIMS;
        a += "\t" + MIN_SERIOUS_VICTIMS_TAG + "=" + MIN_SERIOUS_VICTIMS;
        a += "\t" + MAX_SERIOUS_VICTIMS_TAG + "=" + MAX_SERIOUS_VICTIMS;
        a += "\t" + MIN_DEAD_VICTIMS_TAG + "=" + MIN_DEAD_VICTIMS;
        a += "\t" + MAX_DEAD_VICTIMS_TAG + "=" + MAX_DEAD_VICTIMS;
        a += "\t" + LENGTH_TAG + "=" + LENGTH;

        return a;
    }
}
