package maps;

/**
 *
 * @author luis
 */
public class DirectionsBean {
    private String start = "";
    private String end = "";

    public DirectionsBean(String start, String end) {
        this.setStart(start);
        this.setEnd(end);
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }
    
    public void setEnd (String end){
        this.end=end;
    }
} // .EOF
