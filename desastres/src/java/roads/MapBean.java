package roads;

import java.util.*;

/**
 *
 * @author luis
 */
public class MapBean {
    HashMap<String,ArrayList> hashmap;
    
    public void setHashmap(HashMap<String,ArrayList> hm){
        this.hashmap = hm;
    }
    
    public HashMap getHashmap(){
        HashMap<String,ArrayList> hm = new HashMap<String,ArrayList>();
        ArrayList<String> al0 = new ArrayList<String>();
        al0.add("1 Gran Via, Madrid");
        al0.add("1 Noviciado, Madrid");
        hm.put("10", al0);
        hm.put("40", null);
        hashmap = hm;
        return hashmap;
    }
}
