package game.view;

import game.Game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

public class RectangleOverlay {
	
	private Color color;
	private AlphaComposite composite;
	
	public RectangleOverlay(Color color, float alpha) {
		this.color = color;
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
	}
	
	public void tick(int deltaTime) {
		
	}
	
	public void render(Graphics2D g) {
		Composite temp = g.getComposite();
		g.setComposite(composite);
		g.setColor(color);
		g.fillRect(0, 0, 16 * Game.getUnitX(), 9 * Game.getUnitY());
		g.setComposite(temp);
	}
	
	public void setComposite(AlphaComposite composite) {
		this.composite = composite;
	}
	
	public AlphaComposite getComposite() {
		return composite;
	}
	
}
