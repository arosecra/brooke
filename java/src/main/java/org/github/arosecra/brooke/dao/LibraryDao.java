package org.github.arosecra.brooke.dao;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.model.ItemCatalog;
import org.github.arosecra.brooke.model.ItemLocation;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.Shelf;
import org.github.arosecra.brooke.model.ShelfItem;
import org.github.arosecra.brooke.model.api.CategoryApiModel;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.model.api.ItemApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

@Repository
public class LibraryDao {

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
		if (includeRemote) {
			getShelfItemsFromRemote(result);
		}

		getItemLocations(result);
		return result;
	}

	private void getItemLocations(Library result) {
		for (CollectionApiModel collection : result.getCollections()) {

			ItemCatalog catalog = new ItemCatalog();
			catalog.setName(collection.getName());
			for (CategoryApiModel category : collection.getCategories()) {
				for (ItemApiModel item : category.getItems()) {
					ItemLocation itemLocation = new ItemLocation();
					itemLocation.setCollectionName(collection.getName());
					itemLocation.setCategoryName(category.getName());
					itemLocation.setItemName(item.getName());
					catalog.put(item.getName(), itemLocation);

					if (item.isSeries()) {
						for (ItemApiModel child : item.getChildItems()) {
							ItemLocation childLocation = new ItemLocation();
							childLocation.setCollectionName(collection.getName());
							childLocation.setCategoryName(category.getName());
							childLocation.setSeriesName(item.getName());
							childLocation.setItemName(child.getName());
							catalog.put(child.getName(), childLocation);
						}
					}
				}
			}

			result.getItemCatalogs().add(catalog);
		}
	}

	private void getShelfItemsFromLocal(Library result) {
		for (CollectionApiModel collection : result.getCollections()) {

			Shelf shelf = new Shelf();
			shelf.setName(collection.getName());

			File localDir = new File(collection.getLocalDirectory());
			if (localDir.exists()) {

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

						// check if the parent directory has a thumbnail.png file
						if (new File(baseDir, "../thumbnail.png").exists()) {
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
			}
			result.getShelves().add(shelf);
		}
	}

	private void getShelfItemsFromRemote(Library result) {
		for (CollectionApiModel collection : result.getCollections()) {

			Shelf shelf = null;
			for (Shelf s : result.getShelves()) {
				if (s.getName().equals(collection.getName())) {
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

					if (new File(baseDir, "../thumbnail.png").exists()) {
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
		for (File file : new File(settings.getLibraryHome(), "Collections").listFiles()) {
			if (file.getName().endsWith("yaml")) {
				ObjectMapper mapper = new YAMLMapper();
				try {
					CollectionApiModel apiCollection = mapper.readValue(file, CollectionApiModel.class);
					result.getCollections().add(apiCollection);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}