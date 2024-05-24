package org.github.arosecra.brooke.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.github.arosecra.brooke.model.JobDetails;
import org.github.arosecra.brooke.task.IRunnableTask;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class JobService {
	
	private long lastJobNumber = 1;

	private Map<Long, IRunnableTask> jobs = new HashMap<>();
	
	public JobDetails createJob(IRunnableTask runnable) {
		
		JobDetails jobDetails = new JobDetails();
		jobDetails.setJobNumber(this.lastJobNumber++);		
		
		this.jobs.put(jobDetails.getJobNumber(), runnable);
		
		return jobDetails;
	}
	
	@Async
	public void runJob(JobDetails jobDetails, IRunnableTask runnable) {
		runnable.run();
	}
	
	public JobDetails getJobDetails(Long jobNumber) {
		IRunnableTask pr = this.jobs.get(jobNumber);
		
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
	
	public JobDetails[] getJobDetails() {
		List<JobDetails> details = new ArrayList<>();
		
		for(IRunnableTask pr : this.jobs.values()) {
			
			JobDetails jobDetails = new JobDetails();
			jobDetails.setJobType(pr.getJobType());
			jobDetails.setJobNumber(lastJobNumber);
			jobDetails.setJobDescription(pr.getJobDescription());
			jobDetails.setCurrentProgressDescription(pr.getCurrentProgressDescription());
			jobDetails.setTotalProgressDescription(pr.getTotalProgressDescription());
			jobDetails.setCurrent(pr.getCurrentProgress().get());
			jobDetails.setTotal(pr.getTotalProgress().get());
			jobDetails.setStarted(pr.started().get());
			
		}
		
		return details.toArray(new JobDetails[details.size()]);
	}
	
}
