package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Main20 {
	public static void main(String[] args) throws IOException {
		
		listItemsWithoutEnglishVtt(new File("\\\\drobo5n2\\public\\Anime"), "mp4");

	}

	private static void listItemsWithoutEnglishVtt(File remoteDir, String extension) {
		java.util.Collection<File> remoteFiles = FileUtils.listFiles(remoteDir, null, true);
		
		List<File> missingMkvs = new ArrayList<>();
		
		for(File file : remoteFiles) {
			if(file.getName().endsWith(extension)) {
				File english = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ".mkv");
				if(!english.exists())
					missingMkvs.add(english);
			}
		}
		
		for(int i = 0; i < missingMkvs.size(); i++) {
			System.out.println((i+1) + ": " + missingMkvs.get(i).getParentFile() + " is missing mkv");
		}
	}
}
