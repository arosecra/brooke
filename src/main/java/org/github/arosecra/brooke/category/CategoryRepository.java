package org.github.arosecra.brooke.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	public List<Category> findAllByOrderByCatalog_NameAscNameAsc();
	public List<Category> findAllByCatalog_NameOrderByCatalog_NameAscNameAsc(String catalogName);
}
