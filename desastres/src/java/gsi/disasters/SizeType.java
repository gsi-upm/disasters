package gsi.disasters;

/**
 *
 * @author Sergio
 */
public enum SizeType {
    HUGE, BIG, MEDIUM, SMALL;

    public static SizeType getType(String str){
        if (str.equals("small") || str.equals("SMALL") || str.equals("Small")) {
            return SizeType.SMALL;
        }
            else if (str.equals("medium") || str.equals("MEDIUM") || str.equals("Medium")) {
            return SizeType.MEDIUM;
        }
        else if (str.equals("big") || str.equals("BIG") || str.equals("Big")) {
            return SizeType.BIG;
        }
        else if (str.equals("huge") || str.equals("HUGE") || str.equals("Huge")) {
            return SizeType.HUGE;
        }
        else return null;
    }

    @Override
    public String toString(){
        String str;
        switch (this) {
            case SMALL: str = "small";
                       break;
            case MEDIUM: str = "medium";
                         break;
            case BIG: str = "big";
                       break;
            case HUGE: str = "huge";
                       break;
            default: str="";
        }
        return str;
    }
}
