package org.github.arosecra.brooke.controllers;

import java.io.IOException;
import java.util.List;

import org.github.arosecra.brooke.model.api.CategoryApiModel;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.model.api.ItemApiModel;
import org.github.arosecra.brooke.services.BrookeRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrookeRestController {

	@Autowired
	private BrookeRestService brookeRestService;

	@GetMapping("/rest/collection")
	public List<CollectionApiModel> getCollections() {
		return this.brookeRestService.getLibrary().getCollections();
	}

	@GetMapping("/rest/collection/{collectionName}")
	public CollectionApiModel getCollection(@PathVariable("collectionName") String collectionName) {
		return this.brookeRestService.getCollection(collectionName);
	}
	
	@GetMapping("/rest/category/{collectionName}/{categoryName}")
	public CategoryApiModel getCategory(
			@PathVariable("collectionName") String collectionName,
			@PathVariable("categoryName") String categoryName
	) {
		return this.brookeRestService.getCategory(collectionName, categoryName);
	}
	
	@GetMapping("/rest/series/{collectionName}/{categoryName}/{seriesName}")
	public ItemApiModel getSeries(
			@PathVariable("collectionName") String collectionName,
			@PathVariable("categoryName") String categoryName,	
			@PathVariable("seriesName") String seriesName
	) {
		return this.brookeRestService.getSeries(collectionName, categoryName, seriesName);
	}
	
	@GetMapping(value="/rest/thumbnail/{collectionName}/{itemName}", produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] getThumbnail(
			@PathVariable("collectionName") String collectionName,
			@PathVariable("itemName") String itemName
	) throws IOException {
		return this.brookeRestService.getThumbnail(collectionName, itemName);
	}
	

	
	@GetMapping(value="/rest/large-thumbnail/{collectionName}/{itemName}", produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] getLargeThumbnail(
			@PathVariable("collectionName") String collectionName,
			@PathVariable("itemName") String itemName
	) throws IOException {
		return this.brookeRestService.getLargeThumbnail(collectionName, itemName);
	}
	
	
//	@GetMapping("/rest")
//	public String home(Model model) {
//		model.addAttribute("library", brookeService.getLibrary());
//		model.addAttribute("buttonSet", brookeService.getStandardButtons());		
//		return "library";
//	}
//	
//	@GetMapping("/sync")
//	public String sync(Model model) throws IOException {
//		brookeService.sync();
//		return "redirect:/";
//	}
//	
//	@GetMapping("/reload")
//	public String reload(Model model) throws IOException {
//		brookeService.reloadLibrary();
//		return "redirect:/";
//	}
//	
//	
//	@GetMapping("/collection/{collectionName}")
//	public String getCollectionHome(Model model, @PathVariable(name="collectionName") String collectionName) {
//		System.out.println("getCollectionHome");
//		
//		
//		model.addAttribute("library", brookeService.getLibrary());
//		model.addAttribute("buttonSet", brookeService.getStandardButtons());	
//		model.addAttribute("collection", brookeService.getCollectionByName(collectionName));
//		model.addAttribute("selectedCollection", collectionName);
//		
//		return "collection";
//	}
//	
//	
//	@GetMapping("/collection/{collectionName}/{catalogName}/{categoryName}")
//	public String getCatalogHome(Model model, 
//			@PathVariable(name="collectionName") String collectionName, 
//			@PathVariable(name="catalogName") String catalogName, 
//			@PathVariable(name="categoryName") String categoryName) {
//		System.out.println("getCatalogHome");
//		
//		
//		model.addAttribute("library", brookeService.getLibrary());
//		model.addAttribute("buttonSet", brookeService.getStandardButtons());	
//		model.addAttribute("collection", brookeService.getCollectionByName(collectionName));
//		model.addAttribute("catalog", brookeService.getCatalogByName(collectionName, catalogName));
//		model.addAttribute("category", brookeService.getCategoryByName(collectionName, catalogName, categoryName));
//		model.addAttribute("selectedCollection", collectionName);
//		model.addAttribute("selectedCatalog", catalogName);
//		model.addAttribute("selectedCategory", categoryName);
//		
//		return "collection";
//	}
//	
//	@GetMapping(value="/thumbnail/{collectionName}/{catalogName}/{categoryName}/{itemName}", produces = MediaType.IMAGE_PNG_VALUE)
//	@ResponseBody
//	public byte[] getThumbnail(Model model, 
//			@PathVariable(name="collectionName") String collectionName,
//			@PathVariable(name="catalogName") String catalogName, 
//			@PathVariable(name="categoryName") String categoryName,
//			@PathVariable(name="itemName") String itemName) throws IOException {
//		return brookeService.getThumbnail(collectionName, categoryName, itemName);
//	}
//	
//	@GetMapping(value="/largethumbnail/{collectionName}/{catalogName}/{categoryName}/{itemName}", produces = MediaType.IMAGE_PNG_VALUE)
//	@ResponseBody
//	public byte[] getLargeThumbnail(Model model, 
//			@PathVariable(name="collectionName") String collectionName,
//			@PathVariable(name="catalogName") String catalogName, 
//			@PathVariable(name="categoryName") String categoryName,
//			@PathVariable(name="itemName") String itemName) throws IOException {
//		return brookeService.getLargeThumbnail(collectionName, catalogName, categoryName, itemName, 0);
//	}
//	
//	@PostMapping(value="/addtoc/{collectionName}/{catalogName}/{categoryName}/{itemName}")
//	public String addToC(Model model, 
//			@PathVariable(name="collectionName") String collectionName,
//			@PathVariable(name="catalogName") String catalogName, 
//			@PathVariable(name="categoryName") String categoryName,
//			@PathVariable(name="itemName") String itemName) throws IOException {
//		brookeService.addToc(collectionName, categoryName, itemName, 0, itemName);
//		return "";
//	}
//	
//	@GetMapping(value={
//		"/shelfitem/{collectionName}/{catalogName}/{categoryName}/{itemName}",
//		"/shelfitem/{collectionName}/{catalogName}/{categoryName}/{itemName}/{pageNo}"
//	})
//	public String openShelfItem(Model model,
//			HttpServletRequest  request,
//			HttpServletResponse response,
//			@PathVariable(name="collectionName") String collectionName,
//			@PathVariable(name="catalogName") String catalogName, 
//			@PathVariable(name="categoryName") String categoryName,
//			@PathVariable(name="itemName") String itemName,
//			@PathVariable(name="pageNo", required = false) String pageNumber
//			) throws IOException {
//		System.out.println("openShelfItem");
//		
//		Collection collection = brookeService.getCollectionByName(collectionName);
//		ButtonSet buttonSet = new ButtonSet();
//		ShelfItem item = brookeService.getItemByName(collectionName, catalogName, categoryName, itemName);
//		
//		String result = collection.getOpenType();
//		if(collection.getOpenType().equals("book")) {			
//            buttonSet.addButton(new Button("Add ToC Entry", null, "show('#add-toc-modal')"));
//            buttonSet.addButton(new Button("ToC", null, "show('#toc-modal')"));
//            int number = 0;
//            if(pageNumber != null)
//            	number = Integer.parseInt(pageNumber);
//            
//            if(!brookeService.isCached(collectionName, catalogName, categoryName, itemName, 0)) {
//	        	brookeService.cacheItem(collectionName, catalogName, categoryName, itemName, 0);
//	        }
//            
//            String urlPrototype = "/shelfitem/%s/%s/%s/%s/%d";
//            if(number > 0)
//            	buttonSet.addButton(new Button("Prev", String.format(urlPrototype, collectionName, catalogName, categoryName, itemName, number-2), null));
//            buttonSet.addButton(new Button("Next", String.format(urlPrototype, collectionName, catalogName, categoryName, itemName, number+2), null));
//            
//			model.addAttribute("buttonSet", buttonSet);
//			model.addAttribute("leftPage", number);
//			model.addAttribute("rightPage", number+1);
//			model.addAttribute("tocEntries", brookeService.getToCEntries(collectionName, catalogName, categoryName, itemName));
//		} else if(collection.getOpenType().equals("video")) {
//			if(!item.getChildItems().isEmpty()) {
//				result = "videoseries";
//				model.addAttribute("childItems", item.getChildItems());
//			} else {
//		        if(!brookeService.isCached(collectionName, catalogName, categoryName, itemName, 0)) {
//		        	brookeService.cacheItem(collectionName, catalogName, categoryName, itemName, 0);
//		        }
//			}
//		}
//
//		model.addAttribute("library", brookeService.getLibrary());
//		model.addAttribute("collection", brookeService.getCollectionByName(collectionName));
//		model.addAttribute("catalog", brookeService.getCatalogByName(collectionName, catalogName));
//		model.addAttribute("category", brookeService.getCategoryByName(collectionName, catalogName, categoryName));
//		model.addAttribute("item", item);
//		model.addAttribute("index", 0);
//		
//		if(result.equals("video")) {
//			brookeService.openVLC(collectionName, catalogName, categoryName, itemName, 0);
//			response.sendRedirect(request.getHeader("Referer"));
//			return null;
//		} else {
//			return result;
//		}
//	}
//	
//	@GetMapping(value={
//		"/shelfitem-to-cbt/{collectionName}/{catalogName}/{categoryName}/{itemName}"
//	})
//	public String copyShelfItemToCbt(Model model,
//			HttpServletRequest  request,
//			HttpServletResponse response,
//			@PathVariable(name="collectionName") String collectionName,
//			@PathVariable(name="catalogName") String catalogName, 
//			@PathVariable(name="categoryName") String categoryName,
//			@PathVariable(name="itemName") String itemName,
//			@PathVariable(name="pageNo", required = false) String pageNumber
//			) throws IOException {
//		System.out.println("openShelfItem");
//		
//		Collection collection = brookeService.getCollectionByName(collectionName);
//		ShelfItem item = brookeService.getItemByName(collectionName, catalogName, categoryName, itemName);
//		
//		if(collection.getOpenType().equals("book")) {			
//            brookeService.copyForTablet(collectionName, catalogName, categoryName, itemName);
//		} 
//
//		response.sendRedirect(request.getHeader("Referer"));
//		
//		return null;
//	}
//	
//	@GetMapping(value={
//		"/childshelfitem/{collectionName}/{catalogName}/{categoryName}/{itemName}/{index}"
//	})
//	public String openChildShelfItem(Model model, 
//			HttpServletRequest  request,
//			HttpServletResponse response,
//			@PathVariable(name="collectionName") String collectionName,
//			@PathVariable(name="catalogName") String catalogName, 
//			@PathVariable(name="categoryName") String categoryName,
//			@PathVariable(name="itemName") String itemName,
//			@PathVariable(name="index", required = true) int index
//			) throws IOException {
//		System.out.println("openChildShelfItem");
//		
//		Collection collection = brookeService.getCollectionByName(collectionName);
//		ShelfItem item = brookeService.getItemByName(collectionName, catalogName, categoryName, itemName);
//		
//		String result = collection.getOpenType();
//
//        if(!brookeService.isCached(collectionName, catalogName, categoryName, itemName, index)) {
//        	brookeService.cacheItem(collectionName, catalogName, categoryName, itemName, index);
//        }
//
//		model.addAttribute("library", brookeService.getLibrary());
//		model.addAttribute("collection", brookeService.getCollectionByName(collectionName));
//		model.addAttribute("catalog", brookeService.getCatalogByName(collectionName, catalogName));
//		model.addAttribute("category", brookeService.getCategoryByName(collectionName, catalogName, categoryName));
//		model.addAttribute("item", item);
//		model.addAttribute("index", index);
//		
//		if(result.equals("video")) {
//			brookeService.openVLC(collectionName, catalogName, categoryName, itemName, index);
//			response.sendRedirect(request.getHeader("Referer"));
//			return null;
//		} else {
//			return result;
//		}
//	}
//	
//	@GetMapping(value="/page/{collectionName}/{catalogName}/{categoryName}/{itemName}/{pageNumber}", produces = MediaType.IMAGE_PNG_VALUE)
//	@ResponseBody
//	public byte[] getPage(Model model, 
//			@PathVariable(name="collectionName") String collectionName,
//			@PathVariable(name="catalogName") String catalogName, 
//			@PathVariable(name="categoryName") String categoryName,
//			@PathVariable(name="itemName") String itemName,
//			@PathVariable(name="pageNumber") int pageNumber) throws IOException {
//		return brookeService.getPage(collectionName, categoryName, itemName, pageNumber);
//	}
	
	
}