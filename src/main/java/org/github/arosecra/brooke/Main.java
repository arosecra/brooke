package org.github.arosecra.brooke;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Main {
	public static void main(String[] args) throws Exception {
		createThumbnail("D:\\Scans\\3d game engine architecture\\3d game engine architecture\\3d game engine architecture_Page_001.png");
		createThumbnail("D:\\Scans\\without remorse\\without remorse\\without remorse_Page_001.png");
	}
	
	
	public static void createThumbnail(String filename) throws Exception {
		File file = new File(filename);
		BufferedImage input = ImageIO.read(file);
		int width = input.getWidth();
		int height = input.getHeight();
		
		int newWidth = 250;
		
		int reduction = width / 250;
		int newHeight = height / reduction;
		
		
		BufferedImage thumbnail = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		thumbnail.createGraphics().drawImage(input.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH),0,0,null);
		ImageIO.write(thumbnail, "png", new File(file.getParentFile().getName() + ".png"));
	}
}
