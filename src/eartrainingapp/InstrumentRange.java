
package eartrainingapp;

/**
 *
 * @author christian
 */
public enum InstrumentRange {
    
    GUITAR(40, 76, 27, 57),
    BASS_GUITAR(28, 55, 35, 45),
    PIANO(21, 108, 1, 60)
    ;
    
    int lowestNote;
    int highestNote;
    int gmCode;
    int startingPitch;
    
    InstrumentRange(int lowestNote, int highestNote, int gmCode, int startingPitch){
        this.lowestNote = lowestNote;
        this.highestNote = highestNote;
        this.gmCode = gmCode;
        this.startingPitch = startingPitch;
    }

}
