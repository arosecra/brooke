package org.github.arosecra.brooke.library;

import java.io.File;

import org.apache.commons.compress.utils.FileNameUtils;
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
		
		for(File file : new File(settings.getCatalogsHome()).listFiles()) {
			String name = FileNameUtils.getBaseName(file.getName()).replace(' ', '_');
			Catalog cat = new Catalog();
			cat.setName(name);
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
			    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
			    .configure(params.properties().setFile(file)
			    		.setThrowExceptionOnMissing(false));
			try
			{
			    Configuration config = builder.getConfiguration();
			    String[] categories = config.getStringArray("categories");
			    for(String category : ObjectUtils.firstNonNull(categories, new String[] {})) {
			    	String categoryDisplayName = category;
			    	if(config.getString("categories."+category) != null) 
			    		categoryDisplayName = config.getString("categories."+category);
			    	
			    	cat.getCategories().add(categoryDisplayName);
			    	
			    	if(StringUtils.equals(selectedCatalog, name)) {
			    		cat.setSelected(true);
			    	}

					if(StringUtils.equals(selectedCatalog, name) && 
							StringUtils.equals(categoryDisplayName, selectedCategory)) {
						BookListing listing = new BookListing();
						String[] books = config.getStringArray("books." + category);
					    for(String book : ObjectUtils.firstNonNull(books, new String[] {})) {
					    	listing.getBooks().add(book.replace(' ', '_'));
					    }
						library.setListing(listing);
					}
			    }
			    library.getCatalogs().add(cat);

			} catch(ConfigurationException cex) {
			    // loading of the configuration file failed
			}
		}

		return library;
	}
}
