package org.github.arosecra.brooke.jobs;

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

public class CreateCbtThumbnail implements BrookeJobStep {

	@Override
	public boolean required(File folder) throws IOException {
		File thumbnailFile = new File(folder, "thumbnail.png");
		File tar = new File(folder, folder.getName() + "_RAW.cbt");
		return !thumbnailFile.exists() && tar.exists();
	}

	@Override
	public File execute(File folder) throws IOException {
		System.out.println("Creating thumbnail for " + folder.getName());
		if(required(folder)) {
			File thumbnailFile = new File(folder, "thumbnail.png");
			File tar = new File(folder, folder.getName() + "_RAW.cbt");
			BufferedImage thumbnail = createThumbnailFromTar(tar);
			ImageIO.write(thumbnail, "png", thumbnailFile);
		}
		return folder;
	}

	@Override
	public boolean isManual() {
		return false;
	}
	
	private BufferedImage createThumbnailFromTar(File file) throws IOException {
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
	
	public BufferedImage createThumbnail(BufferedImage input) {
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
