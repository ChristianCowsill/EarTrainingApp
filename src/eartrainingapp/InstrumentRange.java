
package eartrainingapp;

/**
 *
 * @author christian
 */
public enum InstrumentRange {
    
    GUITAR(40, 76, 27, 57, "Guitar"),
    BASS_GUITAR(28, 55, 35, 45, "Four String Bass"),
    PIANO(21, 108, 1, 60, "Piano"),
    TRUMPET(54, 86, 57, 69, "Trumpet")
    ;
    
    int lowestNote;
    int highestNote;
    int gmCode;
    int startingPitch;
    String name;
    
    InstrumentRange(int lowestNote, int highestNote, int gmCode, int startingPitch, String name){
        this.lowestNote = lowestNote;
        this.highestNote = highestNote;
        this.gmCode = gmCode;
        this.startingPitch = startingPitch;
        this.name = name;
    }

}
