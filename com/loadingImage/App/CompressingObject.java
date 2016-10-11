package com.loadingImage.App;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class CompressingObject {
	
	private byte[] reserved = {0, 0, 0};
	private byte index;
	private int repeat;
	private Boolean oddity = false;
	
	public void updateObject(byte indexNum, int indexRepeat)
	{
		this.index = indexNum;
		this.repeat = indexRepeat;
	}
	
	public void increaseRepeat()
	{
		this.repeat++;
	}
	
	public void resetRepeat()
	{
		this.repeat = 1;
	}
	
	public ByteArrayOutputStream putObject() throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		out.write(this.reserved);	// throws exception if wrong, but I don't see how that could happen
		out.write(index);
		out.write(repeat);
		return out;
	}

}
