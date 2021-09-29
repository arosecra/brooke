package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Main20 {
	public static void main(String[] args) throws IOException {
		
		File drobo5n = new File("\\\\drobo5n\\public\\scans");
		File drobo5n2 = new File("\\\\drobo5n2\\public\\Anime");
		File drobo5n22 = new File("\\\\drobo5n2\\public\\Movies");
		File drobo5n23 = new File("\\\\drobo5n2\\public\\");
		
//		for(File pdf : FileUtils.listFiles(drobo5n, new String[] {"cbt"}, true)) {
//			new File(pdf.getParentFile(), FilenameUtils.getBaseName(pdf.getName())+".item").createNewFile();
//		}
//		for(File pdf : FileUtils.listFiles(drobo5n2, new String[] {"mp4"}, true)) {
//			new File(pdf.getParentFile(), FilenameUtils.getBaseName(pdf.getName())+".item").createNewFile();
//		}
//		for(File pdf : FileUtils.listFiles(drobo5n22, new String[] {"mp4"}, true)) {
//			new File(pdf.getParentFile(), FilenameUtils.getBaseName(pdf.getName())+".item").createNewFile();
//		}
		for(File pdf : FileUtils.listFiles(drobo5n23, new String[] {"pdf"}, true)) {
			if(pdf.getName().contains("sleeve")) {
				String name = pdf.getName().replace("sleeve", "cover");
				FileUtils.moveFile(pdf, new File(pdf.getParentFile(), name));
			}
		}
	}
}
