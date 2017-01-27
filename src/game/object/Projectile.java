package game.object;

import game.Resources;
import game.Vector2D;
import game.enemy.Enemy;

import java.awt.image.BufferedImage;

public class Projectile extends PhysicsObject {
	
	public static final int VENOM = 0;
	public static final int ARROW = 1;
	public static final int BANANA = 2;
	public static final int FIREBALL = 3;
	public static final int FIREROCK = 4;
	public static final int type = 10;
	
	private int speed;
	private boolean shatter;
	private boolean flipped;
	
	private Animation shatterAnim;
	private Animation flyingAnim;
	
	public Projectile(int type, int speed,  double xPos, double yPos, double width, double height,
			double mass, double coefficientStaticFriction,
			double coefficientKineticFriction, double coefficientDrag,
			BufferedImage sprite) {
		super(xPos, yPos, width, height, mass, coefficientStaticFriction,
				coefficientKineticFriction, coefficientDrag, sprite);
		this.speed = speed;
		this.flipped = flipped;
		
		if (type == VENOM){
		shatterAnim = new Animation(new BufferedImage[]{Resources.getImage("venom shatter_0"), Resources.getImage("venom shatter_1")
				,Resources.getImage("venom shatter_2"), Resources.getImage("venom shatter_3")}, new int[]{100,100,100,100});
			
		applyForce(new Vector2D(speed,0));
		}
		if (type == ARROW){
			shatterAnim = new Animation(new BufferedImage[]{Resources.getImage("venom shatter_0"), Resources.getImage("venom shatter_1")
					,Resources.getImage("venom shatter_2"), Resources.getImage("venom shatter_3")}, new int[]{100,100,100,100});
			setFlippedX(flipped);
			applyForce(new Vector2D(speed,0
				));
			
		}
		
		if (type == BANANA){
			flyingAnim = new Animation(new BufferedImage[]{Resources.getImage("banana_3"),Resources.getImage("banana_2")
					,Resources.getImage("banana_1"),Resources.getImage("banana_0")} , new int[]{100,100,100,100});
			
			shatterAnim = new Animation(new BufferedImage[]{Resources.getImage("venom shatter_0"), Resources.getImage("venom shatter_1")
					,Resources.getImage("venom shatter_2"), Resources.getImage("venom shatter_3")}, new int[]{100,100,100,100});
			
			setAnimation(flyingAnim);
			
			applyForce(new Vector2D(speed,0));
		}
		
		if (type == FIREROCK) {
			BufferedImage[] frames = new BufferedImage[]{Resources.getImage("lava_rock_shatter_0"), 
					Resources.getImage("lava_rock_shatter_1"), 
					Resources.getImage("lava_rock_shatter_2"), 
					Resources.getImage("lava_rock_shatter_3")};
			int[] delays = new int[]{200,200,200,200};
			shatterAnim = new Animation(frames, delays);
			applyForce(new Vector2D(-90000,100000));
		}
		
	}
	
	public void tick(int deltaTime){
		
		if(shatter){
			stop();
			shatterAnim.tick(deltaTime);
			
		}
		if(shatterAnim.getCurrentFrameIndex() == 3){
			delete();
		}
		if (flyingAnim != null){
			flyingAnim.tick(deltaTime);
		}
		
		super.tick(deltaTime);
	}
	
	public void computeCollision(GameObject object){
		if (!(object instanceof Enemy) && !object.getName().equals("BOUNDS_TOP")){
			shatter(object);
		}
	}
	
	public void shatter(GameObject object){
		
		if(shatter){
			return;
		}
		
		if (object instanceof Player){
			((Player) object).damage(2);
			((Player) object).knockback();
			
		}
		
		if (getAnimation() == null){
			setAnimation(shatterAnim);
		}
		
		shatter = true;
		
	}
	
	
	

}
