package org.github.arosecra.brooke;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Main15 {
	public static void main(String[] args) {
		
		Collection<File> subFiles = FileUtils.listFiles(new File("D:\\Library\\Video_Repository"), new String[] {"sub", "sup"}, true);
		
		for(File file : subFiles) {
			File folder = file.getParentFile();
			String basename = FilenameUtils.getBaseName(file.getName());
			File tar = new File(folder, basename + ".tar");
			
			if(!tar.exists()) {
				File tempFolder = new File(folder, basename);
				tempFolder.mkdirs();
				
//				String cmd1 = "mkdir " + tempFolder.getAbsolutePath();
//				System.out.println(cmd1);
				
				String cmd2 = "java.exe -jar D:\\projects\\bdsup2sub.jar -o "+
						tempFolder.getAbsolutePath() + "\\" + basename + ".xml "+
						file.getAbsolutePath();
				System.out.println(cmd2);
			}
			
		}
		
		
	}
}
