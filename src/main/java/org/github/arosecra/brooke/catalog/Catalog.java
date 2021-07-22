package org.github.arosecra.brooke.catalog;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.github.arosecra.brooke.category.Category;

@Entity
@Table(name = "catalog")
public class Catalog {

    @Id
    @GeneratedValue
    private Long id;
	
	@Column(name="name", length=256)
	private String name;
	
	@OneToMany
	private List<Category> parents = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Category> getParents() {
		return parents;
	}

	public void setParents(List<Category> parents) {
		this.parents = parents;
	}

}
