package org.github.arosecra.brooke.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShelfItem {
	private String name;
	private boolean local;
	private String seriesName;
	private List<File> childItems = new ArrayList<>();
	private File folder;
	
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
	public List<File> getChildItems() {
		return childItems;
	}
	public void setChildItems(List<File> childItems) {
		this.childItems = childItems;
	}
	public void addChildItem(File si) {
		this.childItems.add(si);
	}
	public File getFolder() {
		return folder;
	}
	public void setFolder(File folder) {
		this.folder = folder;
	}	
}
