package org.github.arosecra.brooke.library;

import java.util.ArrayList;
import java.util.List;

import org.github.arosecra.brooke.catalog.BookListing;
import org.github.arosecra.brooke.catalog.Catalog;

public class Library {
	private List<Catalog> catalogs = new ArrayList<>();
	private BookListing listing = new BookListing();

	public List<Catalog> getCatalogs() {
		return catalogs;
	}

	public void setCatalogs(List<Catalog> catalogs) {
		this.catalogs = catalogs;
	}

	public BookListing getListing() {
		return listing;
	}

	public void setListing(BookListing listing) {
		this.listing = listing;
	}
	
	public List<CatalogGroup> getCatalogGroups() {
		List<CatalogGroup> results = new ArrayList<>();
		CatalogGroup current = new CatalogGroup();
		results.add(current);
		int maxGroupLength = 45;
		int groupLength = 0;
		for(int i = 0; i < catalogs.size(); i++) {
			int nextLength = catalogs.get(i).getName().length()+4;
			
			if(groupLength + nextLength > maxGroupLength) {
				current = new CatalogGroup();
				results.add(current);
				groupLength = 0;
			}
			groupLength += nextLength;
			current.getCatalogs().add(catalogs.get(i));
		}
		
		return results;
	}
	
	public List<String> getCatalogNames() {
		List<String> result = new ArrayList<>();
		for(Catalog cat : catalogs) {
			result.add(cat.getName());
		}
		return result;
	}
	
}
