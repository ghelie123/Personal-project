package game.object;

import game.Game;
import game.JukeBox;
import game.Resources;

import java.awt.image.BufferedImage;

public class FallingSpike extends PhysicsObject {
	
	public static final int ICE = 0;
	public static final int ROCK = 1;
	
	private boolean fall;
	private boolean shatter;
	
	private int type;
	
	private Animation shatterAnim;

	public FallingSpike(int type, double xPos, double yPos, double width, double height, double mass, double coefficientStaticFriction, double coefficientKineticFriction, double coefficientDrag, BufferedImage sprite) {
		super(xPos, yPos, width, height, mass, coefficientStaticFriction, coefficientKineticFriction, coefficientDrag, sprite);
		this.type = type;
		if (type == ICE) {
			BufferedImage[] images = new BufferedImage[]{Resources.getImage("ice_shatter_0"),Resources.getImage("ice_shatter_1"),Resources.getImage("ice_shatter_2"),Resources.getImage("ice_shatter_3"), Resources.getImage("ice_shatter_0")};
			int[] delays = new int[]{75, 75, 75, 75, 75};
			shatterAnim = new Animation(images, delays);
		}
		if (type == ROCK){
			BufferedImage[] images = new BufferedImage[]{Resources.getImage("rock_shatter_0"),Resources.getImage("rock_shatter_1"),Resources.getImage("rock_shatter_2"),Resources.getImage("rock_shatter_3"), Resources.getImage("rock_shatter_0")};
			int[] delays = new int[]{75, 75, 75, 75, 75};
			shatterAnim = new Animation(images, delays);
		}
	}
	
	@Override
	public void tick(int deltaTime) {
		if (type == ICE){
		if ((Game.getPlayer().getXPos() >= getXPos() - 4.5) && !shatter) {
			fall = true;
		}
		}
		if (type == ROCK){
			if ((Game.getPlayer().getXPos() >= getXPos() - 2) && !shatter) {
				fall = true;
			}
		}
		if (fall && !shatter) {
			super.tick(deltaTime);
		}
		if (shatter) {
			shatterAnim.tick(deltaTime);
		}
		if (shatterAnim.getCurrentFrameIndex() == 4) {
			delete();
		}
	}
	
	@Override
	public void computeCollision(GameObject object) {
		if (!object.getName().equals("SNOW_CEILING")) {
			shatter(object);
		}
	}
	
	public void shatter(GameObject object) {
		if (shatter) {
			return;
		}
		if (object instanceof Player) {
			Player p = (Player) object;
			p.damage(2);
		}
		if (getAnimation() == null) {
			if (type == ICE) {
				JukeBox.playSoundEffect("ice_shatter");
			}
			setAnimation(shatterAnim);
		}
		shatter = true;
	}

}
