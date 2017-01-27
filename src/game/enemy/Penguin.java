package game.enemy;

import game.Direction;
import game.Edge;
import game.Resources;
import game.Vector2D;
import game.object.Animation;

import java.awt.image.BufferedImage;

public class Penguin extends Enemy {

	public Penguin(int maxHealth, int damage, double xPos, double yPos, double width, double height, double mass, double coefficientStaticFriction, double coefficientKineticFriction, double coefficientDrag, BufferedImage sprite) {
		super(maxHealth, damage, xPos, yPos, width, height, mass, coefficientStaticFriction, coefficientKineticFriction, coefficientDrag, sprite);
		setAnimation(new Animation(new BufferedImage[]{Resources.getImage("penguin_0"),Resources.getImage("penguin_2"),Resources.getImage("penguin_3"),Resources.getImage("penguin_3")}, new int[]{100,100,100,100}));
	}

	@Override
	public void tick(int deltaTime) {
		//System.out.println(getXPos() + " , " + getYPos());
		//System.out.println(getNormalDirectionX());
		if (getDirection() == Direction.LEFT && isGrounded()) {
			applyForce(new Vector2D(-250,0));
			setFlippedX(true);
		}
		else if (getDirection() == Direction.RIGHT && isGrounded()) {
			applyForce(new Vector2D(250,0));
			setFlippedX(false);
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
