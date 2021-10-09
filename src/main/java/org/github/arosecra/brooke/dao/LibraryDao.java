package org.github.arosecra.brooke.dao;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
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
	
	private Settings settings;

	@Autowired
	public void setSettings(Settings settings) { this.settings = settings; }

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
		result.setExcludeExtensions(props.getProperty("excludeExtensions"));
		result.setPipeline(props.getProperty("pipeline"));
		if(props.containsKey("autoGenerateAlphaCategories"))
			result.setAutoGenerateAlphaCategories(Boolean.parseBoolean(props.getProperty("autoGenerateAlphaCategories")));
		
		if(new File(result.getLocalDirectory()).exists())
			result.setLocalFiles(FileUtils.listFiles(new File(result.getLocalDirectory()), null, true));
//		result.setRemoteFiles(FileUtils.listFiles(new File(result.getRemoteDirectory()), null, true));
		result.setShelfItems(getShelfItems(result));
		
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
			if(result.isAutoGenerateAlphaCategories()) {
				for(File folder : Try.listFilesSafe(new File(result.getLocalDirectory()))) {
					Category cat = new Category();
					cat.setName(folder.getName());
					def.getCategories().add(cat);
					for(File file : Try.listFilesSafe(folder)) {
						ShelfItem si = result.getShelfItems().get(file.getName());
						if(si != null) {
							cat.getItems().add(si);
							si.addCategory(cat);
						} else
							System.out.println(result.getName() + " could not find " + file.getName());
					}
				}
			} else {
				if(collectionFolder.listFiles() != null) {
					getCategories(result, def, collectionFolder);
				}
			}
			result.getCatalogs().add(def);
		}
		
		
		for(ShelfItem item : result.getShelfItems().values()) {
			if(item.getCateogries().isEmpty())
				System.out.println(result.getName() + " did not have " + item.getName() + " in a category");
		}

		return result;
	}

	private Map<String, ShelfItem> getShelfItems(Collection result) {
		Map<String, ShelfItem> shelfItems = new TreeMap<>();
		
		for(File file : result.getLocalFiles()) {
			if(file.getName().endsWith(".item")) {
				//if there's a thumbnail here, it's a shelf item
				//if there's a thumbnail in the parent, it's a series shelf item
				File thumbnail = new File(file.getParentFile(), "thumbnail.png");
				File parentThumbnail = new File(file.getParentFile().getParentFile(), "thumbnail.png");
				if(!parentThumbnail.exists()) {
					ShelfItem si = new ShelfItem();
					si.setName(file.getParentFile().getName());
					si.setLocal(new File(file.getParentFile()+"."+result.getItemExtension()).exists());
					si.setFolder(file.getParentFile());
					si.setThumbnail(thumbnail);
					shelfItems.put(si.getName(), si);
				} else if(parentThumbnail.exists()) {
					ShelfItem series = new ShelfItem();
					series.setName(file.getParentFile().getParentFile().getName());
					series.setSeriesName(file.getParentFile().getParentFile().getName());
					if(!shelfItems.containsKey(series.getName())) {
						series.setFolder(file.getParentFile().getParentFile());
						shelfItems.put(series.getSeriesName(), series);
					}
					series.setThumbnail(parentThumbnail);
					
					ShelfItem si = new ShelfItem();
					si.setName(file.getParentFile().getName());
					si.setLocal(new File(file.getParentFile()+"."+result.getItemExtension()).exists());
					si.setThumbnail(parentThumbnail);
					si.setFolder(file.getParentFile());
					shelfItems.get(series.getSeriesName()).addChildItem(si);
				}
				
			}
		}
		
		return shelfItems;
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

		for(String line : lines) {
			ShelfItem si = collection.getShelfItems().get(line);
			if(si != null) {
				result.getItems().add(si);
				si.addCategory(result);
				for(ShelfItem child : si.getChildItems())
					child.addCategory(result);
			} else
				System.out.println(collection.getName() + " could not find " + line);
		}
		
		return result;
	}
	
	private boolean isShelfItemLocal(Collection collection, File itemFolder) {
		File itemFile = new File(itemFolder, itemFolder.getName() + "." + collection.getItemExtension());
		return itemFile.exists();
	}
}