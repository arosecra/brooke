package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Main19 {
	public static void main(String[] args) throws IOException {		
		System.out.println("Anime\r\n-------------");
		processMovieFolder(new File("\\\\drobo5n2\\public\\Anime"));
		System.out.println("Movies\r\n-------------");
		processMovieFolder(new File("\\\\drobo5n2\\public\\Movies"));
		
	}

	private static void processMovieFolder(File drobo5n2) throws IOException {
		Set<String> extras = new HashSet<>();
		for(File pdf : FileUtils.listFiles(drobo5n2, new String[] {"pdf"}, true)) {

			int pdfCount = 0;
			int actualPdfCount = 0;
			for(File file : pdf.getParentFile().listFiles()) {
			
				if(file.getName().endsWith("pdf") && file.getName().toLowerCase().contains("scan")) {
					pdfCount++;
				}
				if(file.getName().endsWith("pdf"))
					actualPdfCount++;
			}
			
			if(pdfCount > 1) {
				extras.add(pdf.getParentFile().getName());
			}
			if(actualPdfCount == 1) {
				for(File file : pdf.getParentFile().listFiles()) {
					if(file.getName().endsWith("pdf") && !file.getName().equals("cover.pdf"))
						FileUtils.moveFile(file, new File(file.getParentFile(), "cover.pdf"));
				}
			}
		}
		List<String> sortedExtras = new ArrayList<>(extras);
		Collections.sort(sortedExtras);
		for(int i = 0; i < sortedExtras.size(); i++)
			System.out.println(i + ". " + sortedExtras.get(i) +  " has extra pdfs");
		

		for(File file : FileUtils.listFiles(drobo5n2, new String[] {"srt"}, true)) {
			if(!file.getName().equals("english.srt"))
				FileUtils.moveFile(file, new File(file.getParentFile(), "english.srt"));
		}

		renameFiles(drobo5n2, "mp4");
		renameFiles(drobo5n2, "mkv");
		renameFiles(drobo5n2, "item");
	}

	private static void renameFiles(File drobo5n2, String extension) throws IOException {
		for(File file : FileUtils.listFiles(drobo5n2, null, true)) {
			String basename = FilenameUtils.getBaseName(file.getName());
			String parentName = file.getParentFile().getName();
			String expectedName = parentName + "." + extension;
			String tempName = "temp."+extension;
			if(file.getName().endsWith(extension) && !basename.equals(parentName)) {
				File temp = new File(file.getParentFile(), tempName);
				File newFile = new File(file.getParentFile(), expectedName);
				FileUtils.moveFile(file, temp);
				FileUtils.moveFile(temp, newFile);
				System.out.println(basename + " -> " + expectedName);
			}
		}
	}
	

}
