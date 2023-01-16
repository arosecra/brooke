package org.github.arosecra.brooke.model;

import java.util.ArrayList;
import java.util.List;

public class Library {
	private List<Collection> collections = new ArrayList<>();

	public List<Collection> getCollections() {
		return collections;
	}

	public void setCollections(List<Collection> collections) {
		this.collections = collections;
	}	
}
