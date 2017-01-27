package game.object;

import java.awt.image.BufferedImage;

import game.Resources;

public class MapIcon extends GameObject {

	private boolean locked;
	private Animation anim;
	
	public MapIcon(double xPos, double yPos) {
		super(xPos, yPos, 0.7, 0.3, Resources.getImage("map_icon_locked"));
		
		BufferedImage[] frames = new BufferedImage[]{Resources.getImage("map_icon_0"), Resources.getImage("map_icon_1"), Resources.getImage("map_icon_2"), Resources.getImage("map_icon_3"),
				Resources.getImage("map_icon_2"),Resources.getImage("map_icon_1")};
		int[] delays = new int[]{100, 100, 100, 100, 100, 100};
		
		locked = true;
		anim = new Animation(frames,delays);
	}
	
	public void lock() {
		locked = true;
		setAnimation(null);
	}
	
	public void unlock() {
		locked = false;
		setAnimation(anim);
	}
	
	public boolean isLocked() {
		return locked;
	}

}
