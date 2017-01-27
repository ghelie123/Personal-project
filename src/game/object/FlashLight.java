package game.object;

import game.Game;

import java.awt.image.BufferedImage;

public class FlashLight extends Pickup {
	
	private int batteryLife;
	private int timePassed;
	private boolean startTimer;
	private boolean alreadyChanged;

	public FlashLight(int liveTime,int batteryLife, double xPos, double yPos, double width,
			double height, BufferedImage sprite) {
		super(liveTime, xPos, yPos, width, height, sprite);
		this.batteryLife = batteryLife;
		
	}
	
	public void tick (int deltaTime){
		if (startTimer){
			if (Game.getPlayer().getFlashlightOn() == true){
				
			}
		if (timePassed >= Game.getPlayer().getBatteryLife()) {
			Game.getPlayer().turnOnFlashlight(false);
			Game.getViewPort().changeSpotlight(true);
			delete();
		}
		timePassed += deltaTime;
		}
		
		super.tick(deltaTime);
	}

	@Override
	public void onPickup() {
		if (!isInvisible()){
		Game.getPlayer().turnOnFlashlight(true);
		Game.getViewPort().changeSpotlight(true);
		Game.getPlayer().setBatteryLife(Game.getPlayer().getBatteryLife()+batteryLife);
		setInvisible(true);
		startTimer = true;
		}
		
	}

}
