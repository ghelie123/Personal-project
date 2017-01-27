package game.object;

import game.Edge;
import game.Game;
import game.Rectangle;
import game.Vector2D;
import game.enemy.Worm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


public class GameObject {
	
	private double xPos;
	private double yPos;
	private double width;
	private double height;
	private double rotation;
	private BufferedImage sprite;
	private Animation currentAnimation;
	private Rectangle collider;
	
	private AffineTransformOp flipOperationX;
	private AffineTransformOp flipOperationY;
	private AffineTransformOp waterFlipOperationX;
	
	private boolean invisible;
	private boolean frozen;
	private boolean flippedX;
	private boolean flippedY;
	private boolean waterFlippedX;
	private boolean deleted;
	private boolean collidable;
	
	private String name;
	
	public GameObject(double xPos , double yPos , double width, double height, BufferedImage sprite){
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		collidable = true;
		collider = new Rectangle(xPos, yPos, width, height);
		
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-sprite.getWidth(null), 0);
		flipOperationX = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		
		AffineTransform ty = AffineTransform.getScaleInstance(1, -1);
		ty.translate(0, -sprite.getHeight(null));
		flipOperationY = new AffineTransformOp(ty, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		
		AffineTransform waterTx = AffineTransform.getScaleInstance(-1, 1);
		waterTx.translate(-sprite.getHeight(), 0);
		waterFlipOperationX = new AffineTransformOp(waterTx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		
		name = "";
	}
	
	public void tick(int deltaTime){
		
		// Recompute collision box
		collider.setBounds(xPos, yPos, width, height);
		
		// Update animation
		if (currentAnimation != null) {
			currentAnimation.tick(deltaTime);
		}
		
	}
	
	public void render(Graphics2D g){
		//Renders the object
		BufferedImage image = null;
		if (currentAnimation == null) {
			image = sprite;
		}
		else {
			image = currentAnimation.getCurrentFrame();
		}
		
		// Flip
		if (flippedX) {
			BufferedImage flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
			flipOperationX.filter(image, flippedImage);
			image = flippedImage;
		}
		if (flippedY) {
			BufferedImage flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
			flipOperationY.filter(image, flippedImage);
			image = flippedImage;
		}
		if (waterFlippedX) {
			BufferedImage flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
			waterFlipOperationX.filter(image, flippedImage);
			image = flippedImage;
		}
		
		int x = (int)((xPos - Game.getViewPort().getXPos()) * Game.getUnitX());
		int y = (int)((yPos - Game.getViewPort().getYPos()) * Game.getUnitY());
		
		g.drawImage(image, x, y, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
		
		if (Game.DRAW_COLLIDER) {
			g.setColor(Color.BLUE);
			g.setStroke(new BasicStroke(2));
			g.drawRect( x, y, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()));
			g.setStroke(new BasicStroke(1));
		}
	}
	
	public boolean isColliding(GameObject object){
		//Returns true if their is a collision between the 2 objects
		return (collider.inside(object.getCollider()));
	}
	
	public Edge isTouching(GameObject object) {
		return (collider.touching(object.getCollider()));
	}
	
	public void computeCollision(GameObject object) {
		if (object instanceof FallingSpike) {
			FallingSpike spike = (FallingSpike) object;
			if (name.equals("SNOW_CEILING")) {
				return;
			}
			spike.shatter(this);
			return;
		}
		
		if (object instanceof Worm){
			System.out.println("hi");
			return;
		}
		
		if (object instanceof Projectile){
			((Projectile) object).shatter(this);
			return;
			
		}
		
		if (object instanceof PhysicsObject) {
			PhysicsObject object2 = (PhysicsObject) object;
			// Push out of object
			Vector2D deltaPos = new Vector2D(object2.getVelocity().getXComponent(), object2.getVelocity().getYComponent());
			deltaPos.setMagnitude(1);
			deltaPos.reverse();
			double deltaX = 0;
			double deltaY = 0;
			
			double xDirection = object2.getXPos() - object2.getLastXPos();
			double yDirection = object2.getYPos() - object2.getLastYPos();
			
			if (xDirection < 0) {
				deltaX = getXPos() + getWidth() - object2.getXPos();
			}
			else if (xDirection > 0){
				deltaX = object2.getXPos() + object2.getWidth() - getXPos();
			}
						
			if (yDirection < 0) {
				deltaY = getYPos() + getHeight() - object2.getYPos();
			}
			else if (yDirection > 0) {
				deltaY = object2.getYPos() + object2.getHeight() - getYPos();
			}
			//System.out.println(deltaX + " " + deltaY);
						
						
			// If rubbing

						
			// Solve linear combination
			if (deltaY != 0 && deltaX == 0) {
				deltaPos.setXComponent(0);
				deltaPos.setYComponent(deltaY);
			}
			else if (deltaX != 0 && deltaY == 0) {
				deltaPos.setXComponent(deltaX);
				deltaPos.setYComponent(0);
			}
			else if (deltaY < deltaX) {
				double k = deltaY / deltaPos.getYComponent();
				deltaPos.setXComponent(k * deltaPos.getXComponent());
				deltaPos.setYComponent(deltaY);
			}
			else {
				double k = deltaX / deltaPos.getXComponent();
				deltaPos.setXComponent(deltaX);
				deltaPos.setYComponent(k * deltaPos.getYComponent());
				if (deltaPos.getYComponent() > deltaY) {
					deltaPos.setYComponent(deltaY);
				}
			}
						
			// Adjust sign
			if (yDirection > 0) {
				deltaPos.setYComponent(-1 * deltaPos.getYComponent());
			}
			if (xDirection > 0) {
				deltaPos.setXComponent(-1 * deltaPos.getXComponent());
			}
						
			//System.out.println("collision! dx: " + deltaPos.getXComponent() + " dy: " + deltaPos.getYComponent());
						
			// Move self
			object2.setLastXPos(object2.getXPos());
			object2.setLastYPos(object2.getYPos());
			
			object2.setXPos(object2.getXPos() + deltaPos.getXComponent());
			object2.setYPos(object2.getYPos() + deltaPos.getYComponent());

			object2.getCollider().setBounds(object2.getXPos(), object2.getYPos(), object2.getWidth(), object2.getHeight());
		}
		else {
			
		}
	}
	
	public void computeEdgeTouch(Edge edgeHorizontal, Edge edgeVertical, int deltaTime, GameObject object) {
		if (object instanceof PhysicsObject) {
			PhysicsObject object2 = (PhysicsObject) object;
			if (object2.getNormalDirectionX() == Edge.NONE) {
				if (edgeVertical == Edge.LEFT) {
					object2.setNormalDirectionX(Edge.RIGHT);
				}
				else if (edgeVertical == Edge.RIGHT){
					object2.setNormalDirectionX(Edge.LEFT);
				}
				else {
					object2.setNormalDirectionX(edgeVertical);
				}
			}
			if (object2.getNormalDirectionY() == Edge.NONE) {
				if (edgeHorizontal == Edge.TOP) {
					object2.setNormalDirectionY(Edge.BOTTOM);
				}
				else if (edgeHorizontal == Edge.BOTTOM){
					object2.setNormalDirectionY(Edge.TOP);
				}
				else {
					object2.setNormalDirectionY(edgeHorizontal);
				}
			}
		}
	}

	public double getXPos(){
		return xPos;
	}
	
	public double getYPos(){
		return yPos;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getRotation(){
		return rotation;
	}
	
	public BufferedImage getSprite(){
		return sprite;
	}
	
	public Animation getAnimation(){
		return currentAnimation;
	}
	
	public Rectangle getCollider() {
		return collider;
	}
	
	public boolean isInvisible() {
		return invisible;
	}
	
	public boolean isFrozen() {
		return frozen;
	}
	
	public boolean isFlippedX() {
		return flippedX;
	}
	
	public boolean isFlippedY() {
		return flippedY;
	}
	
	public boolean isWaterFlippedX(){
		return waterFlippedX;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public boolean isCollidable() {
		return collidable;
	}
	
	public String getName() {
		return name;
	}
	
	public void setXPos(double xPos){
		this.xPos = xPos;
	}
	
	public void setYPos(double yPos){
		this.yPos = yPos;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public void setRotation(double rotation){
		this.rotation = rotation;
	}
	
	public void setSprite(BufferedImage sprite){
		this.sprite = sprite;
	}
	
	public void setAnimation(Animation animation){
		this.currentAnimation = animation;
	}
	
	public void setCollider(Rectangle collider) {
		this.collider = collider;
	}
	
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}
	
	public void setFlippedX(boolean flipped) {
		this.flippedX = flipped;
	}
	
	public void setFlippedY(boolean flipped) {
		this.flippedY = flipped;
	}
	
	public void setWaterFlippedX(boolean flipped){
		this.waterFlippedX = flipped;
	}
	
	public void setVisible(boolean invisible) {
		this.invisible = !invisible;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}
	
	public void freeze() {
		frozen = true;
	}
	
	public void unfreeze() {
		frozen = false;
	}
	
	public void delete() {
		deleted = true;
	}

}
