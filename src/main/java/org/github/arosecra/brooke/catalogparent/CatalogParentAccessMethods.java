package org.github.arosecra.brooke.catalogparent;

import java.util.List;

public interface CatalogParentAccessMethods {

	public List<CatalogParent> findAllByCatalog_NameOrderByParentCategory_Catalog_Name(String catalogName);

}
