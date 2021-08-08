package org.github.arosecra.brooke.model;

public class Button {

	private String name;
	private String href;
	private String onclick;
	
	public Button(String name, String href, String onclick) {
		this.name = name;
		this.href = href;
		this.onclick=onclick;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getOnclick() {
		return onclick;
	}
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}	
}
