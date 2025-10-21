package org.github.arosecra.book.pipeline.book.steps;

import java.io.File;
import java.io.IOException;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.book.pipeline.model.OCRProperties;
import org.github.arosecra.book.pipeline.model.Pipeline;

public class ReadOCRPropertiesStep implements JobStep {

	@Override
	public void execute(Pipeline pipeline, JobFolder job) throws IOException {
		job.bookJob.ocrProperties = OCRProperties.fromProperties(new File(job.remoteFolder.folder, "ocr.properties"));
	}
	
}