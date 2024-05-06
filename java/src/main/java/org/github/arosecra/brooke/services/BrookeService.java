package org.github.arosecra.brooke.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.model.CacheManifest;
import org.github.arosecra.brooke.model.JobDetails;
import org.github.arosecra.brooke.model.Shelf;
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
	private SyncService syncService;
	
	@Autowired
	private LibraryDao libraryDao;

	@Autowired
	private MissingItemService missingItemService;

	@Autowired
	private TabletService tabletService;

	@Autowired
	private VideoService videoService;
	
	@Autowired
	private LibraryCacheService libraryCacheService;
	
	@Autowired
	private ImageService imageService;

	public List<CollectionApiModel> getCollections() {
		return libraryCacheService.getLibrary().getCollections();
	}

	public CollectionApiModel getCollection(String collectionName) {
		return libraryLocationService.getCollection(libraryCacheService.getLibrary(), collectionName);
	}

	public CategoryApiModel getCategory(String collectionName, String categoryName) {
		return this.libraryLocationService.getCategory(libraryCacheService.getLibrary(), collectionName, categoryName);
	}

	public ItemApiModel getItem(String collectionName, String itemName) {
		return this.libraryLocationService.getItem(libraryCacheService.getLibrary(), collectionName, itemName);
	}
	
	public Shelf getShelf(String collectionName) {
		return this.libraryLocationService.getShelf(libraryCacheService.getLibrary(), collectionName);
	}

	public ItemApiModel getSeries(String collectionName, String categoryName, String seriesName) {
		return this.libraryLocationService.getSeries(libraryCacheService.getLibrary(), collectionName, categoryName, seriesName);
	}
	
	public byte[] getThumbnail(String collectionName, String itemName) throws IOException {
		File thumbnailFile = this.libraryLocationService.getLocalRelatedFile(
			libraryCacheService.getLibrary(), 
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
			libraryCacheService.getLibrary(), 
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

	public byte[] getPage(String collectionName, String itemName, int pageNumber, int width) throws IOException {
		File tar = fileCacheService.getCachedFile(libraryCacheService.getLibrary(), collectionName, itemName);
		return tarService.getPageFromTar(tar, pageNumber);
	}
	
	public CacheManifest listCachedItems() throws IOException {		
		return fileCacheService.readCachedFileManifest();
	}
	
	public JobDetails cacheItem(String collectionName, String itemName) throws IOException {		
		return fileCacheService.cacheItem(libraryCacheService.getLibrary(), collectionName, itemName);
	}

	public BookDetailsApiModel getBookDetails(String collectionName, String itemName) throws JsonParseException, JsonMappingException, IOException {
		File cbtDetailsFile = this.libraryLocationService.getLocalRelatedFile(
			libraryCacheService.getLibrary(), 
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
		this.videoService.openVLC(libraryCacheService.getLibrary(), collectionName, itemName);
	}

	public void setLibraryDao(LibraryDao libraryDao) {
		this.libraryDao = libraryDao;
	}

	public JobDetails copyForTablet(String collectionName, String itemName) {
		return tabletService.copyForTablet(libraryCacheService.getLibrary(), collectionName, itemName);
	}

	public List<MissingItemApiModel> getMissingItems() {
		return missingItemService.getMissingItems(libraryCacheService.getLibrary());
	}

	public JobDetails sync() {
		return this.syncService.sync(libraryCacheService.getLibrary().getCollections());
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