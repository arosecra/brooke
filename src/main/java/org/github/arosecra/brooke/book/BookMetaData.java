package org.github.arosecra.brooke.book;

import java.util.ArrayList;
import java.util.List;

public class BookMetaData {
	private byte[] thumbnail;
	private List<ToCEntry> tocEntries = new ArrayList<>();

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

	public List<ToCEntry> getTocEntries() {
		return tocEntries;
	}

	public void setTocEntries(List<ToCEntry> tocEntries) {
		this.tocEntries = tocEntries;
	}
}
