package org.github.arosecra.brooke.model;

import java.util.TreeMap;

public class Shelf extends TreeMap<String, ShelfItem> {
	
	private String name;

	public void add(ShelfItem shelfItem) {
		this.put(shelfItem.getName(), shelfItem);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
