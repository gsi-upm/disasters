package gsi.simulator;

/**
 * This class provides methods for getting random-generated numbers
 * @author al.lopezf
 */
public class RandomGenerator {
    public static int randomInteger(int min, int max) {
        return min + (int) Math.floor((max - min + 1) * Math.random());
    }
}
