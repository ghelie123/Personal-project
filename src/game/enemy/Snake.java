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



public class Snake extends Enemy {
	
	private boolean alreadyAttacked;
	private int attackRegulator;
	private boolean flipped;
	
	public Snake(boolean flipped , int maxHealth, int damage, double xPos, double yPos, double width, double height, double mass, double coefficientStaticFriction, 
			double coefficientKineticFriction, double coefficientDrag, BufferedImage sprite) {
		
		super(maxHealth, damage, xPos, yPos, width, height, mass, coefficientStaticFriction, coefficientKineticFriction, coefficientDrag, sprite);
		this.flipped = flipped;
		setFlippedX(flipped);
	}
	
	
	

	public void tick(int deltaTime) {
		if(attackRegulator % 180 == 0){
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
		if (Game.getPlayer().getXPos() >= (this.getXPos() - 4)){
			if (!alreadyAttacked){
			attack();
			}
		}
		attackRegulator++;
		super.tick(deltaTime);
		
	}
	
	public void attack(){
		alreadyAttacked = true;
		if(isFlippedX()){
		Game.getViewPort().getLevel().addObject(new Projectile(Projectile.VENOM,50,getXPos()+0.4,getYPos()+0.3,0.2,0.2,0.1,0,0,0,Resources.getImage("venom")));
		}
		else{
		Game.getViewPort().getLevel().addObject(new Projectile(Projectile.VENOM,-50,getXPos()+0.3,getYPos()+0.3,0.2,0.2,0.1,0,0,0,Resources.getImage("venom")));
		}
	}

}
