package org.github.arosecra.brooke.catalogparent;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.github.arosecra.brooke.JpaEntity;
import org.github.arosecra.brooke.catalog.Catalog;
import org.github.arosecra.brooke.category.Category;

@Entity
@Table(name = "catalog_parent")
public class CatalogParent implements JpaEntity {


    @Id
    @GeneratedValue
	private Long id;

    
    @OneToOne()
    @JoinColumn(name = "category_id")
	private Category parentCategory;

    
    @OneToOne()
    @JoinColumn(name = "catalog_id")
	private Catalog catalog;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Category getParentCategory() {
		return parentCategory;
	}


	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}


	public Catalog getCatalog() {
		return catalog;
	}


	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
}
