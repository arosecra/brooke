package org.github.arosecra.brooke;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

public class Main9 {
	public static void main(String[] args) throws Exception {
		File videosDir = new File("D:\\Library\\Research_Papers");
		Map<Character, List<String>> categories = new TreeMap<>();
		
		for(File letterDir : videosDir.listFiles()) {
			char c = letterDir.getName().charAt(0);
			categories.put(c, new ArrayList<>());
			if(letterDir.isDirectory()) {
				for(File folder : letterDir.listFiles()) {
					categories.get(c).add(folder.getName());
				}
			}
		}
		
		for(Entry<Character, List<String>> entry : categories.entrySet()) {
			File catFile = new File("D:\\Library\\Collections\\Research_Papers", entry.getKey()+".txt");
			FileUtils.writeLines(catFile, entry.getValue());
		}
		
	}

	
}
