package org.github.arosecra.brooke.admin.catalogbooks;

import java.util.ArrayList;
import java.util.List;

public class AdminBook {
	private String name;
	private String displayName;
	private List<AdminBookCategory> categories = new ArrayList<>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<AdminBookCategory> getCategories() {
		return categories;
	}
	public void setCategories(List<AdminBookCategory> categories) {
		this.categories = categories;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
