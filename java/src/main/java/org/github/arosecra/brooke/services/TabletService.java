package org.github.arosecra.brooke.services;

import java.io.File;

import org.github.arosecra.brooke.model.JobDetails;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.ShelfItem;
import org.github.arosecra.brooke.task.CopyForTabletTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TabletService {

	@Autowired
	private JobService jobService;
	
	@Autowired
	private LibraryLocationService libraryLocationService;
	
	public JobDetails copyForTablet(Library library, String collectionName, String itemName) {
		File tempSsdFolder = new File("C:\\scans\\temp");
		File unzippedFolder = new File(tempSsdFolder, itemName);
		unzippedFolder.mkdirs();
		
		ShelfItem shelfItem = this.libraryLocationService.getShelfItem(library, collectionName, itemName);
		File remoteFile = new File(shelfItem.getRemoteBaseDirectory(), itemName + "_PNG.tar");
		
		CopyForTabletTask task = new CopyForTabletTask(remoteFile);
		JobDetails jobDetails = jobService.createJob(task);
		jobDetails.setJobDescription("Caching");
		jobDetails.setJobType("Cache");
		jobService.runJob(jobDetails, task);
		return jobDetails;
	}
}
