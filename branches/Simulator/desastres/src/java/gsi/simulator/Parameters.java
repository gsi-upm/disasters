/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsi.simulator;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
/**
 * This class contains the diferent parameters which define a simulation
 *
 * @author Sergio
 */
public class Parameters {

    /*
	 * Parameters value
	 */
	private final long seed;
	private final int frequency;

	/*
	 * Parameteres default value
	 */
	private final long DEFAULT_SEED = 1;
	private final int DEFAULT_FREQUENCY = 1;

	/*
	 * Tags to read parameteres from file
	 */
	private static final String SEED = "seed";
	private static final String FREQUENCY = "frequency";

	/**
	 * Default constructor
	 */
	public Parameters() {
		seed = DEFAULT_SEED;
		frequency = DEFAULT_FREQUENCY;
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
		long newSeed = DEFAULT_SEED;
		int newFrequency = DEFAULT_FREQUENCY;

		try {

			Properties properties = new Properties();
			in = new FileInputStream(file);
			properties.load(in);

			String fileSeed = properties.getProperty(SEED);
            newSeed = Long.parseLong(fileSeed);

			String fileFrequency = properties.getProperty(FREQUENCY);
			newFrequency = Integer.parseInt(fileFrequency);

			in.close();

		} catch (FileNotFoundException e) {
			System.out.println("File does not exist or cannot be opened. Default values "
                    + "will be used.");
			throw new IOException("File not found.");

		} finally {

			seed = newSeed;
			frequency = newFrequency;

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
	 * @param seed            to get a repetitive behaviour at random number sequences
	 * @param frequency       how often we refesh the simulator
	 * @throws IllegalArgumentException if any value is out of range
	 */
	public Parameters(long seed, int frequency) throws IllegalArgumentException{
		this.seed = seed;
		this.frequency = frequency;
		checkParameters();
	}

	/**
	 * Check that parameters values are correct
	 *
	 * @throws IllegalArgumentException if any value is out of range
	 */
	private void checkParameters() {
		if (frequency < 0)
			wrongParameter(Integer.toString(frequency), FREQUENCY);
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
	public String toString() {
		String a = "\n";
		a += "\t" + SEED + "=" + getSeed() + "\n";
		a += "\t" + FREQUENCY + "=" + getFrequency();
		return a;
	}

	/**
	 * Seed can force a repetitive behaviour of random number generator.
	 * If it's 0, the number series is diferent in every running.
	 * Any other value forces a repetitive series.
	 *
	 * @return seed to force a repetitive behaviour at random number sequences
	 */
	public long getSeed() {
		return seed;
	}

	/**
	 * Frequency of refreshing the simulator
	 *
	 * @return simulator frequency
	 */
	public int getFrequency() {
		return frequency;
	}
}
