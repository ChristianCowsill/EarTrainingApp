/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eartrainingapp;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;

/**
 *
 * @author christian
 */
public class EarTrainingApp{

    /**
     * @param args the command line arguments
     */
    private RandomNumberGenerator randomNumberGenerator;
    private NoteGenerator noteGenerator;
    private Interval intervalOne;
    private Interval intervalTwo;
    private ArrayList<Interval> intervalList;
    private InstrumentRange instrument;
    private int tempo, trackEventTotal;

    public static void main(String[] args) {

        EarTrainingApp app = new EarTrainingApp();
        try {
            app.play();
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(EarTrainingApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(EarTrainingApp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void play() throws MidiUnavailableException, InvalidMidiDataException {
        
        randomNumberGenerator = new RandomNumberGenerator();
        intervalOne = new SecondInterval(randomNumberGenerator);
        intervalTwo = new SeventhInterval(randomNumberGenerator);
        intervalList = new ArrayList<>();
        intervalList.add(intervalOne);
        intervalList.add(intervalTwo);
        instrument = InstrumentRange.GUITAR;
        tempo = 60;
        trackEventTotal = 40;       
        noteGenerator = new NoteGenerator(instrument, intervalList, randomNumberGenerator);

        Sequence sequence = new Sequence(Sequence.PPQ, Constant.QUARTER_NOTE_VALUE);
        Track track = sequence.createTrack();
        track.add(addEvent(Constant.PROGRAM_CHANGE, Constant.CHANNEL, instrument.gmCode, 0, 1));

        createNoteSequence(track);
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setTempoInBPM(tempo);
        sequencer.setSequence(sequence);
        sequencer.start();
    }

    private MidiEvent addEvent(int comd, int channel, int one, int two, int tick) throws InvalidMidiDataException {

        ShortMessage a = new ShortMessage();
        a.setMessage(comd, channel, one, two);
        MidiEvent event = new MidiEvent(a, tick);

        return event;

    }

    void createNoteSequence(Track track) throws InvalidMidiDataException {

        // Add instrument starting note to track
        int startingNote = instrument.startingPitch;
        int tickCounter = 1;
        int loopCounter = 0;
        while (loopCounter < trackEventTotal) {
            if (tickCounter == 1) {
                addEvents(track, startingNote, tickCounter);
                tickCounter += Constant.HALF_NOTE_VALUE;
            } else {
                int previousNote = track.get(track.size() - 2).getMessage().getMessage()[1];
                int newNote = noteGenerator.generateNote(previousNote);
                addEvents(track, newNote, tickCounter);
                tickCounter += Constant.HALF_NOTE_VALUE;
            }
            loopCounter++;
        }

    }

    void addEvents(Track track, int note, int tick) throws InvalidMidiDataException {
        track.add(addEvent(Constant.NOTE_ON, Constant.CHANNEL, note, Constant.VELOCITY, tick));
        track.add(addEvent(Constant.NOTE_OFF, Constant.CHANNEL, note, Constant.VELOCITY, tick + Constant.QUARTER_NOTE_VALUE));
    }
    
}
