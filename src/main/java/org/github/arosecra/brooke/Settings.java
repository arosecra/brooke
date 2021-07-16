package org.github.arosecra.brooke;

import org.springframework.stereotype.Component;

@Component
public class Settings {
	private String libraryHome = "D:/scans";
	private String booksHome = libraryHome + "/"+ "books";
	private String catalogsHome = libraryHome + "/" + "catalogs";
	
	public String getLibraryHome() {
		return libraryHome;
	}
	
	public String getBooksHome() {
		return booksHome;
	}
	
	public String getCatalogsHome() {
		return catalogsHome;
	}
	
}
