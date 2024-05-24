package org.github.arosecra.brooke.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.github.arosecra.brooke.model.CacheManifest;
import org.github.arosecra.brooke.model.JobDetails;
import org.github.arosecra.brooke.model.api.BookDetailsApiModel;
import org.github.arosecra.brooke.model.api.CategoryApiModel;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.model.api.ItemApiModel;
import org.github.arosecra.brooke.model.api.MissingItemApiModel;
import org.github.arosecra.brooke.services.BrookeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrookeRestController {

	
	private static byte[] DEFAULT_THUMBNAIL;
	static {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(BrookeService.class.getResourceAsStream("/static/images/default_thumbnail.png"), baos);
			DEFAULT_THUMBNAIL = baos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Autowired
	private BrookeService brookeService;

	@GetMapping("/rest/collection")
	public List<CollectionApiModel> getCollections() {
		return this.brookeService.getCollections();
	}

	@GetMapping("/rest/collection/{collectionName}")
	public CollectionApiModel getCollection(@PathVariable("collectionName") String collectionName) {
		return this.brookeService.getCollection(collectionName);
	}
	
	@GetMapping("/rest/category/{collectionName}/{categoryName}")
	public CategoryApiModel getCategory(
			@PathVariable("collectionName") String collectionName,
			@PathVariable("categoryName") String categoryName
	) {
		return this.brookeService.getCategory(collectionName, categoryName);
	}
	
	@GetMapping("/rest/item/{collectionName}/{categoryName}/{itemName}")
	public ItemApiModel getItem(
			@PathVariable("collectionName") String collectionName,
			@PathVariable("itemName") String itemName
	) {
		return this.brookeService.getItem(collectionName, itemName);
	}
	
	@GetMapping("/rest/series/{collectionName}/{categoryName}/{seriesName}")
	public ItemApiModel getSeries(
			@PathVariable("collectionName") String collectionName,
			@PathVariable("categoryName") String categoryName,	
			@PathVariable("seriesName") String seriesName
	) {
		return this.brookeService.getSeries(collectionName, categoryName, seriesName);
	}
	
	@GetMapping("/rest/cache")
	public CacheManifest getCachedItems() throws IOException {
		return this.brookeService.listCachedItems();
	}
	
	@GetMapping("/rest/cache/{collectionName}/{itemName}")
	public JobDetails cacheItem(
			@PathVariable("collectionName") String collectionName,
			@PathVariable("itemName") String seriesName
	) throws IOException {
		JobDetails details = this.brookeService.cacheItem(collectionName, seriesName);
		return details;
	}
	
	@GetMapping(value="/rest/thumbnail/{collectionName}/{itemName}", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getThumbnail(
			@PathVariable("collectionName") String collectionName,
			@PathVariable("itemName") String itemName
	) throws IOException {
		byte[] thumbnail = this.brookeService.getThumbnail(collectionName, itemName);
		if(thumbnail == null)
			thumbnail = DEFAULT_THUMBNAIL;
		return thumbnail;
	}
	
	@GetMapping(value="/rest/large-thumbnail/{collectionName}/{itemName}", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getLargeThumbnail(
			@PathVariable("collectionName") String collectionName,
			@PathVariable("itemName") String itemName
	) throws IOException {
		
		byte[] thumbnail = this.brookeService.getLargeThumbnail(collectionName, itemName);
		if(thumbnail == null)
			thumbnail = DEFAULT_THUMBNAIL;
		return thumbnail;
	}

	@GetMapping(value="/rest/page/{collectionName}/{itemName}/{pageNumber}", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getPage(
			@PathVariable("collectionName") String collectionName,
			@PathVariable("itemName") String itemName,
			@PathVariable("pageNumber") int pageNumber
	) throws IOException {
		return this.brookeService.getPage(collectionName, itemName, pageNumber, 960);
	}
	
	@GetMapping("/rest/job-details/{jobNumber}")
	public JobDetails getJobDetails(
			@PathVariable("jobNumber") long jobNumber
			) throws IOException {
		return this.brookeService.getJobDetails(jobNumber);
	}
	
	@GetMapping("/rest/book-details/{collectionName}/{itemName}")
	public BookDetailsApiModel getBookDetails(
			@PathVariable("collectionName") String collectionName,
			@PathVariable("itemName") String itemName
			) throws IOException {
		return this.brookeService.getBookDetails(collectionName, itemName);
	}
	
	@GetMapping("/rest/video/{collectionName}/{itemName}")
	public JobDetails openVideo(
			@PathVariable("collectionName") String collectionName,
			@PathVariable("itemName") String itemName
			) throws IOException {
		this.brookeService.openVLC(collectionName, itemName);
		return new JobDetails();
	}
	
	@GetMapping("/rest/video-details/{collectionName}/{itemName}")
	public boolean getVideoDetails(
			@PathVariable("collectionName") String collectionName,
			@PathVariable("itemName") String itemName
			) {
		return false;
	}

	@GetMapping("/rest/administration/missing-item")
	public List<MissingItemApiModel> getMissingItems() {
		return brookeService.getMissingItems();
	}

	@GetMapping("/rest/administration/sync")
	public JobDetails sync() {
		return brookeService.sync();
	}

//	@PostMapping(value="/addtoc/{collectionName}/{itemName}")
//	public String addToC(
//			@PathVariable(name="collectionName") String collectionName,
//			@PathVariable(name="itemName") String itemName) throws IOException {
//		brookeService.addToc(collectionName, categoryName, itemName, 0, itemName);
//		return "";
//	}
	
	@GetMapping(value={
		"/rest/copy-to/{device}/{collectionName}/{itemName}"
	})
	public JobDetails copyShelfItemTo(
			@PathVariable(name="collectionName") String collectionName,
			@PathVariable(name="itemName") String itemName,
			@PathVariable(name="device") String device
			) throws IOException {
		return this.brookeService.copyToDevice(collectionName, itemName, device);
	}

}