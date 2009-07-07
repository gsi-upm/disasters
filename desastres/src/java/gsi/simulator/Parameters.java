
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
     * Defines if the simulation is going to refresh constantly.
     */
    private final boolean constant;
    /*
     * Seed for the number generation.
     */
    private final long seed;
    /*
     * If 'constant' is true, we need to know what the value of the frequency of simulation is.
     */
    private final int frequency;
    /*
     * Mean of the time beetween new and random fires.
     */
    private final double timeBeetwenFires;
    /*
     * Standard deviation of the gaussian generating new fires.
     */
    private final double deviationForFires;
    /**
     * Probability in a refresh for a trapped to turn into a victim.
     * 0 <= trappedToVictim <= 1
     */
    private final double trappedToVictim;
    /**
     * Minimum and maximum values for fire strength
     */
    private final int minFireStrength;
    private final int maxFireStrength;
    /**
     * Minimum and maximum number of persons in each status
     */
    private final int minTrappedVictims;
    private final int maxTrappedVictims;
    private final int minSlightVictims;
    private final int maxSlightVictims;
    private final int minSeriousVictims;
    private final int maxSeriousVictims;
    private final int minDeadVictims;
    private final int maxDeadVictims;

    /**
     * Length of the simulation, in seconds
     */
    private final int length;

	/*
	 * Default parameters value
	 */
    private final boolean DEFAULT_CONSTANT = true;
	private final long DEFAULT_SEED = 1;
	private final int DEFAULT_FREQUENCY = 1;
    private final double DEFAULT_TIME_BEETWEN_FIRES = 600;
    private final double DEFAULT_DEVIATION_FOR_FIRES = 1;
    private final double DEFAULT_TRAPPED_TO_VICTIM = 0.1;
    private final int DEFAULT_MIN_FIRE_STRENGTH = 1;
    private final int DEFAULT_MAX_FIRE_STRENGTH = 100;
    //TODO: determine limits
    private final int DEFAULT_MIN_TRAPPED_VICTIMS = 0;
    private final int DEFAULT_MAX_TRAPPED_VICTIMS = 10;
    private final int DEFAULT_MIN_SLIGHT_VICTIMS = 0;
    private final int DEFAULT_MAX_SLIGHT_VICTIMS = 10;
    private final int DEFAULT_MIN_SERIOUS_VICTIMS = 0;
    private final int DEFAULT_MAX_SERIOUS_VICTIMS = 10;
    private final int DEFAULT_MIN_DEAD_VICTIMS = 0;
    private final int DEFAULT_MAX_DEAD_VICTIMS = 10;
    private final int DEFAULT_LENGTH = 1000;


	/*
	 * Tags to read parameteres from file
	 */
    private static final String CONSTANT = "constant";
    private static final String SEED = "seed";
    private static final String FREQUENCY = "frequency";
    private static final String TIME_BEETWEN_FIRES = "time beetwen fires";
    private static final String DEVIATION_FOR_FIRES = "deviation for fires";
    private static final String TRAPPED_TO_VICTIM = "trapped to victim";
    private static final String MIN_FIRE_STRENGTH = "minimum fire strength";
    private static final String MAX_FIRE_STRENGTH = "maximum fire strength";
    private static final String MIN_TRAPPED_VICTIMS = "minimum number of trapped victims";
    private static final String MAX_TRAPPED_VICTIMS = "maximum number of trapped victims";
    private static final String MIN_SLIGHT_VICTIMS = "minimum number of slight victims";
    private static final String MAX_SLIGHT_VICTIMS = "maximum number of slight victims";
    private static final String MIN_SERIOUS_VICTIMS = "minimum number of serious victims";
    private static final String MAX_SERIOUS_VICTIMS = "maximum number of serious victims";
    private static final String MIN_DEAD_VICTIMS = "minimum number of dead victims";
    private static final String MAX_DEAD_VICTIMS = "maximum number of dead victims";
    private static final String LENGTH = "length";
    
    /**
     * Limits for latitude and longitude in Madrid
     */
    public final double MIN_LATITUDE = 40.40;
    public final double MAX_LATITUDE = 40.45;
    public final double MIN_LONGITUDE = -3.73;
    public final double MAX_LONGITUDE = -3.68;
    /**
     * Differences for marker's latitudes and longitudes
     */
     public final double SLIGHT_LATITUDE_DIFFERENCE = - 0.00025;
     public final double SERIOUS_LATITUDE_DIFFERENCE = - 0.00025;
     public final double TRAPPED_LATITUDE_DIFFERENCE = + 0.00025;
     public final double DEAD_LATITUDE_DIFFERENCE = + 0.00025;
     public final double SLIGHT_LONGITUDE_DIFFERENCE = - 0.00025;
     public final double SERIOUS_LONGITUDE_DIFFERENCE = + 0.00025;
     public final double TRAPPED_LONGITUDE_DIFFERENCE = - 0.00025;
     public final double DEAD_LONGITUDE_DIFFERENCE = + 0.00025;

     public final double TRAPPED_TO_VICTIM_PROBABILITY = 0.05;
	/**
	 * Default constructor, with default parameters.
	 */
	public Parameters() {
        constant = DEFAULT_CONSTANT;
		seed = DEFAULT_SEED;
		frequency = DEFAULT_FREQUENCY;
        timeBeetwenFires = DEFAULT_TIME_BEETWEN_FIRES;
        deviationForFires = DEFAULT_DEVIATION_FOR_FIRES;
        trappedToVictim = DEFAULT_TRAPPED_TO_VICTIM;
        minFireStrength = DEFAULT_MIN_FIRE_STRENGTH;
        maxFireStrength = DEFAULT_MAX_FIRE_STRENGTH;
        minTrappedVictims = DEFAULT_MIN_TRAPPED_VICTIMS;
        maxTrappedVictims = DEFAULT_MAX_TRAPPED_VICTIMS;
        minSlightVictims = DEFAULT_MIN_SLIGHT_VICTIMS;
        maxSlightVictims = DEFAULT_MAX_SLIGHT_VICTIMS;
        minSeriousVictims = DEFAULT_MIN_SERIOUS_VICTIMS;
        maxSeriousVictims = DEFAULT_MAX_SERIOUS_VICTIMS;
        minDeadVictims = DEFAULT_MIN_DEAD_VICTIMS;
        maxDeadVictims = DEFAULT_MAX_DEAD_VICTIMS;
        length = DEFAULT_LENGTH;
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
		// Creo unas nuevas variables intermedias para cada parametro, inicializadas
		// al valor por defecto, que tomaran el valor leido del fichero en el caso
		// en que este tenga un formato adecuado.
        boolean newConstant = DEFAULT_CONSTANT;
        long newSeed = DEFAULT_SEED;
        int newFrequency = DEFAULT_FREQUENCY;
        double newTimeBeetwenFires = DEFAULT_TIME_BEETWEN_FIRES;
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

            newConstant = Boolean.parseBoolean(properties.getProperty(CONSTANT));
            newSeed = Long.parseLong(properties.getProperty(SEED));
            newFrequency = Integer.parseInt(properties.getProperty(FREQUENCY));

            newTimeBeetwenFires = Double.parseDouble(properties.getProperty(TIME_BEETWEN_FIRES));
            newDeviationForFires = Double.parseDouble(properties.getProperty(DEVIATION_FOR_FIRES));
            newTrappedToVictim = Double.parseDouble(properties.getProperty(TRAPPED_TO_VICTIM));

            newMinFireStrength = Integer.parseInt(properties.getProperty(MIN_FIRE_STRENGTH));
            newMaxFireStrength = Integer.parseInt(properties.getProperty(MAX_FIRE_STRENGTH));

            newMinTrappedVictims = Integer.parseInt(properties.getProperty(MIN_TRAPPED_VICTIMS));
            newMaxTrappedVictims = Integer.parseInt(properties.getProperty(MAX_TRAPPED_VICTIMS));

            newMinSlightVictims = Integer.parseInt(properties.getProperty(MIN_SLIGHT_VICTIMS));
            newMaxSlightVictims = Integer.parseInt(properties.getProperty(MAX_SLIGHT_VICTIMS));

            newMinSeriousVictims = Integer.parseInt(properties.getProperty(MIN_SERIOUS_VICTIMS));
            newMaxSeriousVictims = Integer.parseInt(properties.getProperty(MAX_SERIOUS_VICTIMS));

            newMinDeadVictims = Integer.parseInt(properties.getProperty(MIN_DEAD_VICTIMS));
            newMaxDeadVictims = Integer.parseInt(properties.getProperty(MAX_DEAD_VICTIMS));

            newLength = Integer.parseInt(properties.getProperty(LENGTH));

			in.close();

		} catch (FileNotFoundException e) {
			System.out.println("File does not exist or cannot be opened. Default values "
                    + "will be used.");
			throw new IOException("File not found.");

		} finally {
            constant = newConstant;
			seed = newSeed;
			frequency = newFrequency;
            timeBeetwenFires = newTimeBeetwenFires;
            deviationForFires = newDeviationForFires;
            trappedToVictim = newTrappedToVictim;
            minFireStrength = newMinFireStrength;
            maxFireStrength = newMaxFireStrength;
            minTrappedVictims = newMinTrappedVictims;
            maxTrappedVictims = newMaxTrappedVictims;
            minSlightVictims = newMinSlightVictims;
            maxSlightVictims = newMaxSlightVictims;
            minSeriousVictims = newMinSeriousVictims;
            maxSeriousVictims = newMaxSeriousVictims;
            minDeadVictims = newMinDeadVictims;
            maxDeadVictims = newMaxDeadVictims;
            length = newLength;

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
	 * @param frequency          how often we refesh the simulator
     * @param timeBeetwenFires   Mean of the time beetween new and random fires.
     * @param deviationForFires  Standard deviation of the gaussian generating new fires.
	 * @throws IllegalArgumentException if any value is out of range
	 */
	public Parameters(boolean constant,long seed, int frequency,
            double timeBeetwenFires, double deviationForFires,
            double trappedToVictim, int minFireStrength, int maxFireStrength,
            int minTrappedVictims, int maxTrappedVictims,
            int minSlightVictims, int maxSlightVictims,
            int minSeriousVictims, int maxSeriousVictims,
            int minDeadVictims, int maxDeadVictims, int length
            ) throws IllegalArgumentException {
        this.constant=constant;
		this.seed = seed;
		this.frequency = frequency;
        this.timeBeetwenFires = timeBeetwenFires;
        this.deviationForFires = deviationForFires;
        this.trappedToVictim = trappedToVictim;
        this.minFireStrength = minFireStrength;
        this.maxFireStrength = maxFireStrength;
        this.minTrappedVictims = minTrappedVictims;
        this.maxTrappedVictims = maxTrappedVictims;
        this.minSlightVictims = minSlightVictims;
        this.maxSlightVictims = maxSlightVictims;
        this.minSeriousVictims = minSeriousVictims;
        this.maxSeriousVictims = maxSeriousVictims;
        this.minDeadVictims = minDeadVictims;
        this.maxDeadVictims = maxDeadVictims;
        this.length = length;

		checkParameters();
	}

	/**
	 * Check that parameters values are correct
	 *
	 * @throws IllegalArgumentException if any value is out of range
	 */
	private void checkParameters() {
		if (frequency < 0){
			wrongParameter(Integer.toString(frequency), FREQUENCY);
        }
        if (timeBeetwenFires < 0){
            wrongParameter(Double.toString(timeBeetwenFires), TIME_BEETWEN_FIRES);
        }
        if (deviationForFires < 0){
            wrongParameter(Double.toString(deviationForFires), DEVIATION_FOR_FIRES);
        }
        if (trappedToVictim < 0 || trappedToVictim > 1) {
            wrongParameter(Double.toString(trappedToVictim), TRAPPED_TO_VICTIM);
        }
        if (maxFireStrength < 1 || maxFireStrength > 100) {
            wrongParameter(Integer.toString(maxFireStrength), MAX_FIRE_STRENGTH);
        }
        if (minFireStrength < 1 || minFireStrength > maxFireStrength) {
            wrongParameter(Integer.toString(minFireStrength), MIN_FIRE_STRENGTH);
        }
        if (maxTrappedVictims < 0) {
            wrongParameter(Integer.toString(maxTrappedVictims), MAX_TRAPPED_VICTIMS);
        }
        if (minTrappedVictims < 0 || minTrappedVictims > maxTrappedVictims) {
            wrongParameter(Integer.toString(minTrappedVictims), MIN_TRAPPED_VICTIMS);
        }
        if (length <= 0) {
            wrongParameter(Integer.toString(length), LENGTH);
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
        if(isConstant()){
            a += "\t" + CONSTANT + "=true" + "\n";
        }else{
            a += "\t" + CONSTANT + "=false" + "\n";
        }
		a += "\t" + SEED + "=" + this.seed + "\n";
		a += "\t" + FREQUENCY + "=" + this.frequency;
        a += "\t" + TIME_BEETWEN_FIRES + "=" + this.timeBeetwenFires;
        a += "\t" + DEVIATION_FOR_FIRES + "=" + this.deviationForFires;
        a += "\t" + TRAPPED_TO_VICTIM + "=" + this.trappedToVictim;

        a += "\t" + MIN_FIRE_STRENGTH + "=" + this.minFireStrength;
        a += "\t" + MAX_FIRE_STRENGTH + "=" + this.maxFireStrength;

        a += "\t" + MIN_TRAPPED_VICTIMS + "=" + this.minTrappedVictims;
        a += "\t" + MAX_TRAPPED_VICTIMS + "=" + this.maxTrappedVictims;
        a += "\t" + MIN_SLIGHT_VICTIMS + "=" + this.minSlightVictims;
        a += "\t" + MAX_SLIGHT_VICTIMS + "=" + this.maxSlightVictims;
        a += "\t" + MIN_SERIOUS_VICTIMS + "=" + this.minSeriousVictims;
        a += "\t" + MAX_SERIOUS_VICTIMS + "=" + this.maxSeriousVictims;
        a += "\t" + MIN_DEAD_VICTIMS + "=" + this.minDeadVictims;
        a += "\t" + MAX_DEAD_VICTIMS + "=" + this.maxDeadVictims;
        a += "\t" + LENGTH + "=" + this.length;

        return a;
	}

    /*
     * Getters
     */
    public int getFrequency() {
        return frequency;
    }

    public boolean isConstant() {
        return constant;
    }

    public long getSeed() {
        return seed;
    }

    public double getTimeBeetwenFires() {
        return timeBeetwenFires;
    }

    public double getDeviationForFires() {
        return deviationForFires;
    }

    public int getMinFireStrength() {
        return minFireStrength;
    }

    public int getMaxFireStrength() {
        return maxFireStrength;
    }

    public int getMinTrappedVictims() {
        return minTrappedVictims;
    }

    public int getMaxTrappedVictims() {
        return maxTrappedVictims;
    }

    public int getMinSlightVictims() {
        return minSlightVictims;
    }

    public int getMaxSlightVictims() {
        return maxSlightVictims;
    }

    public int getMinSeriousVictims() {
        return minSeriousVictims;
    }

    public int getMaxSeriousVictims() {
        return maxSeriousVictims;
    }

    public int getMinDeadVictims() {
        return minDeadVictims;
    }

    public int getMaxDeadVictims() {
        return maxDeadVictims;
    }

    public int getLength() {
        return length;
    }

}
