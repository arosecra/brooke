package org.github.arosecra.book.pipeline.book.steps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.brooke.util.CommandLine;

public class ConvertBookImageToWebpStep implements JobStep {

	@Override
	public void execute(JobFolder job) throws IOException {
		File thumbnailsFolder = new File(job.destFolder, ".thumbnails");
		List<File> filesToProcess = new ArrayList<>();

		filesToProcess.addAll(Arrays.stream(job.destFolder.listFiles())
				.filter(file -> file.getName().endsWith("png")).collect(Collectors.toList()));
		filesToProcess.addAll(Arrays.stream(thumbnailsFolder.listFiles()).filter(file -> file.getName().endsWith("png"))
				.collect(Collectors.toList()));

		for (File inputFile : filesToProcess) {
			File outputFile = new File(job.destFolder, inputFile.getName().replace(".png", ".webp"));

			CommandLine.run(new String[] { "C:\\Software\\libwebp\\bin\\cwebp", "-lossless",
					inputFile.getAbsolutePath(), "-o", outputFile.getAbsolutePath() });
			inputFile.delete();
		}
	}

}