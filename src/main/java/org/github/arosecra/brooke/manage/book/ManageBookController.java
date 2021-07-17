package org.github.arosecra.brooke.manage.book;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.github.arosecra.brooke.catalog.BookListing;
import org.github.arosecra.brooke.catalog.Catalog;
import org.github.arosecra.brooke.library.Library;
import org.github.arosecra.brooke.library.LibraryService;
import org.github.arosecra.brooke.manage.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ManageBookController {

	@Autowired
	private LibraryService libraryService;
	
	@Autowired
	private ManagementService managementService;
	
	@GetMapping("/manage/book/{bookname}")
	public String getManageBook(Model model, @PathVariable(name="bookname") String bookname) {
		
		ManageBook mb = new ManageBook();
		mb.setName(bookname);
		Library library = libraryService.getLibrary();
		Map<String, List<BookListing>> booksToListings = managementService.getBookToListingsMap(library);
		List<BookListing> listings = booksToListings.get(bookname);
		
		for(Catalog cat : library.getCatalogs()) {
			for(String category : cat.getCategories()) {
				boolean listed = false;
				if(listings != null) {
					for(BookListing list : listings) {
						if(list.getCatalog().equals(cat.getName()) && list.getCategory().equals(category)) {
							listed = true;
						}
					}
				}
				ManageBookCategory mbc = new ManageBookCategory();
				mbc.setCatalog(cat.getName());
				mbc.setCategory(category);
				mbc.setListed(listed);
				mb.getCategories().add(mbc);
			}
		}
		
		model.addAttribute("book", mb);
		return "managebook";
	}

	
	@GetMapping("/manage/book/{bookname}/addtocategory/{catalog}/{category}")
	public String addtocategory(Model model, @PathVariable(name="bookname") String bookname, @PathVariable(name="catalog") String catalog, @PathVariable(name="category") String category) throws IOException {
		managementService.addToCategory(bookname, catalog, category);
		return "redirect:/manage/book/"+bookname;
	}

	
	@GetMapping("/manage/book/{bookname}/generatethumbnail")
	public String generateThumbnail(Model model, @PathVariable(name="bookname") String bookname, @PathVariable(name="catalog") String catalog, @PathVariable(name="category") String category) throws IOException {
		managementService.generateThumbnail(bookname);
		return "redirect:/manage/book/"+bookname;
	}
}
