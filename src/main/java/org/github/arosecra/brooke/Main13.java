package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Main13 {
	public static void main(String[] args) throws IOException {

		Collection<File> subFiles = FileUtils.listFiles(new File("D:\\Library\\Video_Repository"),
				new String[] { "sub", "sup", "idx" }, true);

		Set<String> foldersWithMultipleSubs = new HashSet<>();
		for (File file : subFiles) {
			File folder = file.getParentFile();
			String basename = FilenameUtils.getBaseName(file.getName());
			if(!basename.equals("english")) {
				String ext = FilenameUtils.getExtension(file.getName());
			
				File newFile = new File(folder, "english."+ext);
				FileUtils.moveFile(file, newFile);
			}
			
			for(File child : folder.listFiles()) {
				if(child.getName().contains("Chapters"))
					FileUtils.moveFile(child, new File(folder, "chapters.xml"));
			}

		}

		List<String> lines = new ArrayList<>();
		lines.addAll(foldersWithMultipleSubs);
		Collections.sort(lines);

		lines.forEach(s -> System.out.println(s));
		System.out.println(lines.size());

//		for(String line : lines) {
//			for(File file : new File(line).listFiles()) {
//				boolean deleteFile = false;
//				if((file.getName().endsWith("sub") || 
//						file.getName().endsWith("sup")	
//					)	) {
//					System.out.println(line + " : " + file.length());
//				}
//					
//				
//				if(file.getName().endsWith("sub") && (file.length() < 1024*200)) {
//					deleteFile = true;
//				} else if(file.getName().endsWith("sup") && (file.length() < 1024*200)) {
//					deleteFile = true;
//				}
//				if(deleteFile) {
//					File folder = new File(new File(line), FilenameUtils.getBaseName(file.getName()));
//					if(folder.exists()) FileUtils.deleteDirectory(folder);
//					FileUtils.delete(file);
//				}
//			}
//		}


	}
}
