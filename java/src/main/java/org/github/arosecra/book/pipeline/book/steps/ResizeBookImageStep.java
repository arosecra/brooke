package org.github.arosecra.book.pipeline.book.steps;

import java.io.IOException;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.book.pipeline.model.Pipeline;
import org.github.arosecra.brooke.util.CommandLine;

public class ResizeBookImageStep implements JobStep {

	@Override
	public void execute(Pipeline pipeline, JobFolder job) throws IOException {

		CommandLine.run(new String[] {
				"D:\\Software\\ImageMagick\\magick.exe", 
				"mogrify", 
				"-path", job.tempFolder.getAbsolutePath(),
				"-format", "png",
				"-resize", "1920x>", "*-8-.png"
				    
			}, job.tempFolder, System.out);
		
		CommandLine.run(new String[] {
				"D:\\Software\\ImageMagick\\magick.exe", 
				"mogrify", 
				"-path", job.tempFolder.getAbsolutePath(),
				"-format", "png",
				"-depth", "1",
				"-adaptive-resize", "1920x>", "*-1-*.png"
				    
			}, job.tempFolder, System.out);
		
		
//		for(File file : job.tempFolder.listFiles()) {
//			byte[] img = FileUtils.readFileToByteArray(file);
//			byte[] resizedImg = Images.resizeImageToWidth(img, 1920);
//			FileUtils.writeByteArrayToFile(file, resizedImg);
//		}
	}
}