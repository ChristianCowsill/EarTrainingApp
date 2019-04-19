
package eartrainingapp;

import java.util.Random;

/**
 *
 * @author christian
 */
public class RandomNumberGenerator {
    
    Random random;
    int range;
    
    RandomNumberGenerator(){
        random = new Random();
    }
    
    public int getRandomNumber(int range){
        return random.nextInt(range);
    }

}
