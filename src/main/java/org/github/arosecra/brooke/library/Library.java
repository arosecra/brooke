package org.github.arosecra.brooke.library;

import java.util.ArrayList;
import java.util.List;

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
	
	public List<String> getCatalogNames() {
		List<String> result = new ArrayList<>();
		for(Catalog cat : catalogs) {
			result.add(cat.getName());
		}
		return result;
	}
	
}
