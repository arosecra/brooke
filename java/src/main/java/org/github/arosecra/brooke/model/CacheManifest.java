package org.github.arosecra.brooke.model;

import java.util.ArrayList;
import java.util.List;

public class CacheManifest {
	
	
	private List<CachedFile> files = new ArrayList<>();

	public List<CachedFile> getFiles() {
		return files;
	}

	public void setFiles(List<CachedFile> files) {
		this.files = files;
	}
}