package game.object;

import java.awt.image.BufferedImage;


public class Animation {

	private BufferedImage[] frames;
	private int[] delays;
	private int currentFrame;
	private int timePassed;
	private int startFrame;
	
	public Animation(BufferedImage[] frames, int[] delays) {
		this.frames = frames;
		this.delays = delays;
		
	}
	
	public Animation(BufferedImage[] frames, int[] delays, int startFrame) {
		this(frames, delays);
		
		if (startFrame < frames.length) {
			this.startFrame = startFrame;
		}
	}
	
	public void tick(int deltaTime) {
		timePassed += deltaTime;
		if (timePassed >= delays[currentFrame]) {
			timePassed -= delays[currentFrame++];
			
			if (currentFrame >= frames.length) {
				currentFrame = 0;
			}
		}
	}
	
	public void reset() {
		currentFrame = startFrame;
	}
	
	public BufferedImage getCurrentFrame() {
		return frames[currentFrame];
	}
	
	public int getCurrentFrameIndex() {
		return currentFrame;
	}
}
