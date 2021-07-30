package org.github.arosecra.brooke.index;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.github.arosecra.brooke.JpaEntity;
import org.github.arosecra.brooke.book.Book;
import org.github.arosecra.brooke.category.Category;

@Entity
@Table(name="listing")
public class Index implements JpaEntity {

    @Id
    @GeneratedValue
	private Long id;
    
    @OneToOne()
    @JoinColumn(name = "book_id")
    private Book book;
    
    @OneToOne()
    @JoinColumn(name = "category_id")
    private Category category;
    
    @Transient
    private boolean local;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}
}
