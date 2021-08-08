package org.github.arosecra.brooke.model;

import java.util.ArrayList;
import java.util.List;

public class Collection {
	private List<Catalog> catalogs = new ArrayList<>();
	private String type;
	private String name;
	private String remoteDirectory;
	private String remoteCollection;
	private String localDirectory;
	private String itemExtension;
	private String openType;

	public String getRemoteDirectory() {
		return remoteDirectory;
	}

	public void setRemoteDirectory(String remoteDirectory) {
		this.remoteDirectory = remoteDirectory;
	}

	public String getRemoteCollection() {
		return remoteCollection;
	}

	public void setRemoteCollection(String remoteCollection) {
		this.remoteCollection = remoteCollection;
	}

	public String getLocalDirectory() {
		return localDirectory;
	}

	public void setLocalDirectory(String localDirectory) {
		this.localDirectory = localDirectory;
	}

	public List<Catalog> getCatalogs() {
		return catalogs;
	}

	public void setCatalogs(List<Catalog> catalogs) {
		this.catalogs = catalogs;
	}
	
	public List<CatalogGroup> getCatalogGroups() {
		List<CatalogGroup> results = new ArrayList<>();
		CatalogGroup current = new CatalogGroup();
		results.add(current);
		int maxGroupLength = 45;
		int groupLength = 0;
		if(catalogs.size() > 1) {
			for(int i = 0; i < catalogs.size(); i++) {
				int nextLength = catalogs.get(i).getName().length()+4;
				
				if(groupLength + nextLength > maxGroupLength) {
					current = new CatalogGroup();
					results.add(current);
					groupLength = 0;
				}
				groupLength += nextLength;
				current.getCatalogs().add(catalogs.get(i));
			}
		} else {
			//this is for simple anonymous catalog
			current.getCatalogs().add(catalogs.get(0));
		}
		
		return results;
	}
	
	public List<String> getCatalogNames() {
		List<String> result = new ArrayList<>();
		for(Catalog cat : catalogs) {
			result.add(cat.getName());
		}
		return result;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItemExtension() {
		return itemExtension;
	}

	public void setItemExtension(String itemExtension) {
		this.itemExtension = itemExtension;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}
}
