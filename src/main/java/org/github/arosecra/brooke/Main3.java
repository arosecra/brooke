package org.github.arosecra.brooke;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Main3 {
	//convert images to jbig2 for compression test
	public static void main(String[] args) {
		File inputFolder = new File("D:\\Scans\\temp\\Acquisition_of_Strategic_Knowledge\\Acquisition_of_Strategic_Knowledge");
		File outputFolder = new File("D:\\Scans\\temp\\Acquisition_of_Strategic_Knowledge\\Acquisition_of_Strategic_Knowledge_jbig2");
		
		for(File file : inputFolder.listFiles()) {
			convertImage(file, outputFolder);
		}
		
	}
	
	public static void convertImage(File inputFile, File outputFolder) {

    	System.out.println("Converting " + inputFile.getName());
		File outputFile = new File(outputFolder, FilenameUtils.getBaseName(inputFile.getName()) + ".webp");
	    ImageWebpLibraryWrapper.convertToWebP(inputFile, outputFile);
	    
//		try (InputStream is = new FileInputStream(inputFile)) {
//		    BufferedImage image = ImageIO.read(is);
////		    if(isBAndW(image)) {
//			    try (OutputStream os = new FileOutputStream(outputFile)) {
//			    	System.out.println("Converting " + inputFile.getName());
//			        ImageIO.write(image, "jpeg 2000", os);
//			    } catch (Exception exp) {
//			        exp.printStackTrace();
//			    }
////		    } else {
////		    	System.out.println("Copying " + inputFile.getName());
////		    	FileUtils.copyFile(inputFile, new File(outputFolder, inputFile.getName()));
////		    }
//		} catch (Exception exp) {
//		    exp.printStackTrace();
//		}
	}
	
	public static boolean isBAndW(BufferedImage image) {
		Raster ras = image.getRaster();
		int elem = ras.getNumDataElements();
		return elem == 1;
	}
}
