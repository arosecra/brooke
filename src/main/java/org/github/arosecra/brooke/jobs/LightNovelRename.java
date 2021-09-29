package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class LightNovelRename implements BrookeJobStep {
	private static String[][] map = new String[][] {
		new String[] {"slime", "That_Time_I_Reincarnated_as_a_Slime"},
		new String[] {"index", "Certain_Magical_Index"},
		new String[] {"spice", "Spice_and_Wolf"},
		new String[] {"spider", "So_Im_a_Spider_So_What"},
		new String[] {"dungeon", "Is_it_Wrong_to_Pick_Up_Girls_in_a_Dungeon"},
		new String[] {"sword", "Sword_Art_Online"},
		new String[] {"progressive", "Sword_Art_Online_Progressive"},
		new String[] {"parchment", "Wolf_and_Parchment"},
	};

	@Override
	public boolean required(File folder) throws IOException {
		String[] mapItem = null;
		for(String[] item : map) {
			if(folder.getName().startsWith(item[0])) {
				mapItem = item;
			}
		}
		
		return mapItem != null;
	}

	@Override
	public File execute(File localFolder) throws IOException {
		File result = localFolder;
		if(required(localFolder)) {
			String[] mapItem = null;
			for(String[] item : map) {
				if(localFolder.getName().startsWith(item[0])) {
					mapItem = item;
				}
			}
			
			for(File file : localFolder.listFiles()) {
				if(file.getName().startsWith(mapItem[0])) {
					String newName = mapItem[1]+file.getName().substring(mapItem[0].length());
					FileUtils.moveFile(file, new File(localFolder, newName));
				}
			}
			
			String newName = mapItem[1]+localFolder.getName().substring(mapItem[0].length());
			result = new File(localFolder.getParentFile(), newName);
			FileUtils.moveDirectory(localFolder, result);
		}
		return result;
	}

	@Override
	public boolean isManual() {
		return false;
	}

	@Override
	public List<File> filesRequiredForExecution(File folder) {
		List<File> result = new ArrayList<>();
		return result;
	}

	@Override
	public boolean isRemoteStep() {
		return true;
	}
}
