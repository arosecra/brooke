package org.github.arosecra.brooke.book;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.github.arosecra.brooke.category.Category;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue
	private Long id;
    
    @Column(name="filename", length=512)
    private String filename;
    
    @OneToMany(mappedBy = "id")
    private List<Category> categories = new ArrayList<>();
    
	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", filename=" + filename + ", categories=" + categories + "]";
	}
	
}
