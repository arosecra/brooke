package org.github.arosecra.brooke.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.github.arosecra.brooke.dao.JobDao;
import org.github.arosecra.brooke.model2.JobDetails;
import org.github.arosecra.brooke.task.CopyFileAsyncTask;
import org.github.arosecra.brooke.util.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileCacheService {
	
	@Autowired
	private JobDao jobDao;
	
	@Autowired
	private CopyFileAsyncTask copyFileAsync;

	public long cacheRemoteFile(File remoteFile) throws IOException {
		File cacheFolder = new File("D:\\Library\\Cache");
		int numberCached = Try.listFilesSafe(cacheFolder).length;
		
		if(numberCached > 10) {
			do {
				Try.listFilesSafe(cacheFolder)[0].delete();
			} while (Try.listFilesSafe(new File("D:\\Library\\Cache")).length > 10);
		}
		
		JobDetails jobDetails = new JobDetails();
		jobDetails.setJobType("Caching");
		jobDao.addJobDetails(jobDetails);
		
		File cacheFile = new File(cacheFolder, remoteFile.getName());
		this.copyFileAsync.copyFilesWithProgress(remoteFile, cacheFile);
//		FileUtil.copyFile(remoteFile, cacheFile);
		
//		JobId id = BackgroundJob.enqueue(() -> this.copyFilesWithProgress(JobContext.Null, remoteFile, cacheFile));
		return jobDetails.getJobNumber();
	}
	
	public static class BrookeJobProgress {
		private long current;
		private long maxValue;
		public long getCurrent() {
			return current;
		}
		public void setCurrent(long current) {
			this.current = current;
		}
		public long getMaxValue() {
			return maxValue;
		}
		public void setMaxValue(long maxValue) {
			this.maxValue = maxValue;
		}
	}
	
	
	public void copyFilesWithProgress(File remoteFile, File cacheFile) {
//		JobDetails jobDetails = this.jobDao.getJobDetails(jobNumber);
        FileInputStream  fin  = null;
        FileOutputStream fout = null;
        long length  = remoteFile.length();
        
//        JobDashboardProgressBar progressBar = jobContext.progressBar(length);
        BrookeJobProgress progress = new BrookeJobProgress();
        progress.setMaxValue(length);
//        jobContext.getMetadata().put("brooke", progress);
//        jobContext.saveMetadata("brooke-maxvalue", length);
        
        long counter = 0;
        int r = 0;
        byte[] b = new byte[1024];
        try {
                fin  = new FileInputStream(remoteFile);
                fout = new FileOutputStream(cacheFile);
                while( (r = fin.read(b)) != -1) {
                        counter += r;
//                        progressBar.setValue(counter);
                        progress = new BrookeJobProgress();
                        progress.setMaxValue(length);
                        progress.setCurrent(counter);
//                        jobContext.saveMetadata("brooke-current", counter);
//                        System.out.println(progressBar.getProgress());
//                        jobDetails.setPercentComplete((int)(1000.0 * counter / length));
//                        System.out.println(jobDetails.getJobNumber() + ": " + jobDetails.getPercentComplete() );
                        fout.write(b, 0, r);
                }
        }
        catch(Exception e){
                System.out.println("foo");
        }
	}
	
}
