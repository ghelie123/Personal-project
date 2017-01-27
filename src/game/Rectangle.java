package game;

public class Rectangle {

	private double xPos;
	private double yPos;
	private double width;
	private double height;
	
	public Rectangle(double xPos, double yPos, double width, double height) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
	}

	public double getXPos() {
		return xPos;
	}

	public double getYPos() {
		return yPos;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public void setXPos(double xPos) {
		this.xPos = xPos;
	}

	public void setYPos(double yPos) {
		this.yPos = yPos;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	
	public void setBounds(double xPos, double yPos, double width, double height) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
	}
	
	public boolean inside(Rectangle rect) {
		if (xPos < rect.getXPos() + rect.getWidth() && xPos + width > rect.getXPos() && yPos < rect.getYPos() + rect.getHeight() && height + yPos > rect.getYPos()) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public Edge touching(Rectangle rect) {
		if ((xPos + width == rect.xPos || rect.xPos + rect.getWidth() == xPos) && yPos <= rect.getYPos() + rect.getHeight() && height + yPos >= rect.getYPos()) {
			if (xPos + width == rect.xPos) {
				return Edge.RIGHT;
			}
			else {
				return Edge.LEFT;
			}
		}
		else if ((yPos + height == rect.yPos || rect.yPos + rect.getHeight() == yPos) && xPos <= rect.getXPos() + rect.getWidth() && xPos + width >= rect.getXPos()) {
			if (yPos + height == rect.yPos) {
				return Edge.BOTTOM;
			}
			else {
				return Edge.TOP;
			}
		}
		else {
			return Edge.NONE;
		}
	}
	
	public Edge touchingHorizontal(Rectangle rect) {
		if ((yPos + height == rect.yPos || rect.yPos + rect.getHeight() == yPos) && xPos <= rect.getXPos() + rect.getWidth() && xPos + width >= rect.getXPos()) {
			if (xPos + width == rect.getXPos() || rect.getXPos() + rect.getWidth() == xPos) {
				return Edge.NONE;
			}
			if (yPos + height == rect.yPos) {
				return Edge.BOTTOM;
			}
			else {
				return Edge.TOP;
			}
		}
		else {
			return Edge.NONE;
		}
	}
	
	public Edge touchingVertical(Rectangle rect) {
		if ((xPos + width == rect.xPos || rect.xPos + rect.getWidth() == xPos) && yPos <= rect.getYPos() + rect.getHeight() && height + yPos >= rect.getYPos()) {
			if (yPos + height == rect.getYPos() || rect.getYPos() + rect.getHeight() == yPos) {
				return Edge.NONE;
			}
			
			if (xPos + width == rect.xPos) {
				return Edge.RIGHT;
			}
			else {
				return Edge.LEFT;
			}
		}
		else {
			return Edge.NONE;
		}
	}
}
