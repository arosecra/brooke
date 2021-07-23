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

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.github.arosecra.brooke.ConfigService;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.book.BookService;
import org.github.arosecra.brooke.catalog.Catalog;
import org.github.arosecra.brooke.catalog.CatalogService;
import org.github.arosecra.brooke.category.Category;
import org.github.arosecra.brooke.category.CategoryService;
import org.github.arosecra.brooke.index.Index;
import org.github.arosecra.brooke.index.IndexService;
import org.github.arosecra.brooke.library.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {


	@Autowired
	private Settings settings;
	
	@Autowired
	private ConfigService configService;
	
	@Autowired 
	private CatalogService catalogService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private IndexService indexService;
	
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
		Configuration catConfig = configService.getConfig(settings.getCatalogsHome(), catalog, "properties");

		String internalCategoryName = category;
		String[] categories = catConfig.getStringArray("categories");
	    for(String c : ObjectUtils.firstNonNull(categories, new String[] {})) {
	    	String categoryDisplayName = c;
	    	if(catConfig.getString("categories."+c) != null) {
	    		categoryDisplayName = catConfig.getString("categories."+c);
	    		if(categoryDisplayName.equals(category)) {
	    			internalCategoryName = c;
	    		}
	    	}	
	    }
		
	    String lineToAdd = "\r\nbooks."+internalCategoryName+"="+bookname;
	    FileUtils.write(new File(settings.getCatalogsHome(), catalog + ".properties"), lineToAdd, true);
	}

	public void generateThumbnail(String bookname) {
		// TODO Auto-generated method stub
		
	}

	public void addCategory(String catalog, String categoryname) {
		System.out.println("Adding catagory " + categoryname);
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
		
		try {
			JAXBContext jc = JAXBContext.newInstance(Brooke.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
			m.marshal(b, new File("export.xml"));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
