package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

public class Main20 {
	public static void main(String[] args) throws IOException {
		
		createItemFile(new File("\\\\drobo5n\\public\\scans\\Fiction_Repository"), "cbt");
		createItemFile(new File("\\\\drobo5n\\public\\scans\\Light_Novels_Repository"), "cbt");
		createItemFile(new File("\\\\drobo5n\\public\\scans\\NonFiction_Repository"), "cbt");
		createItemFile(new File("\\\\drobo5n\\public\\scans\\Research_Papers_Repository"), "pdf");
		createItemFile(new File("\\\\drobo5n2\\public\\Anime"), "mp4");
		createItemFile(new File("\\\\drobo5n2\\public\\Movies"), "mp4");

	}

	private static void createItemFile(File remoteDir, String extension) throws IOException {
		java.util.Collection<File> remoteFiles = FileUtils.listFiles(remoteDir, null, true);
		
		for(File file : remoteFiles) {
			if(file.getName().endsWith(extension)) {
				File dir = file.getParentFile();
				new File(dir, dir.getName()+".item").createNewFile();
			}
		}
	}
}
