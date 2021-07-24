package org.github.arosecra.brooke.catalogparent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface CatalogParentRepository extends JpaRepository<CatalogParent, Long>, CatalogParentAccessMethods {
}
