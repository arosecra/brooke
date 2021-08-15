package org.github.arosecra.brooke.model;

public class SubtitleEntry {
	private String subtitle;
	private String pngBase64;
	private int imageHeigth;
	private int imageWidth;
	
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getPngBase64() {
		return pngBase64;
	}
	public void setPngBase64(String pngBase64) {
		this.pngBase64 = pngBase64;
	}
	public int getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}
	public int getImageHeigth() {
		return imageHeigth;
	}
	public void setImageHeigth(int imageHeigth) {
		this.imageHeigth = imageHeigth;
	}
		
}
