package org.github.arosecra.brooke.model;

import java.util.TreeMap;

import org.github.arosecra.brooke.model.api.ItemApiModel;

public class ItemCatalog extends TreeMap<String, ItemLocation> {

	private String name;
	
	//shelf item to item location
	//item to item location
	//  BOTH ARE THE SAME - since we expect every item to be unique
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
