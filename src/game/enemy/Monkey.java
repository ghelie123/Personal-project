package game.enemy;

import game.Direction;
import game.Game;
import game.Resources;
import game.Vector2D;
import game.object.Animation;
import game.object.Projectile;

import java.awt.image.BufferedImage;

public class Monkey extends Enemy {
	
	private boolean flipped;
	private boolean alreadyAttacked;
	private int attackRegulator;

	public Monkey(boolean flipped , int maxHealth, int damage, double xPos, double yPos,
			double width, double height, double mass,
			double coefficientStaticFriction,
			double coefficientKineticFriction, double coefficientDrag,
			BufferedImage sprite) {
		
		super(maxHealth, damage, xPos, yPos, width, height, mass,
				coefficientStaticFriction, coefficientKineticFriction, coefficientDrag,
				sprite);
		
		this.flipped = flipped;
		setFlippedX(flipped);
		
		
		
	}
	
	public void tick(int deltaTime) {
		if(attackRegulator % 120 == 0){
			alreadyAttacked = false;
		}
		if (getDirection() == Direction.LEFT && isGrounded()) {
			applyForce(new Vector2D(-250,0));
			setFlippedX(true);
		}
		else if (getDirection() == Direction.RIGHT && isGrounded()) {
			applyForce(new Vector2D(250,0));
			setFlippedX(false);
		}
		else if (getDirection() == Direction.STATIC && isGrounded()){
			setFlippedX(flipped);
		}
		
		
		if (Game.getPlayer().getXPos() >= (this.getXPos() - 8) && !alreadyAttacked){
			if (getAnimation()==null){
			setAnimation(new Animation(new BufferedImage[]{Resources.getImage("monkey_0"),Resources.getImage("monkey_1"),Resources.getImage("monkey_2")}, new int[]{100,100,100}));
			}
			if(getAnimation().getCurrentFrameIndex() == 2){
			attack();
			alreadyAttacked = true;
			}
			
		}
		else {
			setAnimation(null);
		}
		attackRegulator++;
		super.tick(deltaTime);
		
	}
	
	public void attack(){
		
		if(isFlippedX()){
		Game.getViewPort().getLevel().addObject(new Projectile(Projectile.BANANA,-50,getXPos(),getYPos()+0.4,0.5,0.5,0.1,0,0,0,Resources.getImage("banana")));
		}
		else{
		Game.getViewPort().getLevel().addObject(new Projectile(Projectile.BANANA,50,getXPos()+0.5,getYPos()+0.9,0.5,0.5,0.1,0,0,0,Resources.getImage("banana")));
		}
	}

}
