package edu.uwm.cs351;
import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;


/**
 * The Class Jukebox.
 */
public class Jukebox implements MetaEventListener {

	/** Static Constants */
    private static final int TICKS_PER_QUARTER_NOTE = 256;
    private static final int END_OF_TRACK_MESSAGE = 47;
    private static final double NEUTRAL_FACTOR = 1.0;
    private static final int NEUTRAL_INTERVAL = 0;

    /** GUI fields */
    private JFrame frame;
    private JMenuBar bar;
    private JMenu songMenu, instrumentMenu, stretchMenu, transposeMenu;
    private JProgressBar progressBar;
    private Timer progressUpdate;
    private JButton togglePlayback;

    /** MIDI fields */
    private Synthesizer synthesizer;
    private Sequencer sequencer;
    private float bpm;
    private Song song;
    private double factor = NEUTRAL_FACTOR;
    private int interval = NEUTRAL_INTERVAL;
    
    
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
    	readyTransposeMenu();
    	readyStretchMenu();
  		
  		progressBar = new JProgressBar();
  		progressBar.setPreferredSize(new Dimension(300,20));
  		progressBar.setStringPainted(true);
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
  	  		stretchMenu.setEnabled(false);
	  		transposeMenu.setEnabled(false);
  	  		togglePlayback.setText("Stop");
  	  		progressUpdate.start();
  		}
  		else {
  	  		sequencer.stop();
  	  		progressUpdate.stop();
  	  		songMenu.setEnabled(true);
  	  		stretchMenu.setEnabled(true);
  	  		transposeMenu.setEnabled(true);
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
  			menuItem.addActionListener( event -> {
  				this.song = song;
  				this.factor = NEUTRAL_FACTOR;
  				this.interval = NEUTRAL_INTERVAL;
  				loadSong();
  			});
  			songMenu.add(menuItem);
  		}
  		bar.add(songMenu);
	}
	  
	  /**
	   * Prepare the stretch menu and slider.
	   */
	  private void readyStretchMenu(){
		stretchMenu = new JMenu("Stretch");
		// Start at 0 to keep ticks aligned, adjust to 0.1 upon event
		JSlider stretchSlider = new JSlider(JSlider.VERTICAL, 5, 40, 10);
		Hashtable<Integer, JLabel> tickLabels = new Hashtable<>();
		tickLabels.put(new Integer(5), new JLabel("1/2 "));
		tickLabels.put(new Integer(10), new JLabel("1"));
		tickLabels.put(new Integer(20), new JLabel("2"));
		tickLabels.put(new Integer(30), new JLabel("3"));
		tickLabels.put(new Integer(40), new JLabel("4"));
		stretchSlider.setLabelTable(tickLabels);
		stretchSlider.setPaintLabels(true);
		// stretchSlider.setMinorTickSpacing(5);
		stretchSlider.setMajorTickSpacing(5);
		stretchSlider.setPaintTicks(true);
		stretchSlider.setInverted(true);
	    stretchMenu.add(stretchSlider);
	    stretchMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuCanceled(MenuEvent e) {
				factor = Math.max(0.5, stretchSlider.getValue() / 10.0);
				loadSong();
			}
			@Override
			public void menuDeselected(MenuEvent e) {
				factor = Math.max(0.5, stretchSlider.getValue() / 10.0);
				loadSong();
			}
			@Override
			public void menuSelected(MenuEvent e) {}
		});
	    stretchMenu.setEnabled(false);
	    bar.add(stretchMenu);
	  }
	  
	  /**
	   * Prepare the transpose menu and slider.
	   */
	  private void readyTransposeMenu(){
		transposeMenu = new JMenu("Transpose");
		JSlider transposeSlider = new JSlider(JSlider.VERTICAL, -24, 24, 0);
		transposeSlider.setPaintLabels(true);
		transposeSlider.setMinorTickSpacing(4);
		transposeSlider.setMajorTickSpacing(12);
		transposeSlider.setPaintTicks(true);
		transposeMenu.add(transposeSlider);
		transposeMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuCanceled(MenuEvent e) {
				interval = transposeSlider.getValue();
				loadSong();
			}
			@Override
			public void menuDeselected(MenuEvent e) {
				interval = transposeSlider.getValue();
				loadSong();
			}
			@Override
			public void menuSelected(MenuEvent e) {}
		});
		transposeMenu.setEnabled(false);
	    bar.add(transposeMenu);
	  }
  	
  	/**
	   * Loads the given Song into the Sequencer by first converting the song into a Sequence.
	   *
	   * @param song the song to load into the Sequencer
	   */
	  private void loadSong() {
        try {
        	Song toLoad = song.clone();
        	String barLabel = toLoad.getName();
        	
        	if (interval != NEUTRAL_INTERVAL) {
        		toLoad.transpose(interval);
        		barLabel += "( " + ((interval > NEUTRAL_INTERVAL) ? "+" : "") + interval + " )";
        	}
        		
        	if (factor != NEUTRAL_FACTOR) {
        		toLoad.stretch(factor);
        		barLabel += " x "+factor;
        	}
            
        	progressBar.setString(barLabel);
            progressBar.setValue(0);
            progressBar.setMaximum(toTicks(song.getDuration()));
            Sequence sequence = convert(toLoad);
            sequencer.setSequence(sequence);
            bpm = toLoad.getBPM();
            togglePlayback.setEnabled(true);
            stretchMenu.setEnabled(true);
            transposeMenu.setEnabled(true);
        }
        catch (Exception e) {
        	e.printStackTrace();
        	System.exit(1);
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
