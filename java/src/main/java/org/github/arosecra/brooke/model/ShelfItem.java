package org.github.arosecra.brooke.model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The remote and local file details for a Item
 */
public class ShelfItem {
	private String name;
	private File localBaseDirectory;
	private File remoteBaseDirectory;
	private File localCollectionBaseDirectory;
	private File remoteCollectionBaseDirectory;
	
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
		if(remoteBaseDirectory == null) {
			Path second = Paths.get(localBaseDirectory.toURI()); 
			Path first = Paths.get(localCollectionBaseDirectory.toURI());
			Path relative = first.relativize(second);
			this.remoteBaseDirectory = new File(this.remoteCollectionBaseDirectory, relative.toString());
		}
		
		return remoteBaseDirectory;
	}
	public void setRemoteBaseDirectory(File remoteBaseDirectory) {
		this.remoteBaseDirectory = remoteBaseDirectory;
	}
	public File getLocalCollectionBaseDirectory() {
		return localCollectionBaseDirectory;
	}
	public void setLocalCollectionBaseDirectory(File localCollectionBaseDirectory) {
		this.localCollectionBaseDirectory = localCollectionBaseDirectory;
	}
	public File getRemoteCollectionBaseDirectory() {
		return remoteCollectionBaseDirectory;
	}
	public void setRemoteCollectionBaseDirectory(File remoteCollectionBaseDirectory) {
		this.remoteCollectionBaseDirectory = remoteCollectionBaseDirectory;
	}	
}