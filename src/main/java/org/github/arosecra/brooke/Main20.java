package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.model.Catalog;
import org.github.arosecra.brooke.model.Category;
import org.github.arosecra.brooke.model.Collection;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.ShelfItem;

public class Main20 {
	static final String categoryNameFormat   = "  - name: %s";
	static final String categoryItemsFormat  = "    items:";
	static final String shelfItemNameFormat  = "      - name: %s";
	static final String shelfItemSeriesFormat= "        series: true";
	static final String shelfItemCIsFormat   = "        childItems:";
	static final String vlcOptionsFormat     = "        vlcOptions:";
	static final String cIVlcOptionsSubFormat= "          subtitleTrack: %s";
	static final String cIVlcOptionsAudFormat= "          audioTrack: %s";
	static final String childItemNameFormat  = "          - name: %s";
	
	public static void main(String[] args) throws IOException {
		
		
		Settings settings = new Settings();
		
		LibraryDao libraryDao = new LibraryDao();
		libraryDao.setSettings(settings);
		
		Library library = libraryDao.getLibrary();
		
		for(Collection collection : library.getCollections()) {
			List<String> lines = new ArrayList<>();
			
			lines.add("name: " + collection.getName());
			lines.add("remoteDirectory: " + collection.getRemoteDirectory());
			lines.add("localDirectory: " + collection.getLocalDirectory());
			lines.add("itemExtension: " + collection.getItemExtension());
			lines.add("excludeExtensions: ");
			for(String ext : collection.getExcludeExtensions().split(","))
				lines.add("  - " + ext);
			lines.add("openType: " + collection.getOpenType());
			lines.add("pipelineSteps: ");
			for(String step : collection.getPipeline()) 
				lines.add("  - " + step);
			
			
//			if(collection.getName().startsWith("Research")) {
				lines.add("categories:");
				
				for(Catalog catalog : collection.getCatalogs()) {
//					lines.add("  -name: " + cat.getName());
					
					for(Category cat : catalog.getCategories()) {
						lines.add(String.format(categoryNameFormat, cat.getName()));
						lines.add(categoryItemsFormat);
						
						for(ShelfItem si : cat.getItems()) {
							lines.add(String.format(shelfItemNameFormat, si.getName()));
							
							

							checkAndGetVlcOptions(si, lines);
							
							if(!si.getChildItems().isEmpty()) {
								//check if there are vlc options in the first item
								
								checkAndGetVlcOptions(si.getChildItems().get(0), lines);

								lines.add(shelfItemSeriesFormat);
								lines.add(shelfItemCIsFormat);
								for(ShelfItem ci : si.getChildItems()) {
									lines.add(String.format(childItemNameFormat, ci.getName()));
								}
							}
						}
					}
				}
//			}
			FileUtils.writeLines(new File(collection.getName().toLowerCase()+".yaml"), lines);
			
		}

	}

	private static void checkAndGetVlcOptions(ShelfItem shelfItem, List<String> lines) throws IOException {
		// TODO Auto-generated method stub
		File vlcOptionsFile = new File(shelfItem.getFolder(), "vlcOptions.txt");
		
		if(vlcOptionsFile.exists()) {
			List<String> vlcOptions = FileUtils.readLines(vlcOptionsFile);
			
			lines.add(vlcOptionsFormat);
			for(String vlcOption : vlcOptions) {
				String value = vlcOption.substring(vlcOption.indexOf('=')+1);
				if(vlcOption.startsWith(":sub"))
					lines.add(String.format(cIVlcOptionsSubFormat,value));
				else
					lines.add(String.format(cIVlcOptionsAudFormat,value));
					
			}
		}
	}
}
