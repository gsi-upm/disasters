package gsi.disasters;

/**
 *
 * @author Sergio
 */
public enum DensityType {

    HIGH, MEDIUM, LOW;

    public static DensityType getType(String str) {
        if (str.toLowerCase().equals("low")) {
            return DensityType.LOW;
        } else if (str.toLowerCase().equals("medium")) {
            return DensityType.MEDIUM;
        } else if (str.toLowerCase().equals("high")) {
            return DensityType.HIGH;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
