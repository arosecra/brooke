package org.github.arosecra.brooke.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
		jobDetails.setJobNumber(jobNumber);
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
		
		for(Entry<Long, IRunnableTask> entry : this.jobs.entrySet()) {
			
			JobDetails jobDetails = new JobDetails();
			jobDetails.setJobType(entry.getValue().getJobType());
			jobDetails.setJobNumber(entry.getKey());
			jobDetails.setJobDescription(entry.getValue().getJobDescription());
			jobDetails.setCurrentProgressDescription(entry.getValue().getCurrentProgressDescription());
			jobDetails.setTotalProgressDescription(entry.getValue().getTotalProgressDescription());
			jobDetails.setCurrent(entry.getValue().getCurrentProgress().get());
			jobDetails.setTotal(entry.getValue().getTotalProgress().get());
			jobDetails.setStarted(entry.getValue().started().get());
			
			details.add(jobDetails);
			
		}
		
		return details.toArray(new JobDetails[details.size()]);
	}
	
}
