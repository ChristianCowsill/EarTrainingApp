package eartrainingapp;

import java.util.Random;

/**
 *
 * @author christian
 */
public class NoteGenerator {

    private RandomNumberGenerator randomNumber;
    private Interval interval;
    private InstrumentRange instrumentRange;

    public NoteGenerator(InstrumentRange instrumentRange, Interval interval, RandomNumberGenerator randomNumber) {

        this.instrumentRange = instrumentRange;
        this.interval = interval;
        this.randomNumber = randomNumber;

    }

    public int generateNote(int previousNote) {

        int upOrDown = randomNumber.getRandomNumber(2);
        int newNote = 0;
        boolean inRange = false;

        while (!inRange) {
            newNote = interval.getInterval();
            switch (upOrDown) {
                case 0: // UP                   
                    newNote = previousNote + newNote;
                    if (newNote <= instrumentRange.highestNote) {
                        inRange = true;
                    } else {
                        inRange = false;
                        upOrDown = 1; // If the note is out of range, switch to down
                    }
                    break;
                case 1://DOWN
                    newNote = previousNote - newNote;
                    if (newNote >= instrumentRange.lowestNote) {
                        inRange = true;
                    } else {
                        inRange = false; 
                        upOrDown = 0; // if the note is out of range, switch to up

                    }
            }
        }
        return newNote;
    }

    public int getStartingNote() {

        return instrumentRange.startingPitch;
    }

}
