package org.github.arosecra.brooke.category;

import java.util.List;

import org.github.arosecra.brooke.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class CategoryService implements JpaService<Category, Long> {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public JpaRepository<Category, Long> getRepository() {
		return categoryRepository;
	}

	public List<Category> findAllByCatalog_NameOrderByCatalog_NameAscNameAsc(String name) {
		return categoryRepository.findAllByCatalog_NameOrderByCatalog_NameAscNameAsc(name);
	}

}
