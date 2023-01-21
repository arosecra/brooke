package org.github.arosecra.brooke.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.model.JobDetails;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.Shelf;
import org.github.arosecra.brooke.model.ShelfItem;
import org.github.arosecra.brooke.model.api.BookDetailsApiModel;
import org.github.arosecra.brooke.model.api.CategoryApiModel;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.model.api.ItemApiModel;
import org.github.arosecra.brooke.model.api.MissingItemApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

@Service
public class BrookeService {
	
	@Autowired
	private Settings settings;
	
	@Autowired
	private TarService tarService;
	
	@Autowired
	private FileCacheService fileCacheService;
	
	@Autowired
	private JobService jobDao;

	@Autowired
	private LibraryLocationService libraryLocationService;

	@Autowired
	private BrookeSyncService syncService;
	
	@Autowired
	private LibraryDao libraryDao;

	@Autowired
	private MissingItemService missingItemService;

	@Autowired
	private TabletService tabletService;

	@Autowired
	private VideoService videoService;
	
	private Library library;
	
	@PostConstruct()
	public void init() {
		library = libraryDao.getLibrary(false);
	}

	public Library getLibrary() {
		return this.library;
	}

	public CollectionApiModel getCollection(String collectionName) {
		return libraryLocationService.getCollection(library, collectionName);
	}

	public CategoryApiModel getCategory(String collectionName, String categoryName) {
		return this.libraryLocationService.getCategory(library, collectionName, categoryName);
	}

	public ItemApiModel getItem(String collectionName, String itemName) {
		return this.libraryLocationService.getItem(library, collectionName, itemName);
	}
	
	public Shelf getShelf(String collectionName) {
		return this.libraryLocationService.getShelf(library, collectionName);
	}

	public ItemApiModel getSeries(String collectionName, String categoryName, String seriesName) {
		return this.libraryLocationService.getSeries(library, collectionName, categoryName, seriesName);
	}
	
	public byte[] getThumbnail(String collectionName, String itemName) throws IOException {
		File thumbnailFile = this.libraryLocationService.getLocalRelatedFile(
			library, 
			collectionName, 
			itemName, 
			"thumbnail.png", 
			true
		);
		
		if(thumbnailFile == null || !thumbnailFile.exists()) {
			return null;
		} else {
			return FileUtils.readFileToByteArray(thumbnailFile);
		}
	}
	
	public byte[] getLargeThumbnail(String collectionName, String itemName) throws IOException {
		//category may be the series, or not needed
		File thumbnailFile = this.libraryLocationService.getLocalRelatedFile(
			library, 
			collectionName, 
			itemName, 
			"large_thumbnail.png", 
			true
		);
		
		if(thumbnailFile == null || !thumbnailFile.exists()) {
			return null;
		} else {
			return FileUtils.readFileToByteArray(thumbnailFile);
		}
	}

	public byte[] getPage(String collectionName, String itemName, int pageNumber) throws IOException {
		File tar = fileCacheService.getCachedFile(library, collectionName, itemName);
		return tarService.getPageFromTar(tar, pageNumber);
	}
	
	public JobDetails cacheItem(String collectionName, String itemName) throws IOException {		
		return fileCacheService.cacheItem(library, collectionName, itemName);
	}

	public BookDetailsApiModel getBookDetails(String collectionName, String itemName) throws JsonParseException, JsonMappingException, IOException {
		File cbtDetailsFile = this.libraryLocationService.getLocalRelatedFile(
			library, 
			collectionName, 
			itemName, 
			"cbtDetails.yaml", 
			false
		);

		ObjectMapper mapper = new YAMLMapper();
		BookDetailsApiModel result = mapper.readValue(cbtDetailsFile, BookDetailsApiModel.class);
		
		return result;
	}

	public JobDetails getJobDetails(long jobNumber) {
		return jobDao.getJobDetails(jobNumber);
	}

	public void openVLC(String collectionName, String itemName) throws IOException {		
		this.videoService.openVLC(library, collectionName, itemName);
	}

	public void setLibraryDao(LibraryDao libraryDao) {
		this.libraryDao = libraryDao;
	}

	public JobDetails copyForTablet(String collectionName, String itemName) {
		return tabletService.copyForTablet(library, collectionName, itemName);
	}

	public List<MissingItemApiModel> getMissingItems() {
		return missingItemService.getMissingItems(library);
	}

	public JobDetails sync() {
		return this.syncService.sync(library.getCollections());
	}
	
	// public void createBoundingBoxPng() {
	// 	BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
	// 	g = image.createGraphics();  // not sure on this line, but this seems more right
	// 	g.setColor(Color.white);
	// 	g.fillRect(0, 0, 100, 100); // give the whole image a white background
	// 	g.setColor(Color.blue);
	// 	for( ..... ){
	// 	    g.fillRect(X , Y,  width , height );
	// 	        ....        
	// 	}
	// }

}