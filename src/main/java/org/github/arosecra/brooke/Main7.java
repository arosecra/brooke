package org.github.arosecra.brooke;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.github.arosecra.brooke.page.Page;

public class Main7 {
	
	private static final List<String> DO_NOT_CAP = new ArrayList<>();
	static {
		DO_NOT_CAP.add("a");
		DO_NOT_CAP.add("an");
		DO_NOT_CAP.add("and");
		DO_NOT_CAP.add("the");
		DO_NOT_CAP.add("of");
		DO_NOT_CAP.add("for");
		DO_NOT_CAP.add("with");
	}
	private static final List<String> DO_NOT_ADD = new ArrayList<>();
	static {
		DO_NOT_CAP.add("a");
		DO_NOT_CAP.add("an");
		DO_NOT_CAP.add("the");
	}

	
	public static void main(String[] args) throws Exception {
		File baseDir = new File("\\\\drobo5n\\public\\Scans\\_tobesorted");
		
		for(File bookfolder : baseDir.listFiles()) {
			String newBookName = getBookname(bookfolder.getName());
			
			System.out.println(bookfolder.getName() + " -> " + newBookName);
			for(File file : bookfolder.listFiles()) {
				if(file.getName().endsWith("cbt")) {
					String newFilename = newBookName + ".cbt";
					String tempFilename = "temp.cbt";
					if(file.getName().contains("RAW")) {
						newFilename = newBookName + "_RAW.cbt";
						tempFilename = "tempRaw.cbt";
					}
					if(!newFilename.equals(file.getName())) {
						File tempFile = new File(bookfolder, tempFilename);
						FileUtils.moveFile(file, tempFile);
						FileUtils.moveFile(tempFile, new File(bookfolder, newFilename));
					}
				}
			}
			if(!bookfolder.getName().equals(newBookName)) {
				File tempFolder = new File(baseDir, "temp");
				FileUtils.moveDirectory(bookfolder, tempFolder);
				FileUtils.moveDirectory(tempFolder, new File(baseDir, newBookName));
			}
			
			
			
		}
	}
	
	public static String getBookname(String oldName) {
		StringBuilder sb = new StringBuilder();
		String[] parts = oldName.split("[ \\.]");
		for(int i = 0; i < parts.length; i++) {
			if(parts[i].length() > 0) {
				String newPart = parts[i];
				if(i == 0 && DO_NOT_ADD.contains(newPart)) {
					continue;
				}
				if(!DO_NOT_CAP.contains(newPart)) {
					if(newPart.length() > 1)
						newPart = (""+newPart.charAt(0)).toUpperCase() + newPart.substring(1);
					else
						newPart = (""+newPart.charAt(0)).toUpperCase();
				}
				sb.append(newPart);
				if(i < parts.length -1) {
					sb.append('_');
				}
			}
		}
		return sb.toString();
	}
	
	
}
