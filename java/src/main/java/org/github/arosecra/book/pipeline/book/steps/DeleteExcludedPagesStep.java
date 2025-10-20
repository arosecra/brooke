package org.github.arosecra.book.pipeline.book.steps;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;

public class DeleteExcludedPagesStep implements JobStep {

	@Override
	public void execute(JobFolder job) throws IOException {
		List<String> excludedPages = job.bookJob.ocrProperties.excludedPages;
		for(File file : job.tempFolder.listFiles()) {
			String filename = file.getName();
			
			if(excludedPages.contains(filename)) {
				file.delete();
			}
		}
	}
	
}