package org.github.arosecra.brooke.admin.book;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.admin.AdminService;
import org.github.arosecra.brooke.index.Index;
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
	
	@GetMapping("/adminbook/{bookname}")
	public String getManageBook(Model model, @PathVariable(name="bookname") String bookname) {
		
		ManageBook mb = new ManageBook();
		mb.setName(bookname);
		Library library = libraryService.getLibrary();
		Map<String, List<Index>> booksToListings = adminService.getBookToListingsMap(library);
		List<Index> listings = booksToListings.get(bookname);
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

	
	@GetMapping("/adminbook/{bookname}/addtocategory/{catalog}/{category}")
	public String addtocategory(Model model, @PathVariable(name="bookname") String bookname, @PathVariable(name="catalog") String catalog, @PathVariable(name="category") String category) throws IOException {
		adminService.addToCategory(bookname, catalog, category);
		return "redirect:/managebook/"+bookname;
	}
	
	@GetMapping("/adminbook/{bookname}/generatethumbnail")
	public String generateThumbnail(Model model, @PathVariable(name="bookname") String bookname) throws IOException {
		adminService.generateThumbnail(bookname);
		return "redirect:/managebook/"+bookname;
	}
	
	@GetMapping("/adminbook/{bookname}/download")
	public String download(Model model, @PathVariable(name="bookname") String bookname) throws IOException {

		File remoteCbt = new File("\\\\drobo5n\\public\\scans\\books\\" + bookname + "\\" + bookname + ".cbt");
		File localCbt = new File("D:/scans/books/" + bookname + "/" + bookname + ".cbt");
		FileUtils.copyFile(remoteCbt, localCbt);
		
		return "redirect:/adminbook/"+bookname;
	}
}
