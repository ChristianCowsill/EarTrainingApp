
package eartrainingapp;

import java.util.Random;

/**
 *
 * @author christian
 */
public class RandomInterval extends Interval{
    
    public RandomInterval(RandomNumberGenerator randomNumber){
        super(randomNumber);
    }
    
    @Override
    public int getInterval(){
        Random rand = new Random();
        return rand.nextInt(12);
    }
}
