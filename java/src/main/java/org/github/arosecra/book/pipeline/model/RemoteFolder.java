package org.github.arosecra.book.pipeline.model;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RemoteFolder {
	public File folder;
	public File[] contents;
	public boolean hasPDFs() {
		boolean ret = false;
		for(File file : contents) {
			if(file.getName().endsWith(".pdf")) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	public boolean hasOCRProperties() {
		boolean ret = false;
		for(File file : contents) {
			if(file.getName().equalsIgnoreCase("ocr.properties")) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public static List<RemoteFolder> getLeafFolders(String startingPath) {
		List<RemoteFolder> ret = new ArrayList<>();
		
		File startingFile = new File(startingPath);
		
		Queue<File> filesToProcess = new LinkedList<File>();
		addDirectoriesToQueue(filesToProcess, startingFile.listFiles());
		
		while(!filesToProcess.isEmpty()) {
			File folder = filesToProcess.poll();
			File[] childFiles = folder.listFiles();
			if(hasDirectories(childFiles)) {
				addDirectoriesToQueue(filesToProcess, childFiles);
			} else {
				RemoteFolder rf = new RemoteFolder();
				rf.contents = childFiles;
				rf.folder = folder;
				ret.add(rf);
			}
		}
		
		
		return ret;
	}
	
	private static void addDirectoriesToQueue(Queue<File> queue, File[] files) {
		for(File file : files) {
			if(file.isDirectory()) {
				queue.add(file);
			}
		}
	}
	
	private static boolean hasDirectories(File[] files) {
		boolean ret = false;
		for(File file : files) {
			if(file.isDirectory()) {
				ret = true;
				break;
			}
		}
		return ret;
	}
}