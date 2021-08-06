package org.github.arosecra.brooke;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.github.arosecra.brooke.page.Page;

public class Main6 {

	
	public static void main(String[] args) throws Exception {
		File baseDir = new File("D:\\scans\\books");
		
		for(File file : baseDir.listFiles()) {
			File cbtFile = new File(file, file.getName() + ".cbt");
			
			if(cbtFile.exists()) {
			
				int pageCount = 0;
				try (TarArchiveInputStream tarIn = new TarArchiveInputStream(
						new BufferedInputStream(new FileInputStream(cbtFile)))) {
					TarArchiveEntry entry;
					while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
						pageCount++;
					}
				}
				
				
				System.out.println(file.getName() + " " + pageCount);
			}
		}
	}
}
