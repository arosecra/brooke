package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.util.CommandLine;
import org.github.arosecra.brooke.util.Try;


public class Extract implements BrookeJobStep {
	
	private static final String GS_EXE = "C:\\software\\ghostscript\\bin\\gswin64c.exe";

	private static void tarToRawCbt(File pngFolder, File bookFolder) {
		CommandLine.run(new String[] {
				"D:\\software\\7za\\7za.exe", 
    			"a", 
    			"-ttar", 
    			"-o" + pngFolder.getAbsolutePath(),
    			bookFolder.getAbsolutePath()+"\\"+bookFolder.getName()+"_RAW.cbt", 
    			pngFolder.getAbsolutePath() + "\\*.png" 	
		});
	}


	private static void extractPdf(File file, File pngFolder, String bookname, String prefix, int dpi, String device) {
		String[] args = new String[] {
			GS_EXE,
			"-dBATCH",
			"-dNOPAUSE",
			"-dGraphicsAlphaBits=4",
			"-q",
			"-r"+dpi,
			"-sDEVICE="+device,
			"-dUseCropBox",
			"-sOutputFile="+pngFolder.getAbsolutePath()+"\\"+bookname+"-"+prefix+"-%03d.png",
			file.getAbsolutePath()
		};
		
		org.github.arosecra.brooke.util.CommandLine.run(args);
	}


	@Override
	public boolean required(File folder) {
		int pdfCount = 0;
		for(File file : Try.listFilesSafe(folder)) {
			if(file.getName().endsWith("pdf"))
				pdfCount++;
		}
		
		File rawCbtFile = new File(folder, folder.getName()+"_RAW.cbt");
		
		return pdfCount > 1 && !rawCbtFile.exists();
	}

	@Override
	public File execute(File folder) throws IOException {
		if(required(folder)) {
			File pngFolder = new File(folder, "png");
			
			if(!pngFolder.exists()) {
				pngFolder.mkdirs();
				
				File[] children = Try.listFilesSafe(folder);
				
				int pdfCount = 0; 
				for(File file : children) {
					if(file.getName().endsWith("pdf")) {
						pdfCount++;
					}
				}
				
				
				for(File file : children) {
					if(file.getName().endsWith("pdf") && file.getName().contains("covers")) {
						JobSubStep jss = new JobSubStep("Extract", folder, 1, pdfCount);
						jss.startAndPrint();
						extractPdf(file, pngFolder, folder.getName(), "000", 300, "png16m");
						jss.endAndPrint();
					}
				}
				
				
				for(int i = 0; i < children.length; i++) {
					File file = children[i];
					if(file.getName().endsWith("pdf") && !file.getName().contains("covers")) {
						String prefix = String.format("%03d", i+1);
						JobSubStep jss = new JobSubStep("Extract", folder, i+2, pdfCount);
						jss.startAndPrint();
						String color = "pngmono";
						int dpi = 1200;
						if(file.getName().contains("color")) {
							color = "png16m";
							dpi = 300;
						}
						extractPdf(file, pngFolder, folder.getName(), prefix, dpi, color);
						jss.endAndPrint();
					}
				}
			}
			
			tarToRawCbt(pngFolder, folder);
			
			FileUtils.deleteDirectory(pngFolder);
		}
		return folder;
	}


	@Override
	public boolean isManual() {
		return false;
	}
}
