package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class Main16 {
	public static void main(String[] args) throws IOException {
		File basedir = new File("D:\\scans\\tobeexported");
		
		for(File folder : basedir.listFiles()) {
			String name = folder.getName();
			String newName = getNewName(folder.getName());
			if(name.equals(newName)) 
				continue;
			System.out.println(name + " -> " + newName);
			for(File file : folder.listFiles()) {
				if(file.getName().endsWith("_covers.pdf")) {
					moveFileWithTemp(folder, file, newName);
				}
			}
			moveFolderWithTemp(basedir, folder, newName);
		}
	}
	
	private static void moveFolderWithTemp(File basedir, File folder, String newName) throws IOException {
		File temp = new File(basedir, "temp");
		FileUtils.moveDirectory(folder, temp);
		FileUtils.moveDirectory(temp, new File(basedir, newName));
	}

	private static void moveFileWithTemp(File folder, File file, String newName) throws IOException {
		File temp = new File(folder, "temp.pdf");
		FileUtils.moveFile(file, temp);
		FileUtils.moveFile(temp, new File(folder, newName + "_covers.pdf"));
	}

	private static String getNewName(String name) {
		String[] notCapitalized = new String[] {"a", "an", "and", "are", "of", "the"};
		String[] parts = name.split("[ _]");
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < parts.length; i++) {
			boolean doNotCap = false;
			for(String notCap : notCapitalized) {
				if(notCap.equals(parts[i])) {
					doNotCap = true;
					sb.append(parts[i]);
				}
			}
			
			if(!doNotCap) {
				sb.append(StringUtils.upperCase(""+parts[i].charAt(0))+parts[i].substring(1));
			}
			
			if(i < parts.length -1)
				sb.append("_");
		}
		
		
		return sb.toString();
	}
	
	
}
