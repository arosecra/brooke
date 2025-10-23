package org.github.arosecra.book.pipeline.book.steps;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.book.pipeline.model.Pipeline;
import org.github.arosecra.book.pipeline.util.Images;

public class CreateThumbnailsStep implements JobStep {

	@Override
	public void execute(Pipeline pipeline, JobFolder job) throws IOException {
		File thumbnailsFolder = new File(job.destFolder, ".thumbnails");
		thumbnailsFolder.mkdirs();

		for (File file : job.destFolder.listFiles()) {
			if (file.getName().endsWith("png")) {
				this.handleSingleFile(thumbnailsFolder, file, job);
				System.gc();
			}
		}
	}

	private void handleSingleFile(File thumbnailsFolder, File file, JobFolder job) throws IOException {
		byte[] img = Files.readAllBytes(file.toPath());
		byte[] resizedImg = Images.resizeImageToWidth(img, 250);
		Files.write(new File(thumbnailsFolder, file.getName()).toPath(), resizedImg, StandardOpenOption.CREATE_NEW);
	}

}