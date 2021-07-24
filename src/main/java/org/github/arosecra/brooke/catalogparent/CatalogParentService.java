package org.github.arosecra.brooke.catalogparent;

import java.util.List;

import org.github.arosecra.brooke.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class CatalogParentService implements CatalogParentAccessMethods, JpaService<CatalogParent, Long> {

	@Autowired
	private CatalogParentRepository catalogParentRepository;
	
	@Override
	public List<CatalogParent> findAllByCatalog_NameOrderByParentCategory_Catalog_Name(String catalogName) {
		return catalogParentRepository.findAllByCatalog_NameOrderByParentCategory_Catalog_Name(catalogName);
	}

	@Override
	public JpaRepository<CatalogParent, Long> getRepository() {
		return catalogParentRepository;
	}

}
