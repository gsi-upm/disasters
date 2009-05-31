package gsi.disasters;

/**
 *
 * @author Sergio
 */
public enum StateType {
    ACTIVE, CONTROLLED, ERASED;

        public static StateType getType(String str){
        if (str.equals("active") || str.equals("ACTIVE") || str.equals("Active")) {
            return StateType.ACTIVE;
        }
            else if (str.equals("controlled") || str.equals("CONTROLLED") || str.equals("Controlled")) {
            return StateType.CONTROLLED;
        }
        else if (str.equals("erased") || str.equals("ERASED") || str.equals("Erased")) {
            return StateType.ERASED;
        }
        else return null;
    }

    @Override
    public String toString(){
        String str;
        switch (this) {
            case ACTIVE: str = "active";
                       break;
            case CONTROLLED: str = "controlled";
                         break;
            case ERASED: str = "erased";
                       break;
            default: str="";
        }
        return str;
    }
}
