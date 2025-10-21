package org.github.arosecra.book.pipeline.book.steps;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.book.pipeline.model.Pipeline;

public class HandleBlankImagesStep implements JobStep {

	private static String CONTENTS = """
			&nbsp;
			\\pagebreak
			""";

	@Override
	public void execute(Pipeline pipeline, JobFolder job) throws IOException {
		boolean ocrConfigured = pipeline.steps.stream().anyMatch(step -> step.getClass().getSimpleName().contains("OCR"));
		
		for (File file : job.tempFolder.listFiles()) {
			if (job.bookJob.ocrProperties.blankPages.contains(file.getName())) {
				if(ocrConfigured) {
					File mdFolder = new File(job.destFolder, ".md");
					mdFolder.mkdirs();
					File blankFile = new File(mdFolder, file.getName().replace("png", "md"));
					Files.writeString(blankFile.toPath(), CONTENTS, StandardOpenOption.CREATE_NEW);
				}
				
				Files.move(file.toPath(), Path.of(job.destFolder.getAbsolutePath(), file.getName()),
						StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}

}