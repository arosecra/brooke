package org.github.arosecra.brooke.services;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.dao.JobService;
import org.github.arosecra.brooke.dao.LibraryDao2;
import org.github.arosecra.brooke.model.api.BookDetailsApiModel;
import org.github.arosecra.brooke.model.api.CategoryApiModel;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.model.api.ItemApiModel;
import org.github.arosecra.brooke.model.api.VlcOptionsApiModel;
import org.github.arosecra.brooke.model2.JobDetails;
import org.github.arosecra.brooke.model2.Library;
import org.github.arosecra.brooke.model2.Shelf;
import org.github.arosecra.brooke.model2.ShelfItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

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
	
	@Autowired
	private TarService tarService;
	
	@Autowired
	private FileCacheService fileCacheService;
	
	@Autowired
	private JobService jobDao;
	
	private LibraryDao2 libraryDao2;
	
	private Library library;

	@Autowired
	public void setLibraryDao2(LibraryDao2 libraryDao) { this.libraryDao2 = libraryDao; }
	
	@PostConstruct()
	public void init() {
		library = libraryDao2.getLibrary(false);
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
	
	public JobDetails cacheItem(String collectionName, String itemName) throws IOException {
		File remoteFile = null;
		CollectionApiModel collection = this.getCollection(collectionName);
		
		Shelf shelf = this.getShelf(collectionName.replaceAll(" ", "_"));
		if(shelf != null) {
			ShelfItem shelfItem = shelf.get(itemName.replaceAll(" ", "_"));
			if(shelfItem != null) {
				remoteFile = new File(shelfItem.getRemoteBaseDirectory(), shelfItem.getName() + "." + collection.getItemExtension());
			}
		}
		
		return fileCacheService.cacheRemoteFile(remoteFile);
	}

	public byte[] getPage(String collectionName, String itemName, int pageNumber) throws IOException {
		File tar = getCachedFile(collectionName, itemName);
		return tarService.getPageFromTar(tar, pageNumber);
	}
	
	private File getCachedFile(String collectionName, String itemName) {
		File cacheFolder = new File("D:\\Library\\Cache");
		CollectionApiModel collection = this.getCollection(collectionName);
		
		
		File remoteFile = null;
		Shelf shelf = this.getShelf(collectionName.replaceAll(" ", "_"));
		if(shelf != null) {
			ShelfItem shelfItem = shelf.get(itemName.replaceAll(" ", "_"));
			if(shelfItem != null) {
				remoteFile = new File(shelfItem.getRemoteBaseDirectory(), shelfItem.getName() + "." + collection.getItemExtension());
			}
		}
		
		
		File cacheFile = new File(cacheFolder, remoteFile.getName());
		return cacheFile;
	}

	public BookDetailsApiModel getBookDetails(String collectionName, String itemName) throws JsonParseException, JsonMappingException, IOException {
		
		File cbtDetailsFile = null;
		
		Shelf shelf = this.getShelf(collectionName.replaceAll(" ", "_"));
		if(shelf != null) {
			ShelfItem shelfItem = shelf.get(itemName.replaceAll(" ", "_"));
			if(shelfItem != null) {
				cbtDetailsFile = new File(shelfItem.getLocalBaseDirectory(), "cbtDetails.yaml");
			}
		}

		ObjectMapper mapper = new YAMLMapper();
		BookDetailsApiModel result = mapper.readValue(cbtDetailsFile, BookDetailsApiModel.class);
		
		return result;
	}

	public JobDetails getJobDetails(long jobNumber) {
		return jobDao.getJobDetails(jobNumber);
	}

	public void openVLC(String collectionName, String itemName) throws IOException {


		File cacheFolder = new File("D:\\Library\\Cache");
		CollectionApiModel collection = this.getCollection(collectionName);
		
		
		File remoteFile = null;
		Shelf shelf = this.getShelf(collectionName.replaceAll(" ", "_"));
		if(shelf != null) {
			ShelfItem shelfItem = shelf.get(itemName.replaceAll(" ", "_"));
			if(shelfItem != null) {
				remoteFile = new File(shelfItem.getRemoteBaseDirectory(), shelfItem.getName() + "." + collection.getItemExtension());
			}
		}
		
		
		File cacheFile = new File(cacheFolder, remoteFile.getName());
		
		VlcOptionsApiModel vlcOptions = null;
		//TODO - look up the location based off of the collection and item, then
		//       get the vlc options if they are in the child item or series item (if applicable)
		
		
		
		
		String vlcCacheFilename = "file:///" + cacheFile.getAbsolutePath();
		
//		File vlcOptionsFile = new File(shelfItem.getFolder(), "vlcOptions.txt");
//		if(!shelfItem.getChildItems().isEmpty()) {
//			vlcOptionsFile = new File(shelfItem.getChildItems().get(index).getFolder(), "vlcOptions.txt");
//		}
		
		List<String> vlcOptionArgs = new ArrayList<>();
		vlcOptionArgs.add("cmd.exe");
		vlcOptionArgs.add("/C");
		vlcOptionArgs.add("\"C:\\Program Files\\VideoLAN\\VLC\\vlc.exe\"");
		vlcOptionArgs.add(vlcCacheFilename);
		
//		if(vlcOptionsFile.exists()) {
//			List<String> lines = FileUtils.readLines(vlcOptionsFile);
//			vlcOptions.addAll(lines);
//		}
		
		//:sub-track-id=
		//:audio-track-id=
//		for(String s : vlcOptionArgs)
//			System.out.println(s);
		
		ProcessBuilder builder = new ProcessBuilder((String[]) vlcOptionArgs.toArray(new String[vlcOptionArgs.size()]));
		builder.redirectErrorStream(true);
		final Process process = builder.start();

		// Watch the process
		watch(process);
	}
	
	private static void watch(final Process process) {
	    new Thread() {
	        public void run() {
	            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            String line = null; 
	            try {
	                while ((line = input.readLine()) != null) {
	                    System.out.println(line);
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }.start();
	}

}