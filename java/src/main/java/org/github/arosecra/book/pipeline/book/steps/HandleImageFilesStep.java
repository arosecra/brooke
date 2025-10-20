package org.github.arosecra.book.pipeline.book.steps;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;

public class HandleImageFilesStep implements JobStep {

	@Override
	public void execute(JobFolder job) throws IOException {
		List<String> imagePages = job.bookJob.ocrProperties.imagePages;
		for (File file : job.tempFolder.listFiles()) {
			String filename = file.getName();

			if (imagePages.contains(filename)) {
				Files.move(file.toPath(), Path.of(job.destFolder.getAbsolutePath(), file.getName()),
						StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}
}