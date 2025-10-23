package org.github.arosecra.book.pipeline.book.steps;

import java.io.File;
import java.io.IOException;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.book.pipeline.model.Pipeline;
import org.github.arosecra.brooke.util.CommandLine;

public class CreateThumbnailsStep implements JobStep {

	@Override
	public void execute(Pipeline pipeline, JobFolder job) throws IOException {
		File thumbnailsFolder = new File(job.destFolder, ".thumbnails");
		thumbnailsFolder.mkdirs();
		
		CommandLine.run(new String[] {
			"D:\\Software\\ImageMagick\\magick.exe", 
			"mogrify", 
			"-path", thumbnailsFolder.getAbsolutePath(),
			"-format", "webp",
			"-thumbnail", "250x", "*.png"
			    
		}, job.destFolder, System.out);
	}

}