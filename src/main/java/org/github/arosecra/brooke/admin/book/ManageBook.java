package org.github.arosecra.brooke.admin.book;

import java.util.ArrayList;
import java.util.List;

public class ManageBook {
	private String name;
	private List<ManageBookCatalog> catalogs = new ArrayList<>();

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ManageBookCatalog> getCatalogs() {
		return catalogs;
	}

	public void setCatalogs(List<ManageBookCatalog> catalogs) {
		this.catalogs = catalogs;
	}
	
	
}
