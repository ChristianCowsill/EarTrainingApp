
package eartrainingapp;

import java.util.Random;

/**
 *
 * @author christian
 */
public abstract class Interval {

    int pureInterval;
    int alteredInterval;
    RandomNumberGenerator randomNumber;
    
    public Interval(RandomNumberGenerator randomNumber){
        this.randomNumber = randomNumber;
    }
    
    public int getInterval(){
               
        int pureOrAltered = randomNumber.getRandomNumber(2);// 0 = pure, 1 = alt
        int interval = 0;
        switch(pureOrAltered){
            case 0:
                interval = pureInterval;
                break;
            case 1:
                interval = alteredInterval;
                break;
        }
        
        return interval;
    }
    
}
