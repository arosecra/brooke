package org.github.arosecra.brooke.category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.github.arosecra.brooke.JpaEntity;
import org.github.arosecra.brooke.catalog.Catalog;

@Entity
@Table(name = "category")
public class Category implements JpaEntity {

    @Id
    @GeneratedValue
    private Long id;
    
    @OneToOne
    private Catalog catalog;

	@Column(name="name", length=256)
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
