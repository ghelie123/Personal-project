package game.view;

import game.Game;
import game.Resources;

import java.awt.Color;
import java.awt.Graphics2D;

public class FuelBar extends HUDElement{
	
	private static final Color COLOR = new Color(0x00FF6E);
	
	public FuelBar(double xPos, double yPos) {
		super(xPos, yPos,0,0,null);
	}

	@Override
	public void render(Graphics2D g) {
		if (isInvisible()) {
			return;
		}
		
		g.drawImage(Resources.getImage("can_empty"),(int)(getXPos() * Game.getUnitX()), (int)(getYPos() * Game.getUnitY()), (int)(1 * Game.getUnitX()), (int)(1.727 * Game.getUnitY()), null);
		
		g.setColor(COLOR);
		double dy = 1 - (Game.getPlayer().getFuel() / 100.0);
		int x = (int)((getXPos() + 0.25) * Game.getUnitX());
		int y = (int)((getYPos() + 0.45 + dy) * Game.getUnitY());
		int width = (int)(0.5 * Game.getUnitX());
		int height = (int)((1 - dy) * Game.getUnitY());
		g.fillRect(x, y, width, height);
		
		g.drawImage(Resources.getImage("can_glass"),(int)(getXPos() * Game.getUnitX()), (int)(getYPos() * Game.getUnitY()), (int)(1 * Game.getUnitX()), (int)(1.727 * Game.getUnitY()), null);
		
	}

	@Override
	public void tick(int deltaTime) {
	}

}
