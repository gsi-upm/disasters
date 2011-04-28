package gsi.disasters;

/**
 *
 * @author Sergio
 */
public enum SizeType {

    HUGE, BIG, MEDIUM, SMALL;

    public static SizeType getType(String str) {
        if (str.toLowerCase().equals("small")) {
            return SizeType.SMALL;
        } else if (str.toLowerCase().equals("medium")) {
            return SizeType.MEDIUM;
        } else if (str.toLowerCase().equals("big")) {
            return SizeType.BIG;
        } else if (str.toLowerCase().equals("huge")) {
            return SizeType.HUGE;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
