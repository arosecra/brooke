package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.github.arosecra.brooke.util.Try;

public class Main18 {
	public static void main(String[] args) throws IOException {
		List<String> mp4s = new ArrayList<>();
		List<String> mkvs = new ArrayList<>();
		
		File drobo5n2 = new File("\\\\drobo5n2\\public\\Videos\\Anime_TV\\Toaru_Raildex");
		
		for(File folder : Try.listFilesSafe(drobo5n2)) {
			for(File file : folder.listFiles()) {
				if(file.getName().endsWith("mkv")) {
					String[] parts = file.getName().replace(".mkv", "").split("_");
					String idx = parts[parts.length-1];
					
					String newName = folder.getName() + "_" + idx + ".mkv";
					
					System.out.println(file.getName() + " -> " + newName);
					
					FileUtils.moveFile(file, new File(folder, newName));
					
					
				}
				
				
				
			}
		}
		
		
		
	}
}
