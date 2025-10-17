package org.github.arosecra.brooke.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

public class Try {

	public static void loadProperties(Properties props, File propertiesFile) {
		try {
			props.load(new BufferedInputStream(new FileInputStream(propertiesFile)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<String> readLines(File childFile) {
		try {
			return Files.readAllLines(childFile.toPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static File[] listFilesSafe(File file) {
		if(file.listFiles() != null) {
			File[] results = file.listFiles();
			Arrays.sort(results, new Comparator<File>() {

				@Override
				public int compare(File o1, File o2) {
					return Long.compare(o1.lastModified(), o2.lastModified()); 
				}
				
			});
			return results;
		} else {
			return new File[] {};
		}
		
	}

}
