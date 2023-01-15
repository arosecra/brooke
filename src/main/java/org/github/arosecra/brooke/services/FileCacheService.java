package org.github.arosecra.brooke.services;

import java.io.File;
import java.io.IOException;

import org.aspectj.util.FileUtil;
import org.github.arosecra.brooke.util.Try;
import org.springframework.stereotype.Component;

@Component
public class FileCacheService {

	public void cacheRemoteFile(File remoteFile) throws IOException {
		File cacheFolder = new File("D:\\Library\\Cache");
		int numberCached = Try.listFilesSafe(cacheFolder).length;
		
		if(numberCached > 10) {
			do {
				Try.listFilesSafe(cacheFolder)[0].delete();
			} while (Try.listFilesSafe(new File("D:\\Library\\Cache")).length > 10);
		}
		File cacheFile = new File(cacheFolder, remoteFile.getName());
		FileUtil.copyFile(remoteFile, cacheFile);
	}
	
}
