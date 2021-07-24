package org.github.arosecra.brooke.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.ObjectUtils;
import org.github.arosecra.brooke.ConfigService;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.book.Book;
import org.github.arosecra.brooke.book.BookRepository;
import org.github.arosecra.brooke.catalog.Catalog;
import org.github.arosecra.brooke.catalog.CatalogRepository;
import org.github.arosecra.brooke.category.Category;
import org.github.arosecra.brooke.category.CategoryRepository;
import org.github.arosecra.brooke.index.Index;
import org.github.arosecra.brooke.index.IndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBInitializer {

	@Autowired
	private Settings settings;
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private CatalogRepository catalogRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private IndexRepository indexRepository;
	
	@Autowired
	private AdminService adminService;
	
	@PostConstruct
	public void init() {
//		Configuration catalogs = configService.getConfig(settings.getCatalogsHome(), "Catalogs", "properties");
//		
//		Map<String, Book> books = loadBooks();
//		List<Index> indices = new ArrayList<>();
//		
//		for(String catalogName : catalogs.getStringArray("catalogs")) {
//			Catalog cat = readCatalog(catalogName);
//			catalogRepository.save(cat);
//			catalogRepository.flush();
//			System.out.println(cat);
//			
//			Map<String, Category> categories = readCatagories(cat);
//			categoryRepository.saveAll(categories.values());
//			categoryRepository.flush();
//			indices.addAll(assignBooks(books, categories, catalogName));
//		}
//		
//		bookRepository.saveAll(books.values());
//		bookRepository.flush();
//		
//		indexRepository.saveAll(indices);
//		indexRepository.flush();
		
		adminService.imprt();
	}
	
	
	private List<Index> assignBooks(Map<String, Book> booksMap, 
			Map<String, Category> categoriesMap, 
			String catalogName) {
		
		List<Index> results = new ArrayList<>();
		Configuration catConfig = configService.getConfig(settings.getCatalogsHome(), catalogName, "properties");
		
		
		String[] categories = catConfig.getStringArray("categories");
	    for(String c : ObjectUtils.firstNonNull(categories, new String[] {})) {
	    	String categoryDisplayName = c;
	    	if(catConfig.getString("categories."+c) != null) {
	    		categoryDisplayName = catConfig.getString("categories."+c);
	    	}
	    	
	    	String[] books = catConfig.getStringArray("books." + c);
		    for(String book : ObjectUtils.firstNonNull(books, new String[] {})) {
		    	
		    	Index listing = new Index();
		    	listing.setBook(booksMap.get(book));
		    	listing.setCategory(categoriesMap.get(categoryDisplayName));
		    	results.add(listing);
		    }
	    }
		
		return results;
	}


	private Map<String, Book> loadBooks() {
		Map<String, Book> result = new HashMap<>();
		for(File bookfile : new File(settings.getBooksHome()).listFiles()) {
			Book book = new Book();
			book.setFilename(bookfile.getName());
			result.put(book.getFilename(), book);
		}
		return result;
	}


	private Catalog readCatalog(String catalogName) {
		Catalog cat = new Catalog();
		cat.setName(catalogName);
		Configuration catConfig = configService.getConfig(settings.getCatalogsHome(), catalogName, "properties");
//		cat.setParentCatalog(catConfig.getString("parent", null));
		
		String[] categories = catConfig.getStringArray("categories");
	    for(String category : ObjectUtils.firstNonNull(categories, new String[] {})) {
	    	
	    	String categoryDisplayName = category;
	    	if(catConfig.getString("categories."+category) != null) 
	    		categoryDisplayName = catConfig.getString("categories."+category);

	    	Category ctg = new Category();
	    	ctg.setName(categoryDisplayName);
	    }
	    return cat;
	}
	
	
	private Map<String, Category> readCatagories(Catalog cat) {
		Map<String, Category> result = new HashMap<>();
		Configuration catConfig = configService.getConfig(settings.getCatalogsHome(), cat.getName(), "properties");
//		cat.setParentCatalog(catConfig.getString("parent", null));
		String[] categories = catConfig.getStringArray("categories");
	    for(String category : ObjectUtils.firstNonNull(categories, new String[] {})) {
	    	
	    	String categoryDisplayName = category;
	    	if(catConfig.getString("categories."+category) != null) 
	    		categoryDisplayName = catConfig.getString("categories."+category);

	    	Category ctg = new Category();
	    	ctg.setName(categoryDisplayName);
	    	ctg.setCatalog(cat);
	    	result.put(ctg.getName(), ctg);
	    }
	    return result;
	}
}
