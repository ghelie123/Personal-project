package game.level;

import game.Direction;
import game.Game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class LoopingBackgroundLayer extends BackgroundLayer {
	
	private BufferedImage stitchedImage;
	private BufferedImage image;
	private double xView;
	private double yView;
	private double xSpeed;
	private double ySpeed;
	
	private Direction direction;
	private double width;
	private double height;

	public LoopingBackgroundLayer(BufferedImage image, double xPos, double yPos, double width, double height, Direction direction, double speed) {
		super(image, xPos, yPos, width, height);		
		this.direction = direction;
		this.image = image;
		this.width = width;
		this.height = height;
		
		Graphics2D g;
		switch (direction) {
		case UP:
			stitchedImage = new BufferedImage((int)(width * Game.getUnitX()), (int)(height * 2 * Game.getUnitY()), BufferedImage.TYPE_INT_ARGB);
			ySpeed = speed;
			g = stitchedImage.createGraphics();
			g.drawImage(image, 0, 0, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.drawImage(image, 0, (int)(height * Game.getUnitY()), (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.dispose();
			break;
		case DOWN:
			stitchedImage = new BufferedImage((int)(width * Game.getUnitX()), (int)(height * Game.getUnitY() * 2), BufferedImage.TYPE_INT_ARGB);
			ySpeed = -speed;
			yView = (height * Game.getUnitY());
			g = stitchedImage.createGraphics();
			g.drawImage(image, 0, 0, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.drawImage(image, 0, (int)(height * Game.getUnitY()), (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.dispose();
			break;
		case LEFT:
			stitchedImage = new BufferedImage((int)(width * Game.getUnitX() * 2), (int)(height * Game.getUnitY()), BufferedImage.TYPE_INT_ARGB);
			xSpeed = speed;
			g = stitchedImage.createGraphics();
			g.drawImage(image, 0, 0, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.drawImage(image, (int)(width * Game.getUnitX()), 0, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.dispose();
			break;
		case RIGHT:
			stitchedImage = new BufferedImage((int)(width * Game.getUnitX() * 2), (int)(height * Game.getUnitY()), BufferedImage.TYPE_INT_ARGB);
			xSpeed = -speed;
			xView = (width * Game.getUnitX());
			g = stitchedImage.createGraphics();
			g.drawImage(image, 0, 0, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.drawImage(image, (int)(width * Game.getUnitX()), 0, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.dispose();
			break;
		default:
			stitchedImage = image;
		}
		
		
	}
	
	public void tick(int deltaTime) {
		xView += xSpeed * deltaTime;
		yView += ySpeed * deltaTime;
		
		if (xView < 0) {
			xView = (getWidth() * Game.getUnitX()) + xView;
		}
		if (xView + (getWidth() * Game.getUnitX()) >= stitchedImage.getWidth()) {
			xView = (xView + (getWidth() * Game.getUnitX())) - stitchedImage.getWidth();
		}
		if (yView < 0) {
			yView = (getHeight() * Game.getUnitY()) + yView;
		}
		if (yView + (getHeight() * Game.getUnitY()) >= stitchedImage.getHeight()) {
			yView = (yView + (getHeight() * Game.getUnitY())) - stitchedImage.getHeight();
		}
		setImage(stitchedImage.getSubimage((int) xView, (int) yView, (int)(getWidth() * Game.getUnitX()), (int)(getHeight() * Game.getUnitY())));
	}
	
	public void recompute() {
		Graphics2D g;
		switch (direction) {
		case UP:
			stitchedImage = new BufferedImage((int)(width * Game.getUnitX()), (int)(height * 2 * Game.getUnitY()), BufferedImage.TYPE_INT_ARGB);
			g = stitchedImage.createGraphics();
			g.drawImage(image, 0, 0, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.drawImage(image, 0, (int)(height * Game.getUnitY()), (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.dispose();
			break;
		case DOWN:
			stitchedImage = new BufferedImage((int)(width * Game.getUnitX()), (int)(height * Game.getUnitY() * 2), BufferedImage.TYPE_INT_ARGB);
			yView = (height * Game.getUnitY());
			g = stitchedImage.createGraphics();
			g.drawImage(image, 0, 0, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.drawImage(image, 0, (int)(height * Game.getUnitY()), (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.dispose();
			break;
		case LEFT:
			stitchedImage = new BufferedImage((int)(width * Game.getUnitX() * 2), (int)(height * Game.getUnitY()), BufferedImage.TYPE_INT_ARGB);
			g = stitchedImage.createGraphics();
			g.drawImage(image, 0, 0, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.drawImage(image, (int)(width * Game.getUnitX()), 0, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.dispose();
			break;
		case RIGHT:
			stitchedImage = new BufferedImage((int)(width * Game.getUnitX() * 2), (int)(height * Game.getUnitY()), BufferedImage.TYPE_INT_ARGB);
			xView = (width * Game.getUnitX());
			g = stitchedImage.createGraphics();
			g.drawImage(image, 0, 0, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.drawImage(image, (int)(width * Game.getUnitX()), 0, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
			g.dispose();
			break;
		default:
			stitchedImage = image;
		}
	}

}
