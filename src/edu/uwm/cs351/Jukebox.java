package edu.uwm.cs351;
import javax.sound.midi.*;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;


/**
 * The Class Jukebox.
 */
public class Jukebox implements MetaEventListener {

	/** Static Constants */
    private static final int TICKS_PER_QUARTER_NOTE = 4; // each tick is a 16th note
    public static final int END_OF_TRACK_MESSAGE = 47;

    /** GUI fields */
    private JFrame frame;
    private JMenuBar bar;
    private JMenu songMenu, instrumentMenu;
    private JProgressBar progressBar;
    private Timer progressUpdate;
    private JButton togglePlayback;

    /** MIDI fields */
    private Synthesizer synthesizer;
    private Sequencer sequencer;
    private float bpm;
    
    /**
     * Instantiates a new Jukebox.
     */
    public Jukebox() {
    	SwingUtilities.invokeLater( ()->createGUI() );
        createMidiComponents();
    }
    
    /**
     * Creates the MIDI components of the Jukebox.
     */
    private void createMidiComponents() {
    	try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            sequencer = MidiSystem.getSequencer(false);
            sequencer.open();
            sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
            sequencer.addMetaEventListener(this);
            progressUpdate = new Timer(5, event -> progressBar.setValue((int) sequencer.getTickPosition()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the GUI of the Jukebox.
     */
  	private void createGUI(){
  		frame = new JFrame("MIDI Jukebox");
  		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  		frame.setLocationRelativeTo(null);

  		bar = new JMenuBar();
    	readySongs();
    	readyInstruments();
  		
  		progressBar = new JProgressBar();
  		progressBar.setPreferredSize(new Dimension(300,20));
  		togglePlayback = new JButton("Play");
  		togglePlayback.addActionListener( event -> togglePlayback());
  		togglePlayback.setEnabled(false);
  		
  		frame.setLayout(new FlowLayout());
  		frame.add(progressBar);
  		frame.add(togglePlayback);
  		frame.setJMenuBar(bar);
  		frame.pack();
  		frame.setVisible(true);
  	}
  	
  	/**
	   * Toggles playback of the Jukebox.
	   */
	  private void togglePlayback() {
  		if (togglePlayback.getText().equals("Play")) {
  	  		sequencer.start();
  	  		sequencer.setTempoInBPM(bpm);
  	  		
  	  		songMenu.setEnabled(false);
  	  		togglePlayback.setText("Stop");
  	  		progressUpdate.start();
  		}
  		else {
  	  		sequencer.stop();
  	  		progressUpdate.stop();
  	  		songMenu.setEnabled(true);
  	  		togglePlayback.setText("Play");
  		}
  	}

  	
  	/**
	   * Loads all songs from ./songs directory into Song objects, and maps them to menu items.
	   */
	  private void readySongs(){
  		songMenu = new JMenu("Songs");
  		for (final File songFile : new File("./songs").listFiles()) {
  			Song song = read(songFile);
  			JMenuItem menuItem = new JMenuItem(song.getName());
  			menuItem.addActionListener( event -> loadSong(song));
  			songMenu.add(menuItem);
  		}
  		bar.add(songMenu);
	}
  	
  	/**
	   * Loads the given Song into the Sequencer by first converting the song into a Sequence.
	   *
	   * @param song the song to load into the Sequencer
	   */
	  private void loadSong(Song song) {
		  
		// For fun, try transposing the song here!
		  
        try {
            Sequence sequence = convert(song);
            progressBar.setValue(0);
            progressBar.setMaximum(toTicks(song.getDuration()));
            progressBar.setStringPainted(true);
            progressBar.setString(song.getName());
            sequencer.setSequence(sequence);
            bpm = song.getBPM();
            togglePlayback.setEnabled(true);
        }
        catch (InvalidMidiDataException e) {
        	System.out.println("Unable to convert song: "+song.getName());
        }
    }
  	
  	/**
	   * Reads and parses the given file into a Song object.
	   *
	   * @param file the file containing the song
	   * @return the song if successful, otherwise null
	   */
	  private Song read(File file) {
  		Song song = null;
  		
  			
  		
  		
  		try (Scanner s = new Scanner(file)) {
  			String name = s.nextLine().trim();
  			float bpm = Integer.parseInt(s.nextLine().trim().split(" ")[0]);
  			song = new Song(name, (int)bpm);
  			
  			while (s.hasNextLine()) {
  				String line = s.nextLine().trim();
  				if (line.equals("") || line.startsWith("[")) continue;
  				// #(
  	  	 		song.add(new Note(line));
  	  	 		/* #)
  	  	 		song.insert(new Note(line)); // TODO: Change to use Song's new Collection API.
  	  	 		 ## */
  			}
  		}
  		catch (FileNotFoundException e) { System.out.println("Cannot find file"); }
        	
  		return song;
  	}
  	
    /**
     * Creates the menu of instruments, and maps each instrument's menu item to load that instrument.
     */
    private void readyInstruments() {
    	instrumentMenu = new JMenu("Instruments");
        // The instruments we will use for our Jukebox.
        ArrayList<String> names = new ArrayList<String>(Arrays.asList("kalimba", "honky tonk", "woodblock", "mutedtrumpet",
        		      "fingered bs.", "soprano sax", "steel-str.gt", "nylon-str.gt", "bass & lead", "bassoon", "bandoneon",
                            "gun shot", "piano 2", "solo vox", "piano 1", "piano 3", "agogo", "shakuhachi", "french horns",
                                "breath noise", "celesta", "whistle", "tuba", "vibraphone", "slap bass 1", "slap bass 2"));
        
        for (Instrument instrument : synthesizer.getAvailableInstruments()) {
            String name = instrument.getName().trim().toLowerCase();
            if (names.contains(name.toLowerCase())) {
            	JMenuItem menuItem = new JMenuItem(name);
            	menuItem.addActionListener( event -> loadInstrument(instrument) );
            	instrumentMenu.add(menuItem);
            	names.remove(name.toLowerCase());
            	if (name.equals("fingered bs."))
            		loadInstrument(instrument);
            }
        }
        bar.add(instrumentMenu);
    }
    
    /**
     * Loads the given instrument into the Jukebox's sequencer, first unloading any loaded instruments,
     * and then switching channel 0 to use that instrument's patch's program. For our initial version
     * of Jukebox, we will only use channel 0.
     *
     * @param instrument the instrument to load
     */
    private void loadInstrument(Instrument instrument) {
    	synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
        synthesizer.loadInstrument(instrument);
        synthesizer.getChannels()[0].programChange(instrument.getPatch().getProgram());
    }

    /* (non-Javadoc)
     * @see javax.sound.midi.MetaEventListener#meta(javax.sound.midi.MetaMessage)
     */
    @Override
    public void meta(MetaMessage msg) {
        if (msg.getType() == END_OF_TRACK_MESSAGE) {
            togglePlayback();
            progressBar.setValue(progressBar.getMaximum());
            sequencer.setTickPosition(0);
        }
    }

    private static int toTicks(double duration) {
    	return (int) Math.round(duration / 0.25 * TICKS_PER_QUARTER_NOTE);
    }
    
    /**
     * Converts a Song to a Sequence, which is playable by a Sequencer.
     * 
     * @param song - Song to convert
     * @return song as a Sequence
     */
    private static Sequence convert(Song song) {
        Sequence sequence = null;
        try {
            sequence = new Sequence(Sequence.PPQ, TICKS_PER_QUARTER_NOTE);
            Track track = sequence.createTrack();
            long timestamp = 0;
            // #(
            for (Iterator<Note> it = song.iterator(); it.hasNext();){
            	Note note = it.next();
            	putNote(track, note, timestamp);
                timestamp += toTicks(note.getDuration());
    		}
	  	 	/* #)
	  	 	// TODO: Change to use Song's new Collection API.
	  	 	for (song.start(); song.hasCurrent(); song.advance()) {
            	Note note = song.getCurrent();
            	putNote(track, note, timestamp);
                timestamp += toTicks(note.getDuration());
    		}
	  	 	## */
        }
        catch (Exception e) {e.printStackTrace();}
        return sequence;
    }

    /**
     * Puts the note into the track at the timestamp.
     *
     * @param track - the track to add the note to
     * @param note - the note to add
     * @param timestamp - tick value at which to place the note
     * @throws InvalidMidiDataException if midiNote is outside range [0, 127] or
     * intensity is outside range [0, 127]
     */
    private static void putNote(Track track, Note note, long timestamp) throws InvalidMidiDataException {
    	
    	int midiPitch = note.getMidiPitch();
    	int intensity = Note.DEFAULT_INTENSITY;
    	
    	if (midiPitch == 128) {
    		midiPitch = 0; // rest note
    		intensity = 0;
    	}
    	
        ShortMessage noteOn = new ShortMessage(ShortMessage.NOTE_ON, 0, midiPitch, intensity);
        ShortMessage noteOff = new ShortMessage(ShortMessage.NOTE_OFF, 0, midiPitch, 0);
        track.add(new MidiEvent(noteOn, timestamp));
        track.add(new MidiEvent(noteOff, timestamp + toTicks(note.getDuration())));
    }
    
    /**
     * The main method: Instantiates a new Jukebox.
     *
     * @param args the arguments to launch (ignored)
     */
    public static void main(String[] args) { new Jukebox(); }
}
