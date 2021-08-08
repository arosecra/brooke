package org.github.arosecra.brooke;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.FileUtils;

public class Main10 {
	public static void main(String[] args) throws Exception {
		File videosDir = new File("\\\\drobo5n2\\public\\videos");
		File localVideosDir = new File("D:\\Library\\Video_Repository");
		
		FileUtils.copyDirectory(videosDir, localVideosDir, new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return !pathname.getName().endsWith("mp4");
			}
			
		});
		
		
	}
}
