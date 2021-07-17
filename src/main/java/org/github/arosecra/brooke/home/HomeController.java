package org.github.arosecra.brooke.home;

import org.github.arosecra.brooke.catalog.BookListing;
import org.github.arosecra.brooke.catalog.CatalogService;
import org.github.arosecra.brooke.library.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
	
	@Autowired
	private LibraryService libraryService;
	
	@Autowired
	private CatalogService catalogService;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("library", libraryService.getLibrary());
		model.addAttribute("listing", new BookListing());
		return "home";
	}

	@GetMapping("/{catalog}/{category}")
	public String home(@PathVariable(name="catalog") String catalog, 
			@PathVariable(name="category") String category, 
			Model model) {
		model.addAttribute("library", libraryService.getLibrary());
		model.addAttribute("selectedCatalogName", catalog);
		model.addAttribute("listing", catalogService.getBookListing(catalog, category));
		return "home";
	}

}