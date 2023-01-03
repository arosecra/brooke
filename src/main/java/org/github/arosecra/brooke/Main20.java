package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.model.Catalog;
import org.github.arosecra.brooke.model.Category;
import org.github.arosecra.brooke.model.Collection;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.ShelfItem;

public class Main20 {
	public static void main(String[] args) throws IOException {
		Settings settings = new Settings();
		
		LibraryDao libraryDao = new LibraryDao();
		libraryDao.setSettings(settings);
		
		Library library = libraryDao.getLibrary();
		
		for(Collection collection : library.getCollections()) {
			List<String> lines = new ArrayList<>();
			
			lines.add("name: " + collection.getName());
			lines.add("remoteDirectory: " + collection.getRemoteDirectory());
			lines.add("localDirectory: " + collection.getLocalDirectory());
			lines.add("itemExtension: " + collection.getItemExtension());
			lines.add("excludeExtensions:");
			for(String ext : collection.getExcludeExtensions().split(","))
				lines.add("  - " + ext);
			lines.add("openType: " + collection.getOpenType());
			lines.add("pipelineSteps:");
			for(String step : collection.getPipeline()) 
				lines.add("  - " + step);
			
			
//			if(collection.getName().startsWith("Research")) {
				lines.add("categories:");
				
				for(Catalog catalog : collection.getCatalogs()) {
//					lines.add("  -name: " + cat.getName());
					
					for(Category cat : catalog.getCategories()) {
						lines.add("    -name: " + cat.getName());
						lines.add("    -items: ");
						
						for(ShelfItem si : cat.getItems()) {
							lines.add("      -name: " + si.getName());
						}
					}
				}
//			}
			FileUtils.writeLines(new File(collection.getName().toLowerCase()+".yaml"), lines);
			
		}

	}
}
