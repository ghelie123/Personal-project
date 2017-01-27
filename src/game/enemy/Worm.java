package game.enemy;

import game.Game;
import game.Resources;
import game.Vector2D;
import game.object.Animation;
import game.object.GameObject;

import java.awt.image.BufferedImage;

public class Worm extends Enemy {
	
	
	private double attackDuration;
	private int timePassed;
	private boolean attackStance;
	private int attackRegulator;
	private double originalXPos;
	private double originalYPos;
	
	
	private Animation wormAttack;
	
	public Worm(int maxHealth, int damage,double attackDuration, double xPos, double yPos, double width, double height, double mass, double coefficientStaticFriction, double coefficientKineticFriction, double coefficientDrag, BufferedImage sprite) {
		super(maxHealth, damage, xPos, yPos, width, height, mass, coefficientStaticFriction, coefficientKineticFriction, coefficientDrag, sprite);
		
		this.attackDuration = attackDuration;
		this.originalXPos = xPos;
		this.originalYPos = yPos;
		wormAttack = new Animation(new BufferedImage[]{Resources.getImage("worm_0"),Resources.getImage("worm_1")
				,Resources.getImage("worm_2")} , new int[]{100,100,100});
		
}
	
	public void tick(int deltaTime){
		
		if (Game.getPlayer().getXPos() >= getXPos() - 4){
		if (attackRegulator % 120 == 0){
			attackStance = true;
		}
		
		if (attackStance){
			if (getAnimation() == null){
			setAnimation(wormAttack);
			}
			if (timePassed <= attackDuration/2){
				applyForce(new Vector2D(0,-200));
				
			}
			if (timePassed > attackDuration/2 ){
				
				applyForce(new Vector2D(0,200));
				
			}
			if (timePassed >= attackDuration){
				attackStance = false;
				setAnimation(null);
			}
			
		}
		
		
		
		if (!attackStance){
			setXPos(originalXPos);
			setYPos(originalYPos);
			timePassed = 0;
			attackRegulator++;
		}
		if (attackStance){
		timePassed += deltaTime;
		}
		super.tick(deltaTime);
		}
	}
	
	public void computeCollision(GameObject object){
		return;
	}
	
}
