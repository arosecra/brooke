package org.github.arosecra.brooke.dao;

import java.util.HashMap;
import java.util.Map;

import org.github.arosecra.brooke.model2.JobDetails;
import org.springframework.stereotype.Repository;

@Repository
public class JobDao {
	
	private long lastJobNumber = 0;

	private Map<Integer, JobDetails> jobs = new HashMap<>();
	
//	private InMemoryStorageProvider inMemoryStorageProvider;
	
//	@PostConstruct() 
//	public void init() {
//		this.inMemoryStorageProvider = new InMemoryStorageProvider();
//		
//		
//		JobRunr.configure()                                      //
//        .useStorageProvider(this.inMemoryStorageProvider)       //
//        .useBackgroundJobServer()                                //
////        .useDashboard()                                          //
//        .initialize();
//	}
	
	
	public JobDetails addJobDetails(JobDetails jobDetails) {
//		jobDetails.setJobNumber(this.lastJobNumber++);
//		this.jobs.put(jobDetails.getJobNumber(), jobDetails);
		return jobDetails;
	}
	
	public JobDetails getJobDetails(String jobNumber) {
//		return this.jobs.get(jobNumber);
//		UUID uuid = UUID.fromString(jobNumber);
//		Job job = this.inMemoryStorageProvider.getJobById(uuid);
		
		
//		JobDetails result = new JobDetails();
//		result.setJobNumber(jobNumber);
//		result.setCurrent(0);
//		result.setTotal(0);
//		result.setStarted(false);
//		
//		Long progress = (Long) job.getMetadata().get("brooke-current");
//		if(progress != null) {
//			result.setStarted(true);
////			result.setTotal(progress.getMaxValue());
//			result.setCurrent(progress);
//		}
//		
//		Long maxValue = (Long) job.getMetadata().get("brooke-maxvalue");
//		if(progress != null) {
//			result.setStarted(true);
//			result.setTotal(maxValue);
//		}
		
//		JobDashboardProgressBar progress2 = (JobDashboardProgressBar) job.getMetadata().get("jobRunrDashboardProgressBar-2");
//		result.setJobNumber(jobNumber);
		
//		result.setCurrent(progress.get);
//		result.setPercentComplete(progress.getProgress());
		
		return new JobDetails();
	}
	
}
