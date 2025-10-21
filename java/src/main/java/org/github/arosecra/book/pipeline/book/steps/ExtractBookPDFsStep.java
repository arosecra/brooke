package org.github.arosecra.book.pipeline.book.steps;

import java.io.File;
import java.io.IOException;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.book.pipeline.model.JobSubStep;
import org.github.arosecra.book.pipeline.model.Pipeline;
import org.github.arosecra.book.pipeline.util.CommandLine;

public class ExtractBookPDFsStep implements JobStep {

	private static final String GS_EXE = "C:\\software\\ghostscript\\bin\\gswin64c.exe";

	private static void extractPdf(File file, String outputFile, int dpi, String device) {
		String[] args = new String[] { GS_EXE, //
				"-dBATCH", //
				"-dNOPAUSE", //
				"-dGraphicsAlphaBits=4", //
				"-q", //
				"-r" + dpi, //
				"-sDEVICE=" + device, //
				"-dUseCropBox", //
				"-sOutputFile=" + outputFile, //
				file.getAbsolutePath() //
		};

		CommandLine.run(args);
	}

	@Override
	public void execute(Pipeline pipeline, JobFolder job) throws IOException {

		File[] workFiles = job.sourceFolder.listFiles();

		int pdfCount = 0;
		for (File file : workFiles) {
			if (file.getName().endsWith("pdf")) {
				pdfCount++;
			}
		}

		for (File file : workFiles) {
			if (file.getName().endsWith("pdf") && file.getName().contains("covers")) {
				JobSubStep jss = new JobSubStep("Extract", job.workFolder, 1, pdfCount);
				jss.startAndPrint();
				extractPdf(file, job.tempFolder.getAbsolutePath() + "\\" + job.workFolder.getName() + "-000-%03d.png",
						300, "png16m");
				jss.endAndPrint();
			}
		}

		for (int i = 0; i < workFiles.length; i++) {
			File file = workFiles[i];
			if (file.getName().endsWith("pdf") && !file.getName().contains("covers")) {
				String prefix = String.format("%03d", i + 1);
				String colorSpace = "pngmono";
				int size = 1200;
				if (file.getName().contains("color")) {
					size = 300;
					colorSpace = "png16m";
				}
				JobSubStep jss = new JobSubStep("Extract", job.workFolder, i + 2, pdfCount);
				jss.startAndPrint();
				extractPdf(file,
						job.tempFolder.getAbsolutePath() + "\\" + job.workFolder.getName() + "-" + prefix + "-%03d.png",
						size, colorSpace);
				jss.endAndPrint();
			}
		}
	}

}
