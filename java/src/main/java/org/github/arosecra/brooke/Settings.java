package org.github.arosecra.brooke;

import java.io.File;

import org.github.arosecra.brooke.util.AppData;
import org.springframework.stereotype.Component;

@Component
public class Settings {


	private String libraryHome = "D:/Library";
	
	public Settings() {
		String settingsPath = AppData.getPath();
		new File(settingsPath).mkdirs();

		
	}

	public String getLibraryHome() {
		return libraryHome;
	}



	
	
}
