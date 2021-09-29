package org.github.arosecra.brooke;

import java.io.IOException;

import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.model.Collection;
import org.github.arosecra.brooke.model.ShelfItem;
import org.github.arosecra.brooke.services.BrookeService;

public class BrookeLibraryListApplication {

	public static void main(String[] args) throws IOException {
		BrookeService service = new BrookeService();
		Settings settings = new Settings();
		LibraryDao libraryDao = new LibraryDao();
		libraryDao.setSettings(settings);
		service.setLibraryDao(libraryDao);

		for(Collection collection : libraryDao.getLibrary().getCollections()) {
			
			System.out.println(collection.getName());
			for(ShelfItem item : collection.getShelfItems().values() ) {
				System.out.println("\t"+item.getName());
			}
			
		}

	}

}
