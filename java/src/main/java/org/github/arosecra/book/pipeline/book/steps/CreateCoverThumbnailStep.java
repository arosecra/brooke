package org.github.arosecra.book.pipeline.book.steps;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.book.pipeline.util.Images;

public class CreateCoverThumbnailStep implements JobStep {

	@Override
	public void execute(JobFolder job) throws IOException {
		File remoteThumbnail = new File(job.remoteFolder.folder, "thumbnail.png");
		
		for(File file : job.destFolder.listFiles()) {
			if(file.getName().endsWith("png")) {
				byte[] img = Files.readAllBytes(file.toPath());
				byte[] resizedImg = Images.resizeImageToWidth(img, 250);
				Files.write(remoteThumbnail.toPath(), resizedImg, StandardOpenOption.CREATE_NEW);
				break;
			}
		}
	}
	
}