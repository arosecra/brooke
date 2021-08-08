package org.github.arosecra.brooke;

import org.springframework.stereotype.Component;

@Component
public class Settings {
	private String libraryHome = "D:/Library";
	
	public String getLibraryHome() {
		return libraryHome;
	}
	
}
