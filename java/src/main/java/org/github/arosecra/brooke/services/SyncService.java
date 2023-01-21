package org.github.arosecra.brooke.services;

import java.util.List;

import org.apache.tomcat.jni.Library;
import org.github.arosecra.brooke.model.JobDetails;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.task.SynchronizeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BrookeSyncService {

	@Autowired
	private JobService jobService;

	@Autowired
	private LibraryCacheService libraryCacheService;

	public JobDetails sync(List<CollectionApiModel> collections) {
		SynchronizeTask syncTask = new SynchronizeTask(libraryCacheService, collections);

		JobDetails jobDetails = jobService.createJob(syncTask);
		jobDetails.setJobDescription("Synchronizing");
		jobDetails.setJobType("Synchronizing");
		jobService.runJob(jobDetails, syncTask);
		return jobDetails;
	}

}
