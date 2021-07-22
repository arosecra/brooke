package org.github.arosecra.brooke.admin.book;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.github.arosecra.brooke.admin.AdminService;
import org.github.arosecra.brooke.catalog.BookListing;
import org.github.arosecra.brooke.catalog.Catalog;
import org.github.arosecra.brooke.library.Library;
import org.github.arosecra.brooke.library.LibraryService;
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
	private AdminService adminService;
	
	@GetMapping("/manage/book/{bookname}")
	public String getManageBook(Model model, @PathVariable(name="bookname") String bookname) {
		
		ManageBook mb = new ManageBook();
		mb.setName(bookname);
		Library library = libraryService.getLibrary();
		Map<String, List<BookListing>> booksToListings = adminService.getBookToListingsMap(library);
		List<BookListing> listings = booksToListings.get(bookname);
		//TODO fix
//		for(Catalog cat : library.getCatalogs()) {
//			ManageBookCatalog mbCat = new ManageBookCatalog();
//			mbCat.setName(cat.getName());
//			for(String category : cat.getCategories()) {
//				boolean listed = false;
//				if(listings != null) {
//					for(BookListing list : listings) {
//						if(list.getCatalog().equals(cat.getName()) && list.getCategory().equals(category)) {
//							listed = true;
//						}
//					}
//				}
//				ManageBookCategory mbc = new ManageBookCategory();
//				mbc.setCategory(category);
//				mbc.setListed(listed);
//				mbCat.getCategories().add(mbc);
//			}
//			mb.getCatalogs().add(mbCat);
//		}

		model.addAttribute("library", library);
		model.addAttribute("selectedCatalogName", "none");
		model.addAttribute("book", mb);
		return "managebook";
	}

	
	@GetMapping("/manage/book/{bookname}/addtocategory/{catalog}/{category}")
	public String addtocategory(Model model, @PathVariable(name="bookname") String bookname, @PathVariable(name="catalog") String catalog, @PathVariable(name="category") String category) throws IOException {
		adminService.addToCategory(bookname, catalog, category);
		return "redirect:/manage/book/"+bookname;
	}

	
	@GetMapping("/manage/book/{bookname}/generatethumbnail")
	public String generateThumbnail(Model model, @PathVariable(name="bookname") String bookname, @PathVariable(name="catalog") String catalog, @PathVariable(name="category") String category) throws IOException {
		adminService.generateThumbnail(bookname);
		return "redirect:/manage/book/"+bookname;
	}
}
