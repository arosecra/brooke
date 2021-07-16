package org.github.arosecra.brooke.library;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
	private String name;
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
	
	
}
