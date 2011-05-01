package gsi.disasters;

/**
 *
 * @author Sergio
 */
public enum DisasterType {

    FIRE, FLOOD, COLLAPSE, LOST_PERSON, INJURED_PERSON;

    public static DisasterType getType(String str) {
        if (str.toLowerCase().equals("fire")) {
            return DisasterType.FIRE;
        } else if (str.toLowerCase().equals("flood")) {
            return DisasterType.FLOOD;
        } else if (str.toLowerCase().equals("collapse")) {
            return DisasterType.COLLAPSE;
        } else if (str.toLowerCase().equals("lostPerson")) {
            return DisasterType.LOST_PERSON;
        } else if (str.toLowerCase().equals("injuredPerson")) {
            return DisasterType.INJURED_PERSON;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
