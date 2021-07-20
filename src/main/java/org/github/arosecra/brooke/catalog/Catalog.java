package org.github.arosecra.brooke.catalog;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
	private String name;
	private boolean selected;
	private String parentCatalog;
	private String parentCategories;
	private List<String> categories = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getParentCatalog() {
		return parentCatalog;
	}

	public void setParentCatalog(String parentCatalog) {
		this.parentCatalog = parentCatalog;
	}

	public String getParentCategories() {
		return parentCategories;
	}

	public void setParentCategories(String parentCategories) {
		this.parentCategories = parentCategories;
	}

	
	
	
}
