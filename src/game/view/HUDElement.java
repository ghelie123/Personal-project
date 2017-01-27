package game.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class HUDElement {
	
	private double xPos;
	private double yPos;
	private double width;
	private double height;
	private BufferedImage sprite;
	private boolean invisible;
	
	public HUDElement(double xPos, double yPos, double width, double height, BufferedImage sprite) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}

	public abstract void render(Graphics2D g);

	public abstract void tick(int deltaTime);

	public double getXPos() {
		return xPos;
	}

	public double getYPos() {
		return yPos;
	}
	
	public BufferedImage getSprite() {
		return sprite;
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
	
	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}

	public void show() {
		invisible = false;
	}
	
	public void hide() {
		invisible = true;
	}
	
	public boolean isInvisible() {
		return invisible;
	}
}
