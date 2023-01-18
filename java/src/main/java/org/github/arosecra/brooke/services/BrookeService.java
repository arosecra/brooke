package org.github.arosecra.brooke.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.dao.JobService;
import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.model.ItemLocation;
import org.github.arosecra.brooke.model.JobDetails;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.Shelf;
import org.github.arosecra.brooke.model.ShelfItem;
import org.github.arosecra.brooke.model.api.BookDetailsApiModel;
import org.github.arosecra.brooke.model.api.CategoryApiModel;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.model.api.ItemApiModel;
import org.github.arosecra.brooke.model.api.VlcOptionsApiModel;
import org.github.arosecra.brooke.util.CommandLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

@Service
public class BrookeService {
	
	@Autowired
	private Settings settings;
	
	@Autowired
	private TarService tarService;
	
	@Autowired
	private FileCacheService fileCacheService;
	
	@Autowired
	private JobService jobDao;

	@Autowired
	private LibraryLocationService libraryLocationService;
	
	private LibraryDao libraryDao2;
	
	private Library library;

	@Autowired
	public void setLibraryDao2(LibraryDao libraryDao) { this.libraryDao2 = libraryDao; }
	
	@PostConstruct()
	public void init() {
		library = libraryDao2.getLibrary(false);
	}
	

	public Library getLibrary() {
		return this.library;
	}

	public CollectionApiModel getCollection(String collectionName) {
		return libraryLocationService.getCollection(library, collectionName);
	}

	public CategoryApiModel getCategory(String collectionName, String categoryName) {
		return this.libraryLocationService.getCategory(library, collectionName, categoryName);
	}

	public ItemApiModel getItem(String collectionName, String itemName) {
		return this.libraryLocationService.getItem(library, collectionName, itemName);
	}
	
	public Shelf getShelf(String collectionName) {
		return this.libraryLocationService.getShelf(library, collectionName);
	}

	public ItemApiModel getSeries(String collectionName, String categoryName, String seriesName) {
		return this.libraryLocationService.getSeries(library, collectionName, categoryName, seriesName);
	}
	
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
			return null;
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
			return null;
		} else {
			return FileUtils.readFileToByteArray(thumbnailFile);
		}
	}

	public byte[] getPage(String collectionName, String itemName, int pageNumber) throws IOException {
		File tar = getCachedFile(collectionName, itemName);
		return tarService.getPageFromTar(tar, pageNumber);
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
	
	private File getCachedFile(String collectionName, String itemName) {
		File cacheFolder = new File("D:\\Library\\Cache");
		
		File remoteFile = this.libraryLocationService.getRemoteFile(library, collectionName, itemName);
		
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
		File cacheFile = getCachedFile(collectionName, itemName);
		
		VlcOptionsApiModel vlcOptions = null;
		ItemLocation itemLocation = this.libraryLocationService.getItemLocation(library, collectionName, itemName);
		ItemApiModel item = this.libraryLocationService.getItemByLocation(library, itemLocation);

		if(item.getVlcOptions() != null) {
			vlcOptions = item.getVlcOptions();
		} else if(itemLocation.getSeriesName() != null) {
			ItemApiModel series = this.libraryLocationService.getSeriesByLocation(library, itemLocation);
			if(series.getVlcOptions() != null) {
				vlcOptions = series.getVlcOptions();
			}
		}
		
		String vlcCacheFilename = "file:///" + cacheFile.getAbsolutePath();
		
		List<String> vlcOptionArgs = new ArrayList<>();
		vlcOptionArgs.add("cmd.exe");
		vlcOptionArgs.add("/C");
		vlcOptionArgs.add("\"C:\\Program Files\\VideoLAN\\VLC\\vlc.exe\"");
		vlcOptionArgs.add(vlcCacheFilename);

		if(vlcOptions != null) {
			if(vlcOptions.getAudioTrack() > 0)
				vlcOptionArgs.add(":audio-track-id="+vlcOptions.getAudioTrack());
			if(vlcOptions.getSubtitleTrack() > 0)
				vlcOptionArgs.add(":sub-track-id="+vlcOptions.getSubtitleTrack());
		}
		
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

	public void setLibraryDao(LibraryDao libraryDao) {
		this.libraryDao2 = libraryDao;
	}


	
	public void copyForTablet(String collectionName, String catalogName, String categoryName, String itemName) throws IOException {
		File tempSsdFolder = new File("C:\\scans\\temp");
		File unzippedFolder = new File(tempSsdFolder, itemName);
		unzippedFolder.mkdirs();
		
		ShelfItem shelfItem = this.libraryLocationService.getShelfItem(library, collectionName, itemName);
		File remoteFile = new File(shelfItem.getRemoteBaseDirectory(), itemName + "_PNG.tar");
		
		FileUtils.copyFileToDirectory(remoteFile, tempSsdFolder);
		File localSourceFile = new File(tempSsdFolder, remoteFile.getName());
		File localCbzFile = new File(tempSsdFolder.getAbsolutePath(), itemName + ".cbz");
		
		CommandLine.run(new String[] {
				"D:\\software\\7za\\7za.exe", 
				"e", 
				"-o" + unzippedFolder.getAbsolutePath(),
				localSourceFile.getAbsolutePath()	
		});
		
		
		CommandLine.run(new String[] {
				"D:\\software\\7za\\7za.exe", 
				"a", 
				"-tzip", 
				"-o" + unzippedFolder.getAbsolutePath(),
				localCbzFile.getAbsolutePath(), 
				unzippedFolder.getAbsolutePath() + "\\*.png"		
			});
		
		FileUtils.copyFileToDirectory(localCbzFile, new File("\\\\drobo5n\\Public\\Scans\\ForTablet"));
		FileUtils.deleteDirectory(unzippedFolder);
		FileUtils.delete(localSourceFile);
		FileUtils.delete(localCbzFile);
		System.out.println("Done copying new CBZ to tablet sync directory");
	}
	
	// public void createBoundingBoxPng() {
	// 	BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
	// 	g = image.createGraphics();  // not sure on this line, but this seems more right
	// 	g.setColor(Color.white);
	// 	g.fillRect(0, 0, 100, 100); // give the whole image a white background
	// 	g.setColor(Color.blue);
	// 	for( ..... ){
	// 	    g.fillRect(X , Y,  width , height );
	// 	        ....        
	// 	}
	// }

}