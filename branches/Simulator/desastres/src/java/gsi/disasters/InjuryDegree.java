package gsi.disasters;

/**
 *
 * @author Luis Delgado
 */
public enum InjuryDegree {
    SLIGHT, SEVERE, DEAD, TRAPPED;

    public static InjuryDegree getType(String str){
        if (str.equals("slight") || str.equals("SLIGHT") || str.equals("Slight")) {
            return InjuryDegree.SLIGHT;
        }
        else if (str.equals("severe") || str.equals("SEVERE") || str.equals("Severe")) {
            return InjuryDegree.SEVERE;
        }
        else if (str.equals("dead") || str.equals("DEAD") || str.equals("Dead")) {
            return InjuryDegree.DEAD;
        }
        else if (str.equals("trapped") || str.equals("TRAPPED") || str.equals("Trapped")) {
            return InjuryDegree.TRAPPED;
        }
        else return null;
    }

    @Override
    public String toString(){
        String str;
        switch (this) {
            case SLIGHT: str = "slight";
                       break;
            case SEVERE: str = "severe";
                         break;
            case DEAD: str = "dead";
                       break;
            case TRAPPED: str = "trapped";
                       break;
            default: str="";
        }
        return str;
    }
}
