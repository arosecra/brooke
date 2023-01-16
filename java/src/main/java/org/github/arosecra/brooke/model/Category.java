package org.github.arosecra.brooke.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
	private String name;
	private List<ShelfItem> items = new ArrayList<ShelfItem>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ShelfItem> getItems() {
		return items;
	}

	public void setItems(List<ShelfItem> items) {
		this.items = items;
	}
}
