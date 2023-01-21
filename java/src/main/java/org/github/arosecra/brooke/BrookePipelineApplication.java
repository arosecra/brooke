package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.jobs.BrookeJobStep.JobFolder;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.services.PipelineService;

public class BrookePipelineApplication {

	public static void main(String[] args) throws IOException {
		PipelineService service = new PipelineService();
		Settings settings = new Settings();
		LibraryDao libraryDao = new LibraryDao();
		libraryDao.setSettings(settings);
		// service.setLibraryDao(libraryDao);

		File workDirectory = new File("D://scans//tobeexported");

		Map<String, List<JobFolder>> foldersToProcess = new TreeMap<>();
		Map<String, CollectionApiModel> collections = new HashMap<>();
		
		service.selectWork(libraryDao.getLibrary(false), foldersToProcess, collections);
		service.printWorkStatus(foldersToProcess);
		service.executePipelines(workDirectory, foldersToProcess, collections);
	}
}