package game.object;

import game.Game;
import java.awt.image.BufferedImage;

public class FuelPickup extends Pickup {

	private double value;
	
	public FuelPickup(double value, int liveTime, double xPos, double yPos, BufferedImage sprite) {
		super(liveTime, xPos, yPos, 0.7, 1, sprite);
		this.value = value;
	}

	@Override
	public void onPickup() {
		double maxFuel = Game.getPlayer().getMaxFuel();
		double fuel = Game.getPlayer().getFuel() + value;
		if (fuel >  maxFuel) {
			fuel = maxFuel;
		}
		Game.getPlayer().setFuel(fuel);
	}

}