package com.loadingImage.App;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
@SuppressWarnings("serial")
public class LoadImageApp extends Component{
	
	private static final float SIZE_LIMIT = (float) 1.5;
	
	private BufferedImage inputImg;
	private static BufferedImage showImg;
    public void paint(Graphics g) {
    	super.paint(g);
        g.drawImage(showImg, 0, 0, null);
    }
 
    public LoadImageApp(BufferedImage inMage)
    {
    	showImg = inMage;
    }
    public LoadImageApp(File filename) {
       try 
       {
       	   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
           inputImg = ImageIO.read(filename);
           if(inputImg.getHeight() > (screenSize.getHeight()/SIZE_LIMIT) || inputImg.getWidth() > (screenSize.getWidth()/SIZE_LIMIT))
           {
               double inputX, inputY, proportional;
               inputImg = ImageIO.read(filename);
         	   inputX = inputImg.getWidth();
         	   inputY = inputImg.getHeight();
               if(inputX > (screenSize.getWidth()/SIZE_LIMIT))
               {
            	   proportional = (screenSize.getWidth()/SIZE_LIMIT)/inputX;
            	   inputY *= proportional;
            	   inputX *= proportional;
               }	   
                if(inputY > (screenSize.getHeight()/SIZE_LIMIT))
                {
             	   proportional = (screenSize.getHeight()/SIZE_LIMIT)/inputY;
             	   inputY *= proportional;
             	   inputX *= proportional;
                }

                showImg = resize(inputImg, (int)inputX, (int)inputY);
           }
           else
           {
        	   showImg = inputImg;
           }
           
       } catch (IOException e) {
    	   System.out.println("Invalid filename");
       }
 
    }
 
    public Dimension getPreferredSize() {

        if (showImg == null) {
             return new Dimension(100,100);
        } else {
        	return new Dimension(showImg.getWidth(), showImg.getHeight());
       }
	
    }
    
    public static BufferedImage resize(BufferedImage img, int newW, int newH)
    {
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());  
        Graphics2D g = dimg.createGraphics();  
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
        g.dispose();  
        return dimg;  
    }
    
    public Raster getRaster()
    {
    	return showImg.getData();
    }
    
    public LoadImageApp resize(LoadImageApp image, int x, int y)
    {
    	LoadImageApp newImage = image;
    	newImage.showImg = resize(showImg, x, y);
    	return newImage;
    }
 
}
