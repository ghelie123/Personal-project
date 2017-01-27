package game.enemy;

import game.Direction;
import game.Edge;
import game.Game;
import game.Resources;
import game.Vector2D;
import game.object.Animation;
import game.object.Player;
import game.object.Projectile;

import java.awt.image.BufferedImage;

public class Archer extends Enemy {
	
	private boolean alreadyAttacked;
	private int attackRegulator;
	private boolean flipped;

	public Archer(boolean flipped , int maxHealth, int damage, double xPos, double yPos,double width, double height, double mass,double coefficientStaticFriction,
			double coefficientKineticFriction, double coefficientDrag,BufferedImage sprite) {
		
		super(maxHealth, damage, xPos, yPos, width, height, mass,coefficientStaticFriction, coefficientKineticFriction, coefficientDrag,sprite);
		
		this.flipped = flipped;
		setFlippedX(flipped);
		
		//setAnimation(new Animation(new BufferedImage[]{Resources.getImage("archer_0"),Resources.getImage("archer_2"),Resources.getImage("archer_1")}, new int[]{1000,1000,10}));
	}
	
	public void tick(int deltaTime) {
		if(attackRegulator % 30 == 0){
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
			setAnimation(new Animation(new BufferedImage[]{Resources.getImage("archer_0"),Resources.getImage("archer_2"),Resources.getImage("archer_1")}, new int[]{1000,1000,10}));
			}
			if(getAnimation().getCurrentFrameIndex() == 2){
			alreadyAttacked = true;
			attack();
			}
		}
		else {
			setAnimation(null);
		}
		attackRegulator++;
		super.tick(deltaTime);
		
	}
	
	public void attack(){
		alreadyAttacked = true;
		if(isFlippedX()){
		Game.getViewPort().getLevel().addObject(new Projectile(Projectile.ARROW,100,getXPos()+0.8,getYPos()+0.9,1,0.2,0.1,0,0,0,Resources.getImage("arrow2")));
		}
		else{
		Game.getViewPort().getLevel().addObject(new Projectile(Projectile.ARROW,-100,getXPos(),getYPos()+0.9,1,0.2,0.1,0,0,0,Resources.getImage("arrow")));
		}
	}

}
