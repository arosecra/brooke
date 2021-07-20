package org.github.arosecra.brooke.admin;

import java.util.ArrayList;
import java.util.List;

public class AdminBookListing {
	private List<AdminBook> books = new ArrayList<>();

	public List<AdminBook> getBooks() {
		return books;
	}

	public void setBooks(List<AdminBook> books) {
		this.books = books;
	}
}
