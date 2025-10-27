package org.github.arosecra.book.pipeline.book.steps;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.book.pipeline.model.Pipeline;
import org.github.arosecra.brooke.util.CommandLine;

public class ModifyBookImageStep implements JobStep {

	@Override
	public void execute(Pipeline pipeline, JobFolder job) throws IOException {
		File[] fileList = job.tempFolder.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			this.handleSingleFile(file, job);
            System.gc();
		}
	}
	
	private void handleSingleFile(File file, JobFolder job) throws IOException {
		File outputFile = new File(job.destFolder, file.getName());
		if (file.getName().contains("-8-")) {
			Files.move(file.toPath(), Path.of(job.destFolder.toPath().toString(), file.getName()),
					StandardCopyOption.REPLACE_EXISTING);
		} else {
			double skew = imageMagickDeskewDegrees(file);
			if (skew != 0) {
				imageMagickRotate(file, skew, outputFile);
				file.delete();
			} else {
				Files.move(file.toPath(), Path.of(job.destFolder.toPath().toString(), file.getName()),
						StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}

	
//	D:\scans\pipeline_temp>magick Calibans_War-001-bnw-030.png -virtual-pixel white -distort SRT 0.92314312097010420288 rotated.png

	private void imageMagickRotate(File sourceFile, double degrees, File destFile) {
		CommandLine.run(new String[] { //
			"D:\\Software\\ImageMagick\\magick.exe", //
			sourceFile.getAbsolutePath(),
			"-virtual-pixel",
			"white",
			"-distort",
			"SRT",
			Double.toString(degrees),
			destFile.getAbsolutePath()
		});
	}
	
	private double imageMagickDeskewDegrees(File file) throws IOException {
		// D:\Software\ImageMagick\magick.exe
		// C:\scans\deskew\Redemption_of_Althalus_RAW\Redemption_of_Althalus-001-001.png
		// -deskew 40% -format '%[deskew:angle]' info:
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final String utf8 = StandardCharsets.UTF_8.name();
		try (PrintStream ps = new PrintStream(baos, true, utf8)) {
			CommandLine.run(new String[] { "D:\\Software\\ImageMagick\\magick.exe", file.getAbsolutePath(), "-deskew",
					"40%", "-format", "%[deskew:angle]", "info:" }, ps);
		}
		return Double.parseDouble(baos.toString(utf8));
	}

}