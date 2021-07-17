package org.github.arosecra.brooke.library;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.github.arosecra.brooke.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {

	@Autowired
	private Settings settings;

	public Library getLibrary(String selectedCatalog, String selectedCategory) {
		Library library = new Library();
		
		Configuration catalogs = getConfig("Catalogs");
		
		for(String catalogName : catalogs.getStringArray("catalogs")) {
			Catalog cat = new Catalog();
			cat.setName(catalogName);
			Configuration catConfig = getConfig(catalogName);
			
			String[] categories = catConfig.getStringArray("categories");
		    for(String category : ObjectUtils.firstNonNull(categories, new String[] {})) {
		    	String categoryDisplayName = category;
		    	if(catConfig.getString("categories."+category) != null) 
		    		categoryDisplayName = catConfig.getString("categories."+category);
		    	
		    	cat.getCategories().add(categoryDisplayName);
		    	
		    	if(StringUtils.equals(selectedCatalog, catalogName)) {
		    		cat.setSelected(true);
		    	}

				if(StringUtils.equals(selectedCatalog, catalogName) && 
						StringUtils.equals(categoryDisplayName, selectedCategory)) {
					BookListing listing = new BookListing();
					String[] books = catConfig.getStringArray("books." + category);
				    for(String book : ObjectUtils.firstNonNull(books, new String[] {})) {
				    	listing.getBooks().add(book.replace(' ', '_'));
				    }
					library.setListing(listing);
				}
		    }
		    library.getCatalogs().add(cat);
		}

		return library;
	}
	
	private Configuration getConfig(String filename) {
		File file = new File(settings.getCatalogsHome(), filename + ".properties");
			if(file.exists()) {
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
			    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
			    .configure(params.properties().setFile(file)
			    		.setThrowExceptionOnMissing(false));
		    try {
				Configuration config = builder.getConfiguration();
				return config;
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("Could not find " + file.getName());
		}
	    return null;
	}
}
