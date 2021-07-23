package org.github.arosecra.brooke.admin;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.github.arosecra.brooke.book.Book;
import org.github.arosecra.brooke.catalog.Catalog;
import org.github.arosecra.brooke.category.Category;
import org.github.arosecra.brooke.index.Index;

@XmlRootElement(name="brooke")
public class Brooke {
	private List<Catalog> catalogs = new ArrayList<>();
	private List<Category> categories = new ArrayList<>();
	private List<Book> books = new ArrayList<>();
	private List<Index> indices = new ArrayList<>();
	public List<Catalog> getCatalogs() {
		return catalogs;
	}
	public void setCatalogs(List<Catalog> catalogs) {
		this.catalogs = catalogs;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	public List<Index> getIndices() {
		return indices;
	}
	public void setIndices(List<Index> indices) {
		this.indices = indices;
	}
}
