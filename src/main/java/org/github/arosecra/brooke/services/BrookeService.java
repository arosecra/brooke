package org.github.arosecra.brooke.services;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.util.FileUtil;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.model.Button;
import org.github.arosecra.brooke.model.ButtonSet;
import org.github.arosecra.brooke.model.Catalog;
import org.github.arosecra.brooke.model.Category;
import org.github.arosecra.brooke.model.Collection;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.ShelfItem;
import org.github.arosecra.brooke.model.ToCEntry;
import org.github.arosecra.brooke.util.CommandLine;
import org.github.arosecra.brooke.util.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrookeService {
	
	private static byte[] DEFAULT_THUMBNAIL;
	static {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(BrookeService.class.getResourceAsStream("/static/images/default_thumbnail.png"), baos);
			DEFAULT_THUMBNAIL = baos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Autowired
	private Settings settings;
	
	private LibraryDao libraryDao;
	
	private Library library;
	

	@Autowired
	public void setLibraryDao(LibraryDao libraryDao) { this.libraryDao = libraryDao; }
	
	
	@PostConstruct()
	public void init() {
		library = libraryDao.getLibrary();
	}
	
	public Library getLibrary() {
		return library;
	}

	public ButtonSet getStandardButtons() {
		ButtonSet result = new ButtonSet();
		result.addButton(new Button("Home", "/", null));
		for(Collection collection : library.getCollections()) {
			result.addButton(new Button(collection.getName(), "/collection/" + collection.getName(), null));
		}
		result.addButton(new Button("Sync", "/sync", null));
		result.addButton(new Button("Manage", "/manage", null));
		result.addButton(new Button("Reload Library", "/reload", null));
		return result;
	}

	public Collection getCollectionByName(String collectionName) {
		Collection collection = new Collection();
		for(Collection coll : library.getCollections()) {
			if(coll.getName().equals(collectionName))
				collection = coll;
		}
		return collection;
	}

	public Catalog getCatalogByName(String collectionName, String catalogName) {
		return getCatalogByName(getCollectionByName(collectionName), catalogName);
	}

	private Catalog getCatalogByName(Collection collectionByName, String catalogName) {
		Catalog catalog = new Catalog();
		for(Catalog cat : collectionByName.getCatalogs())
			if(cat.getName().equals(catalogName))
				catalog = cat;
		return catalog;
	}

	public Category getCategoryByName(String collectionName, String catalogName, String categoryName) {
		return getCategoryByName(getCollectionByName(collectionName), getCatalogByName(collectionName, catalogName), categoryName);
	}

	private Category getCategoryByName(Collection collectionByName, Catalog catalogByName, String categoryName) {
		Category result = new Category();
		for(Category cat : catalogByName.getCategories()) 
			if(cat.getName().equals(categoryName))
				result = cat;
		return result;
	}

	public byte[] getThumbnail(String collectionName, String categoryName, String itemName) throws IOException {
		//category may be the series, or not needed
		
		Collection collection = getCollectionByName(collectionName);
		File thumbnailFile = collection.getShelfItems().get(itemName).getThumbnail();
		
		if(!thumbnailFile.exists()) {
			return DEFAULT_THUMBNAIL;
		} else {
			return FileUtils.readFileToByteArray(thumbnailFile);
		}
	}

	public byte[] getLargeThumbnail(String collectionName, String catalogName, String categoryName, String itemName, int index) throws IOException {
		ShelfItem shelfItem = this.getItemByName(collectionName, catalogName, categoryName, itemName);
		
		File thumbnailFile = new File(shelfItem.getFolder(), "large_thumbnail.png");
		
		if(!thumbnailFile.exists()) {
			return DEFAULT_THUMBNAIL;
		} else {
			return FileUtils.readFileToByteArray(thumbnailFile);
		}
	}

	public byte[] getPage(String collectionName, String categoryName, String itemName, int pageNumber) throws IOException {
		File tar = getCachedFile(collectionName, collectionName, categoryName, itemName, 0);
		return getPageFromTar(tar, pageNumber);
	}
	
	private byte[] getPageFromTar(File file, int page) throws IOException {
		int currentPage = 0;
		byte[] result = new byte[0];
		try (TarArchiveInputStream tarIn = new TarArchiveInputStream(
				new BufferedInputStream(new FileInputStream(file)))) {
			TarArchiveEntry entry;
			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
				if (page == currentPage) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(tarIn, baos);
					result = baos.toByteArray();
					System.out.println("Loading page " + currentPage + " from tarball");
				}
				currentPage++;
			}
		}
		return result;
	}

	public ShelfItem getItemByName(String collectionName, String catalogName, String categoryName, String itemName) {
		Category category = getCategoryByName(collectionName, catalogName, categoryName);
		ShelfItem result = new ShelfItem();
		for(ShelfItem si : category.getItems())
			if(si.getName().equals(itemName))
				result = si;
		return result;
	}

	public List<ToCEntry> getToCEntries(String collectionName, String catalogName, String categoryName, String itemName) throws IOException {
		List<ToCEntry> results = new ArrayList<>();
		Collection collection = getCollectionByName(collectionName);
		File itemDirectory = collection.getShelfItems().get(itemName).getFolder();
		
		File tocFile = new File(itemDirectory, "toc.txt");
		if(tocFile.exists()) {
			List<String> lines = FileUtils.readLines(tocFile, StandardCharsets.UTF_8);
			for(String line : lines) {
				if(!StringUtils.isEmpty(line.trim())) {
					String[] parts=line.split("=");
					ToCEntry entry = new ToCEntry();
					entry.setPageIndex(Integer.parseInt(parts[0]));
					entry.setName(parts[1]);
					results.add(entry);
				}
			}
		}
		return results;
	}

	public File getRemoteFile(String collectionName, String catalogName, String categoryName, String itemName, int index) {
		Collection collection = getCollectionByName(collectionName);

		File itemDirectory = getRemoteItemFolder(collection, categoryName, itemName, index);
		return new File(itemDirectory, itemDirectory.getName() + "." + collection.getItemExtension());
	}

	private File getRemoteItemFolder(Collection collection, String categoryName, String itemName, int index) {
		ShelfItem item = collection.getShelfItems().get(itemName);
		File folder = item.getFolder();
		if(!item.getChildItems().isEmpty()) {
			folder = item.getChildItems().get(index).getFolder();
		}
		
		String relativePath = folder.getAbsolutePath().substring(collection.getLocalDirectory().length());
		File itemDirectory = new File(collection.getRemoteDirectory(), relativePath);
		return itemDirectory;
	}

	public void addToc(String collectionName, String categoryName, String itemName, int pageNumber, String name) throws IOException {

		Collection collection = getCollectionByName(collectionName);
		File itemDirectory = collection.getShelfItems().get(itemName).getFolder();
		
		File tocFile = new File(itemDirectory, "toc.txt");
		
		FileUtils.write(tocFile, pageNumber+"="+name, true);
	}

	public void sync() throws IOException {
		for(Collection collection : library.getCollections()) {
			File local = new File(collection.getLocalDirectory());
			File remote = new File(collection.getRemoteDirectory());
			
//			System.out.println("Synchronizing " + collection.getName());
			
			//for each remote file, compute the local spot for it
			//  create a .item file there if the file doesn't exist
			//  copy all other files
			
			for(File file : FileUtils.listFiles(remote, new String[] {collection.getItemExtension()}, true)) {
				
				String relativePath = file.getAbsolutePath().substring(remote.getAbsolutePath().length());
//				if(collection.getName().contains("Movies"))
//					System.out.println(relativePath);
				File localFile = new File(local, relativePath);
				if(!localFile.exists()) {
					localFile.getParentFile().mkdirs();
					String basename = FilenameUtils.getBaseName(localFile.getName());
					File placeholder = new File(localFile.getParentFile(), basename+".item");
					placeholder.createNewFile();
				}
				
				for(File sibling : file.getParentFile().listFiles()) {
					String ext = FilenameUtils.getExtension(sibling.getName());
					File localSibling = new File(localFile.getParentFile(), sibling.getName());
					if(!ext.equals(collection.getItemExtension()) &&
						!collection.getExcludeExtensions().contains(ext) &&
						!localSibling.exists()) {
						System.out.println("Copying "+sibling.getAbsolutePath() + " to " + localSibling.getAbsolutePath());
						FileUtils.copyFile(sibling, localSibling);
					}
				}
				
				//copy loose files from the parent folder
				for(File parentSibling : file.getParentFile().getParentFile().listFiles()) {

					String ext = FilenameUtils.getExtension(parentSibling.getName());
					File localSibling = new File(localFile.getParentFile().getParentFile(), parentSibling.getName());
					if(parentSibling.isFile() && 
						!ext.equals(collection.getItemExtension()) &&
						!collection.getExcludeExtensions().contains(ext) &&
						!localSibling.exists()) {
//						System.out.println("Copying "+parentSibling.getAbsolutePath() + " to " + localSibling.getAbsolutePath());
						FileUtils.copyFile(parentSibling, localSibling);
					}
				}
			}
		}
	}


	public void cacheItem(String collectionName, String catalogName, String categoryName, String itemName, int index) throws IOException {
		File cacheFolder = new File("D:\\Library\\Cache");
		int numberCached = Try.listFilesSafe(cacheFolder).length;
		
		if(numberCached > 10) {
			do {
				Try.listFilesSafe(cacheFolder)[0].delete();
			} while (Try.listFilesSafe(new File("D:\\Library\\Cache")).length > 10);
		}
		File remoteFile = getRemoteFile(collectionName, catalogName, categoryName, itemName, index);
		File cacheFile = new File(cacheFolder, remoteFile.getName());
		FileUtil.copyFile(remoteFile, cacheFile);
		
	}	
	
	public boolean isCached(String collectionName, String catalogName, String categoryName, String itemName, int index) {
		return getCachedFile(collectionName, catalogName, categoryName, itemName, index).exists();
	}
	
	public File getCachedFile(String collectionName, String catalogName, String categoryName, String itemName, int index) {
		File cacheFolder = new File("D:\\Library\\Cache");
		File remoteFile = getRemoteFile(collectionName, catalogName, categoryName, itemName, index);
		File cacheFile = new File(cacheFolder, remoteFile.getName());
		return cacheFile;
	}


	public void reloadLibrary() {
		library = libraryDao.getLibrary();
	}


	public void openVLC(String collectionName, String catalogName, String categoryName, String itemName, int index) throws IOException {
		// TODO Auto-generated method stub
		ShelfItem shelfItem = this.getItemByName(collectionName, catalogName, categoryName, itemName);
		File cacheFolder = new File("D:\\Library\\Cache");
		File remoteFile = getRemoteFile(collectionName, catalogName, categoryName, itemName, index);
		File cacheFile = new File(cacheFolder, remoteFile.getName());
		
		String vlcCacheFilename = "file:///" + cacheFile.getAbsolutePath();
		System.out.println(shelfItem.getFolder());
		
		File vlcOptionsFile = new File(shelfItem.getFolder(), "vlcOptions.txt");
		if(!shelfItem.getChildItems().isEmpty()) {
			vlcOptionsFile = new File(shelfItem.getChildItems().get(index).getFolder(), "vlcOptions.txt");
		}
		
		List<String> vlcOptions = new ArrayList<>();
		vlcOptions.add("cmd.exe");
		vlcOptions.add("/C");
		vlcOptions.add("\"C:\\Program Files\\VideoLAN\\VLC\\vlc.exe\"");
		vlcOptions.add(vlcCacheFilename);
		
		if(vlcOptionsFile.exists()) {
			List<String> lines = FileUtils.readLines(vlcOptionsFile);
			vlcOptions.addAll(lines);
		}
		
		//:sub-track-id=
		//:audio-track-id=
		for(String s : vlcOptions)
			System.out.println(s);
		
		ProcessBuilder builder = new ProcessBuilder((String[]) vlcOptions.toArray(new String[vlcOptions.size()]));
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


	public void copyForTablet(String collectionName, String catalogName, String categoryName, String itemName) throws IOException {
		File tempSsdFolder = new File("C:\\scans\\temp");
		File unzippedFolder = new File(tempSsdFolder, itemName);
		unzippedFolder.mkdirs();
		Collection collection = getCollectionByName(collectionName);
		
		File remoteFolder = getRemoteItemFolder(collection, categoryName, itemName, 0);
		File remoteFile = new File(remoteFolder, itemName + "_PNG.tar");
		
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
	
	public void createBoundingBoxPng() {
//		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//		g = image.createGraphics();  // not sure on this line, but this seems more right
//		g.setColor(Color.white);
//		g.fillRect(0, 0, 100, 100); // give the whole image a white background
//		g.setColor(Color.blue);
//		for( ..... ){
//		    g.fillRect(X , Y,  width , height );
//		        ....        
//		}
	}
}