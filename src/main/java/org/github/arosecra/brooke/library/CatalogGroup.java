package org.github.arosecra.brooke.library;

import java.util.ArrayList;
import java.util.List;

public class CatalogGroup {
	private List<Catalog> catalogs = new ArrayList<>();

	public List<Catalog> getCatalogs() {
		return catalogs;
	}

	public void setCatalogs(List<Catalog> catalogs) {
		this.catalogs = catalogs;
	}
}
