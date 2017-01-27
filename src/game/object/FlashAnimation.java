package game.object;

import java.awt.AlphaComposite;

public class FlashAnimation {

	private int delay;
	private int timePassed;
	private int totalTime;
	private int totalTimePassed;
	
	private Player player;
	private AlphaComposite composite;
	
	public FlashAnimation(int delay, int totalTime, Player player) {
		this.delay = delay;
		this.totalTime = totalTime;
		this.player = player;
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
	}
	
	public void tick(int deltaTime) {
		timePassed += deltaTime;
		totalTimePassed += deltaTime;
		if (totalTimePassed >= totalTime) {
			player.setInvulnerable(false);
			reset();
			return;
		}
		if (timePassed >= delay) {
			timePassed = 0;
			if (composite.getAlpha() == 1) {
				composite = composite.derive(0f);
			}
			else {
				composite = composite.derive(1f);
			}
		}
	}
	
	public AlphaComposite getComposite() {
		return composite;
	}
	
	public void reset() {
		timePassed = 0;
		totalTimePassed = 0;
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
	}
}
