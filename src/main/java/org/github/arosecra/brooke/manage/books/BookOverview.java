package org.github.arosecra.brooke.manage.books;

public class BookOverview {
	private String name;
	private int assigned; // the number of categories this book is in
	private boolean tocGenerated;
	private boolean thumbnailGenerated;
	private boolean cbtExists;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAssigned() {
		return assigned;
	}
	public void setAssigned(int assigned) {
		this.assigned = assigned;
	}
	public boolean isTocGenerated() {
		return tocGenerated;
	}
	public void setTocGenerated(boolean tocGenerated) {
		this.tocGenerated = tocGenerated;
	}
	public boolean isThumbnailGenerated() {
		return thumbnailGenerated;
	}
	public void setThumbnailGenerated(boolean thumbnailGenerated) {
		this.thumbnailGenerated = thumbnailGenerated;
	}
	public boolean isCbtExists() {
		return cbtExists;
	}
	public void setCbtExists(boolean cbtExists) {
		this.cbtExists = cbtExists;
	}
	
	
}
