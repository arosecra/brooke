package org.github.arosecra.brooke.dao;

import java.util.HashMap;
import java.util.Map;

import org.github.arosecra.brooke.model.JobDetails;
import org.github.arosecra.brooke.task.ProgressableRunnable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class JobService {
	
	private long lastJobNumber = 1;

	private Map<Long, ProgressableRunnable> jobs = new HashMap<>();
	
	public JobDetails createJob(ProgressableRunnable runnable) {
		
		JobDetails jobDetails = new JobDetails();
		jobDetails.setJobNumber(this.lastJobNumber++);		
		
		this.jobs.put(jobDetails.getJobNumber(), runnable);
		
		return jobDetails;
	}
	
	@Async
	public void runJob(JobDetails jobDetails, ProgressableRunnable runnable) {
		runnable.run();
	}
	
	public JobDetails getJobDetails(Long jobNumber) {
		ProgressableRunnable pr = this.jobs.get(jobNumber);
		
		JobDetails jobDetails = new JobDetails();
		jobDetails.setJobNumber(lastJobNumber);
		jobDetails.setCurrent(pr.getCurrentProgress().get());
		jobDetails.setTotal(pr.getTotalProgress().get());
		jobDetails.setStarted(pr.started().get());
		
		return jobDetails;
	}
	
}
