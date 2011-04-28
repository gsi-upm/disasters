package gsi.disasters;

/**
 *
 * @author Sergio
 */
public enum StateType {
    ACTIVE, CONTROLLED, ERASED;

        public static StateType getType(String str){
        if (str.toLowerCase().equals("active")) {
            return StateType.ACTIVE;
        }
            else if (str.toLowerCase().equals("controlled")) {
            return StateType.CONTROLLED;
        }
        else if (str.toLowerCase().equals("erased")) {
            return StateType.ERASED;
        }
        else return null;
    }

    @Override
    public String toString(){
        return super.toString().toLowerCase();
    }
}
