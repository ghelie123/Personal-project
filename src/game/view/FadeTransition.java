package game.view;

import game.Game;
import game.level.Level;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class FadeTransition {

	private int timePassed;

	private AlphaComposite composite;
	private double slope;
	
	private boolean negative;
	private boolean finished;
	private boolean half;
	
	private Level level;
	
	public FadeTransition(int duration, Level level) {
		this.level = level;
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
		slope = 1/(duration / 2.0);
	}
	
	public void tick(int deltaTime) {
		if (finished) {
			return;
		}
		timePassed += deltaTime;
		float alpha = (float) ((timePassed * ((negative) ? -slope:slope)) + ((negative) ? 1:0));
		if (alpha <= 0) {
			composite = composite.derive(0f);
			finished = true;
			return;
		}
		if (alpha >= 1) {
			negative = true;
			half = true;
			timePassed = 0;
			alpha = (float) ((timePassed * ((negative) ? -slope:slope)) + ((negative) ? 1:0));
		}
		composite = composite.derive(alpha);
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public boolean isHalf() {
		return half;
	}
	
	public void setHalf(boolean  half) {
		this.half = half;
	}
	
	public void render(Graphics2D g) {
		g.setComposite(composite);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 16 * Game.getUnitX(), 9 * Game.getUnitY());
	}
	
	public Level getLevel() {
		return level;
	}
}
