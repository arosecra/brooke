package org.github.arosecra.brooke.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.github.arosecra.brooke.model.Button;
import org.github.arosecra.brooke.model.ButtonSet;
import org.github.arosecra.brooke.model.Collection;
import org.github.arosecra.brooke.model.ShelfItem;
import org.github.arosecra.brooke.services.BrookeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BrookeController {

	@Autowired
	private BrookeService brookeService;
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("library", brookeService.getLibrary());
		model.addAttribute("buttonSet", brookeService.getStandardButtons());		
		return "library";
	}
	
	@GetMapping("/sync")
	public String sync(Model model) throws IOException {
		brookeService.sync();
		return "redirect:/";
	}
	
	
	@GetMapping("/collection/{collectionName}")
	public String getCollectionHome(Model model, @PathVariable(name="collectionName") String collectionName) {
		
		
		model.addAttribute("library", brookeService.getLibrary());
		model.addAttribute("buttonSet", brookeService.getStandardButtons());	
		model.addAttribute("collection", brookeService.getCollectionByName(collectionName));
		model.addAttribute("selectedCollection", collectionName);
		
		return "collection";
	}
	
	
	@GetMapping("/collection/{collectionName}/{catalogName}/{categoryName}")
	public String getCatalogHome(Model model, 
			@PathVariable(name="collectionName") String collectionName, 
			@PathVariable(name="catalogName") String catalogName, 
			@PathVariable(name="categoryName") String categoryName) {
		
		
		model.addAttribute("library", brookeService.getLibrary());
		model.addAttribute("buttonSet", brookeService.getStandardButtons());	
		model.addAttribute("collection", brookeService.getCollectionByName(collectionName));
		model.addAttribute("catalog", brookeService.getCatalogByName(collectionName, catalogName));
		model.addAttribute("category", brookeService.getCategoryByName(collectionName, catalogName, categoryName));
		model.addAttribute("selectedCollection", collectionName);
		model.addAttribute("selectedCatalog", catalogName);
		model.addAttribute("selectedCategory", categoryName);
		
		return "collection";
	}
	
	@GetMapping(value="/thumbnail/{collectionName}/{catalogName}/{categoryName}/{itemName}", produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] greeting(Model model, 
			@PathVariable(name="collectionName") String collectionName,
			@PathVariable(name="catalogName") String catalogName, 
			@PathVariable(name="categoryName") String categoryName,
			@PathVariable(name="itemName") String itemName) throws IOException {
		return brookeService.getThumbnail(collectionName, categoryName, itemName);
	}
	
	@PostMapping(value="/addtoc/{collectionName}/{catalogName}/{categoryName}/{itemName}")
	public String addToC(Model model, 
			@PathVariable(name="collectionName") String collectionName,
			@PathVariable(name="catalogName") String catalogName, 
			@PathVariable(name="categoryName") String categoryName,
			@PathVariable(name="itemName") String itemName) throws IOException {
		brookeService.addToc(collectionName, categoryName, itemName, 0, itemName);
		return "";
	}
	
	@GetMapping(value={
		"/shelfitem/{collectionName}/{catalogName}/{categoryName}/{itemName}",
		"/shelfitem/{collectionName}/{catalogName}/{categoryName}/{itemName}/{pageNo}"
	})
	public String openShelfItem(Model model, 
			@PathVariable(name="collectionName") String collectionName,
			@PathVariable(name="catalogName") String catalogName, 
			@PathVariable(name="categoryName") String categoryName,
			@PathVariable(name="itemName") String itemName,
			@PathVariable(name="pageNo", required = false) String pageNumber
			) throws IOException {
		
		Collection collection = brookeService.getCollectionByName(collectionName);
		ButtonSet buttonSet = brookeService.getStandardButtons();
		ShelfItem item = brookeService.getItemByName(collectionName, catalogName, categoryName, itemName);
		
		String result = collection.getOpenType();
		if(collection.getOpenType().equals("book")) {			
            buttonSet.addButton(new Button("Add ToC Entry", null, "show('#add-toc-modal')"));
            buttonSet.addButton(new Button("ToC", null, "show('#toc-modal')"));
            int number = 0;
            if(pageNumber != null)
            	number = Integer.parseInt(pageNumber);
            
            String urlPrototype = "/shelfitem/%s/%s/%s/%s/%d";
            if(number > 0)
            	buttonSet.addButton(new Button("Prev", String.format(urlPrototype, collectionName, catalogName, categoryName, itemName, number-2), null));
            buttonSet.addButton(new Button("Next", String.format(urlPrototype, collectionName, catalogName, categoryName, itemName, number+2), null));
            
			model.addAttribute("buttonSet", buttonSet);
			model.addAttribute("leftPage", number);
			model.addAttribute("rightPage", number+1);
			model.addAttribute("tocEntries", brookeService.getToCEntries(collectionName, catalogName, categoryName, itemName));
		} else if(collection.getOpenType().equals("video")) {
			if(!item.getChildItems().isEmpty()) {
				result = "videoseries";
				model.addAttribute("childItems", brookeService.getItemByName(collectionName, catalogName, categoryName, itemName).getChildItems());
			} else {
				model.addAttribute("subtitles", brookeService.getSubtitles(collectionName, catalogName, categoryName, itemName));
			}
		}

		model.addAttribute("library", brookeService.getLibrary());
		model.addAttribute("collection", brookeService.getCollectionByName(collectionName));
		model.addAttribute("catalog", brookeService.getCatalogByName(collectionName, catalogName));
		model.addAttribute("category", brookeService.getCategoryByName(collectionName, catalogName, categoryName));
		model.addAttribute("item", item);
		
		return result;
	}
	
	@GetMapping(value={
		"/childshelfitem/{collectionName}/{catalogName}/{categoryName}/{itemName}/{index}"
	})
	public String openChildShelfItem(Model model, 
			@PathVariable(name="collectionName") String collectionName,
			@PathVariable(name="catalogName") String catalogName, 
			@PathVariable(name="categoryName") String categoryName,
			@PathVariable(name="itemName") String itemName,
			@PathVariable(name="index", required = true) int index
			) throws IOException {
		
		Collection collection = brookeService.getCollectionByName(collectionName);
		ShelfItem item = brookeService.getItemByName(collectionName, catalogName, categoryName, itemName).getChildItems().get(index);
		
		String result = collection.getOpenType();
		if(collection.getOpenType().equals("video")) {
			model.addAttribute("subtitles", brookeService.getSubtitles(collectionName, catalogName, categoryName, itemName));
		}

		model.addAttribute("library", brookeService.getLibrary());
		model.addAttribute("collection", brookeService.getCollectionByName(collectionName));
		model.addAttribute("catalog", brookeService.getCatalogByName(collectionName, catalogName));
		model.addAttribute("category", brookeService.getCategoryByName(collectionName, catalogName, categoryName));
		model.addAttribute("item", item);
		
		return result;
	}
	
	@GetMapping(value="/page/{collectionName}/{catalogName}/{categoryName}/{itemName}/{pageNumber}", produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] getPage(Model model, 
			@PathVariable(name="collectionName") String collectionName,
			@PathVariable(name="catalogName") String catalogName, 
			@PathVariable(name="categoryName") String categoryName,
			@PathVariable(name="itemName") String itemName,
			@PathVariable(name="pageNumber") int pageNumber) throws IOException {
		return brookeService.getPage(collectionName, categoryName, itemName, pageNumber);
	}

	
	@GetMapping(value="/video/{collectionName}/{catalogName}/{categoryName}/{itemName}", produces="video/mp4")
	@ResponseBody
	public void getVideo(Model model, 
			HttpServletResponse response,
			@PathVariable(name="collectionName") String collectionName,
			@PathVariable(name="catalogName") String catalogName, 
			@PathVariable(name="categoryName") String categoryName,
			@PathVariable(name="itemName") String itemName) throws IOException {
		
		File videoFile = brookeService.getVideoFile(collectionName, catalogName, categoryName, itemName, 0);
		InputStream is = new BufferedInputStream(new FileInputStream(videoFile));
		IOUtils.copyLarge(is, response.getOutputStream());
        IOUtils.closeQuietly(is);
		response.flushBuffer();
	}

	
	@GetMapping(value="/subtitle/{collectionName}/{catalogName}/{categoryName}/{itemName}/{vttName}", produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public void geSubtitle(Model model, 
			HttpServletResponse response,
			@PathVariable(name="collectionName") String collectionName,
			@PathVariable(name="catalogName") String catalogName, 
			@PathVariable(name="categoryName") String categoryName,
			@PathVariable(name="itemName") String itemName,
			@PathVariable(name="vttName") String vttName) throws IOException {
		File subtitleFile = brookeService.getSubtitleFile(collectionName, categoryName, itemName, vttName);
        InputStream is = new BufferedInputStream(new FileInputStream(subtitleFile));
        IOUtils.copy(is, response.getOutputStream());
        IOUtils.closeQuietly(is);
        response.flushBuffer();
	}
	

	@GetMapping("/editsubtitles/{collectionName}/{catalogName}/{categoryName}/{itemName}")
	public String getSubtitleEditor(Model model, 
			@PathVariable(name="collectionName") String collectionName, 
			@PathVariable(name="catalogName") String catalogName, 
			@PathVariable(name="categoryName") String categoryName,
			@PathVariable(name="itemName") String itemName) throws IOException {
		
		
		model.addAttribute("library", brookeService.getLibrary());
		model.addAttribute("buttonSet", brookeService.getStandardButtons());	
		model.addAttribute("collection", brookeService.getCollectionByName(collectionName));
		model.addAttribute("catalog", brookeService.getCatalogByName(collectionName, catalogName));
		model.addAttribute("category", brookeService.getCategoryByName(collectionName, catalogName, categoryName));
		model.addAttribute("subtitleEntries", brookeService.getSubtitleEntries(collectionName, catalogName, categoryName, itemName));
		
		return "subtitleeditor";
	}
}