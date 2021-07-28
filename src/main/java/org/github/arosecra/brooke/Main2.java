package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class Main2 {
	
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
	
	private static class Action {
		private String oldName;
		private String newName;
		private boolean rename;
		private boolean delete;
		File file;
		File folder;
	}
	
	//rename books / folders / delete extra files & folders
	public static void main(String[] args) throws IOException {
		File tempFolder = new File("D:/scans/temp");
		
		List<Action> actions = new ArrayList<>();
		//just do 'O' at first
		for(File folder : tempFolder.listFiles()) {
			File realFolder = folder;
			if(folder.isFile()) {
				realFolder = new File(tempFolder, FilenameUtils.getBaseName(folder.getName()));
				FileUtils.moveFileToDirectory(folder, realFolder, true);
			}
			
			
			String oldName = realFolder.getName();
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
			String newName = sb.toString();
			
			for(File file : realFolder.listFiles()) {
				Action action = new Action();
				actions.add(action);
				action.oldName = oldName;
				action.newName = newName;
				action.file = file;
				action.folder = realFolder;
				
				if(file.getName().endsWith("RAW.cbt")) {
					action.delete = true;
				} else if(file.getName().endsWith(".cbt")) {
					action.rename = !StringUtils.equals(action.newName, action.oldName);
				} else {
					action.delete = true;
				}
			}
			
		}
		
		for(Action action : actions) {
			if(action.delete)  {
				action.file.delete();
			} 
		}
		
		for(Action action : actions) {
			if(action.rename)  {
				action.file.renameTo(new File(action.folder, action.newName + ".cbt"));
				action.folder.renameTo(new File(action.folder.getParentFile(), action.newName));
			} 
		}
	}
}
