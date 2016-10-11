/*
 * This software is currently under "do-whatever-you-want-but-don't-sue-me-license" as I call it. Its pretty self-explanatory.
 * Feel free to put pull requests!
 * PPM By Nico7.
 */

package com.loadingImage.App;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.events.EventException;

public class PPM_main implements ActionListener, ComponentListener{
	final static Dimension buttonSize = new Dimension(300, 24);
	final static Dimension smallerSize = new Dimension(200, 24);
	static JFileChooser chooser = new JFileChooser();
	static JFileChooser saver = new JFileChooser();	// We use this to save files
	final static int DEBUG = 0;
	public static int widthVal;
	public static JButton debugButton;		// Button that will start processes for debugging
	public static BetterGridBag c =  new BetterGridBag();
	public static JButton fileChoose;		// Button that starts the file chooser to choose image
	public static JTextField inputLabel;	// Label that indicates the directory of the selected image
	public static JTextField sizeText;		// Text field that informs user of size of image
	public static JTextField height;		// Text field that takes input from user to resize height of image
	public static JTextField width;			// Text field that takes input from user to resize width of image
	public static JLabel wLabel;			// Label that tells user that the text field next to it is for width
	public static JLabel hLabel;			// Label that tells user that the text field next to it is for height
	public static JButton imgToBin;			// Button to convert the image to Binary format
	public static JButton cToC;				// Button to convert the image to C header format
	public static JButton cToAsm;			// Button to convert the image to ASM format (Microchip)
	public static JButton resize;			// Button to resize image
	public static Checkbox showWidth;
	public static Checkbox compress;		// Option to convert AND compress or just convert
	public static JPanel masterPane;		// Panel that contains control and image panels
	public static JPanel control;			// Panel that contains buttons, text fields, and other GUI control objects
	public static JPanel image;				// Panel that contains the image selected
	public static JPanel resPanel;			// Panel that contains resize label and text field
	public static JFrame window;			// Window that contains most of the program GUI
	public static LoadImageApp img;			// Image that is read and displayed if its not PPM
	public static Raster R;					// Rasterized image
	public static InputStream input;
	public static byte[][] palette;
	public static FileInputStream fin;
	public static DataOutputStream fout;
	public static ByteArrayOutputStream data;
	public static ByteArrayOutputStream widthValue;	// This will be width that gets printed in the output file optionally
	
	public static void main(String[] args) {
		
		new PPM_main();
	
	}
	
	private PPM_main()
	{
		FileNameExtensionFilter filter;
		String[] suffices = ImageIO.getReaderFileSuffixes();
		for(int i = 0; i< suffices.length; i++)
		{
			filter = new FileNameExtensionFilter(suffices[i] + " files", suffices[i]);
			chooser.addChoosableFileFilter(filter);
		}
		filter = new FileNameExtensionFilter("ppm files", "ppm", "PPM");
		chooser.addChoosableFileFilter(filter);
		//---------initialize GUI objects and layout-----------
		
		if(DEBUG == 1)
		{
			debugButton = new JButton("Debug");
		}
		
		masterPane = new JPanel(new GridBagLayout());
		resPanel = new JPanel(new GridBagLayout());
		window = new JFrame("PPM Converter");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		control = new JPanel(new GridBagLayout());
		
		image = new JPanel();
		fileChoose = new JButton("Select File");
		inputLabel = new JTextField("");
		inputLabel.setEditable(false);
		imgToBin = new JButton("Convert to Binary");
		cToC = new JButton("Convert to C");
		cToAsm = new JButton("Convert to ASM");
		resize = new JButton("Resize");
		compress = new Checkbox("Compress");
		showWidth = new Checkbox("Width 32-bit");
		sizeText = new JTextField("");
		height = new JTextField("");
		width = new JTextField("");
		wLabel = new JLabel("Width (Px) ");
		hLabel = new JLabel("Height (Px) ");
		sizeText.setEditable(false);
		sizeText.setVisible(false);
		
		masterPane = new JPanel(new GridBagLayout());
//-----------------------------------------------------
		
//----------------Set up GUI nicely--------------------
		
		//------set sizes--------------
		
		if(DEBUG == 1)
		{
			debugButton.setPreferredSize(buttonSize);
		}
		
		inputLabel.setPreferredSize(buttonSize);
		fileChoose.setPreferredSize(buttonSize);
		imgToBin.setPreferredSize(buttonSize);
		cToC.setPreferredSize(buttonSize);
		cToAsm.setPreferredSize(buttonSize);
		resize.setPreferredSize(buttonSize);
		sizeText.setPreferredSize(buttonSize);
		height.setPreferredSize(smallerSize);
		width.setPreferredSize(smallerSize);
		//-----------------------------
		c.position(0, 0);
		
		resPanel.add(wLabel, c);
		c.gridx++;
		resPanel.add(width, c);
		c.position(0, 1);
		resPanel.add(hLabel, c);
		c.gridx++;
		resPanel.add(height, c);
		//---------set positions-------
		c.position(0, 0);
		
		if(DEBUG == 1)
		{
			control.add(debugButton, c);
			c.gridy++;
		}
		
		control.add(fileChoose, c);
		c.gridy ++;
		control.add(inputLabel, c);
		c.gridy ++;
		control.add(imgToBin, c);
		c.gridy ++;
		control.add(cToC, c);
		c.gridy ++;
		control.add(cToAsm, c);
		c.gridy ++;
		control.add(compress, c);
		c.gridx++;
		control.add(showWidth, c);
		c.gridx--;
		c.gridy ++;
		control.add(resize, c);
		c.gridy ++;
		control.add(resPanel, c);
		c.gridy++;
		control.add(sizeText, c);
		c.position(0, 0);
		masterPane.add(control, c);
		c.position(2, 0);
		masterPane.add(image, c);
		window.add(masterPane);
		
		window.setSize(500,  700);
		window.setVisible(true);
		
		//----------------------------
		
//------------------Set up events -------------------
		if(DEBUG == 1)
		{
			debugButton.addActionListener(this);
		}
		window.addComponentListener(this);
		resize.addActionListener(this);
		fileChoose.addActionListener(this);
		imgToBin.addActionListener(this);
		cToC.addActionListener(this);
		cToAsm.addActionListener(this);
//---------------------------------------------------
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{	
		if(DEBUG == 1)
		{
			if(e.getSource() == debugButton)
			{
				// Put any function to debug here
			}
		}
		if(e.getSource() == resize)
		{
			System.out.println("got to resize!");
			Boolean isNumber = false;		// is Number will be used to determine if the user entered numeric values in the "resize" field
			int w =0, h =0;
			try					// Below: if it works that w and h contain numeric values then isNumber = true.
			{
				w = Integer.parseInt(width.getText());
				h = Integer.parseInt(height.getText());
				isNumber = true;
			}catch(Exception ex)	// Otherwise throw "some error"
			{
					System.out.println("there was a problem resizing");
			}
			if(isNumber)	// If the user didn't input anything stupid...:
			{
				System.out.println("w = " + w + "  h = " + h);
				img = img.resize(img, w, h);		// Resize  image.
				image.removeAll();					// Remove old image that contains the previous value (beffore resizing)
				// Below: setText displays size of image after "resize".
				sizeText.setText("Displayed Size: " + img.getPreferredSize().getHeight() + " High" + " x " + img.getPreferredSize().getWidth() + " Wide");
				image.add(img);	// Display new resized image.
				window.setMinimumSize(new Dimension(0,0));
				window.pack();		// keep the window nice and size appropriate.
				window.setMinimumSize(window.getSize());
			}

		}
		if(e.getSource() == imgToBin)	// C to Binary //TODO finish
		{
			if(R != null)
			{		
					widthValue = new ByteArrayOutputStream();
					
					int ret = saver.showSaveDialog(null);
					if(ret == JFileChooser.APPROVE_OPTION)
					{
						try	//Building "data": palette
						{
							palette = getPalette565_16(R);	// get palette from raster R
							int size = R.getHeight() * R.getWidth() + 1 + palette.length;	//get size of image + palette + /0
							// fout =  file that will be outputted.
							fout = new DataOutputStream(new FileOutputStream(saver.getSelectedFile().getPath()));
							// data = buffer before fout.
							data = new ByteArrayOutputStream(size);
							
							data = compress1(palette, R);
							data = bytesToNibs(data);
							
							if(compress.getState())	// At this point we already have the data in terms of bytes {0x01, 02, 02, 03, 03, 03, 03,...}
							{
								data = compressToNibs(data); // The input is  a file that has a pixel per index of the palette.
							}
							if(showWidth.getState())
							{
								widthVal = R.getWidth();
								if(widthVal <= 0xFF)
								{
									for(int w = 0; w < 3; w++)
									{
										widthValue.write(0x00);										
									}
								}
								else if(widthVal <= 0xFFFF)
								{
									for(int w = 0; w < 2; w++)
									{
										widthValue.write(0x00);										
									}
								}
								else if(widthVal <= 0xFFFFFF)
								{
									for(int w = 0; w < 1; w++)
									{
										widthValue.write(0x00);										
									}
								}
								widthValue.write(widthVal);
								widthValue.writeTo(fout);
							}
							data.writeTo(fout);
							fout.close();
							JOptionPane.showMessageDialog(new JFrame(), "DONE!");
							System.out.println("DONE!");
						}catch(Exception ex)
						{
							ex.printStackTrace();
						}
					}
				
			}
		}// If imgToBin
		if(e.getSource()== fileChoose)
		{

			int ret = chooser.showDialog(null,  "Open File");
			if(ret == JFileChooser.APPROVE_OPTION)
			{
			  try
			  {
					try
					{
						image.removeAll();	// Remove old images from the image panel
					}
					catch(Exception exception)
					{// just making sure that we don't keep an old image
						
					}
			  // If the image selected is a PPM of any kind...
					if(chooser.getSelectedFile().getName().endsWith("ppm") || chooser.getSelectedFile().getName().endsWith("PPM"))
					{
			  //========= These are the variables that will be used if our selected image happens to be a PPM===================================
						BufferedImage canvas;
						int red=0, green=0, blue=0, rgb;
						int i, j;
						int imageWidth, imageHeight, imageColors;
						
						input = new FileInputStream(chooser.getCurrentDirectory().getAbsolutePath()+"\\"+chooser.getSelectedFile().getName());
						StringBuilder builder = new StringBuilder();
						
						byte[] head = new byte[3]; 
						String magic;
						byte helper = 0;
			  //============================================================================================================				
												// DETERMINE if PPM:
						input.read(head, 0, 2);	// Read 2 bytes to get P3 or P6			
						builder.append((char)head[0]);
						builder.append((char) head[1]);	// builder becomes "P3" or "P6"
						
						// In case the next character is some garbage \r or \n
						input.read(head, 0, 1);	
						if(head[0] == '\r')
						{
							input.read(head, 0, 1);
						}
						if(head[0] == '\n')
						{
							input.read(head, 0, 1);
						}
					//---- save the P3 or P6 as a string that will be used later to determine the type of PPM---
						magic = builder.toString();
						builder = new StringBuilder();
						while(head[0] == '#')
						{
							helper = (byte) input.read();
							while(helper != '\n')
							{
								helper = (byte) input.read();
								//Do nothing
							}
					
							head[0] = helper;
						}
						// ----Get Width of image------
						helper = head[0];
						if(helper == '\n')
						{
							helper = (byte) input.read();
						}
						while((helper != '\n') && (helper != '\r') && (helper != ' '))
						{
							builder.append((char) helper);
							helper = (byte) input.read();
						}
						imageWidth = Integer.parseInt(builder.toString());
						
						//-----Get Height of image------
						builder = new StringBuilder();
						if(helper == '\n' || helper ==' ')
						{
							helper = (byte) input.read();
							builder.append((char)helper);
						}
						
						helper = (byte) input.read();
						while((helper != '\n') && (helper != '\r') && (helper != ' '))
						{
							builder.append((char) helper);
							helper = (byte) input.read();
						}
						imageHeight = Integer.parseInt(builder.toString());
					//	System.out.println(imageWidth + " is the width and the height is " + imageHeight);
						
						//-----Get the max pixel number-----
						builder = new StringBuilder();
						helper = (byte) input.read();
						while((helper != '\n') && (helper != '\r') && (helper != ' '))
						{
							builder.append((char) helper);
							helper = (byte) input.read();
						}
						imageColors = Integer.parseInt(builder.toString());
						
						// Ensure the max value per pixel is not a weird number
						if((imageColors > 255)||(imageColors <0))
						{
							System.out.println("you have the wrong amount of colors here");
							return;
						}
						
						// Here we determine if our file is in ASCII
						if(magic.equals("P3") || magic.equals("p3"))
						{
							builder = new StringBuilder();
							canvas = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
							for( i=0; i<imageWidth; i++)		// for each x
							{
								for( j=0; j< imageHeight; j++ )		// for each y
								{
									for(int k=0; k<3; k++)			//for each element {r, g, b} value) AKA Pixel
									{
										input.read(head, 0, 1);					// Read next byte
										if((head[0] == ' ')||(head[0]== '\n')) 	// If sapce or "/n" 
										{
											input.read(head, 0, 1);				// then read next byte
										}
										while((head[0] != ' ')&&(head[0]!= '\n'))	// while following stream is actual data
										{
/*essentially: builder = head[0]*/   		builder.append((char)head[0]);			// append data and read next byte K = (R, or G, or B)
											input.read(head, 0, 1);					
										}
	/* here "builder" breaks up into*/	switch(k)			// Take 3 bytes representing R, G, and B respectively.
										{						// and save them as 1 byte rrrrrggg gggbbbbb = 2 bytes
	/* red, */								case 0:	// Get Red value as a number from 0 to 255.
												red = Integer.parseInt(builder.toString()); 
												break;										
	/* green, */							case 1: // Get Green value as a number from 0 to 255. 
												green = Integer.parseInt(builder.toString()); 
												break;
   /* and blue */										case 2: // Get Bllue value as a number from 0 to 255.
												blue = Integer.parseInt(builder.toString());
												break;
											default:
												break;
										}
   /* now that we got r, *//* --->*/	builder = new StringBuilder();	// purge "builder" of any data .
   /* g, and b values,   */										
   /* clear builder for  */			} // 
   /* next round         */  		rgb = create_rgb(red, green, blue);		// create RGB (integer representing the appendage of r, g, b in 565 format.
									if(rgb<255 )	// Custom filter here that acts funny depending on lowest value.
									{
	/* r, g, and b are     */			rgb =  ((int) ((i+j)*Math.cos(i-j)))^ i*j; // just playing around with values.  Changes rgb.
	/* compressed into rgb */		}
									canvas.setRGB(j,  i, rgb ); // Creates pixel on the display image.
				
					
								}// for j
							}// for i
//------------------------------- In this section we end up with a canvas displaying the image ----------------------//
					
							
// ----------------------------- Here we determine whether the file is encoded in binary:----------------------------//							
						// Below we determine if our file is in binary	where each byte is 1 pixel.	
						}else if(magic.equals("P6") || magic.equals("p6"))
						{
							canvas = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
							for( i=0; i<imageHeight; i++)
							{
								for( j=0; j<imageWidth; j++)
								{
									red = input.read();
									green = input.read();
									blue = input.read();
									
									// NOTE: Could make a filter here
									rgb = create_rgb(red, green, blue); // make pixel into {r r r r r g g g g g g b b b b b} mode.
									canvas.setRGB(j, i,  rgb);	// Display image.
									// This is a very tricky function. if you put the i and j parameters 
									// wrong, it kicks you out without notice or exception
								}
						//		System.out.println(i);
							}
						}
						else
						{
							return;//error!
						}	// ============= Update GUI image below==============//
						img = new LoadImageApp(canvas);	// Actually draw the image in the canvas
						image.removeAll();
						image.add(img);					
						image.setVisible(true);
						masterPane.add(image, c);
						sizeText.setText("Displayed Size: " + img.getPreferredSize().getHeight() + " High" + " x " + img.getPreferredSize().getWidth() + " Wide");	
							
					//	System.out.println(builder);
					} else	// If the image is natively supported by ImageIO, the following puts it on the screen.
					{
						img = new LoadImageApp(chooser.getSelectedFile());
						image.add(img);					
						image.setVisible(true);
						masterPane.add(image, c);
						sizeText.setText("Displayed Size: " + img.getPreferredSize().getHeight() + " High" + " x " + img.getPreferredSize().getWidth() + " Wide");

					}
					// Deal with changing or getting image. (GUI Housekeeping)
					sizeText.setVisible(true);					
					window.setMinimumSize(new Dimension(0,0));
					window.pack();
					window.setMinimumSize(window.getSize());
					inputLabel.setVisible(true);
					inputLabel.setText(chooser.getCurrentDirectory().getAbsolutePath()+"\\"+chooser.getSelectedFile().getName());
					
					// Get image as raster. Where R is the matrix of pixels.
					R = img.getRaster();
							
			  }catch(Exception ex)
			  {
				ex.printStackTrace();
			  }
			
			}// JFile Chooser approve
		}// e.source == fileChoose
	}
	/**
	 * @brief This function joins the colors to make an 888 rgb pixel int
	 * @param red
	 * @param green
	 * @param blue
	 * @return pixel
	 */
	private int create_rgb(int red, int green, int blue) 
	{
		int pixel;
		pixel = (red << 8) | green;
		pixel = (pixel << 8) | blue;
		return pixel;
	}
	/**
	 * @brief This function joins the colors to make a 565 rgb pixel int
	 * @param i
	 * @param j
	 * @param image
	 * @return
	 */
	public byte[] create_rgb(int i, int j, Raster image)
	{
		byte[] color = new byte[2];
		int[] iArray = new int[3];

		iArray[0] = 0;
		iArray[1] = 0;
		iArray[2] = 0;
		
		image.getPixel(i, j, iArray);	
		
		iArray[0] *= 32;	// Normalizing values to 5 and 6 bits
		iArray[0] /= 256;
		iArray[1] *= 64;
		iArray[1] /= 256;
		iArray[2] *= 32;
		iArray[2] /= 256;

		color[0] =  (byte) ((byte) (iArray[0]<<3)|((byte)iArray[1]>>3));
		color[1] = (byte) ((iArray[1] & 0x07)<<5);
		color[1] |= (byte) iArray[2]; 
		
		return color;
	}
	/**
	 * @brief This function determines whether the pixel entered in it is already in the palette for the image.
	 * @param pix
	 * @param palette
	 * @return index of palette, representing color of pixel.
	 */
	public int isInPalette(byte[] pix, byte[][] palette)
	{
		for(int i = 1; i<palette.length; i++)
		{
			if((pix[0] == palette[i][0])&&(pix[1] == palette[i][1]))
			{
				return i;	//TODO Test!
			}
		}
		return 0;
	}
	/**
	 * @brief This function takes the image and sets up a 16-color-max palette from it. (in 565 format)
	 * @param imgRaster
	 * @return palette[][]
	 */
	public byte[][] getPalette565_16(Raster imgRaster)
	{
		byte [][] palette = new byte[16][2];
		byte[] pixel = new byte[2];
		int index = 1;
		int width = imgRaster.getWidth();
		int height = imgRaster.getHeight();
		boolean zero = false;
		
		for(int i=0; i<height; i++)
		{
			for(int j=0; j<width; j++)
			{
				pixel = create_rgb(j, i, imgRaster);

				if(isInPalette(pixel, palette) == 0)
				{
					
					if(!zero)
					{
						if(pixel[0] == 0 && pixel[1] == 0)
						{
							zero = true;
							palette[index][0] = 0;
							palette[index][1] = 0;
							index++;
						}
						else
						{
							palette[index] = pixel;
							index++;
						}
					}
					else
					{
						palette[index] = pixel;
						index++;
					}
					
				}
				if(index >= 16)
				{
					System.out.println("More than 15 Colors!");
					return palette;
				}
				
			}
		}
		System.out.println(index-1 + " colors here!");
		return palette;
	}
	/** 
	 * @brief This function takes the raster and outputs a ByteArrayOutputStream composed of the indexes in 1 byte per index
	 * @param[in] palette
	 * @param[in] raster
	 * @return output
	 * @throws IOException
	 */
	public ByteArrayOutputStream compress1( byte[][] palette, Raster raster) throws IOException
	{
		int size = R.getHeight()*R.getWidth();
		int i = 0;
		byte preBuff = 0;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		for(i=0; i<palette.length; i++)
		{
			output.write((byte) (palette[i][0]));
			output.write((byte) (palette[i][1]));
		}
		for(i=0; i<raster.getHeight(); i++)
		{
			for(int j=0; j<raster.getWidth(); j++)
			{
				preBuff = (byte) returnIndex(i, j, palette, raster);  //for each pixel, create a byte of index.
				output.write(preBuff);		//output = {0x01, 0x02, ..., 0x0F}	
			}
		}
		return output;
	}	//	compress1
	/**
	 * @brief This function takes the 1-byte indexes from compress 1 and returns compressed data.
	 * @description Input file would come in format {0x0F,0x02,0x02,0x03,0x03,0x03,0x03}
	 * 				Output file would come in format {0xF2, 0x23, 0x33, 0x30}
	 * @param inData
	 * @return outData in the format ([index_nibble:repeat_start:number_of_repetition:repeat_end])
	 */
	public ByteArrayOutputStream bytesToNibs( ByteArrayOutputStream inData)
	{	
		int i = 0;
		byte[] inputArray = inData.toByteArray();
		ByteArrayOutputStream outData  = new ByteArrayOutputStream();
		Boolean odd = false;
		byte buffer = 0x00;
		byte[] palette = new byte[16*2];
		for(i = 0; i<16*2; i++)
		{
			palette[i] = inputArray[i];
			outData.write(palette[i]);
		}
		
		for(i = 16*2; i < inputArray.length; i++)
		{
			if(!odd)	// data falls in Least Significant Nibble -LSN- (right Nibble)
			{
				buffer = inputArray[i];
				odd = !odd;
			}
			else if(odd)	// nibble gets shifted left and new data falls in LSN.
			{
				buffer <<= 4;
				buffer |= inputArray[i];
				outData.write(buffer);
				buffer = 0x00; // just making sure buffer is clean, but it doesn't have to happen.
				odd = !odd;
			}
		}
		if(odd) // if buffer ended up with just one nibble of data, shifted left and insert 0
		{
			buffer <<= 4;
			outData.write(buffer);
		}
		
		return outData;
	}
	/**
	 * @brief This function performs the 2nd part of the commpression. Compresses 11111 to 1050
	 * @param inData
	 * @return outData
	 */
	public ByteArrayOutputStream compressToNibs(ByteArrayOutputStream inData)
	{
		final int numWorthRep = 4;	// number of repetitions that make it worth compressing instead of putting the nibs
		ByteArrayOutputStream outData = new ByteArrayOutputStream();
		byte lastValue = 0x00;
		byte outBuffer = 0x00;	// both nibs get loaded here before this byte get written on outData.
		Boolean repeatOn = false;
		Boolean odd = false;
		byte currentDataNib = 0x00;
		byte[] dataInArray = inData.toByteArray();
		int count = 1;
		int i;
		for( i = 0; i < 16*2; i++)
		{
			outData.write(dataInArray[i]);
		}
		
		for(; i< dataInArray.length; i++)
		{
			//----------MSN------------------| PART A |-------------------------------------
				currentDataNib = (byte) ((dataInArray[i] >> 4) & 0x0F);
				if(currentDataNib == lastValue)
				{
					if(repeatOn)
					{
						count++;
					}
					else  // if it hasn't been repeating until now
					{
						repeatOn = true;
						count++;
					}
				}
				else // if new color
				{
				
					if(repeatOn)	// If the previous color was repeating, so we gotta put a 0 and the value of the new color
					{				// so odd doesn't change
						repeatOn = false;
	// ================= This is the part where we put the first 0 to signal that the last value will repeat itself ==============					
						
						if(count < numWorthRep)	// This is the only option that shouldn't put a 0
						{// The part below here puts the number repeated 3 or 2 times
							for(int k = 1; k < count; k++)
							{
								if(!odd)
								{
									outBuffer = lastValue;
									odd = !odd;
								}
								else	// if odd
								{
									outBuffer <<= 4;
									outBuffer |= lastValue;
									outData.write(outBuffer);
									odd = !odd;
								}
							}
							count = 1;
							lastValue = currentDataNib;
				//********* The part below puts the new number on the buffer ******************			
							if(!odd)
							{
								outBuffer = lastValue;
								odd = !odd;
							}
							else	// if even
							{
								outBuffer <<= 4;
								outBuffer |= lastValue;
								outData.write(outBuffer); 
								odd = !odd;
							}
						}
						else if(count >= 15)
						{// The part below puts a 0 to start the "repeat" signaling
							if(!odd)
							{
								outBuffer = 0x00;
								odd = !odd;
							}
							else // if odd
							{
								outBuffer <<= 4;
								outBuffer &= 0xF0; // Shouldn't have to do this, but for the sake of habit
								outData.write(outBuffer);
								outBuffer = 0x00;
								odd = !odd;
							}
						
						// The following part should put the number of repetitions:
							while(count >= 15)
							{
								if(!odd)
								{
									outBuffer = 0x0F;
									odd = !odd;
									count -=15;
									
								}
								else	// if odd
								{
									outBuffer <<= 4;
									outBuffer |= 0x0F;
									outData.write(outBuffer);
									odd = !odd;
									count -= 15;
								}
							}
							if(count == 0)
							{
								count++; 
							}
							else // if the count was something meaningful
							{
								if(!odd)
								{
									outBuffer = (byte)count;
									odd = !odd;
									count = 1;
								}
								else // if odd
								{
									outBuffer <<= 4;
									outData.write(outBuffer);
									odd = !odd;
									outBuffer = 0x00;
									count = 1;
								}
							}
				// The following part puts the 0 to signal end of repetition and the new value
							lastValue = currentDataNib;
							if(!odd)
							{
								outBuffer = currentDataNib;
								outData.write(outBuffer);
								outBuffer = 0x00;
							}
							else
							{
								outBuffer <<= 4;
								outBuffer &= 0xF0;
								outData.write(outBuffer);
								outBuffer = currentDataNib;
							}
							
						}	// if count >= 15
						else	// if between 3 and 15
						{
							// Here we're going to try to put the 0, the nib for repetition times, the other 0, and the new value
							if(!odd)
							{
								outBuffer = (byte) count;
								System.out.println("count in line 835 is " + count + "\n");
								outData.write(outBuffer);
								outBuffer = currentDataNib;
								outData.write(outBuffer);
							}
							else // if odd
							{
								outBuffer <<= 4;
								outBuffer &= 0xF0;
								outData.write(outBuffer);
								outBuffer = (byte) count;
								outBuffer <<= 4;
								outBuffer &= 0xF0;
								outData.write(outBuffer);
								outBuffer = currentDataNib;
							}
							count = 1;
							lastValue = currentDataNib;
						}
					}
					else	// if the previous color was NOT repeating
					{
						
						lastValue = currentDataNib;
						if(!odd)	
						{
							outBuffer = currentDataNib;
							odd = !odd;
						}
						else // if odd
						{
							outBuffer <<= 4;
							outBuffer |= currentDataNib;
							outData.write(outBuffer);
							outBuffer = 0x00; // This doesn't have to be done, but just in case.
							odd = !odd;
						}//if odd
					}	// if the previous color was not repeating
				}	// if new color
			//----------LSN------------------| PART B |-------------------------------------
				currentDataNib = (byte) (dataInArray[i] & 0x0F);
				if(currentDataNib == lastValue)
				{
					if(repeatOn)
					{
						count++;
					}
					else  // if it hasn't been repeating until now
					{
						repeatOn = true;
						count++;
					}
				}
				else // if new color
				{
				
					if(repeatOn)	// If the previous color was repeating, so we gotta put a 0 and the value of the new color
					{				// so odd doesn't change
						repeatOn = false;
	// ================= This is the part where we put the first 0 to signal that the last value will repeat itself ==============					
						
						if(count < numWorthRep)	// This is the only option that shouldn't put a 0
						{// The part below here puts the number repeated 3 or 2 times
							for(int k = 1; k < count; k++)
							{
								if(!odd)
								{
									outBuffer = lastValue;
									odd = !odd;
								}
								else	// if odd
								{
									outBuffer <<= 4;
									outBuffer |= lastValue;
									outData.write(outBuffer);
									odd = !odd;
								}
							}
							count = 1;
							lastValue = currentDataNib;
				//********* The part below puts the new number on the buffer ******************			
							if(!odd)
							{
								outBuffer = lastValue;
								odd = !odd;
							}
							else	// if even
							{
								outBuffer <<= 4;
								outBuffer |= lastValue;
								outData.write(outBuffer); 
								odd = !odd;
							}
						}
						else if(count >= 15)
						{// The part below puts a 0 to start the "repeat" signaling
							if(!odd)
							{
								outBuffer = 0x00;
								odd = !odd;
							}
							else // if odd
							{
								outBuffer <<= 4;
								outBuffer &= 0xF0; // Shouldn't have to do this, but for the sake of habit
								outData.write(outBuffer);
								outBuffer = 0x00;
								odd = !odd;
							}
						
						// The following part should put the number of repetitions:
							while(count >= 15)
							{
								if(!odd)
								{
									outBuffer = 0x0F;
									odd = !odd;
									count -=15;
									
								}
								else	// if odd
								{
									outBuffer <<= 4;
									outBuffer |= 0x0F;
									outData.write(outBuffer);
									odd = !odd;
									count -= 15;
								}
							}
							if(count == 0)
							{
								count++; 
							}
							else // if the count was something meaningful
							{
								if(!odd)
								{
									outBuffer = (byte)count;
									odd = !odd;
									count = 1;
								}
								else // if odd
								{
									outBuffer <<= 4;
									outData.write(outBuffer);
									odd = !odd;
									outBuffer = 0x00;
									count = 1;
								}
							}
				// The following part puts the 0 to signal end of repetition and the new value
							lastValue = currentDataNib;
							if(!odd)
							{
								outBuffer = currentDataNib;
								outData.write(outBuffer);
								outBuffer = 0x00;
							}
							else
							{
								outBuffer <<= 4;
								outBuffer &= 0xF0;
								outData.write(outBuffer);
								outBuffer = currentDataNib;
							}
							
						}	// if count >= 15
						else	// if between 3 and 15
						{
							// Here we're going to try to put the 0, the nib for repetition times, the other 0, and the new value
							if(!odd)
							{
								outBuffer = (byte) count;
								System.out.println("count in line 835 is " + count + "\n");
								outData.write(outBuffer);
								outBuffer = currentDataNib;
								outData.write(outBuffer);
							}
							else // if odd
							{
								outBuffer <<= 4;
								outBuffer &= 0xF0;
								outData.write(outBuffer);
								outBuffer = (byte) count;
								outBuffer <<= 4;
								outBuffer &= 0xF0;
								outData.write(outBuffer);
								outBuffer = currentDataNib;
							}
							count = 1;
							lastValue = currentDataNib;
						}
					}	// if the previous color was repeating
					else	// if the previous color was NOT repeating
					{
						
						lastValue = currentDataNib;
						if(!odd)	
						{
							outBuffer = currentDataNib;
							odd = !odd;
						}
						else // if odd
						{
							outBuffer <<= 4;
							outBuffer |= currentDataNib;
							outData.write(outBuffer);
							outBuffer = 0x00; // This doesn't have to be done, but just in case.
							odd = !odd;
						}//if odd
					}	// if the previous color was not repeating
				}	// if new color
		}
		
		if(repeatOn)	// If the previous color was repeating, so we gotta put a 0 and the value of the new color
		{				// so odd doesn't change
			repeatOn = false;
// ================= This is the part where we put the first 0 to signal that the last value will repeat itself ==============					
			
			if(count < numWorthRep)	// This is the only option that shouldn't put a 0
			{// The part below here puts the number repeated 3 or 2 times
				for(int k = 1; k < count; k++)
				{
					if(!odd)
					{
						outBuffer = lastValue;
						odd = !odd;
					}
					else	// if odd
					{
						outBuffer <<= 4;
						outBuffer |= lastValue;
						outData.write(outBuffer);
						odd = !odd;
					}
				}
				count = 1;
				lastValue = currentDataNib;
	
			}
			else if(count >= 15)
			{// The part below puts a 0 to start the "repeat" signaling
				if(!odd)
				{
					outBuffer = 0x0F;
					outData.write(outBuffer);
				}
				else // if odd
				{
					outBuffer <<= 4;
					outBuffer &= 0xF0;
					outData.write(outBuffer);
					outBuffer = 0x0F;
				}
				count -= 15;
			
		// The following part should put the number of repetitions:
				while(count >= 15)
				{
					if(!odd)
					{
						outBuffer = 0x0F;
						odd = !odd;
					}
					else // if odd
					{
						outBuffer <<= 4;
						outBuffer |= 0x0F;
						odd = !odd;
					}
					count -= 15;
				}
				if(count == 0)
				{
					count++;
				}
				else // if the count was something meaningful
				{
					if(!odd)
					{
						outBuffer = (byte) count;
						odd = !odd;
					}
					else // if odd
					{
						outBuffer <<= 4;
						outBuffer |= count;
						outData.write(outBuffer);
						odd = !odd;
					}
				}
	// The following part puts the 0 to signal end of repetition and the new value
				if(!odd)
				{
					outBuffer = 0x00;
					odd = !odd;
				}
				else
				{
					outBuffer <<= 4;
					outData.write(outBuffer);
					odd = !odd;
				}
				
			}	// if count >= 15
			else	// if between 3 and 15
			{
				// Here we're going to try to put the 0, the nib for repetition times, the other 0, and the new value
				if(!odd)
				{
					outBuffer = (byte) count;
					odd = !odd;
				}
				else // if odd
				{
					outBuffer <<= 4;
					outBuffer |= count;
					odd = !odd;
				}
				count = 1;
			}	// if between 3 and 15
		}
		if(!odd)
		{
			outBuffer = 0x00;
			outData.write(outBuffer);
		}
		else // if not odd
		{
			outBuffer <<= 4;
			outData.write(outBuffer);
			outBuffer = 0x00;
			outData.write(outBuffer);
		}
		
		return outData;
	}
	/**
	 * 
	 * @param i
	 * @param j
	 * @param palette
	 * @param raster
	 * @return index
	 */
	public int returnIndex( int i, int j, byte[][] palette, Raster raster)
	{
		byte[] pixel = new byte[2];
		pixel = create_rgb(j, i, raster);
		
		for(int index = 1; index<palette.length; index++)
		{
			if((pixel[0] == palette[index][0])&& (pixel[1] == palette[index][1]))
			{
				return index;
			}
			
		}

		return 0;	// This should never happen
		
	}
	
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		window.repaint();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	


}
