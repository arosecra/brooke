package org.github.arosecra.brooke.library;

import java.util.ArrayList;
import java.util.List;

import org.github.arosecra.brooke.catalog.Catalog;
import org.github.arosecra.brooke.category.Category;

public class Library {
	private List<Catalog> catalogs = new ArrayList<>();
	private List<Category> categories = new ArrayList<>();

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public List<Catalog> getCatalogs() {
		return catalogs;
	}

	public void setCatalogs(List<Catalog> catalogs) {
		this.catalogs = catalogs;
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
