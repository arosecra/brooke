package org.github.arosecra.brooke;

import java.io.IOException;

import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.services.BrookeService;

public class BrookeSync {
	public static void main(String[] args) throws IOException {
		BrookeService service = new BrookeService();
		Settings settings = new Settings();
		LibraryDao libraryDao =new LibraryDao();
		libraryDao.setSettings(settings);
		service.setLibraryDao(libraryDao);
		service.init();
		service.sync();
		
	}
}
