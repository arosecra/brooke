package org.github.arosecra.brooke.services;

import java.io.File;

import org.github.arosecra.brooke.model.ItemCatalog;
import org.github.arosecra.brooke.model.ItemLocation;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.Shelf;
import org.github.arosecra.brooke.model.ShelfItem;
import org.github.arosecra.brooke.model.api.CategoryApiModel;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.model.api.ItemApiModel;
import org.springframework.stereotype.Component;

@Component
public class LibraryLocationService {
	
	public CollectionApiModel getCollection(Library library, String collectionName) {
		CollectionApiModel result = new CollectionApiModel();
		
		for(CollectionApiModel collection : library.getCollections()) {
			if(collection.getName().equals(collectionName) || 
				collection.getName().equals(collectionName.replaceAll(" ", "_"))
			) {
				result = collection;
			}
		}
		
		return result;
	}

	public CategoryApiModel getCategory(Library library, String collectionName, String categoryName) {
		CategoryApiModel result = null;
		
		CollectionApiModel collection = this.getCollection(library, collectionName);
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

	public ItemApiModel getItem(Library library, String collectionName, String itemName) {
		ItemLocation il = this.getItemLocation(library, collectionName, itemName);
		return this.getItemByLocation(library, il);
	}
	
	
	public Shelf getShelf(Library library, String collectionName) {
		Shelf result = null;
		
		for(Shelf shelf : library.getShelves()) {
			if(shelf.getName().equals(collectionName) || 
				shelf.getName().equals(collectionName.replaceAll(" ", "_"))
			) {
				result = shelf;
			}
		}
		
		return result;
	}

	public ItemApiModel getSeries(Library library, String collectionName, String categoryName, String seriesName) {
		ItemApiModel result = null;
		CategoryApiModel cat = this.getCategory(library, collectionName, categoryName);
		if(cat != null) {
			for(ItemApiModel item : cat.getItems()) {
				if(item.getName().equals(seriesName)) {
					result = item;
				}
			}
		}
		return result;
	}

	private ItemCatalog getItemCatalog(Library library, String collectionName) {
		ItemCatalog ic = null;
		for(ItemCatalog cat : library.getItemCatalogs()) {
			if(cat.getName().equals(collectionName)) {
				ic = cat;
			}
		}
		return ic;
	}

	public ItemLocation getItemLocation(Library library, String collectionName, String itemName) {

		ItemCatalog ic = this.getItemCatalog(library, collectionName);
		ItemLocation il = ic.get(itemName);
		return il;
	}

	public ItemApiModel getItemByLocation(Library library, ItemLocation itemLocation) {
		CategoryApiModel category = this.getCategory(library, itemLocation.getCollectionName(), itemLocation.getCategoryName());

		ItemApiModel result = null;
		if(itemLocation.getSeriesName() != null) {
			ItemApiModel parent = this.getItemByName(category, itemLocation.getSeriesName());
			result = this.getItemByName(parent, itemLocation.getItemName());
		} else {
			result = this.getItemByName(category, itemLocation.getItemName());
		}

		return result;
	}

	public ItemApiModel getSeriesByLocation(Library library, ItemLocation itemLocation) {
		CategoryApiModel category = this.getCategory(library, itemLocation.getCollectionName(), itemLocation.getCategoryName());

		ItemApiModel result = null;
		if(itemLocation.getSeriesName() != null) {
			result = this.getItemByName(category, itemLocation.getSeriesName());
		}

		return result;
	}

	public ShelfItem getShelfItem(Library library, String collectionName, String itemName) {
		Shelf shelf = this.getShelf(library, collectionName.replaceAll(" ", "_"));
		if(shelf != null) {
			return shelf.get(itemName.replaceAll(" ", "_"));
		}
		return null;
	}

	public File getRemoteFile(Library library, String collectionName, String itemName) {
		File remoteFile = null;
		CollectionApiModel collection = this.getCollection(library, collectionName);
		Shelf shelf = this.getShelf(library, collectionName.replaceAll(" ", "_"));
		if(shelf != null) {
			ShelfItem shelfItem = shelf.get(itemName.replaceAll(" ", "_"));
			if(shelfItem != null) {
				remoteFile = new File(shelfItem.getRemoteBaseDirectory(), shelfItem.getName() + "." + collection.getItemExtension());
			}
		}
		return remoteFile;
	}

	public File getLocalRelatedFile(
		Library library, 
		String collectionName, 
		String itemName, 
		String relatedFilename, 
		boolean includeParent
	) {
		File relatedFile = null;
		Shelf shelf = this.getShelf(library, collectionName.replaceAll(" ", "_"));
		if(shelf != null) {
			ShelfItem shelfItem = shelf.get(itemName.replaceAll(" ", "_"));
			if(shelfItem != null) {
				relatedFile = new File(shelfItem.getLocalBaseDirectory(), relatedFilename);
				if(!relatedFile.exists() && includeParent) {
					relatedFile = new File(shelfItem.getLocalBaseDirectory().getParentFile().getParentFile(), relatedFilename);
				}
			}
		}
		return relatedFile;
	} 

	private ItemApiModel getItemByName(ItemApiModel parent, String itemName) {
		ItemApiModel result = null;
		for(ItemApiModel item : parent.getChildItems()) {
			if(item.getName().equals(itemName)
			) {
				result = item;
			}
		}
		return result;
	}

	private ItemApiModel getItemByName(CategoryApiModel category, String itemName) {
		ItemApiModel result = null;
		for(ItemApiModel item : category.getItems()) {
			if(item.getName().equals(itemName)
			) {
				result = item;
			}
		}
		return result;
	}

}
