package org.github.arosecra.brooke.book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

	public List<Book> findByCategories_Name(String categoryName);
	
	public Book findByFilename(String filename);
}
