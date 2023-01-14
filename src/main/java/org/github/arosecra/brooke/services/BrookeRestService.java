package org.github.arosecra.brooke.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.dao.LibraryDao2;
import org.github.arosecra.brooke.model.api.CategoryApiModel;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.model.api.ItemApiModel;
import org.github.arosecra.brooke.model2.Library;
import org.github.arosecra.brooke.model2.Shelf;
import org.github.arosecra.brooke.model2.ShelfItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrookeRestService {
	
	private static byte[] DEFAULT_THUMBNAIL;
	static {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(BrookeRestService.class.getResourceAsStream("/static/images/default_thumbnail.png"), baos);
			DEFAULT_THUMBNAIL = baos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Autowired
	private Settings settings;
	
	private LibraryDao2 libraryDao2;
	
	private Library library;

	@Autowired
	public void setLibraryDao2(LibraryDao2 libraryDao) { this.libraryDao2 = libraryDao; }
	
	@PostConstruct()
	public void init() {
		library = libraryDao2.getLibrary();
	}
	

	public Library getLibrary() {
		return this.library;
	}

	public CollectionApiModel getCollection(String collectionName) {
		CollectionApiModel result = new CollectionApiModel();
		
		for(CollectionApiModel collection : this.library.getCollections()) {
			if(collection.getName().equals(collectionName) || 
				collection.getName().equals(collectionName.replaceAll(" ", "_"))
			) {
				result = collection;
			}
		}
		
		return result;
	}

	public CategoryApiModel getCategory(String collectionName, String categoryName) {
		CategoryApiModel result = null;
		
		CollectionApiModel collection = this.getCollection(collectionName);
		if(collection != null) {
			for(CategoryApiModel cat : collection.getCategories()) {
				if(cat.getName().equals(categoryName) ||
					cat.getName().equals(categoryName.replaceAll(" ", "_"))	
				) {
					result = cat;
				}
			}
		}
		
		return result;
	}
	
	public Shelf getShelf(String collectionName) {
		Shelf result = null;
		
		for(Shelf shelf : this.library.getShelves()) {
			if(shelf.getName().equals(collectionName) || 
				shelf.getName().equals(collectionName.replaceAll(" ", "_"))
			) {
				result = shelf;
			}
		}
		
		return result;
	}

	public ItemApiModel getSeries(String collectionName, String categoryName, String seriesName) {
		ItemApiModel result = null;
		CategoryApiModel cat = this.getCategory(collectionName, categoryName);
		if(cat != null) {
			for(ItemApiModel item : cat.getItems()) {
				if(item.getName().equals(seriesName)) {
					result = item;
				}
			}
		}
		return result;
	}
	
//	public getItem(String collectionName, String itemName) {
//		
//	}
//	
//	private File getThumbnailFile(ItemApiModel item) {
//		
//	}
	
	public byte[] getThumbnail(String collectionName, String itemName) throws IOException {
		//category may be the series, or not needed
		File thumbnailFile = null;
		
		Shelf shelf = this.getShelf(collectionName.replaceAll(" ", "_"));
		if(shelf != null) {
			ShelfItem shelfItem = shelf.get(itemName.replaceAll(" ", "_"));
			if(shelfItem != null) {
				thumbnailFile = new File(shelfItem.getLocalBaseDirectory(), "thumbnail.png");
				if(!thumbnailFile.exists()) {
					thumbnailFile = new File(shelfItem.getLocalBaseDirectory().getParentFile().getParentFile(), "thumbnail.png");
				}
			}
		}
		
		if(thumbnailFile == null || !thumbnailFile.exists()) {
			return DEFAULT_THUMBNAIL;
		} else {
			return FileUtils.readFileToByteArray(thumbnailFile);
		}
	}
	
	public byte[] getLargeThumbnail(String collectionName, String itemName) throws IOException {
		//category may be the series, or not needed
		File thumbnailFile = null;
		
		Shelf shelf = this.getShelf(collectionName.replaceAll(" ", "_"));
		if(shelf != null) {
			ShelfItem shelfItem = shelf.get(itemName.replaceAll(" ", "_"));
			if(shelfItem != null) {
				thumbnailFile = new File(shelfItem.getLocalBaseDirectory(), "large_thumbnail.png");
				if(!thumbnailFile.exists()) {
					thumbnailFile = new File(shelfItem.getLocalBaseDirectory().getParentFile().getParentFile(), "large_thumbnail.png");
				}
			}
		}
		
		if(thumbnailFile == null || !thumbnailFile.exists()) {
			return DEFAULT_THUMBNAIL;
		} else {
			return FileUtils.readFileToByteArray(thumbnailFile);
		}
	}

}