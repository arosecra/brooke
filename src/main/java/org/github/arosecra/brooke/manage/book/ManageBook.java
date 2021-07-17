package org.github.arosecra.brooke.manage.book;

import java.util.ArrayList;
import java.util.List;

public class ManageBook {
	private String name;
	private List<ManageBookCategory> categories = new ArrayList<>();

	public List<ManageBookCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<ManageBookCategory> categories) {
		this.categories = categories;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
