package org.github.arosecra.brooke.model.api;

import java.util.ArrayList;
import java.util.List;

public class BookDetailsApiModel {
	private int numberOfPages;
	private List<TocEntryApiModel> tocEntries = new ArrayList<>();
	private long rawSize;
	private long cbtSize;
	private double compression;
	
	public long getRawSize() {
		return rawSize;
	}
	public void setRawSize(long rawSize) {
		this.rawSize = rawSize;
	}
	public long getCbtSize() {
		return cbtSize;
	}
	public void setCbtSize(long cbtSize) {
		this.cbtSize = cbtSize;
	}
	public double getCompression() {
		return compression;
	}
	public void setCompression(double compression) {
		this.compression = compression;
	}
	public int getNumberOfPages() {
		return numberOfPages;
	}
	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	public List<TocEntryApiModel> getTocEntries() {
		return tocEntries;
	}
	public void setTocEntries(List<TocEntryApiModel> tocEntries) {
		this.tocEntries = tocEntries;
	}
}
