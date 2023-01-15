package org.github.arosecra.brooke.model2;

import java.util.TreeMap;

import org.github.arosecra.brooke.model.api.ItemApiModel;

public class ItemCatalog extends TreeMap<String, ItemApiModel> {

	private String name;
	
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
