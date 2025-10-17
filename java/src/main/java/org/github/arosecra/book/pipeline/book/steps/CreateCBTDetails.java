package org.github.arosecra.book.pipeline.book.steps;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;

public class CreateCBTDetails implements JobStep {

	@Override
	public void execute(JobFolder job) throws IOException {
		Path remoteCbtDetails = Path.of(job.destFolder.getAbsolutePath(), "cbtDetails.yaml");
		int size = 0;
		for (File file : job.tempFolder.listFiles()) {
			if (file.getName().endsWith("png"))
				size++;
		}

		String contents = "---" + System.lineSeparator() + "numberOfPages: " + size + System.lineSeparator();
		Files.writeString(remoteCbtDetails, contents, StandardOpenOption.CREATE_NEW);
	}

}