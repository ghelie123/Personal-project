package game.object;

import game.Edge;
import game.Game;
import game.Resources;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Liquid extends GameObject {
	
	public static final int LAVA = 0;
	public static final int WATER = 1;
	
	private BufferedImage stitchedImage;
	
	private double xView;
	private double xSpeed;
	private int type;

	public Liquid(double xPos, double yPos, int type) {
		super(xPos, yPos, 1, 1, Resources.getImage("lava_8"));
		this.type = type;
		xSpeed = 0.03;
		stitchedImage = new BufferedImage(2  * Game.getUnitX(), Game.getUnitY(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = stitchedImage.createGraphics();
		
		switch (type) {
			case LAVA:
				g.drawImage(Resources.getImage("lava_8"), 0, 0, Game.getUnitX(), Game.getUnitY(), null);
				g.drawImage(Resources.getImage("lava_8"), Game.getUnitX(), 0, Game.getUnitX(), Game.getUnitY(), null);
				break;
			case WATER:
				setSprite(Resources.getImage("cave_0"));
				g.drawImage(Resources.getImage("cave_0"), 0, 0, Game.getUnitX(), Game.getUnitY(), null);
				g.drawImage(Resources.getImage("cave_0"), Game.getUnitX(), 0, Game.getUnitX(), Game.getUnitY(), null);
				break;
			default:
				g.drawImage(Resources.getImage("lava_8"), 0, 0, Game.getUnitX(), Game.getUnitY(), null);
				g.drawImage(Resources.getImage("lava_8"), Game.getUnitX(), 0, Game.getUnitX(), Game.getUnitY(), null);
				break;
		}

		g.dispose();
	}
	
	@Override
	public void tick(int deltaTime) {
		xView += xSpeed * deltaTime;
		if (xView < 0) {
			xView = (getWidth() * Game.getUnitX()) + xView;
		}
		if (xView + (getWidth() * Game.getUnitX()) >= stitchedImage.getWidth()) {
			xView = (xView + (getWidth() * Game.getUnitX())) - stitchedImage.getWidth();
		}
		setSprite(stitchedImage.getSubimage((int) xView, 0, Game.getUnitX(), stitchedImage.getHeight()));
		super.tick(deltaTime);
	}
	
	@Override
	public void computeCollision(GameObject object) {

	}
	
	@Override
	public void computeEdgeTouch(Edge edgeHorizontal, Edge edgeVertical,
			int deltaTime, GameObject object) {
	}
	
	public int getType() {
		return type;
	}

}
