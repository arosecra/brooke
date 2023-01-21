package org.github.arosecra.brooke.services;

import java.io.File;
import java.io.IOException;

import org.github.arosecra.brooke.model.JobDetails;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.Shelf;
import org.github.arosecra.brooke.model.ShelfItem;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.task.CopyFileTask;
import org.github.arosecra.brooke.util.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileCacheService {
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private LibraryLocationService libraryLocationService;
	
	public File getCachedFile(Library library, String collectionName, String itemName) {
		File cacheFolder = new File("D:\\Library\\Cache");
		File remoteFile = this.libraryLocationService.getRemoteFile(library, collectionName, itemName);
		File cacheFile = new File(cacheFolder, remoteFile.getName());
		return cacheFile;
	}

	public JobDetails cacheItem(Library library, String collectionName, String itemName) throws IOException {
		File remoteFile = this.libraryLocationService.getRemoteFile(library, collectionName, itemName);
		return cacheRemoteFile(remoteFile);
	}

	public JobDetails cacheRemoteFile(File remoteFile) throws IOException {
		File cacheFolder = new File("D:\\Library\\Cache");
		int numberCached = Try.listFilesSafe(cacheFolder).length;
		
		if(numberCached > 10) {
			do {
				Try.listFilesSafe(cacheFolder)[0].delete();
			} while (Try.listFilesSafe(new File("D:\\Library\\Cache")).length > 10);
		}

		File cacheFile = new File(cacheFolder, remoteFile.getName());
		CopyFileTask task = new CopyFileTask(remoteFile, cacheFile);
		JobDetails jobDetails = jobService.createJob(task);
		jobDetails.setJobDescription("Caching");
		jobDetails.setJobType("Cache");
		jobService.runJob(jobDetails, task);
		return jobDetails;
	}
}
