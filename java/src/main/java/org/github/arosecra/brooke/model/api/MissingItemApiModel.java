package org.github.arosecra.brooke.model.api;

public class MissingItemApiModel {
	private String collection;
	private String itemName;
	private boolean itemMissing;

	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public boolean isItemMissing() {
		return itemMissing;
	}
	public void setItemMissing(boolean itemMissing) {
		this.itemMissing = itemMissing;
	}
}
