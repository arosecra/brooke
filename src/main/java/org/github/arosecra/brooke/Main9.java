package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Main9 {
	public static void main(String[] args) throws Exception {
		File videosDir = new File("\\\\drobo5n2\\public\\videos");
		
		for(File letterDir : videosDir.listFiles()) {
			renameFilesAndFolders(letterDir);
		}
		
	}

	private static void renameFilesAndFolders(File file) throws IOException {
		if(file.isDirectory() && file.listFiles() != null) {
			for(File child : file.listFiles()) {
				renameFilesAndFolders(child);
			}
		}
		
		if(file.getName().contains(" ")) {
			File newFile = new File(file.getParentFile(), file.getName().replace(' ', '_'));
			if(file.isFile()) 
				FileUtils.moveFile(file, newFile);
			else if(file.isDirectory())
				FileUtils.moveDirectory(file, newFile);
		}
	}
}
