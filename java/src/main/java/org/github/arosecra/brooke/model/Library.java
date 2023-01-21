package org.github.arosecra.brooke.model;

import java.util.ArrayList;
import java.util.List;

import org.github.arosecra.brooke.model.api.CollectionApiModel;

public class Library {
	private List<CollectionApiModel> collections = new ArrayList<>();
	private List<ItemCatalog> itemCatalogs = new ArrayList<>();
	private List<Shelf> shelves = new ArrayList<>();
	
	public List<CollectionApiModel> getCollections() {
		return collections;
	}

	public void setCollections(List<CollectionApiModel> collections) {
		this.collections = collections;
	}

	public List<Shelf> getShelves() {
		return shelves;
	}

	public void setShelves(List<Shelf> shelves) {
		this.shelves = shelves;
	}

	public List<ItemCatalog> getItemCatalogs() {
		return itemCatalogs;
	}

	public void setItemCatalogs(List<ItemCatalog> itemCatalogs) {
		this.itemCatalogs = itemCatalogs;
	}

}