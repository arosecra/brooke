package org.github.arosecra.brooke.catalog;

import java.util.ArrayList;
import java.util.List;

public class BookListing {
	private String catalog;
	private String category;

	private List<String> books = new ArrayList<>();
	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<String> getBooks() {
		return books;
	}

	public void setBooks(List<String> books) {
		this.books = books;
	}
}
