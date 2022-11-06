package org.github.arosecra.brooke.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShelfItem {
	private String name;
	private boolean local;
	private String seriesName;
	private List<ShelfItem> childItems = new ArrayList<>();
	private List<Category> cateogries = new ArrayList<>();
	private File thumbnail;
	private File folder;
	
	public ShelfItem() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isLocal() {
		return local;
	}
	public void setLocal(boolean local) {
		this.local = local;
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public List<ShelfItem> getChildItems() {
		return childItems;
	}
	public void setChildItems(List<ShelfItem> childItems) {
		this.childItems = childItems;
	}
	public void addChildItem(ShelfItem si) {
		this.childItems.add(si);
	}
	public File getFolder() {
		return folder;
	}
	public void setFolder(File folder) {
		this.folder = folder;
	}
	public File getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(File thumbnail) {
		this.thumbnail = thumbnail;
	}
	public List<Category> getCateogries() {
		return cateogries;
	}
	public void setCateogries(List<Category> cateogries) {
		this.cateogries = cateogries;
	}	
	public void addCategory(Category category) {
		this.cateogries.add(category);
	}
}
