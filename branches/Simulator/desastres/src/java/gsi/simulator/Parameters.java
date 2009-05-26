
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
	/*
	 * Default parameters value
	 */
    private final boolean DEFAULT_CONSTANT = true;
	private final long DEFAULT_SEED = 1;
	private final int DEFAULT_FREQUENCY = 1;
    private final double DEFAULT_TIME_BEETWEN_FIRES = 600;
    private final double DEFAULT_DEVIATION_FOR_FIRES = 1;
    private final double DEFAULT_TRAPPED_TO_VICTIM = 0.1;


	/*
	 * Tags to read parameteres from file
	 */
    private static final String CONSTANT = "constant";
    private static final String SEED = "seed";
    private static final String FREQUENCY = "frequency";
    private static final String TIME_BEETWEN_FIRES = "time beetwen fires";
    private static final String DEVIATION_FOR_FIRES = "deviation for fires";
    private static final String TRAPPED_TO_VICTIM = "trapped to victim";

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

		try {

            Properties properties = new Properties();
            in = new FileInputStream(file);
            properties.load(in);

            String fileConstant = properties.getProperty(CONSTANT);
            newConstant = Boolean.parseBoolean(fileConstant);

            String fileSeed = properties.getProperty(SEED);
            newSeed = Long.parseLong(fileSeed);

            String fileFrequency = properties.getProperty(FREQUENCY);
            newFrequency = Integer.parseInt(fileFrequency);

            String fileTimeBeetwenFires = properties.getProperty(TIME_BEETWEN_FIRES);
            newTimeBeetwenFires = Double.parseDouble(fileTimeBeetwenFires);

            String fileDeviationForFires = properties.getProperty(DEVIATION_FOR_FIRES);
            newDeviationForFires = Double.parseDouble(fileDeviationForFires);

            String fileTrappedToVictim = properties.getProperty(TRAPPED_TO_VICTIM);
            newTrappedToVictim = Double.parseDouble(fileTrappedToVictim);

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
            double trappedToVictim) throws IllegalArgumentException{
        this.constant=constant;
		this.seed = seed;
		this.frequency = frequency;
        this.timeBeetwenFires = timeBeetwenFires;
        this.deviationForFires = deviationForFires;
        this.trappedToVictim = trappedToVictim;
        
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
        if(trappedToVictim < 0 || trappedToVictim > 1) {
            wrongParameter(Double.toString(trappedToVictim), TRAPPED_TO_VICTIM);
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

}
