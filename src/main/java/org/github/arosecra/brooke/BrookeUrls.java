package org.github.arosecra.brooke;



public enum BrookeUrls {
	HOME("/"),
	ADMIN(""),
	VIEW_BOOK(""),
	ADMIN_BOOK(""),
	ADMIN_CATEGORY(""),
	ADMIN_CATEGORY_INDICES("")
	
	
	;
	
	private String url;
	
	private BrookeUrls(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}