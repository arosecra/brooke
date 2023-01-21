package org.github.arosecra.brooke.services;

import javax.annotation.PostConstruct;

import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.model.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LibraryCacheService {
	
	@Autowired
	private LibraryDao libraryDao;

	private Library library;
	
	@PostConstruct()
	public void init() {
		library = libraryDao.getLibrary(false);
	}

	public Library getLibrary() {
		return this.library;
	}

	public void reload() {
		this.init();
	}
}
