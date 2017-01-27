package game.object;

import game.Edge;
import game.Game;
import game.Vector2D;
import game.enemy.Bat;
import game.enemy.Enemy;
import game.enemy.Octopus;
import game.enemy.Squid;
import game.enemy.Worm;
import game.level.Level;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class PhysicsObject extends GameObject {
	
	private double lastXPos;
	private double lastYPos;
	
	private double mass;
	private double coefficientStaticFriction;
	private double coefficientKineticFriction;
	private double coefficientDrag;
	
	private Vector2D netForce;
	private Vector2D acceleration;
	private Vector2D velocity;
	private Vector2D normal;
	
	private boolean verticalNormalApplied;
	private boolean horizontalNormalApplied;
	private Edge normalDirectionY;
	private Edge normalDirectionX;
	private String standingOn;
	
	private boolean grounded;
	private boolean ceiling;
	
	private ArrayList<Vector2D> appliedForces;

	public PhysicsObject(double xPos, double yPos, double width, double height, double mass, double coefficientStaticFriction, double coefficientKineticFriction, double coefficientDrag, BufferedImage sprite) {
		super(xPos, yPos, width, height, sprite);
		
		this.mass = mass;
		this.coefficientStaticFriction = coefficientStaticFriction;
		this.coefficientKineticFriction = coefficientKineticFriction;
		this.coefficientDrag = coefficientDrag;
		
		netForce = new Vector2D(0, 0);
		acceleration = new Vector2D(0, 0);
		velocity = new Vector2D(0, 0);
		normal = new Vector2D(0, 0);
		appliedForces = new ArrayList<Vector2D>();
		normalDirectionX = Edge.NONE;
		normalDirectionY = Edge.NONE;
		standingOn = "";
	}
	
	public void applyForce(Vector2D force) {
		normal.setXComponent(normal.getXComponent() + force.getXComponent() * -1);
		normal.setYComponent(normal.getYComponent() + force.getYComponent() * -1);
		appliedForces.add(force);
	}
	
	@Override
	public void tick(int deltaTime) {
		Level level = Game.getViewPort().getLevel();
		
		// Flip if needed
		if (level.getGravity().getYComponent() < 0) {
			setFlippedY(true);
		}
		else {
			setFlippedY(false);
		}
		
		// Set net force back to zero
		netForce.setXComponent(0);
		netForce.setYComponent(0);
		
		for (int i = 0; i < appliedForces.size(); i++) {
			netForce.add(appliedForces.get(i));
			//System.out.println(appliedForces.get(i));
		}
		appliedForces.clear();
		
		// Compute gravity
		if (!(this instanceof Octopus || this instanceof Squid || this instanceof Worm || this instanceof Bat)){
		netForce.add(new Vector2D(level.getGravity().getXComponent() * mass, level.getGravity().getYComponent() * mass));
		}
		//System.out.println(netForce);
		// Compute drag force
		// FIXME drag force will only work in specific direction for now!
		double density = level.getAirDensity();
		double area = getWidth() * getHeight();
		double relativeSpeedX = velocity.getXComponent();
		double relativeSpeedY = velocity.getYComponent();
		double forceX = density * area * coefficientDrag * Math.pow(relativeSpeedX, 2) / 2.0;
		double forceY = density * area * coefficientDrag * Math.pow(relativeSpeedY, 2) / 2.0;
		
		if (velocity.getXComponent() > 0) {
			forceX *= -1;
		}
		if (velocity.getYComponent() > 0) {
			forceY *= -1;
		}
		//System.out.println("x: " + forceX + " " + "y: " + forceY);
		netForce.add(new Vector2D(forceX, forceY));
		
		netForce.add(level.getWindForce());
		
		//System.out.println(netForce.getYComponent() + "  NET");
		// Normal force
		//System.out.println(contactingEdge);
		//System.out.println(normalDirectionX);
		//System.out.println(normalDirectionY);
		if (normalDirectionY == Edge.BOTTOM) {
			velocity.setYComponent(0);
			if (netForce.getYComponent() > 0) {
				normal.setYComponent(netForce.getYComponent() * -1);
				netForce.setYComponent(0);
				grounded = true;
			}
			
			if (velocity.getXComponent() != 0 && netForce.getYComponent() >= 0) {
				// Friction
				double friction = coefficientKineticFriction
						* Math.abs(normal.getYComponent());
				if (velocity.getXComponent() > 0) {
					friction *= -1;
				}
				
				if (!standingOn.equals("ICE")) {
					netForce.add(new Vector2D(friction, 0));
				}
			}
		}
		else {
			grounded = false;
		}
		
		if (normalDirectionY == Edge.TOP) {
			velocity.setYComponent(0);
			if (netForce.getYComponent() < 0) {
				normal.setYComponent(netForce.getYComponent());
				netForce.setYComponent(0);
				ceiling = true;
			}
			
			if (velocity.getXComponent() != 0 && netForce.getYComponent() <= 0 && Game.getViewPort().getLevel().isGravityFlipped()) {
				// Friction
				double friction = coefficientKineticFriction
						* Math.abs(normal.getYComponent());
				if (velocity.getXComponent() > 0) {
					friction *= -1;
				}
				
				if (!standingOn.equals("ICE")) {
					netForce.add(new Vector2D(friction, 0));
				}
			}
		}
		else {
			ceiling = false;
		}
		if (normalDirectionX == Edge.LEFT) {
			velocity.setXComponent(0);
			if (netForce.getXComponent() < 0) {
				normal.setXComponent(netForce.getXComponent());
				netForce.setXComponent(0);
			}
		}
		if (normalDirectionX == Edge.RIGHT) {
			velocity.setXComponent(0);
			if (netForce.getXComponent() > 0) {
				normal.setXComponent(netForce.getXComponent() * -1);
				netForce.setXComponent(0);
			}
		}
		normalDirectionX = Edge.NONE;
		normalDirectionY = Edge.NONE;
		//System.out.println(normal);
		
		//System.out.println(netForce);
		
		// Compute acceleration
		acceleration.setXComponent(netForce.getXComponent() / mass);
		acceleration.setYComponent(netForce.getYComponent() / mass);
		
		// Compute velocity
		velocity.setXComponent((acceleration.getXComponent() * (deltaTime / 1000.0)) + velocity.getXComponent());
		velocity.setYComponent((acceleration.getYComponent() * (deltaTime / 1000.0)) + velocity.getYComponent());
		
		if (velocity.getXComponent() < 0.1 && velocity.getXComponent() > -0.1) {
			velocity.setXComponent(0);
		}
		
		// Compute new position
		lastXPos = getXPos();
		lastYPos = getYPos();
		
		setXPos(getXPos() + (velocity.getXComponent() * (deltaTime / 1000.0)));
		setYPos(getYPos() + (velocity.getYComponent() * (deltaTime / 1000.0)));
		
		if (!isGrounded()) {
			standingOn = "";
		}
		
		//System.out.println("x: " + getXPos() + " y: " + getYPos());
		super.tick(deltaTime);
		//System.out.println(velocity);
		//System.out.println("velocity: " + velocity.toString());
		//System.out.println(getXPos());
		//System.out.println(getYPos());
		//System.out.println(velocity.getYComponent() * (deltaTime/ 1000.0));
		//double t = Math.sqrt((mass * 2 * level.getGravity().getMagnitude())/(coefficientDrag * density * area));
		//System.out.println("velocity: " + velocity.getMagnitude() + " terminal velocity: " + t);
	}
	
	@Override
	public void computeCollision(GameObject object) {
		if (object instanceof Enemy || object instanceof Liquid) {
			return;
		}
		if (object instanceof Projectile && this instanceof Enemy){
			return;
		}
		if(object instanceof Projectile && this instanceof Player){
			Projectile projectile = (Projectile) object;
			projectile.shatter(this);
			return;
		}
		if (object instanceof FallingSpike) {
			FallingSpike spike = (FallingSpike) object;
			spike.shatter(this);
			return;
		}
		// Push out of object
		//double slope = (getYPos() - lastYPos) / (getXPos() - lastXPos);
		
		//double deltaX = object.getXPos() - getXPos();
		//double deltaY = object.getYPos() - getYPos();
		
		Vector2D deltaPos = new Vector2D(velocity.getXComponent(), velocity.getYComponent());
		deltaPos.setMagnitude(1);
		deltaPos.reverse();
		double deltaX = 0;
		double deltaY = 0;
		//System.out.println(deltaPos);
		//System.out.println("object1: " + getXPos() + "," + getYPos() + " object2: " + object.getXPos() + "," + object.getYPos() + " v: " + velocity.getXComponent() + "," + velocity.getYComponent());
		// FIXME screws up when moving backwards?
		//System.out.println((getXPos()) + " : " + object.getXPos());
		double xDirection = getXPos() - lastXPos;
		double yDirection = getYPos() - lastYPos;
		
		if (object instanceof PhysicsObject) {
			PhysicsObject pObject = (PhysicsObject) object;
			double xDirection2 = pObject.getXPos() - pObject.getLastXPos();
			double yDirection2 = pObject.getYPos() - pObject.getLastYPos();
			if (xDirection < 0 && xDirection2 < 0) {
				if (xDirection > xDirection2) {
					xDirection *= -1;
				}
				else if (xDirection == xDirection2) {
					xDirection *= -1;
				}
			}
			else if (xDirection > 0 && xDirection2 > 0) {
				if (xDirection < xDirection2) {
					xDirection *= -1;
				}
			}
			
			if (yDirection < 0 && yDirection2 < 0) {
				if (yDirection > yDirection2) {
					yDirection *= -1;
				}
			}
			else if (yDirection > 0 && yDirection2 > 0) {
				if (yDirection < yDirection2) {
					yDirection *= -1;
				}
			}
		}
		
		
		if (xDirection < 0) {
			deltaX = object.getXPos() + object.getWidth() - getXPos();
		}
		else if (xDirection > 0){
			deltaX = getXPos() + getWidth() - object.getXPos();
		}
			
		if (yDirection < 0) {
			deltaY = object.getYPos() + object.getHeight() - getYPos();
		}
		else if (yDirection > 0) {
			deltaY = getYPos() + getHeight() - object.getYPos();
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
			//System.out.println("a");
		}
		else {
			double k = deltaX / deltaPos.getXComponent();
			deltaPos.setXComponent(deltaX);
			deltaPos.setYComponent(k * deltaPos.getYComponent());
			if (deltaPos.getYComponent() > deltaY) {
				deltaPos.setYComponent(deltaY);
			}
			//System.out.println(k);
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
		
		lastXPos = getXPos(); 
		lastYPos = getYPos(); 
		
		setXPos(getXPos() + deltaPos.getXComponent());
		setYPos(getYPos() + deltaPos.getYComponent());

		getCollider().setBounds(getXPos(), getYPos(), getWidth(), getHeight());
		//System.out.println((getYPos() + getHeight()) + " : " + object.getYPos());
		//System.out.println((getCollider().getYPos() + getHeight()) + " : " + object.getYPos());
		//System.out.println(getXPos() + " " + getYPos());
			
		// TODO change for friction and normal
		//stop();
		
		if (object instanceof PhysicsObject) {
			PhysicsObject pObject = (PhysicsObject) object;
			double cR = 0;
			
			double vxa = ((cR * pObject.getMass() * (pObject.getVelocity().getXComponent() - velocity.getXComponent())) + (mass * velocity.getXComponent()) + (pObject.getMass() * pObject.getVelocity().getXComponent())) / (mass + pObject.getMass());
			double vxb = ((cR * mass * (velocity.getXComponent() - pObject.getVelocity().getXComponent())) + (mass * velocity.getXComponent()) + (pObject.getMass() * pObject.getVelocity().getXComponent())) / (mass + pObject.getMass());
			double vya = ((cR * pObject.getMass() * (pObject.getVelocity().getYComponent() - velocity.getYComponent())) + (mass * velocity.getYComponent()) + (pObject.getMass() * pObject.getVelocity().getYComponent())) / (mass + pObject.getMass());
			double vyb = ((cR * mass * (velocity.getYComponent() - pObject.getVelocity().getYComponent())) + (mass * velocity.getYComponent()) + (pObject.getMass() * pObject.getVelocity().getYComponent())) / (mass + pObject.getMass());
			
			velocity = new Vector2D(vxa,vya);
			pObject.setVelocity(new Vector2D(vxb,vyb));
			
			
		}
		
	}
	
	@Override
	public void computeEdgeTouch(Edge edgeHorizontal, Edge edgeVertical, int deltaTime, GameObject object) {
		if (object instanceof Liquid) {
			return;
		}
		if (normalDirectionX == Edge.NONE) {
			normalDirectionX = edgeVertical;
		}
		if (normalDirectionY == Edge.NONE) {
			standingOn = object.getName();
			normalDirectionY = edgeHorizontal;
		}
		//System.out.println(normalDirectionX + " " + normalDirectionY);
//		if (edge == Edge.HORIZONTAL) {
//			if (!verticalNormalApplied) {
//				verticalNormalApplied = true;
//			}
//			
//			// Friction
//			if (velocity.getXComponent() > 0) {
//				double force = coefficientKineticFriction * normal.getYComponent() * 1;
//				//applyForce(new Vector2D(force, 0));
//			}
//			else if (velocity.getXComponent() < 0) {
//				double force = coefficientKineticFriction * normal.getYComponent() * -1;
//				//applyForce(new Vector2D(force, 0));
//			}
//			
//			if (normal.getYComponent() < 0) {
//				grounded = true;
//			}
//		}
//		else {
//			if (!horizontalNormalApplied) {
//				horizontalNormalApplied = true;
//			}
//		}
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		if (Game.DRAW_EDGE_DETECT) {
			g.setStroke(new BasicStroke(5));
			g.setColor(Color.RED);
			if (normalDirectionX == Edge.LEFT) {
				int x1 = (int)((getXPos() - Game.getViewPort().getXPos())* Game.getUnitX());
				int x2 = (int)((getXPos() - Game.getViewPort().getXPos()) * Game.getUnitX());
				int y1 = (int)((getYPos() - Game.getViewPort().getYPos()) * Game.getUnitY());
				int y2 = (int)((getYPos() + getHeight() - Game.getViewPort().getYPos()) * Game.getUnitY());
				g.drawLine(x1, y1, x2, y2);
			}
			if (normalDirectionX == Edge.RIGHT) {
				int x1 = (int)((getXPos() + getWidth() - Game.getViewPort().getXPos()) * Game.getUnitX());
				int x2 = (int)((getXPos() + getWidth() - Game.getViewPort().getXPos()) * Game.getUnitX());
				int y1 = (int)((getYPos() - Game.getViewPort().getYPos()) * Game.getUnitY());
				int y2 = (int)((getYPos() + getHeight() - Game.getViewPort().getYPos()) * Game.getUnitY());
				g.drawLine(x1, y1, x2, y2);
			}
			if (normalDirectionY == Edge.BOTTOM) {
				int x1 = (int)((getXPos() - Game.getViewPort().getXPos()) * Game.getUnitX());
				int x2 = (int)((getXPos() + getWidth() - Game.getViewPort().getXPos()) * Game.getUnitX());
				int y1 = (int)((getYPos() + getHeight() - Game.getViewPort().getYPos()) * Game.getUnitY());
				int y2 = (int)((getYPos() + getHeight() - Game.getViewPort().getYPos()) * Game.getUnitY());
				g.drawLine(x1, y1, x2, y2);
			}
			if (normalDirectionY == Edge.TOP) {
				int x1 = (int)((getXPos() - Game.getViewPort().getXPos()) * Game.getUnitX());
				int x2 = (int)((getXPos() + getWidth() - Game.getViewPort().getXPos()) * Game.getUnitX());
				int y1 = (int)((getYPos() - Game.getViewPort().getYPos()) * Game.getUnitY());
				int y2 = (int)((getYPos() - Game.getViewPort().getYPos()) * Game.getUnitY());
				g.drawLine(x1, y1, x2, y2);
			}
			g.setStroke(new BasicStroke(1));
		}
		
		if (Game.DRAW_VELOCITY) {
			g.setStroke(new BasicStroke(5));
			g.setColor(Color.RED);
			int x1 = (int) (((getXPos() - Game.getViewPort().getXPos()) + (getWidth() / 2.0)) * Game.getUnitX());
			int y1 = (int) (((getYPos() - Game.getViewPort().getYPos()) + (getHeight() / 2.0)) * Game.getUnitY());
			int x2 = (int)(x1 + (velocity.getXComponent() * 5));
			int y2 = (int)(y1 + (velocity.getYComponent() * 5));
			g.drawLine(x1, y1, x2, y2);
		}
	}

	public double getMass() {
		return mass;
	}

	public double getCoefficientStaticFriction() {
		return coefficientStaticFriction;
	}

	public double getCoefficientKineticFriction() {
		return coefficientKineticFriction;
	}

	public double getCoefficientDrag() {
		return coefficientDrag;
	}

	public Vector2D getNetForce() {
		return netForce;
	}

	public Vector2D getAcceleration() {
		return acceleration;
	}

	public Vector2D getVelocity() {
		return velocity;
	}
	
	public Vector2D getNormal() {
		return normal;
	}
	
	public Edge getNormalDirectionY() {
		return normalDirectionY;
	}
	
	public Edge getNormalDirectionX() {
		return normalDirectionX;
	}
	
	public boolean isVerticalNormalApplied() {
		return verticalNormalApplied;
	}
	
	public boolean isHorizontalNormalApplied() {
		return horizontalNormalApplied;
	}
	
	public boolean isGrounded() {
		return grounded;
	}
	
	public boolean isCeiling() {
		return ceiling;
	}
	
	public double getLastXPos() {
		return lastXPos;
	}
	
	public double getLastYPos() {
		return lastYPos;
	}

	public String getStandingOn() {
		return standingOn;
	}
	
	public void setMass(double mass) {
		this.mass = mass;
	}

	public void setCoefficientStaticFriction(double coefficientStaticFriction) {
		this.coefficientStaticFriction = coefficientStaticFriction;
	}

	public void setCoefficientKineticFriction(double coefficientKineticFriction) {
		this.coefficientKineticFriction = coefficientKineticFriction;
	}

	public void setCoefficientDrag(double coefficientDrag) {
		this.coefficientDrag = coefficientDrag;
	}
	
	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}
	
	public void setVerticalNormalApplied(boolean b) {
		verticalNormalApplied = b;
	}
	
	public void setHorizontalNormalApplied(boolean b) {
		horizontalNormalApplied = b;
	}
	
	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}
	
	public void setNormalDirectionX(Edge direction) {
		normalDirectionX = direction;
	}
	
	public void setNormalDirectionY(Edge direction) {
		normalDirectionY = direction;
	}
	
	public void setLastXPos(double lastXPos) {
		this.lastXPos = lastXPos;
	}
	
	public void setLastYPos(double lastYPos) {
		this.lastYPos = lastYPos;
	}
	
	public void stop() {
		netForce.setXComponent(0);
		netForce.setYComponent(0);
		
		velocity.setXComponent(0);
		velocity.setYComponent(0);
		
		acceleration.setXComponent(0);
		acceleration.setYComponent(0);
		
		appliedForces.clear();
	}

}
