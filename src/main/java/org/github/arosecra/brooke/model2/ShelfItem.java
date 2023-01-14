package org.github.arosecra.brooke.model2;

import java.io.File;

/**
 * The remote and local file details for a Item
 */
public class ShelfItem {
	private String name;
	private File localBaseDirectory;
	private File remoteBaseDirectory;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public File getLocalBaseDirectory() {
		return localBaseDirectory;
	}
	public void setLocalBaseDirectory(File localBaseDirectory) {
		this.localBaseDirectory = localBaseDirectory;
	}
	public File getRemoteBaseDirectory() {
		return remoteBaseDirectory;
	}
	public void setRemoteBaseDirectory(File remoteBaseDirectory) {
		this.remoteBaseDirectory = remoteBaseDirectory;
	}
	
	
}