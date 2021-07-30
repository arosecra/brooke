package org.github.arosecra.brooke.index;

import java.util.List;

import org.github.arosecra.brooke.JpaService;
import org.github.arosecra.brooke.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class IndexService implements JpaService<Index, Long> {
	
	@Autowired
	private IndexRepository indexRepository;
	
	@Autowired
	private BookService bookService;
	
	public List<Index> findAllByCategory_Catalog_NameAndCategory_NameOrderByBook_FilenameAsc(String catalog, String category) {
		List<Index> results =  indexRepository.findAllByCategory_Catalog_NameAndCategory_NameOrderByBook_FilenameAsc(catalog, category);
		for(Index index : results) {
			index.setLocal(bookService.isLocal(index.getBook().getFilename()));
		}
		return results;
	}

	@Override
	public JpaRepository<Index, Long> getRepository() {
		return indexRepository;
	}

}
