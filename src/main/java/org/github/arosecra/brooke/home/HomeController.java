package org.github.arosecra.brooke.home;

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

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("library", libraryService.getLibrary(null, null));
		return "home";
	}

	@GetMapping("/{catalog}/{category}")
	public String home(@PathVariable(name="catalog") String catalog, 
			@PathVariable(name="category") String category, 
			Model model) {
		model.addAttribute("library", libraryService.getLibrary(catalog, category));
		return "home";
	}

}