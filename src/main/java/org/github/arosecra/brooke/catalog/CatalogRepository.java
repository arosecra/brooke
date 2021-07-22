package org.github.arosecra.brooke.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

	public Catalog findByName(String name);
}
