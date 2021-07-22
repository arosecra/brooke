package org.github.arosecra.brooke.library;

import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.catalog.CatalogRepository;
import org.github.arosecra.brooke.catalog.CatalogService;
import org.github.arosecra.brooke.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {

	@Autowired
	private Settings settings;

	@Autowired 
	private CatalogService catalogService;
	
	@Autowired
	private CatalogRepository catalogRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public Library getLibrary() {
		Library library = new Library();
		
		library.setCatalogs(catalogRepository.findAll());
		library.setCategories(categoryRepository.findAllByOrderByCatalog_NameAscNameAsc());
		
		

		return library;
	}
}
