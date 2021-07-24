package org.github.arosecra.brooke.admin.catalog;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.github.arosecra.brooke.admin.AdminService;
import org.github.arosecra.brooke.book.BookService;
import org.github.arosecra.brooke.catalog.Catalog;
import org.github.arosecra.brooke.catalog.CatalogService;
import org.github.arosecra.brooke.catalogparent.CatalogParent;
import org.github.arosecra.brooke.catalogparent.CatalogParentService;
import org.github.arosecra.brooke.category.Category;
import org.github.arosecra.brooke.category.CategoryService;
import org.github.arosecra.brooke.library.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminCatalogController {
	
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
	


	@GetMapping("/admin/catalog/{catalog}")
	public String manageCatalog(@PathVariable(name="catalog") String catalog, 
			Model model) throws IOException {
		Catalog cat = catalogService.findByName(catalog);
		
		Set<String> parents = new HashSet<>();
		List<CatalogParent> catalogParents = catalogParentService.findAllByCatalog_NameOrderByParentCategory_Catalog_Name(catalog);
		for(CatalogParent parent : catalogParents) {
			System.out.println("parent " + parent.getParentCategory().getCatalog().getName() + "." + parent.getParentCategory().getName());
			parents.add(parent.getParentCategory().getCatalog().getName() + "." + parent.getParentCategory().getName());
		}
		
		model.addAttribute("catalog", cat);
		model.addAttribute("library", libraryService.getLibrary());
		model.addAttribute("parentKeys", parents);
		return "managecatalog";
	}
	
	@GetMapping("/admincatalog/{catalog}/addparent/{parentcatalog}/{category}")
	public String addParent(@PathVariable(name="catalog") String catalog, @PathVariable(name="parentcatalog") String parentcatalog, @PathVariable(name="category") String category, Model model) {
		
		Catalog cat = catalogService.findByName(catalog);
		Category parent = categoryService.findByCatalog_NameAndName(parentcatalog, category);
		
		CatalogParent cp = new CatalogParent();
		cp.setCatalog(cat);
		cp.setParentCategory(parent);
		catalogParentService.save(cp);
		
		return "redirect:/admin/catalog/" + catalog;
	}


	@GetMapping("/admin/catalog/{catalog}/addcategory/{categoryname}")
	public String addCategory(@PathVariable(name="catalog") String catalog, @PathVariable(name="categoryname") String categoryname,
			Model model) throws IOException {
		
		adminService.addCategory(catalog, categoryname);
		return "redirect:/admin/catalog/"+catalog;
	}
}
