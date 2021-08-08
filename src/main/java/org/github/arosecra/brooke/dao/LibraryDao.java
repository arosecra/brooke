package org.github.arosecra.brooke.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.model.Catalog;
import org.github.arosecra.brooke.model.Category;
import org.github.arosecra.brooke.model.Collection;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.ShelfItem;
import org.github.arosecra.brooke.util.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LibraryDao {
	
	@Autowired
	private Settings settings;

	public Library getLibrary() {
		Library result = new Library();
		
		for(File collection : new File(settings.getLibraryHome(), "Collections").listFiles()) {
			
			if(collection.isDirectory()) {
				result.getCollections().add(getCollection(collection));
			}
		}
		
		return result;
	}

	private Collection getCollection(File collectionFolder) {
		Collection result = new Collection();
		
		File propertiesFile = new File(collectionFolder.getParentFile(), collectionFolder.getName() + ".properties");
		Properties props = new Properties();
		Try.loadProperties(props, propertiesFile);
		result.setName(collectionFolder.getName());
		result.setType(collectionFolder.getName());
		result.setLocalDirectory(props.getProperty("localDirectory"));
		result.setRemoteCollection(props.getProperty("remoteCollection"));
		result.setRemoteDirectory(props.getProperty("remoteDirectory"));
		result.setItemExtension(props.getProperty("itemExtension"));
		result.setOpenType(props.getProperty("openType"));
		
		//now we need to get the catalogs, if they exist. there's a couple patterns here, so let's figure out which one it is
		boolean hasSubCategories = false;
		if(collectionFolder.listFiles() != null) {
			for(File childFile : collectionFolder.listFiles()) {
				if(childFile.isDirectory())
					hasSubCategories = true;
			}
		}
		
		if(hasSubCategories) {
			for(File catalogFolder : collectionFolder.listFiles()) {
				if(catalogFolder.isDirectory()) {
					Catalog cat = new Catalog();
					cat.setName(catalogFolder.getName());
					getCategories(result, cat, catalogFolder);
					result.getCatalogs().add(cat);
				}
			}
		} else {
			//we have a default catalog with no name and multiple categories
			Catalog def = new Catalog();
			def.setName("def");
			if(collectionFolder.listFiles() != null) {
				getCategories(result, def, collectionFolder);
			}
			result.getCatalogs().add(def);
		}

		return result;
	}

	private void getCategories(Collection result, Catalog catalog, File catalogFolder) {
		for(File childFile : catalogFolder.listFiles()) {
			catalog.getCategories().add(getCategory(result, childFile));
		}
	}

	private Category getCategory(Collection collection, File childFile) {
		Category result = new Category();
		result.setName(FilenameUtils.getBaseName(childFile.getName()));
		List<String> lines = Try.readLines(childFile);
		
		if(lines.size() == 1 && isSeries(collection, lines.get(0).trim())) {
			//expand the series into multiple items
			String series = lines.get(0);
			result.getItems().addAll(getSeriesShelfItems(collection, series));
			
		} else {
			for(String line : lines) {
				ShelfItem si = new ShelfItem();
				si.setName(line.trim());
				si.setLocal(isShelfItemLocal(collection, si.getName()));
				result.getItems().add(si);
			}
		}		
		
		return result;
	}

	private List<ShelfItem> getSeriesShelfItems(Collection collection, String series) {
		List<ShelfItem> result = new ArrayList<>();
		File localRepositoryHome = new File(collection.getLocalDirectory());
		File charDirectory = new File(localRepositoryHome, series.charAt(0)+"");
		File seriesDirectory = new File(charDirectory, series);
		
		if(seriesDirectory.listFiles() != null) {
			for(File file : seriesDirectory.listFiles()) {
				if(file.isDirectory()) {
					ShelfItem si = new ShelfItem();
					si.setName(FilenameUtils.getBaseName(file.getName()));
					si.setSeriesName(series);
					si.setLocal(isShelfItemLocal(collection, file));
					result.add(si);
				}
			}
		}
		
		return result;
	}

	private boolean isSeries(Collection collection, String item) {
		File localRepositoryHome = new File(collection.getLocalDirectory());
		File charDirectory = new File(localRepositoryHome, item.charAt(0)+"");
		File itemDirectory = new File(charDirectory, item);
		
		boolean hasDirectories = false;
		if(itemDirectory.listFiles() != null) {
			for(File file : itemDirectory.listFiles()) {
				if(file.isDirectory()) hasDirectories = true;
			}
		}
		
		return hasDirectories;
	}

	private boolean isShelfItemLocal(Collection collection, String item) {
		File localRepositoryHome = new File(collection.getLocalDirectory());
		File charDirectory = new File(localRepositoryHome, item.charAt(0)+"");
		File itemDirectory = new File(charDirectory, item);
		return isShelfItemLocal(collection, itemDirectory);
	}
	
	private boolean isShelfItemLocal(Collection collection, File itemFolder) {
		File itemFile = new File(itemFolder, itemFolder.getName() + "." + collection.getItemExtension());
		return itemFile.exists();
	}
}