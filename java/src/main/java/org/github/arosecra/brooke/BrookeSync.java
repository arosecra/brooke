package org.github.arosecra.brooke;

import java.io.IOException;

import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.services.JobService;
import org.github.arosecra.brooke.services.LibraryCacheService;
import org.github.arosecra.brooke.services.SyncService;

// import org.github.arosecra.brooke.dao.LibraryDao;
// import org.github.arosecra.brooke.services.BrookeService;

public class BrookeSync {
	public static void main(String[] args) throws IOException {
		SyncService service = new SyncService();
		service.setJobService(new JobService());
		service.setLibraryCacheService(new LibraryCacheService());
		Settings settings = new Settings();
		LibraryDao libraryDao =new LibraryDao();
		libraryDao.setSettings(settings);
		service.sync(libraryDao.getLibrary(false).getCollections());
		
	}
}
