package game.enemy;

import game.Direction;
import game.Edge;
import game.Resources;
import game.Vector2D;
import game.object.Animation;

import java.awt.image.BufferedImage;

public class Golum extends Enemy {

	public Golum(double xPos, double yPos) {
		super(1, 2, xPos, yPos, 2, 2, 200, 1, 1, 1, Resources.getImage("golum_0"));
		BufferedImage[] frames = new BufferedImage[]{Resources.getImage("golum_4"), Resources.getImage("golum_5"), Resources.getImage("golum_7"), Resources.getImage("golum_7")};
		int[] delays = new int[]{300,300,300,100};
		setAnimation(new Animation(frames, delays));
	}
	
	@Override
	public void tick(int deltaTime) {

		if (getDirection() == Direction.LEFT && isGrounded()) {
			applyForce(new Vector2D(-2000,0));
			setFlippedX(false);
		}
		else if (getDirection() == Direction.RIGHT && isGrounded()) {
			applyForce(new Vector2D(2000,0));
			setFlippedX(true);
		}
		
		
		if (getNormalDirectionX() == Edge.RIGHT) {
			setDirection(Direction.LEFT);
		}
		else if (getNormalDirectionX() == Edge.LEFT) {
			setDirection(Direction.RIGHT);
		}
		
		super.tick(deltaTime);
		
	}

}
