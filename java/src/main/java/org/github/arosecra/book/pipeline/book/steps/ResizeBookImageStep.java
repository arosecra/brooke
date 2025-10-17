package org.github.arosecra.book.pipeline.book.steps;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.book.pipeline.util.Images;

public class ResizeBookImageStep implements JobStep {

	@Override
	public void execute(JobFolder job) throws IOException {
		for(File file : job.tempFolder.listFiles()) {
			byte[] img = FileUtils.readFileToByteArray(file);
			byte[] resizedImg = Images.resizeImageToWidth(img, 1920);
			FileUtils.writeByteArrayToFile(file, resizedImg);
		}
	}
}