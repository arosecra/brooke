package org.github.arosecra.brooke;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

public class Main14 {
	public static void main(String[] args) throws Exception {
		
		File remoteVideoRepository = new File("\\\\drobo5n2\\Public\\Anime");
		
		Collection<File> files = FileUtils.listFiles(remoteVideoRepository, null, true);
		
		System.out.println(files.size());
//		for(File file : files) 
//			System.out.println(file);
		

//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		PrintStream ps = new PrintStream(os);
//		CommandLine.run(new String[] {
//				"cmd.exe",
//				"/c",
//				"start",
//				"/wait",
//				"cmd",
//				"/c",
//				"dir",
//				remoteVideoRepository.getAbsolutePath(),
//				"/S",
//				"/B"
//		}, ps);
//		System.out.println(os.toString());
	}
}
