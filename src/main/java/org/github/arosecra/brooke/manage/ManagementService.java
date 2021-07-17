package org.github.arosecra.brooke.manage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.github.arosecra.brooke.ConfigService;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.catalog.BookListing;
import org.github.arosecra.brooke.catalog.Catalog;
import org.github.arosecra.brooke.catalog.CatalogService;
import org.github.arosecra.brooke.library.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagementService {

	@Autowired
	private Settings settings;
	
	@Autowired
	private ConfigService configService;
	
	@Autowired 
	private CatalogService catalogService;
	
	public Map<String, List<BookListing>> getBookToListingsMap(Library library) {
		Map<String, List<BookListing>> booksToListings = new HashMap<>();
		for(Catalog cat : library.getCatalogs()) {
			for(String category : cat.getCategories()) {
				BookListing listing = catalogService.getBookListing(cat.getName(), category);
				for(String book : listing.getBooks()) {
					if(!booksToListings.containsKey(book))
						booksToListings.put(book, new ArrayList<>());
					booksToListings.get(book).add(listing);
				}
			}
		}
		return booksToListings;
	}

	public void addToCategory(String bookname, String catalog, String category) throws IOException {
		Configuration catConfig = configService.getConfig(settings.getCatalogsHome(), catalog, "properties");

		String internalCategoryName = category;
		String[] categories = catConfig.getStringArray("categories");
	    for(String c : ObjectUtils.firstNonNull(categories, new String[] {})) {
	    	String categoryDisplayName = c;
	    	if(catConfig.getString("categories."+c) != null) {
	    		categoryDisplayName = catConfig.getString("categories."+c);
	    		if(categoryDisplayName.equals(category)) {
	    			internalCategoryName = c;
	    		}
	    	}	
	    }
		
	    String lineToAdd = "\r\nbooks."+internalCategoryName+"="+bookname;
	    FileUtils.write(new File(settings.getCatalogsHome(), catalog + ".properties"), lineToAdd, true);
	}

	public void generateThumbnail(String bookname) {
		// TODO Auto-generated method stub
		
	}
	
}
