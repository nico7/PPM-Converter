package com.loadingImage.App;

import java.awt.GridBagConstraints;

@SuppressWarnings("serial")
public class BetterGridBag extends GridBagConstraints{

	public GridBagConstraints position( int x, int y)
	{
		this.gridx = x;
		this.gridy = y;
		return this;
		
	}
}
