package game.level;

import java.awt.image.BufferedImage;


public class BackgroundLayer {

	private BufferedImage image;
	private double xPos;
	private double yPos;
	private double width;
	private double height;
	
	public BackgroundLayer(BufferedImage image, double xPos, double yPos, double width, double height) {
		this.image = image;
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		
	}

	public BufferedImage getImage() {
		return image;
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

	public void setImage(BufferedImage image) {
		this.image = image;
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
	
}
