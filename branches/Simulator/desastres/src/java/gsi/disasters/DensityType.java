package gsi.disasters;

/**
 *
 * @author Sergio
 */
public enum DensityType {
    HIGH, MEDIUM, LOW;

    public static DensityType getType(String str){
        if (str.equals("low") || str.equals("LOW") || str.equals("Low")) {
            return DensityType.LOW;
        }
        else if (str.equals("medium") || str.equals("MEDIUM") || str.equals("Medium")) {
            return DensityType.MEDIUM;
        }
        else if (str.equals("high") || str.equals("HIGH") || str.equals("High")) {
            return DensityType.HIGH;
        }
        else return null;
    }

    @Override
    public String toString(){
        String str;
        switch (this) {
            case LOW: str = "low";
                       break;
            case MEDIUM: str = "medium";
                         break;
            case HIGH: str = "high";
                       break;
            default: str="";
        }
        return str;
    }
}
