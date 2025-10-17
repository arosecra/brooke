package org.github.arosecra.book.pipeline.model;

import java.io.File;

public class JobFolder {
	public RemoteFolder remoteFolder;
	public File workFolder;
	public File sourceFolder;
	public File destFolder;
	public File tempFolder;
	
	public BookJob bookJob = new BookJob();
	
	//for movies - tbd
}