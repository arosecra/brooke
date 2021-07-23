package org.github.arosecra.brooke.index;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface IndexRepository extends JpaRepository<Index, Long> {

	public List<Index> findAllByCategory_Catalog_NameAndCategory_NameOrderByBook_FilenameAsc(String catalogName, String categoryName);
}
