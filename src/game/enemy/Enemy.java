package game.enemy;

import game.Direction;
import game.object.PhysicsObject;

import java.awt.image.BufferedImage;

public class Enemy extends PhysicsObject {

	private int health;
	private int maxHealth;
	private int damage;
	
	private Direction direction;

	public Enemy(int maxHealth, int damage, double xPos, double yPos, double width, double height, double mass, double coefficientStaticFriction, double coefficientKineticFriction, double coefficientDrag, BufferedImage sprite) {
		super(xPos, yPos, width, height, mass, coefficientStaticFriction, coefficientKineticFriction, coefficientDrag, sprite);
		this.maxHealth = maxHealth;
		health = maxHealth;
		this.damage = damage;
		direction = Direction.RIGHT;
		freeze();
	}

	public int getHealth() {
		return health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}

	public int getDamage() {
		return damage;
	}
	
	public Direction getDirection() {
		return direction;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}
