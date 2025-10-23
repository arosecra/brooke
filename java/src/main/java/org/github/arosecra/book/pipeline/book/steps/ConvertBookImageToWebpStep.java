package org.github.arosecra.book.pipeline.book.steps;

import java.io.File;
import java.io.IOException;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.book.pipeline.model.Pipeline;
import org.github.arosecra.brooke.util.CommandLine;

public class ConvertBookImageToWebpStep implements JobStep {

	@Override
	public void execute(Pipeline pipeline, JobFolder job) throws IOException {
		convertFolder(job.destFolder);
	}
	
	private void convertFolder(File folder) {
		for (File inputFile : folder.listFiles()) {
			if(inputFile.getName().endsWith("png")) {
				File outputFile = new File(folder, inputFile.getName().replace(".png", ".webp"));

				CommandLine.run(new String[] { "C:\\Software\\libwebp\\bin\\cwebp", "-lossless",
						inputFile.getAbsolutePath(), "-o", outputFile.getAbsolutePath() });
				inputFile.delete();
				
			}
		}
	}

}