package org.github.arosecra.brooke.services;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.model.Button;
import org.github.arosecra.brooke.model.ButtonSet;
import org.github.arosecra.brooke.model.Catalog;
import org.github.arosecra.brooke.model.Category;
import org.github.arosecra.brooke.model.Collection;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.ShelfItem;
import org.github.arosecra.brooke.model.SubtitleEntry;
import org.github.arosecra.brooke.model.ToCEntry;
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
	
	@Autowired
	private LibraryDao libraryDao;
	
	private Library library;
	
	
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

		File itemDirectory = getLocalItemFolder(collection, categoryName, itemName);
		
		File thumbnailFile = new File(itemDirectory, "thumbnail.png");
		
		if(!thumbnailFile.exists()) {
			return DEFAULT_THUMBNAIL;
		} else {
			return FileUtils.readFileToByteArray(thumbnailFile);
		}
	}

	public byte[] getPage(String collectionName, String categoryName, String itemName, int pageNumber) throws IOException {
		Collection collection = getCollectionByName(collectionName);

		File itemDirectory = getLocalItemFolder(collection, categoryName, itemName);
		
		File tar = new File(itemDirectory, itemName + "." + collection.getItemExtension());
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
		File itemDirectory = getLocalItemFolder(collection, categoryName, itemName);
		
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

	public File getVideoFile(String collectionName, String catalogName, String categoryName, String itemName) {
		Collection collection = getCollectionByName(collectionName);

		File itemDirectory = getRemoteItemFolder(collection, categoryName, itemName);
		return new File(itemDirectory, itemDirectory.getName() + "." + collection.getItemExtension());
	}

	public File getSubtitleFile(String collectionName, String categoryName, String itemName, String vttName) {
		Collection collection = getCollectionByName(collectionName);

		File itemDirectory = getLocalItemFolder(collection, categoryName, itemName);
		return new File(itemDirectory, vttName);
	}

	public List<String> getSubtitles(String collectionName, String catalogName, String categoryName, String itemName) {
		Collection collection = getCollectionByName(collectionName);

		File itemDirectory = getLocalItemFolder(collection, categoryName, itemName);
		
		List<String> result = new ArrayList<>();
		for(File file : itemDirectory.listFiles()) {
			if(file.getName().endsWith("vtt") && !file.getName().equals("chapters.vtt")) {
				result.add(file.getName());
			}
		}
		
		return result;
	}

	private File getRemoteItemFolder(Collection collection, String categoryName, String itemName) {
		File remoteRepositoryHome = new File(collection.getRemoteDirectory());
		char c = itemName.charAt(0);
		if(NumberUtils.isDigits(c+"")) {
			c = '0';
		}
		File charDirectory = new File(remoteRepositoryHome, c+"");		
		File itemDirectory = new File(charDirectory, itemName);
		
		if(!itemDirectory.exists()) {
			File categoryDirectory = new File(charDirectory, categoryName);
			itemDirectory = new File(categoryDirectory, itemName);
		}
		return itemDirectory;
	}

	private File getLocalItemFolder(Collection collection, String categoryName, String itemName) {
		File localRepositoryHome = new File(collection.getLocalDirectory());
		char c = itemName.charAt(0);
		if(NumberUtils.isDigits(c+"")) {
			c = '0';
		}
		File charDirectory = new File(localRepositoryHome, c+"");	
		File itemDirectory = new File(charDirectory, itemName);
		
		if(!itemDirectory.exists()) {
			File categoryDirectory = new File(charDirectory, categoryName);
			itemDirectory = new File(categoryDirectory, itemName);
		}
		return itemDirectory;
	}

	public List<SubtitleEntry> getSubtitleEntries(String collectionName, String catalogName, String categoryName, String itemName) throws IOException {
		Collection collection = getCollectionByName(collectionName);
		File localItemFolder = getLocalItemFolder(collection, categoryName, itemName);
		
		return getSubtitleContents(localItemFolder);
	}

	private List<SubtitleEntry> getSubtitleContents(File localItemFolder) throws IOException {
		List<SubtitleEntry> result = new ArrayList<>();

		List<String> vttLines = FileUtils.readLines(new File(localItemFolder, "english.vtt"), StandardCharsets.UTF_8);
		List<String> subtitles = getSubtitlesFromVttContent(vttLines);
		int i = 0;
		try (TarArchiveInputStream tarIn = new TarArchiveInputStream(
				new BufferedInputStream(new FileInputStream(new File(localItemFolder, "english.tar"))))) {
			TarArchiveEntry entry;
			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
				if (entry.getName().endsWith("png")) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(tarIn, baos);
					byte[] rawBytes = baos.toByteArray();
					String base64png = DatatypeConverter.printBase64Binary(rawBytes);
					SubtitleEntry sub = new SubtitleEntry();
					sub.setPngBase64(base64png);
					if(i < subtitles.size())
						sub.setSubtitle(subtitles.get(i));
					result.add(sub);
					i++;
				}
			}
		}
		
		return result;
	}

	private List<String> getSubtitlesFromVttContent(List<String> vttLines) {
		List<String> result = new ArrayList<>();

		int lastArrow = vttLines.size();
		StringBuilder sb = null;
		for(int i = 0; i < vttLines.size(); i++) {
			String line = vttLines.get(i);
			if(StringUtils.contains(line, "-->")) {
				lastArrow = i;
				if(sb != null)
					result.add(sb.toString().trim());
				sb = new StringBuilder();
			} else if(i > lastArrow) {
				if(!sb.isEmpty())
					sb.append("\r\n");
				sb.append(line);
			} 
		}
		
		if(!sb.isEmpty())
			result.add(sb.toString().trim());
		
		return result;
	}
	
}