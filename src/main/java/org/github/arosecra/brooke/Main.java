package org.github.arosecra.brooke;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;

public class Main {
	public static void main(String[] args) throws Exception {
		//TODO - no need to deal with the tarball mechanics. expand the tarball if necessary, then create the files & re-tar
		//       although it might take longer to do, this should be a bit easier to deal with. can probably bake it into the deskew
//		extractTarGZ("D:\\Scans\\3d game engine architecture\\3d game engine architecture.cbt");
		
		
		File baseFolder = new File("D:/Scans/books");
		for(File bookFolder : baseFolder.listFiles()) {
			File thumbnailFile = new File(bookFolder, "thumbnail.png");
			if(!thumbnailFile.exists()) {
				System.out.println("Creating thumbnail for " + bookFolder.getName());
				File tar = new File(bookFolder, bookFolder.getName() + ".cbt");
				BufferedImage thumbnail = createThumbnailFromTar(tar);
				ImageIO.write(thumbnail, "png", thumbnailFile);
			}
		}
	}

	private static BufferedImage createThumbnailFromTar(File file) throws IOException {
		BufferedImage cover = null;
		try (TarArchiveInputStream tarIn = new TarArchiveInputStream(new BufferedInputStream(new FileInputStream(file)))) {
	        TarArchiveEntry entry;
	        while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
	        	if(!entry.getName().startsWith(".metadata") && cover == null) {
	        		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        		IOUtils.copy(tarIn, baos);
	        		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	        		cover = ImageIO.read(bais);
	        	}
	        }
	    }
		return createThumbnail(cover);
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
}
