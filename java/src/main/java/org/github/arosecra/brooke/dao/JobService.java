package org.github.arosecra.brooke.dao;

import java.util.HashMap;
import java.util.Map;

import org.github.arosecra.brooke.model.JobDetails;
import org.github.arosecra.brooke.task.RunnableTask;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class JobService {
	
	private long lastJobNumber = 1;

	private Map<Long, RunnableTask> jobs = new HashMap<>();
	
	public JobDetails createJob(RunnableTask runnable) {
		
		JobDetails jobDetails = new JobDetails();
		jobDetails.setJobNumber(this.lastJobNumber++);		
		
		this.jobs.put(jobDetails.getJobNumber(), runnable);
		
		return jobDetails;
	}
	
	@Async
	public void runJob(JobDetails jobDetails, RunnableTask runnable) {
		runnable.run();
	}
	
	public JobDetails getJobDetails(Long jobNumber) {
		RunnableTask pr = this.jobs.get(jobNumber);
		
		JobDetails jobDetails = new JobDetails();
		jobDetails.setJobType(pr.getJobType());
		jobDetails.setJobNumber(lastJobNumber);
		jobDetails.setJobDescription(pr.getJobDescription());
		jobDetails.setCurrentProgressDescription(pr.getCurrentProgressDescription());
		jobDetails.setTotalProgressDescription(pr.getTotalProgressDescription());
		jobDetails.setCurrent(pr.getCurrentProgress().get());
		jobDetails.setTotal(pr.getTotalProgress().get());
		jobDetails.setStarted(pr.started().get());
		
		return jobDetails;
	}
	
}
