package org.github.arosecra.brooke.book;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.github.arosecra.brooke.JpaEntity;

@Entity
@Table(name = "book")
public class Book implements JpaEntity {

    @Id
    @GeneratedValue
	private Long id;
    
    @Column(name="filename", length=512)
    private String filename;

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
		return "Book [id=" + id + ", filename=" + filename + "]";
	}
	
}
