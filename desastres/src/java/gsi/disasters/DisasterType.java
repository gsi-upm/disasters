package gsi.disasters;

/**
 *
 * @author Sergio
 */
public enum DisasterType {
    FIRE, FLOOD, COLLAPSE;

    public static DisasterType getType(String str){
        if (str.equals("fire") || str.equals("FIRE") || str.equals("Fire")) {
            return DisasterType.FIRE;
        }
            else if (str.equals("flood") || str.equals("FLOOD") || str.equals("Flood")) {
            return DisasterType.FLOOD;
        }
        else if (str.equals("collapse") || str.equals("COLLAPSE") || str.equals("Collapse")) {
            return DisasterType.COLLAPSE;
        }
        else return null;
    }

    @Override
    public String toString(){
        String str;
        switch (this) {
            case FIRE: str = "fire";
                       break;
            case FLOOD: str = "flood";
                         break;
            case COLLAPSE: str = "collapse";
                       break;
            default: str="";
        }
        return str;
    }
}
