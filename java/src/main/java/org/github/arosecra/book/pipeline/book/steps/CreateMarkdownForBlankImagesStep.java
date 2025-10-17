package org.github.arosecra.book.pipeline.book.steps;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;

public class CreateMarkdownForBlankImagesStep implements JobStep {

	private static String CONTENTS = """
&nbsp;
\\pagebreak
""";

	@Override
	public void execute(JobFolder job) throws IOException {
		for(File file : job.destFolder.listFiles()) {
			if(job.bookJob.ocrProperties.blankPages.contains(file.getName())) {
				File mdFolder = new File(job.destFolder, ".md");
				mdFolder.mkdirs();
				
				File blankFile = new File(mdFolder, file.getName().replace("png", "md"));
				Files.writeString(blankFile.toPath(), CONTENTS, StandardOpenOption.CREATE_NEW);
			}
		}
	}
	
}