package org.github.arosecra.brooke;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.imageio.ImageIO;

public class Main23 {
	public static void main(String[] args) throws Exception {

		
		//load an image && resize to a thumbnail
		File dir = new File("D:\\scans\\Books\\Redemption_of_Althalus\\");
		
		for(File file : dir.listFiles()) {
			autocrop(file);
		}
	}
	
	public static void autocrop(File file) throws Exception {

		BufferedImage img = ImageIO.read(new FileInputStream(file));
		
		
		//determine a vertical and horizontal histogram for the image
		int[] vHistogram = new int[img.getHeight()];
		int[] hHistogram = new int[img.getWidth()];
		
		final int xmin = img.getMinX();
		final int ymin = img.getMinY();
		    
		final int ymax = ymin + img.getHeight();
		final int xmax = xmin + img.getWidth();


		for (int i = xmin;i<xmax;i++)
		{
		   for (int j = ymin;j<ymax;j++)
		   {
		                
		    int pixel = img.getRGB(i, j);
		        
		    if (((pixel & 0x00FFFFFF) == 0))
		    {
		    	vHistogram[j-ymin]++;
		    	hHistogram[i-xmin]++;
//		        System.out.println("black at "+i+","+j);
		    }
		   }
		}
		
		//print out the horizontal and vertical histogram - we'll try cropping a few this way
//		ImageIO.write(createThumbnail(img), "png", new File("D:\\scans\\Books\\bwthumbnail\\"+file.getName()));

		int reduction = img.getWidth() / 250;
		
		int startX = 0; int startY = 0;
		int endX = 0; int endY = 0;
		for(int i = 0; i < hHistogram.length; i++) {
			int adjustedValue = hHistogram[i] / 250;
			if(startX == 0 && adjustedValue != 0) 
				startX = i;
			
			if(adjustedValue != 0)
				endX = i;
		}
		for(int i = 0; i < vHistogram.length; i++) {
			int adjustedValue = vHistogram[i] / reduction;
//			System.out.println(i + "\t" + vHistogram[i] + "\t" + adjustedValue);
			if(startY == 0 && adjustedValue > 5) 
				startY = i;
			
			if(adjustedValue > 5)
				endY = i;
		}
		
		endX = endX - startX;
		endY = endY - startY;
		
		BigDecimal width = new BigDecimal(img.getWidth()).setScale(2).divide(new BigDecimal(1200), 2, RoundingMode.UP);
		BigDecimal height = new BigDecimal(img.getHeight()).setScale(2).divide(new BigDecimal(1200), 2, RoundingMode.UP);
		
		BigDecimal croppedWidth = new BigDecimal(endX).setScale(2).divide(new BigDecimal(1200), 2, RoundingMode.UP);
		BigDecimal croppedHeight = new BigDecimal(endY).setScale(2).divide(new BigDecimal(1200), 2, RoundingMode.UP);
		
		int estimatedXInches = (img.getWidth())/1200;
		int estimatedYInches = (img.getHeight())/1200;
		
		System.out.println(startX + " " + startY + " " + endX + " " + endY + " " + width.toPlainString() + " " + height.toPlainString() + " " + croppedWidth.toPlainString() + " " + croppedHeight.toPlainString());
		
		//scale back up our start/end positions		
		
		
//		BufferedImage cropped = cropImage(img, startX, startY, endX, endY);
//
//		ImageIO.write(cropped, "png", new File("D:\\scans\\Books\\cropped\\"+ file.getName()));
	}
	
	public static BufferedImage createThumbnail(BufferedImage input) {
		int width = input.getWidth();
		int height = input.getHeight();
		
		int newWidth = 250;
		
		int reduction = width / 250;
		int newHeight = height / reduction;
		
		
		BufferedImage thumbnail = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		thumbnail.createGraphics().drawImage(input.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH),0,0,null);
		return thumbnail;
	}
	
	public static BufferedImage cropImage(BufferedImage image, int startX, int startY, int endX, int endY) {
		BufferedImage img = image.getSubimage(startX, startY, endX, endY); //fill in the corners of the desired crop location here
		BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = copyOfImage.createGraphics();
		g.drawImage(img, 0, 0, null);
		return copyOfImage; //or use it however you want
	}
}