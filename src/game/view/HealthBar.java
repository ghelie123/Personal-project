package game.view;

import game.Game;
import game.Resources;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class HealthBar extends HUDElement {
	
	private BufferedImage[] hearts;
	private ArrayList<Integer> displayedHearts;

	public HealthBar(double xPos, double yPos) {
		super(xPos, yPos, 0,0,null);
		hearts = new BufferedImage[] {Resources.getImage("heart_empty"), Resources.getImage("heart_full"), Resources.getImage("heart_half")};
		displayedHearts = new ArrayList<Integer>();
	}

	@Override
	public void render(Graphics2D g) {
		if (isInvisible()) {
			return;
		}
		
		double dx = 0;
		for (int i = 0; i < displayedHearts.size(); i++) {
			g.drawImage(hearts[displayedHearts.get(i)], (int)((getXPos() + dx) * Game.getUnitX()), (int)(getYPos() * Game.getUnitY()), (int)(0.6 * Game.getUnitX()), (int)(0.5 * Game.getUnitY()), null);
			dx += 0.7;
		}
	}

	@Override
	public void tick(int deltaTime) {
		// Compute hearts to display
		displayedHearts.clear();
		int maxHealth = Game.getPlayer().getMaxHealth();
		int health = Game.getPlayer().getHealth();
		int numEmpty = maxHealth - health;
		boolean even = (health % 2 == 0); 
		
		if (even) {
			for (int i = 0; i < health; i+=2) {
				displayedHearts.add(1);
			}
			
			for (int i = 0; i < numEmpty; i+=2) {
				displayedHearts.add(0);
			}
		}
		else {
			for (int i = 0; i < health - 1; i+=2) {
				displayedHearts.add(1);
			}
			displayedHearts.add(2);
			for (int i = 0; i < numEmpty - 1; i+=2) {
				displayedHearts.add(0);
			}
		}
	}

}
