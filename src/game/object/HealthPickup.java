package game.object;

import java.awt.image.BufferedImage;
import game.Game;

public class HealthPickup extends Pickup {

	private int value;

	public HealthPickup(int value, int liveTime, double xPos, double yPos,
			BufferedImage sprite) {
		super(liveTime, xPos, yPos, 1, 0.8, sprite);
		// TODO Auto-generated constructor stub
		
		this.value = value;
	}

	@Override
	public void onPickup() {
		int maxHealth = Game.getPlayer().getMaxHealth();
		int health = Game.getPlayer().getHealth() + value;
		
		if (health > maxHealth){
			health = maxHealth;
		}
		Game.getPlayer().setHealth(health);
	}

}
