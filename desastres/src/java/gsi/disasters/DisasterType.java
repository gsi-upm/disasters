package gsi.disasters;

/**
 *
 * @author Sergio
 */
public enum DisasterType {

    FIRE, FLOOD, COLLAPSE;

    public static DisasterType getType(String str) {
        if (str.toLowerCase().equals("fire")) {
            return DisasterType.FIRE;
        } else if (str.toLowerCase().equals("flood")) {
            return DisasterType.FLOOD;
        } else if (str.toLowerCase().equals("collapse")) {
            return DisasterType.COLLAPSE;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
