package com.hyperconix.game2D;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * This class represents and encapsulates
 * the functionality required to play
 * a MIDI track in Java. 
 * 
 * @author Luke S
 *
 */
public class MIDIPlayer {
	
	/**
	 * Responsible for storing the file name of the track.
	 */
	private String fileName;
	
	/**
	 * Responsible for storing the sequence which represents the score.
	 */
	private Sequence score;
	
	/**
	 * Responsible for storing the sequencer which will play the track
	 */
	private Sequencer sequencer;
	
	/**
	 * Creates the state of a MIDIPlayer. This requires
	 * the fully qualified file name/path to create the
	 * score and sequencer.
	 * 
	 * @param fileName The fully qualified file name/path of the MIDI track
	 */
	public MIDIPlayer(String fileName) {
		this.fileName = fileName;
		
		try {
			score = MidiSystem.getSequence(new File(fileName));
			
			sequencer = MidiSystem.getSequencer();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This method will play the current score of the midi track
	 * 
	 * @param loop whether or not to loop the track continuously
	 */
	public void playScore(boolean loop) {
		try {
			sequencer.open();
			
			sequencer.setSequence(score);
			
			if(loop) {
				sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			}
			
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sequencer.start();
		
	}
	
	/**
	 * This method will return whether
	 * or not the sequencer is currently playing.
	 */
	public boolean playing() {
		return sequencer.isRunning();
	}
	
	/**
	 * This method allows a part of the MIDI score
	 * to be soloed.
	 * 
	 * @param track The index of the track
	 * @param toSolo Whether or not to solo
	 */
	public void solo(int track, boolean toSolo) {
		sequencer.setTrackSolo(track, toSolo);
	}
	
	/**
	 * This method allows a part of the MIDI
	 * score to be muted.
	 * 
	 * @param track The index of the track
	 * @param toMute Whether or not to mute
	 */
	public void mute(int track, boolean toMute) {
		sequencer.setTrackMute(track, toMute);
	}
	
	/**
	 * This method allows the tempo of the score
	 * to be altered.
	 */
	public void alterTempo(float factor) {
		sequencer.setTempoFactor(factor);
	}
	
	/**
	 * This method is responsible for stopping the 
	 * score .
	 */
	public void stopScore() {
		sequencer.stop();
	}
	
	
}
