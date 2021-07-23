package org.github.arosecra.brooke.index;

import java.util.List;

import org.github.arosecra.brooke.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class IndexService implements JpaService<Index, Long> {
	
	@Autowired
	private IndexRepository indexRepository;
	
	public List<Index> findAllByCategory_Catalog_NameAndCategory_NameOrderByBook_FilenameAsc(String catalog, String category) {
		return indexRepository.findAllByCategory_Catalog_NameAndCategory_NameOrderByBook_FilenameAsc(catalog, category);
	}

	@Override
	public JpaRepository<Index, Long> getRepository() {
		return indexRepository;
	}

}
