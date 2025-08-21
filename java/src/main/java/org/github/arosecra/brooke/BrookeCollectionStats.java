package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.jobs.BrookeJobStep.JobFolder;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.api.CategoryApiModel;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.model.api.ItemApiModel;
import org.github.arosecra.brooke.services.LibraryLocationService;
import org.github.arosecra.brooke.services.PipelineService;

public class BrookeCollectionStats {

	public static void main(String[] args) throws IOException {
		PipelineService service = new PipelineService();
		Settings settings = new Settings();
		LibraryDao libraryDao = new LibraryDao();
		libraryDao.setSettings(settings);
		// service.setLibraryDao(libraryDao);

		File workDirectory = new File("D://scans//tobeexported");

		Map<String, List<JobFolder>> foldersToProcess = new TreeMap<>();
		Map<String, CollectionApiModel> collections = new HashMap<>();
		
		LibraryLocationService lls = new LibraryLocationService();
		Library library = libraryDao.getLibrary(true);
		
//		service.selectWork(libraryDao.getLibrary(false), foldersToProcess, collections);
//		service.printWorkStatus(foldersToProcess);
//		service.executePipelines(workDirectory, foldersToProcess, collections);
		
		Map<String, Long> categorySizes = new HashMap<String, Long>();
		long total = 0L;
		
		for(CollectionApiModel collection : library.getCollections()) {
			if(collection.getItemExtension().equals("cbt")) {
				
				for(CategoryApiModel category : collection.getCategories()) {
					
					for(ItemApiModel item : category.getItems()) {
						File remoteFile = lls.getRemoteFile(library, collection.getName(), item.getName());
						File remoteFileFolder = remoteFile.getParentFile();
						for(File childFile : remoteFileFolder.listFiles()) {
								if(!categorySizes.containsKey(collection.getName())) {
									categorySizes.put(collection.getName(), 0L);
								}
								
								System.out.println(
									collection.getName() + " - " + 
									category.getName() + " - " + 
									childFile.getName() + " - "  + 
									getStringSizeLengthFile(childFile.length())
								);
								
								categorySizes.put(collection.getName(), categorySizes.get(collection.getName()) + childFile.length());
								total += childFile.length();
							}
					}
				}
			}
		}
		
		for(Map.Entry<String, Long> entry : categorySizes.entrySet()) {
			System.out.println(entry.getKey() + " - " + getStringSizeLengthFile(entry.getValue()));
		}
		System.out.println("Total - " + getStringSizeLengthFile(total));
	}
	
	public static String getStringSizeLengthFile(long size) {

	    DecimalFormat df = new DecimalFormat("0.00");

	    float sizeKb = 1024.0f;
	    float sizeMb = sizeKb * sizeKb;
	    float sizeGb = sizeMb * sizeKb;
	    float sizeTerra = sizeGb * sizeKb;


	    if(size < sizeMb)
	        return df.format(size / sizeKb)+ " Kb";
	    else if(size < sizeGb)
	        return df.format(size / sizeMb) + " Mb";
	    else if(size < sizeTerra)
	        return df.format(size / sizeGb) + " Gb";

	    return "";
	}
}