package org.github.arosecra.brooke.admin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
	
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

	@GetMapping("/admin")
	public String getAdmin(Model model) {
		model.addAttribute("library", libraryService.getLibrary());
		return "admin";
	}

	@GetMapping("/admin/addcatalog/{catalog}")
	public String addCatalog(@PathVariable(name="catalog") String catalog,
			Model model) throws IOException {
		
		adminService.addCatalog(catalog);
		return "redirect:/admin/catalog/"+catalog;
	}

	@GetMapping("/admin/export")
	public String export(Model model) throws IOException {
		adminService.export();
		return "redirect:/admin/";
	}
}
