package org.github.arosecra.brooke.catalog;

import java.util.Collections;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.ObjectUtils;
import org.github.arosecra.brooke.ConfigService;
import org.github.arosecra.brooke.JpaService;
import org.github.arosecra.brooke.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CatalogService implements JpaService<Catalog, Long> {

	@Autowired
	private Settings settings;
	
	@Autowired 
	private ConfigService configService;
	
	@Autowired
	private CatalogRepository catalogRepository;

	
	public Catalog getCatalog(String catalogName) {
		Catalog cat = new Catalog();
		cat.setName(catalogName);
		Configuration catConfig = configService.getConfig(settings.getCatalogsHome(), catalogName, "properties");
//		cat.setParentCatalog(catConfig.getString("parent", null));
		
		String[] categories = catConfig.getStringArray("categories");
	    for(String category : ObjectUtils.firstNonNull(categories, new String[] {})) {
	    	String categoryDisplayName = category;
	    	if(catConfig.getString("categories."+category) != null) 
	    		categoryDisplayName = catConfig.getString("categories."+category);

			//TODO fix
//	    	cat.getCategories().add(categoryDisplayName);
	    }
	    return cat;
	}
	
	public BookListing getBookListing(String catalogName, String category) {
		BookListing result = new BookListing();
		result.setCatalog(catalogName);
		result.setCategory(category);
		
		Configuration catConfig = configService.getConfig(settings.getCatalogsHome(), catalogName, "properties");
		
		
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
		
		String[] books = catConfig.getStringArray("books." + internalCategoryName);
	    for(String book : ObjectUtils.firstNonNull(books, new String[] {})) {
	    	result.getBooks().add(book.replace(' ', '_'));
	    }
	    
	    Collections.sort(result.getBooks());
		
		return result;
	}

	@Override
	public JpaRepository<Catalog, Long> getRepository() {
		return catalogRepository;
	}
}
