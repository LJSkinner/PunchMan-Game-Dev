package com.hyperconix.game2D;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * 
 * This class represents a custom novel
 * sound filter. Which was created to add
 * a volume distorting effect. The idea
 * behind this sound filter, is to be
 * played when the player steps
 * through a portal, taking them to
 * the next level. It is meant to give the 
 * illusion of "time" being changed by
 * causing the audio to pop and whiz at
 * certain points of the track.
 * 
 * @author Luke S
 *
 */
public class VolumeDistorterFilterStream extends FilterInputStream {

	protected VolumeDistorterFilterStream(InputStream in) {
		super(in);
	}
	
	// Get a value from the array 'buffer' at the given 'position'
	// and convert it into short big-endian format
	public short getSample(byte[] buffer, int position)
	{
	  return (short) (((buffer[position + 1] & 0xff) << 8) |
						     (buffer[position] & 0xff));
	}

	// Set a short value 'sample' in the array 'buffer' at the
	// given 'position' in little-endian format
	public void setSample(byte[] buffer, int position, short sample)
	{
		buffer[position] = (byte) (sample & 0xFF);
		
		buffer[position + 1] = (byte) ((sample >> 8) & 0xFF);
	}
	
	@Override
	public int read(byte [] sample, int offset, int length) throws IOException
	{
		int bytesRead = super.read(sample, offset, length);
	
		float change = 2.0f * (0.5f / bytesRead);
	
		float volume = 1.0f;
			
		short amp = 0;
			
		Random rand = new Random();
			
		for (int p = 0; p < bytesRead; p = p + 2)
		{
			//create a random num
			int ranNum = rand.nextInt(51) + 1;
				
			amp = getSample(sample,p);
				
			//if it is even, keep the volume the same, otherwise multiply it by the change
			amp = ranNum % 2 == 0 ? (short)(amp * volume)
						          : (short)(amp * (volume * change));
			
			setSample(sample,p,amp);	
		}
			
		return length;
	}

}
