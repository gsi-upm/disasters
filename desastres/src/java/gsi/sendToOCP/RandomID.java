package gsi.sendToOCP;

import java.util.Random;

/**
 *  ELIMINARRRRRRRRRRRRRRRRRRRRRRRRRRRRR
 * @author lara
 */
public class RandomID { 
    
    public static int randomId(int id, Random generator){
        int r = generator.nextInt();

        while(r <= 0){
            r = generator.nextInt();
        }

        return r;
    }
    
    
    /* Para comprobar que el nº aleatorio generado es correcto 
    public static void main(String[] args){

        Random generator = new Random();
        int id = 1;

        while(true){
            int x = RandomID.randomId(id, generator);
            System.out.println("Nº aleatorio generado: "+Integer.toString(x));
        }

    }*/
    
}
