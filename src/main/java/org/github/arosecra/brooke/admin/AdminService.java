package org.github.arosecra.brooke.admin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.JpaEntity;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.book.Book;
import org.github.arosecra.brooke.book.BookService;
import org.github.arosecra.brooke.catalog.Catalog;
import org.github.arosecra.brooke.catalog.CatalogService;
import org.github.arosecra.brooke.catalogparent.CatalogParent;
import org.github.arosecra.brooke.catalogparent.CatalogParentService;
import org.github.arosecra.brooke.category.Category;
import org.github.arosecra.brooke.category.CategoryService;
import org.github.arosecra.brooke.index.Index;
import org.github.arosecra.brooke.index.IndexService;
import org.github.arosecra.brooke.library.Library;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
	
	@Autowired
	private Logger logger;
	
	@Autowired 
	private CatalogService catalogService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private IndexService indexService;
	
	@Autowired
	private CatalogParentService catalogParentService;
	
	@Autowired
	private Settings settings;
	
	public Map<String, List<Index>> getBookToListingsMap(Library library) {
		//TODO fix
		Map<String, List<Index>> booksToListings = new HashMap<>();
		
		for(Catalog cat : library.getCatalogs()) {
			for(Category category : categoryService.findAllByCatalog_NameOrderByCatalog_NameAscNameAsc(cat.getName())) {
				for(Index index : indexService.findAllByCategory_Catalog_NameAndCategory_NameOrderByBook_FilenameAsc(cat.getName(), category.getName())) {
					if(!booksToListings.containsKey(index.getBook().getFilename())) {
						booksToListings.put(index.getBook().getFilename(), new ArrayList<>());
					}
					booksToListings.get(index.getBook().getFilename()).add(index);					
				}
			}
		}
		return booksToListings;
	}

	public void addToCategory(String bookname, String catalog, String category) throws IOException {
//		Configuration catConfig = configService.getConfig(settings.getCatalogsHome(), catalog, "properties");
//
//		String internalCategoryName = category;
//		String[] categories = catConfig.getStringArray("categories");
//	    for(String c : ObjectUtils.firstNonNull(categories, new String[] {})) {
//	    	String categoryDisplayName = c;
//	    	if(catConfig.getString("categories."+c) != null) {
//	    		categoryDisplayName = catConfig.getString("categories."+c);
//	    		if(categoryDisplayName.equals(category)) {
//	    			internalCategoryName = c;
//	    		}
//	    	}	
//	    }
//		
//	    String lineToAdd = "\r\nbooks."+internalCategoryName+"="+bookname;
//	    FileUtils.write(new File(settings.getCatalogsHome(), catalog + ".properties"), lineToAdd, true);
		
		
		Index index = new Index();
		Book book = bookService.findByFilename(bookname);
		if(book == null) {
			book = new Book();
			book.setFilename(bookname);
			bookService.save(book);
			bookService.flush();
		}
		index.setBook(book);
		index.setCategory(categoryService.findByCatalog_NameAndName(catalog, category));
		indexService.save(index);
	}

	public void generateThumbnail(String bookname) {
		// TODO Auto-generated method stub
		
	}

	public void addCategory(String catalog, String categoryname) {
		Category cry = new Category();
		cry.setName(categoryname);
		cry.setCatalog(catalogService.findByName(catalog));
		categoryService.save(cry);
	}

	public void addCatalog(String catalog) {
		Catalog cat = new Catalog();
		cat.setName(catalog);
		System.out.println("saving");
		catalogService.save(cat);
	}

	public void export() {
		Brooke b = new Brooke();
		b.setCatalogs(catalogService.findAll());
		b.setCategories(categoryService.findAll());
		b.setBooks(bookService.findAll());
		b.setIndices(indexService.findAll());
		b.setCatalogParents(catalogParentService.findAll());
		
		try {
			JAXBContext jc = JAXBContext.newInstance(Brooke.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
			m.marshal(b, new File(settings.getLibraryHome(), "export.xml"));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void imprt() {

		
		try {
			JAXBContext jc = JAXBContext.newInstance(Brooke.class);
			Unmarshaller m = jc.createUnmarshaller();
			Brooke b = (Brooke) m.unmarshal(new File(settings.getLibraryHome(), "export.xml"));
			
			Map<Long, Catalog> catalogs = new MapSorter<Catalog>().sort(b.getCatalogs());
			Map<Long, Category> categories = new MapSorter<Category>().sort(b.getCategories());
			Map<Long, Book> books = new MapSorter<Book>().sort(b.getBooks());
			Map<Long, Index> indices = new MapSorter<Index>().sort(b.getIndices());
			Map<Long, CatalogParent> catalogParents = new MapSorter<CatalogParent>().sort(b.getCatalogParents());
			
			for(Category category : categories.values()) {
				long id = category.getCatalog().getId();
				category.setCatalog(catalogs.get(id));
			}
			
			for(Index index : indices.values()) {
				index.setBook(books.get(index.getBook().getId()));
				index.setCategory(categories.get(index.getCategory().getId()));
			}
			
			for(CatalogParent parent : catalogParents.values()) {
				parent.setCatalog(catalogs.get(parent.getCatalog().getId()));
				parent.setParentCategory(categories.get(parent.getParentCategory().getId()));
			}
			
			catalogService.saveAll(catalogs.values());
			catalogService.flush();
			
			categoryService.saveAll(categories.values());
			categoryService.flush();
			
			bookService.saveAll(books.values());
			bookService.flush();
			
			indexService.saveAll(indices.values());
			indexService.flush();
			
			catalogParentService.saveAll(catalogParents.values());
			catalogParentService.flush();
			
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static class MapSorter<T extends JpaEntity> {
		public Map<Long, T> sort(List<T> values) {
			Map<Long, T> res = new HashMap<>();
			for(T t : values) {
				res.put(t.getId(), t);
				t.setId(null);
			}
			return res;
		}
	}

	public void synch() throws IOException {
		File remoteBooksFolder = new File("\\\\drobo5n\\public\\scans\\books");
		File localBooksFolder = new File(settings.getBooksHome());
		for(File remoteBookFolder : remoteBooksFolder.listFiles()) {
			File localBookFolder = new File(localBooksFolder, remoteBookFolder.getName());
			localBookFolder.mkdirs();
			syncFile(remoteBookFolder, localBookFolder, "thumbnail.png");
			syncFile(remoteBookFolder, localBookFolder, "toc.txt");
		}
		
		//if there are books that are local but not remote, copy the book remotely
		for(File localBookFolder : localBooksFolder.listFiles()) {
			File remoteBookFolder = new File(remoteBooksFolder, localBookFolder.getName());
			if(!remoteBookFolder.exists()) {
				logger.info("Synching " + localBookFolder.getName());
				FileUtils.copyDirectory(localBookFolder, remoteBookFolder);
			}
		}
	}

	private void syncFile(File remoteBookFolder, File localBookFolder, String filename) throws IOException {
		File localFile = new File(localBookFolder, filename);
		File remoteFile = new File(remoteBookFolder, filename);
		
		boolean isLocal = localFile.exists();
		boolean isRemote = remoteFile.exists();
		boolean isRemoteNewer = localFile.lastModified() < remoteFile.lastModified();
		
		boolean copy = false;
		File srcFile = null;
		File destFile = null;
		
		if(isLocal && !isRemote) {
			copy = true;
			srcFile = localFile; destFile = remoteFile; 
		} else if (!isLocal && isRemote) {
			copy = true;
			srcFile = remoteFile; destFile = localFile; 
		} else if (isRemote && isRemoteNewer) {
			copy = true;
			srcFile = remoteFile; destFile = localFile; 
		} else if (isLocal && !isRemoteNewer) {
			copy = true;
			srcFile = localFile; destFile = remoteFile; 
		}
		
		if(copy) {
			logger.info("Copying " + srcFile.getAbsolutePath() + " -> " + destFile.getAbsolutePath());
			FileUtils.copyFile(srcFile, destFile);
		}
	}
}
