package org.github.arosecra.brooke.book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
	
	public Book findByFilename(String filename);
}
