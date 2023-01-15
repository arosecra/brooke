package org.github.arosecra.brooke.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.model.api.CategoryApiModel;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.model.api.ItemApiModel;
import org.github.arosecra.brooke.model2.ItemCatalog;
import org.github.arosecra.brooke.model2.Library;
import org.github.arosecra.brooke.model2.Shelf;
import org.github.arosecra.brooke.model2.ShelfItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

@Repository
public class LibraryDao2 {

	private Settings settings;

	@Autowired
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public Library getLibrary(boolean includeRemote) {
		Library result = new Library();

		// get the collections
		getCollectionsFromDisk(result);
		
		
		getShelfItemsFromLocal(result);
		if(includeRemote) {
			getShelfItemsFromRemote(result);
		}
		
		getItemLocations(result);
		
		System.out.println("Finished loading library");
		return result;
	}

	private void getItemLocations(Library result) {

	}

	private void getShelfItemsFromLocal(Library result) {
		for (CollectionApiModel collection : result.getCollections()) {

			Shelf shelf = new Shelf();
			shelf.setName(collection.getName());

			File localDir = new File(collection.getLocalDirectory());

			for (File childFile : FileUtils.listFiles(localDir, null, true)) {
				if (childFile.getName().endsWith(".item")) {
					File baseDir = childFile.getParentFile();
					String name = FilenameUtils.getBaseName(childFile.getName());
					if (!shelf.containsKey(name)) {
						ShelfItem shelfItem = new ShelfItem();
						shelfItem.setName(name);
						shelfItem.setLocalCollectionBaseDirectory(localDir);
						shelfItem.setRemoteCollectionBaseDirectory(new File(collection.getRemoteDirectory()));
						shelf.add(shelfItem);
					}

					ShelfItem shelfItem = shelf.get(name);
					shelfItem.setLocalBaseDirectory(baseDir);
					
					//check if the parent directory has a thumbnail.png file
					if(new File(baseDir, "../thumbnail.png").exists()) {
						File parentBaseDir = baseDir.getParentFile();
						String parentName = FilenameUtils.getBaseName(parentBaseDir.getName());
						if (!shelf.containsKey(parentName)) {
							ShelfItem parentShelfItem = new ShelfItem();
							parentShelfItem.setName(parentName);
							parentShelfItem.setLocalCollectionBaseDirectory(localDir);
							parentShelfItem.setRemoteCollectionBaseDirectory(new File(collection.getRemoteDirectory()));
							shelf.add(parentShelfItem);
						}

						ShelfItem parentShelfItem = shelf.get(parentName);
						parentShelfItem.setLocalBaseDirectory(parentBaseDir);
					}
				}
			}
			result.getShelves().add(shelf);
		}
	}
	


	private void getShelfItemsFromRemote(Library result) {
		for (CollectionApiModel collection : result.getCollections()) {

			Shelf shelf = null;
			for(Shelf s : result.getShelves()) {
				if(s.getName().equals(collection.getName())) {
					shelf = s;
				}
			}

			// get the shelf remote shelf items

			File remoteParentDir = new File(collection.getRemoteDirectory());

			for (File childFile : FileUtils.listFiles(remoteParentDir, null, true)) {
				if (childFile.getName().endsWith(".item")) {
					File baseDir = childFile.getParentFile();
					String name = FilenameUtils.getBaseName(childFile.getName());
					if (!shelf.containsKey(name)) {
						ShelfItem shelfItem = new ShelfItem();
						shelfItem.setName(name);
						shelfItem.setLocalCollectionBaseDirectory(new File(collection.getLocalDirectory()));
						shelfItem.setRemoteCollectionBaseDirectory(new File(collection.getRemoteDirectory()));
						shelf.add(shelfItem);
					}
					ShelfItem shelfItem = shelf.get(name);
					
					
					shelfItem.setRemoteBaseDirectory(baseDir);
					shelf.add(shelfItem);
					
					if(new File(baseDir, "../thumbnail.png").exists()) {
						File parentBaseDir = baseDir.getParentFile();
						String parentName = FilenameUtils.getBaseName(parentBaseDir.getName());
						if (!shelf.containsKey(parentName)) {
							ShelfItem parentShelfItem = new ShelfItem();
							parentShelfItem.setName(parentName);
							parentShelfItem.setLocalCollectionBaseDirectory(new File(collection.getLocalDirectory()));
							parentShelfItem.setRemoteCollectionBaseDirectory(new File(collection.getRemoteDirectory()));
							shelf.add(parentShelfItem);
						}

						ShelfItem parentShelfItem = shelf.get(parentName);
						parentShelfItem.setRemoteBaseDirectory(parentBaseDir);
					}
				}
			}
		}
	}

	private void getCollectionsFromDisk(Library result) {
		for (File file : new File(".").listFiles()) {
			if (file.getName().endsWith("yaml")) {
				ObjectMapper mapper = new YAMLMapper();
				try {
					CollectionApiModel apiCollection = mapper.readValue(file, CollectionApiModel.class);
					result.getCollections().add(apiCollection);
					
					ItemCatalog ic = new ItemCatalog();
					ic.setName(apiCollection.getName());
					
					for(CategoryApiModel cat : apiCollection.getCategories()) {
						for(ItemApiModel item : ObjectUtils.firstNonNull(cat.getItems(), new ArrayList<ItemApiModel>())) {
							ic.put(item.getName(), item);
							
							for(ItemApiModel child : ObjectUtils.firstNonNull(item.getChildItems(), new ArrayList<ItemApiModel>())) {
								ic.put(child.getName(), child);
							}
						}
						
					}
					result.getItemCatalogs().add(ic);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}