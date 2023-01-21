package org.github.arosecra.brooke.services;

import java.util.ArrayList;
import java.util.List;

import org.github.arosecra.brooke.model.ItemLocation;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.Shelf;
import org.github.arosecra.brooke.model.ShelfItem;
import org.github.arosecra.brooke.model.api.CategoryApiModel;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.model.api.ItemApiModel;
import org.github.arosecra.brooke.model.api.MissingItemApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MissingItemService {

	@Autowired
	private LibraryLocationService libraryLocationService;
	
	public List<MissingItemApiModel> getMissingItems(Library library) {
		List<MissingItemApiModel> result = new ArrayList<>();
		//find all of the shelf items that do not have item locations
		for(Shelf shelf : library.getShelves()) {
			for(ShelfItem si : shelf.values()) {
				ItemLocation il = this.libraryLocationService.getItemLocation(library, shelf.getName(), si.getName());

				if(il == null) {
					MissingItemApiModel missing = new MissingItemApiModel();
					missing.setCollection(shelf.getName());
					missing.setItemMissing(true);
					missing.setItemName(si.getName());
					result.add(missing);
				}
			}
		}

		//find all of the item and child items that do not have a shelf item
		for(CollectionApiModel collection : library.getCollections()) {
			for(CategoryApiModel category : collection.getCategories()) {
				for(ItemApiModel item : category.getItems()) {
					ShelfItem si = this.libraryLocationService.getShelfItem(library, collection.getName(), item.getName());

					if(si == null) {
						MissingItemApiModel missing = new MissingItemApiModel();
						missing.setCollection(collection.getName());
						missing.setItemMissing(false);
						missing.setItemName(item.getName());
						result.add(missing);
					}

					for(ItemApiModel child : item.getChildItems()) {
						
						si = this.libraryLocationService.getShelfItem(library, collection.getName(), child.getName());

						if(si == null) {
							MissingItemApiModel missing = new MissingItemApiModel();
							missing.setCollection(collection.getName());
							missing.setItemMissing(false);
							missing.setItemName(child.getName());
							result.add(missing);
						}
					}
				}
			}
		}

		return result;
	}
}
