package org.github.arosecra.brooke;



public class Point
{
	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Point(double x, double y) {
		this.x = (int)x;
		this.y = (int)y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}