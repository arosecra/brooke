package org.github.arosecra.brooke.admin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.github.arosecra.brooke.book.Book;
import org.github.arosecra.brooke.book.BookService;
import org.github.arosecra.brooke.catalog.BookListing;
import org.github.arosecra.brooke.catalog.Catalog;
import org.github.arosecra.brooke.catalog.CatalogService;
import org.github.arosecra.brooke.library.Library;
import org.github.arosecra.brooke.library.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController {
	
	@Autowired
	private LibraryService libraryService;
	
	@Autowired
	private CatalogService catalogService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private AdminService adminService;

	@GetMapping("/admin")
	public String getAdmin(Model model) {
		model.addAttribute("library", libraryService.getLibrary());
		return "admin";
	}

	@GetMapping("/admin/catalog/{catalog}")
	public String manageCatalog(@PathVariable(name="catalog") String catalog, 
			Model model) throws IOException {
		
		Library library = libraryService.getLibrary();
		Map<String, List<BookListing>> booksToListings = adminService.getBookToListingsMap(library);
		Catalog cat = catalogService.getCatalog(catalog);

		AdminBookListing books = new AdminBookListing();

		if(StringUtils.isEmpty(cat.getParentCatalog())) {
			getAdminBookListing(catalog, booksToListings, cat, books, false);
		} else {
			getAdminBookListing(catalog, booksToListings, cat, books, true);
		}
		
		model.addAttribute("catalog", cat);
		model.addAttribute("books", books);
		return "managecatalog";
	}

	private void getAdminBookListing(String catalog, Map<String, List<BookListing>> booksToListings, Catalog cat,
			AdminBookListing books, boolean onlyAddIfInParent) throws IOException {
		for(File file : new File("D:/scans/books").listFiles()) {
			Book book = bookService.getBook(file.getName());
			AdminBook adminBook = new AdminBook();
			books.getBooks().add(adminBook);
			adminBook.setName(book.getName());
			adminBook.setDisplayName(book.getDisplayName());
			List<BookListing> listings = booksToListings.get(book.getName());
			Set<String> assignedCategories = new HashSet<>();
			boolean foundInParent = false;
			if(listings != null) {
				for(BookListing listing : listings) {
					if(StringUtils.equals(listing.getCatalog(), catalog)) {
						assignedCategories.add(listing.getCategory());
					}
					if(StringUtils.equals(cat.getParentCatalog(), listing.getCatalog()) &&
							StringUtils.contains(cat.getParentCategories(), listing.getCategory())) {
						foundInParent = true;
					}
				}
			}
			
			for(String category : cat.getCategories()) {
				AdminBookCategory abc = new AdminBookCategory();
				abc.setAssigned(assignedCategories.contains(category));
				abc.setName(category);
				
				if(!onlyAddIfInParent || (foundInParent && onlyAddIfInParent)) {
					adminBook.getCategories().add(abc);
				}
			}
		}
	}
}
