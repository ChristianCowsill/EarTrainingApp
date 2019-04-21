/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eartrainingapp;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author christian
 */
public class EarTrainingApp {

    /**
     * @param args the command line arguments
     */
    private RandomNumberGenerator randomNumberGenerator;
    private NoteGenerator noteGenerator;
    private ArrayList<Interval> intervalList;
    private InstrumentRange instrument;
    private int tempo, trackEventTotal;
    private GUI gui;
    private static EarTrainingApp app;
    private JTextArea bpmTextArea;
    private JTextArea sequenceLengthTextArea;
    private Sequencer sequencer;

    public EarTrainingApp() {
        randomNumberGenerator = new RandomNumberGenerator();
        intervalList = new ArrayList<>();
        Interval interval = new RandomInterval(randomNumberGenerator);
        intervalList.add(interval);
        tempo = 120;
        gui = new GUI();
    }

    public static void main(String[] args) {

        app = new EarTrainingApp();
        app.gui.go();

    }

    private void play() throws MidiUnavailableException, InvalidMidiDataException {

        if (intervalList.size() > 0) {
            instrument = InstrumentRange.GUITAR;

            if (!bpmTextArea.getText().equals("")) {
                tempo = Integer.parseInt(bpmTextArea.getText());
            } else {
                tempo = Constant.DEFAULT_BPM;
                bpmTextArea.setText(String.valueOf(Constant.DEFAULT_BPM));
            }

            if (!sequenceLengthTextArea.getText().equals("")) {
                trackEventTotal = Integer.parseInt(sequenceLengthTextArea.getText());
            } else {
                trackEventTotal = Constant.DEFAULT_LENGTH;
                sequenceLengthTextArea.setText(String.valueOf(Constant.DEFAULT_LENGTH));
            }
            noteGenerator = new NoteGenerator(instrument, intervalList, randomNumberGenerator);

            Sequence sequence = new Sequence(Sequence.PPQ, Constant.QUARTER_NOTE_VALUE);
            Track track = sequence.createTrack();
            track.add(addEvent(Constant.PROGRAM_CHANGE, Constant.CHANNEL, instrument.gmCode, 0, 1));

            createNoteSequence(track);
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setTempoInBPM(tempo);
            sequencer.setSequence(sequence);
            sequencer.start();
        }

    }
    
    private void stop(){
        sequencer.stop();
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

    class GUI {

        public void go() {

            // Master frame
            JFrame frame = new JFrame();
            frame.setSize(200, 300);
            frame.setLayout(new FlowLayout());

            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent windowEvent) {
                    System.exit(0);
                }
            });

            JPanel masterPanel = new JPanel();

            frame.getContentPane().add(masterPanel);
            masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));

            // Build Top Panel
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
            JPanel bpmPanel = new JPanel();
            bpmPanel.setLayout(new FlowLayout());
            JLabel bpmLabel = new JLabel("Set BPM:");
            bpmTextArea = new JTextArea(String.valueOf(Constant.DEFAULT_BPM));
            JPanel lengthPanel = new JPanel();
            lengthPanel.setLayout(new FlowLayout());
            JLabel lengthLabel = new JLabel("Set Sequence Length");
            sequenceLengthTextArea = new JTextArea(String.valueOf(Constant.DEFAULT_LENGTH));

            bpmPanel.add(bpmLabel);
            bpmPanel.add(bpmTextArea);
            lengthPanel.add(lengthLabel);
            lengthPanel.add(sequenceLengthTextArea);

            topPanel.add(bpmPanel);
            topPanel.add(lengthPanel);

            // Build Bottom Panel
            JPanel bottomPanel = new JPanel();

            JPanel checkBoxPanel = new JPanel();
            checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

            JCheckBox chkRandom = new JCheckBox("Random");
            chkRandom.setName("random");
            chkRandom.setSelected(true);
            JCheckBox chkSecond = new JCheckBox("Second");
            chkSecond.setName("second");
            JCheckBox chkThird = new JCheckBox("Third");
            chkThird.setName("third");
            JCheckBox chkFourth = new JCheckBox("Fourth");
            chkFourth.setName("fourth");
            JCheckBox chkFifth = new JCheckBox("Fifth");
            chkFifth.setName("fifth");
            JCheckBox chkSixth = new JCheckBox("Sixth");
            chkSixth.setName("sixth");
            JCheckBox chkSeventh = new JCheckBox("Seventh");
            chkSeventh.setName("seventh");

            class CheckBoxListener implements ItemListener {

                @Override
                public void itemStateChanged(ItemEvent e) {

                    JCheckBox chkBox = (JCheckBox) e.getSource();
                    if (chkBox.isSelected()) {
                        intervalList.add(getInterval(e));
                    } else {
                        removeInterval(e);
                    }

                }

            }

            ArrayList<JCheckBox> chkBoxList = new ArrayList<>();
            chkBoxList.add(chkRandom);
            chkBoxList.add(chkSecond);
            chkBoxList.add(chkThird);
            chkBoxList.add(chkFourth);
            chkBoxList.add(chkFourth);
            chkBoxList.add(chkFifth);
            chkBoxList.add(chkSixth);
            chkBoxList.add(chkSeventh);

            for (JCheckBox chkBox : chkBoxList) {
                checkBoxPanel.add(chkBox);
                chkBox.addItemListener(new CheckBoxListener());

            }

            class StartButtonListener implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        app.play();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }

            JButton startButton = new JButton("Start");
            startButton.addActionListener(new StartButtonListener());
            
            class StopButtonListener implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    app.stop();
                }
                
            }
            
            JButton stopButton = new JButton("Stop");
            stopButton.addActionListener(new StopButtonListener());
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            
            buttonPanel.add(startButton);
            buttonPanel.add(stopButton);

            bottomPanel.setLayout(new FlowLayout());
            bottomPanel.add(checkBoxPanel);
            bottomPanel.add(buttonPanel);

            masterPanel.add(topPanel);
            masterPanel.add(bottomPanel);

            frame.setVisible(true);
        }
        
        void removeInterval(ItemEvent e) {

            JCheckBox chkBox = (JCheckBox) e.getSource();
            
            switch (chkBox.getName()) {
                case "random":
                    for (Interval interval : intervalList) {
                        if (interval instanceof RandomInterval) {
                            intervalList.remove(interval);
                            break;
                        }
                    }
                    break;
                case "second":
                    for (Interval interval : intervalList) {
                        if (interval instanceof SecondInterval) {
                            intervalList.remove(interval);
                            break;
                        }
                    }
                    break;
                case "third":
                    for (Interval interval : intervalList) {
                        if (interval instanceof ThirdInterval) {
                            intervalList.remove(interval);
                            break;
                        }
                    }
                    break;

                case "fourth":
                    for (Interval interval : intervalList) {
                        if (interval instanceof FourthInterval) {
                            intervalList.remove(interval);
                            break;
                        }
                    }
                    break;

                case "fifth":
                    for (Interval interval : intervalList) {
                        if (interval instanceof FifthInterval) {
                            intervalList.remove(interval);
                            break;
                        }
                    }
                    break;

                case "sixth":
                    for (Interval interval : intervalList) {
                        if (interval instanceof SixthInterval) {
                            intervalList.remove(interval);
                            break;
                        }
                    }
                    break;
                case "seventh":
                    for (Interval interval : intervalList) {
                        if (interval instanceof SeventhInterval) {
                            intervalList.remove(interval);
                            break;
                        }
                    }

            }
        }

        Interval getInterval(ItemEvent e) {
            JCheckBox c = (JCheckBox) e.getSource();
            String name = c.getName();

            Interval interval = null;

            switch (name) {
                case "random":
                    interval = new RandomInterval(randomNumberGenerator);
                    break;

                case "second":
                    interval = new SecondInterval(randomNumberGenerator);
                    break;

                case "third":
                    interval = new ThirdInterval(randomNumberGenerator);
                    break;

                case "fourth":
                    interval = new FourthInterval(randomNumberGenerator);
                    break;

                case "fifth":
                    interval = new FifthInterval(randomNumberGenerator);
                    break;

                case "sixth":
                    interval = new SixthInterval(randomNumberGenerator);
                    break;

                case "seventh":
                    interval = new SeventhInterval(randomNumberGenerator);

            }

            return interval;
        }

    }

}
