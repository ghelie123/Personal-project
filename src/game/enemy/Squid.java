package game.enemy;
import game.Direction;
import game.Edge;
import game.Resources;
import game.Vector2D;
import game.object.Animation;

import java.awt.image.BufferedImage;

public class Squid extends Enemy {
	
	public Squid(int maxHealth, int damage, double xPos, double yPos,
			double width, double height, double mass,
			double coefficientStaticFriction,
			double coefficientKineticFriction, double coefficientDrag,
			BufferedImage sprite) {
		
		super(maxHealth, damage, xPos, yPos, width, height, mass,
				coefficientStaticFriction, coefficientKineticFriction, coefficientDrag,
				sprite);
		setAnimation(new Animation(new BufferedImage[]{Resources.getImage("squid_0"), Resources.getImage("squid_1")}, new int[]{300,300}));
	}

	
	@Override
	public void tick(int deltaTime) {
		//System.out.println(getXPos() + " , " + getYPos());
		//System.out.println(getNormalDirectionX());
		//System.out.println("hi");
		if (getDirection() == Direction.LEFT) {
			applyForce(new Vector2D(-250,0));
			setFlippedX(true);
		}
		else if (getDirection() == Direction.RIGHT) {
			applyForce(new Vector2D(250,0));
			setFlippedX(false);
		}
		else if (getDirection() == Direction.UP){
			applyForce(new Vector2D(0,-250));
		}
		else if (getDirection() == Direction.DOWN){
			applyForce(new Vector2D(0,250));
		}
		
		
		if (getNormalDirectionX() == Edge.RIGHT) {
			setDirection(Direction.LEFT);
		}
		else if (getNormalDirectionX() == Edge.LEFT) {
			setDirection(Direction.RIGHT);
		}
		else if (getNormalDirectionY() == Edge.BOTTOM){
			setDirection(Direction.UP);
		}
		else if (getNormalDirectionY() == Edge.TOP){
			setDirection(Direction.DOWN);
		}
		
		super.tick(deltaTime);
		
	}
}
