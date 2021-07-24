package org.github.arosecra.brooke.catalog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.github.arosecra.brooke.JpaEntity;

@Entity
@Table(name = "catalog")
public class Catalog implements JpaEntity {

    @Id
    @GeneratedValue
    private Long id;
	
	@Column(name="name", length=256)
	private String name;

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
}
