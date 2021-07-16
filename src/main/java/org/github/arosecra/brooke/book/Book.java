package org.github.arosecra.brooke.book;

public class Book {
	private BookMetaData bookMetaData = new BookMetaData();
	private String displayName;
	private String name;
	private int leftPage = -1;
	private int rightPage = -1;

	public BookMetaData getBookMetaData() {
		return bookMetaData;
	}

	public void setBookMetaData(BookMetaData bookMetaData) {
		this.bookMetaData = bookMetaData;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getRightPage() {
		return rightPage;
	}

	public void setRightPage(int rigtPage) {
		this.rightPage = rigtPage;
	}

	public int getLeftPage() {
		return leftPage;
	}

	public void setLeftPage(int leftPage) {
		this.leftPage = leftPage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
