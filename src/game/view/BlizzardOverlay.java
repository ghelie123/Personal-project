package game.view;

import java.awt.Color;

public class BlizzardOverlay extends RectangleOverlay {

	double slope;
	
	private boolean fadedIn;
	private boolean finished;
	private boolean fadeOut;
	
	private int timePassed;
	private float maxAlpha;
	
	public BlizzardOverlay() {
		super(Color.WHITE, 0);
		maxAlpha = 0.75f;
		slope = maxAlpha / 500.0;
	}
	
	@Override
	public void tick(int deltaTime) {
		if (finished) {
			return;
		}
		if (!fadedIn || fadeOut) {
			timePassed += deltaTime;
		}
		if (!fadedIn) {
			float alpha = (float)(slope * timePassed);
			if (alpha > maxAlpha) {
				alpha = maxAlpha;
				fadedIn = true;
				timePassed = 0;
			}
			if (alpha < 0) {
				alpha = 0;
			}
			setComposite(getComposite().derive(alpha));
		}
		if (fadeOut) {
			float alpha = (float)((slope * timePassed) + 0.75);
			if (alpha < 0) {
				alpha = 0;
				finished = true;
			}
			setComposite(getComposite().derive(alpha));
		}
	}
	
	public void fadeOut() {
		fadeOut = true;
		if (getComposite().getAlpha() != maxAlpha) {
			maxAlpha = getComposite().getAlpha();
		}
		slope = maxAlpha / -300.0;
	}
	
	public boolean isFinished() {
		return finished;
	}

}
