package org.github.arosecra.brooke.catalog;

import org.github.arosecra.brooke.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CatalogService implements JpaService<Catalog, Long> {
	
	@Autowired
	private CatalogRepository catalogRepository;

	@Override
	public JpaRepository<Catalog, Long> getRepository() {
		return catalogRepository;
	}

	public Catalog findByName(String catalog) {
		return catalogRepository.findByName(catalog);
	}
}
