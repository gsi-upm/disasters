package gsi.disasters;

/**
 *
 * @author Luis Delgado
 */
public enum InjuryDegree {
    SLIGHT, SERIOUS, DEAD, TRAPPED;

    public static InjuryDegree getType(String str){
        if (str.toLowerCase().equals("slight")) {
            return InjuryDegree.SLIGHT;
        }
        else if (str.toLowerCase().equals("serious")) {
            return InjuryDegree.SERIOUS;
        }
        else if (str.toLowerCase().equals("dead")) {
            return InjuryDegree.DEAD;
        }
        else if (str.toLowerCase().equals("trapped")) {
            return InjuryDegree.TRAPPED;
        }
        else return null;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
