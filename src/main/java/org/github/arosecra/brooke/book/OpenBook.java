package org.github.arosecra.brooke.book;

public class OpenBook {
	private String displayName;
	private String name;
	private boolean local;
	private int leftPage = -1;
	private int rightPage = -1;

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

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}
}
