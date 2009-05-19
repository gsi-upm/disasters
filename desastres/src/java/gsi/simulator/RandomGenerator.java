package gsi.simulator;

/**
 * This class provides methods for getting random-generated numbers
 * @author al.lopezf
 */
public class RandomGenerator {

    /**
     *
     * @param min minimum number wanted
     * @param max maximum number wanted
     * @return a random number between min and max, both included
     */
    public static int randomInteger(int min, int max) {
        return min + (int) Math.floor((max - min + 1) * Math.random());
    }
}
