package game.object;

import java.awt.image.BufferedImage;

public abstract class Pickup extends GameObject {

	private int liveTime;
	private int timePassed;
	
	private boolean infinite;
	
	public Pickup(int liveTime, double xPos, double yPos, double width, double height, BufferedImage sprite) {
		super(xPos, yPos, width, height, sprite);
		this.liveTime = liveTime;
		if (liveTime == -1) {
			infinite = true;
		}
	}
	
	public int getLiveTime() {
		return liveTime;
	}

	public void setLiveTime(int liveTime) {
		this.liveTime = liveTime;
	}
	
	@Override
	public void tick(int deltaTime) {
		if (!infinite) {
			if (timePassed >= liveTime) {
				delete();
			}
			timePassed += deltaTime;
		}
		super.tick(deltaTime);
	}
	
	public abstract void onPickup();
	
}
