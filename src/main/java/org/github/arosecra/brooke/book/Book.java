package org.github.arosecra.brooke.book;

import java.util.HashMap;
import java.util.Map;

public class Book {
	private BookMetaData bookMetaData = new BookMetaData();
	
	Map<Integer, byte[]> pages = new HashMap<>();

	public BookMetaData getBookMetaData() {
		return bookMetaData;
	}

	public void setBookMetaData(BookMetaData bookMetaData) {
		this.bookMetaData = bookMetaData;
	}

	public Map<Integer, byte[]> getPages() {
		return pages;
	}

	public void setPages(Map<Integer, byte[]> pages) {
		this.pages = pages;
	}
	
	
	
}
