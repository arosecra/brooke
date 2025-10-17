package org.github.arosecra.book.pipeline.book.steps;

import java.io.File;
import java.io.IOException;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;

public class DeleteExcludedPagesStep implements JobStep {

	@Override
	public void execute(JobFolder job) throws IOException {
		for(File file : job.tempFolder.listFiles()) {
			if(job.bookJob.ocrProperties.excludedPages.contains(file.getName())) {
				file.delete();
			}
		}
	}
	
}