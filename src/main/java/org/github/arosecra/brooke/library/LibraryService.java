package org.github.arosecra.brooke.library;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.github.arosecra.brooke.ConfigService;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.catalog.Catalog;
import org.github.arosecra.brooke.catalog.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {

	@Autowired
	private Settings settings;
	
	@Autowired
	private ConfigService configService;

	@Autowired 
	private CatalogService catalogService;
	
	public Library getLibrary() {
		Library library = new Library();
		
		Configuration catalogs = configService.getConfig(settings.getCatalogsHome(), "Catalogs", "properties");
		
		for(String catalogName : catalogs.getStringArray("catalogs")) {
			Catalog cat = catalogService.getCatalog(catalogName);
			if(StringUtils.equals(catalogName, catalogName)) {
	    		cat.setSelected(true);
	    	}
		    library.getCatalogs().add(cat);
		}

		return library;
	}
}
