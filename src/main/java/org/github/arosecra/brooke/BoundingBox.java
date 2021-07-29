package org.github.arosecra.brooke;



public class BoundingBox
{
	private Point topLeft;
	private Point bottomRight;
	public BoundingBox(Point topLeft, Point bottomRight) {
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
	}
	public Point getTopLeft() {
		return topLeft;
	}
	public void setTopLeft(Point topLeft) {
		this.topLeft = topLeft;
	}
	public Point getBottomRight() {
		return bottomRight;
	}
	public void setBottomRight(Point bottomRight) {
		this.bottomRight = bottomRight;
	}
	
	public int width(){return this.brx()-this.tlx();}
	public int height(){return this.bry()-this.tly();}
	
	public int tlx(){return this.topLeft.getX();}
	public int tly(){return this.topLeft.getY();}
	public int brx(){return this.bottomRight.getX();}
	public int bry(){return this.bottomRight.getY();}
	
	public boolean intersects(BoundingBox bb)
	{
		return !(
//				  ________     ________
//				 |        |   |        |
//				 |   r1   |   |   r2   |
//				 |        |   |        |
//				 |________|   |________|
				 brx() < bb.tlx() ||
//				  ________     ________
//				 |        |   |        |
//				 |   r2   |   |   r1   |
//				 |        |   |        |
//				 |________|   |________|				 
				 bb.brx() < tlx() ||
//				  ________ 
//				 |        |
//				 |   r1   |
//				 |        |
//				 |________|
//				  ________ 
//				 |        |
//				 |   r2   |
//				 |        |
//				 |________|				 
				 bry() < bb.tly() ||
//				  ________ 
//				 |        |
//				 |   r2   |
//				 |        |
//				 |________|
//				  ________ 
//				 |        |
//				 |   r1   |
//				 |        |
//				 |________|				 
				 bb.bry() < tly()
				);
	}
}