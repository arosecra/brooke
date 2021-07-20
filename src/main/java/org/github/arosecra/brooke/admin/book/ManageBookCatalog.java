package org.github.arosecra.brooke.admin.book;

import java.util.ArrayList;
import java.util.List;

public class ManageBookCatalog {
	private String name;
	private List<ManageBookCategory> categories = new ArrayList<>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ManageBookCategory> getCategories() {
		return categories;
	}
	public void setCategories(List<ManageBookCategory> categories) {
		this.categories = categories;
	}
	
	
}
