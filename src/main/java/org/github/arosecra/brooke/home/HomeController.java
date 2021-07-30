package org.github.arosecra.brooke.home;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.index.Index;
import org.github.arosecra.brooke.index.IndexService;
import org.github.arosecra.brooke.library.LibraryService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
	
	@Autowired
	private Logger logger;
	
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
		model.addAttribute("selectedCategoryName", category);
		model.addAttribute("indices", indexService.findAllByCategory_Catalog_NameAndCategory_NameOrderByBook_FilenameAsc(catalog, category));
		return "home";
	}

	@GetMapping("/downloadcategory/{catalog}/{category}")
	public String downloadCategory(@PathVariable(name="catalog") String catalog, 
			@PathVariable(name="category") String category, 
			Model model) throws IOException {
		List<Index> indices = indexService.findAllByCategory_Catalog_NameAndCategory_NameOrderByBook_FilenameAsc(catalog, category);
		model.addAttribute("library", libraryService.getLibrary());
		model.addAttribute("selectedCatalogName", catalog);
		model.addAttribute("selectedCategoryName", category);
		model.addAttribute("indices", indices);
		
		for(Index idx : indices) {
			if(!idx.isLocal()) {
				String bookname = idx.getBook().getFilename();
				logger.info("Downloading " + bookname);
				File remoteCbt = new File("\\\\drobo5n\\public\\scans\\books\\" + bookname + "\\" + bookname + ".cbt");
				File localCbt = new File("D:/scans/books/" + bookname + "/" + bookname + ".cbt");
				FileUtils.copyFile(remoteCbt, localCbt);
			}
		}
		
		return "home";
	}

}