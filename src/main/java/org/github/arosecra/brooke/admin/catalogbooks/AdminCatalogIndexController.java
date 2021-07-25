package org.github.arosecra.brooke.admin.catalogbooks;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.github.arosecra.brooke.admin.AdminService;
import org.github.arosecra.brooke.book.BookService;
import org.github.arosecra.brooke.book.OpenBook;
import org.github.arosecra.brooke.catalog.Catalog;
import org.github.arosecra.brooke.catalog.CatalogService;
import org.github.arosecra.brooke.catalogparent.CatalogParent;
import org.github.arosecra.brooke.catalogparent.CatalogParentService;
import org.github.arosecra.brooke.category.Category;
import org.github.arosecra.brooke.category.CategoryService;
import org.github.arosecra.brooke.index.Index;
import org.github.arosecra.brooke.library.Library;
import org.github.arosecra.brooke.library.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminCatalogIndexController {
	
	@Autowired
	private LibraryService libraryService;
	
	@Autowired
	private CatalogService catalogService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired 
	private CatalogParentService catalogParentService;


	@GetMapping("/admin/catalog/{catalog}/books")
	public String manageCatalogBooks(@PathVariable(name="catalog") String catalog, 
			Model model) throws IOException {
		
		Library library = libraryService.getLibrary();
		Map<String, List<Index>> booksToListings = adminService.getBookToListingsMap(library);
		Catalog cat = catalogService.findByName(catalog);

		AdminBookListing books = new AdminBookListing();

		getAdminBookListing(catalog, booksToListings, cat, books);
		
		model.addAttribute("catalog", cat);
		model.addAttribute("books", books);
		return "managecatalogbooks";
	}

	private void getAdminBookListing(String catalog, Map<String, List<Index>> booksToListings, Catalog cat,
			AdminBookListing books) throws IOException {
		List<CatalogParent> parentCatalogs = catalogParentService.findAllByCatalog_NameOrderByParentCategory_Catalog_Name(catalog);
		for(File file : new File("D:/scans/books").listFiles()) {
			OpenBook book = bookService.openBookTo(file.getName());
			AdminBook adminBook = new AdminBook();
			adminBook.setName(book.getName());
			adminBook.setDisplayName(book.getDisplayName());
			List<Index> listings = booksToListings.get(book.getName());
			Set<String> assignedCategories = new HashSet<>();
			boolean foundInParent = false;
			if(listings != null) {
				for(Index listing : listings) {
					if(StringUtils.equals(listing.getCategory().getCatalog().getName(), catalog)) {
						assignedCategories.add(listing.getCategory().getName());
					}
					
					if(!CollectionUtils.isEmpty(parentCatalogs)) {
						for(CatalogParent parentCatalog : parentCatalogs) {
							if(listing.getCategory().getCatalog().getName().equals(parentCatalog.getParentCategory().getCatalog().getName()) && 
									listing.getCategory().getName().equals(parentCatalog.getParentCategory().getName())
									) {
								foundInParent = true;
							}
						}
					}
				}
			}

			for(Category category : categoryService.findAllByCatalog_NameOrderByCatalog_NameAscNameAsc(catalog)) {
				AdminBookCategory abc = new AdminBookCategory();
				abc.setAssigned(assignedCategories.contains(category.getName()));
				abc.setName(category.getName());
				
				adminBook.getCategories().add(abc);
			}
			
			if(CollectionUtils.isEmpty(parentCatalogs) || (!CollectionUtils.isEmpty(parentCatalogs) && foundInParent))
				books.getBooks().add(adminBook);
		}
	}
}
