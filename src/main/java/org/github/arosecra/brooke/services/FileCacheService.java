package org.github.arosecra.brooke.services;

import java.io.File;
import java.io.IOException;

import org.github.arosecra.brooke.dao.JobService;
import org.github.arosecra.brooke.model2.JobDetails;
import org.github.arosecra.brooke.task.CopyFileAsyncTask;
import org.github.arosecra.brooke.util.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileCacheService {
	
	@Autowired
	private JobService jobDao;

	public JobDetails cacheRemoteFile(File remoteFile) throws IOException {
		File cacheFolder = new File("D:\\Library\\Cache");
		int numberCached = Try.listFilesSafe(cacheFolder).length;
		
		if(numberCached > 10) {
			do {
				Try.listFilesSafe(cacheFolder)[0].delete();
			} while (Try.listFilesSafe(new File("D:\\Library\\Cache")).length > 10);
		}

		File cacheFile = new File(cacheFolder, remoteFile.getName());
		CopyFileAsyncTask task = new CopyFileAsyncTask(remoteFile, cacheFile);
		JobDetails jobDetails = jobDao.createJob(task);
		jobDao.runJob(jobDetails, task);
		return jobDetails;
	}
}
