package org.github.arosecra.brooke.home;

import java.util.ArrayList;

import org.github.arosecra.brooke.index.Index;
import org.github.arosecra.brooke.index.IndexService;
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
	private IndexService indexService;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("library", libraryService.getLibrary());
		model.addAttribute("indices", new ArrayList<Index>());
		return "home";
	}

	@GetMapping("/home/{catalog}/{category}")
	public String home(@PathVariable(name="catalog") String catalog, 
			@PathVariable(name="category") String category, 
			Model model) {
		model.addAttribute("library", libraryService.getLibrary());
		model.addAttribute("selectedCatalogName", catalog);
		model.addAttribute("indices", indexService.findAllByCategory_Catalog_NameAndCategory_NameOrderByBook_FilenameAsc(catalog, category));
		return "home";
	}

}