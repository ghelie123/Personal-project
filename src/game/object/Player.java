package game.object;

import game.Game;
import game.Input;
import game.JukeBox;
import game.Resources;
import game.Vector2D;
import game.enemy.Enemy;
import game.level.MainMenu;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;


public class Player extends PhysicsObject {
	
	private int health;
	private int maxHealth;
	private int score;
	private double fuel;
	private int maxFuel;
	
	private Animation jetAnim;
	private Animation walkAnim;
	private Animation swimAnim;
	
	private FlashAnimation invulnerableAnimation;
	
	private boolean jet;
	private boolean invulnerable;
	private boolean flashlight = false;
	private int batteryLife;
	
	private Random rand;
	
	public Player(double xPos, double yPos, int maxHealth , int maxFuel){
		super(xPos, yPos, 1, 2, 62, 0 , 1, 1, Resources.getImage("player_0"));
		this.maxHealth = maxHealth;
		this.maxFuel = maxFuel;
		health = maxHealth;
		fuel = maxFuel;
		jetAnim = new Animation(new BufferedImage[]{Resources.getImage("player_1"), Resources.getImage("player_2")},new int[]{100,100});
		walkAnim = new Animation(new BufferedImage[]{Resources.getImage("player_4"), Resources.getImage("player_5"), Resources.getImage("player_6"), Resources.getImage("player_7")}, new int[]{150,150,150,150});
		swimAnim = new Animation(new BufferedImage[] {
				Resources.getImage("swim_1"), Resources.getImage("swim_2"),
				Resources.getImage("swim_0") }, new int[] { 450, 450, 450 });
		invulnerableAnimation = new FlashAnimation(100,1250,this);
		rand = new Random();
	}
	
	@Override
	public void tick(int deltaTime) {
		//System.out.println(getNetForce());
		if (Game.getViewPort().getLevel().getId() == -1) {
			super.tick(deltaTime);
			return;
		}
		
		if (invulnerable) {
			invulnerableAnimation.tick(deltaTime);
		}
		
		// DAMAGE TEST
		if (Input.isKeyPressed(KeyEvent.VK_Q)) {
			health--;
			Input.requireRelease(KeyEvent.VK_Q);
		}
		
		//GRAVITY TEST
		if (Input.isKeyPressed(KeyEvent.VK_CONTROL)) {
			Game.getViewPort().getLevel().addObjectFirst(new Projectile(Projectile.FIREROCK, 0, 8, -1.5, 1.5, 1.5,200, 1, 1, 1, Resources.getImage("lava_rock")));
			Input.requireRelease(KeyEvent.VK_CONTROL);
		}
		//System.out.println(isCeiling());
		if (Input.isKeyPressed(KeyEvent.VK_ESCAPE)){
//			Resources.saveProgress(Game.getViewPort().getLevel().getId() , 
//					(int)Game.getPlayer().getXPos(),(int)Game.getPlayer().getYPos(), Resources.getFile("SavedData") );
			//Game.getViewPort().changeLevel(0);
			setWidth(1);
			setHeight(2);
			Game.getViewPort().pause();
			Game.getViewPort().getHUD().getHUDElements().get(2).show();
			MainMenu.inGameMenuOpened = true;
			MainMenu.isOnResume = true;
			MainMenu.isOnNew = false;
			MainMenu.isOnLoad = false;
			
			
			
		}
		
		if (Game.getViewPort().getLevel().getId() == 2) {
			setWidth(2);
			setHeight(1.5);
			setSprite(Resources.getImage("swim_0"));

			if (Input.isKeyPressed(KeyEvent.VK_W)) {
				applyForce(new Vector2D(0, -1000));
				setAnimation(swimAnim);
				setGrounded(false);
			}
			
			if (Input.isKeyPressed(KeyEvent.VK_S)){
					applyForce(new Vector2D(0,500));
					setAnimation(swimAnim);
			}
			if (Input.isKeyPressed(KeyEvent.VK_D)) {
				if (isWaterFlippedX()) {
					setWaterFlippedX(false);
				}
				applyForce(new Vector2D(1000,0));
				setAnimation(swimAnim);
				
			}	
			if (Input.isKeyPressed(KeyEvent.VK_A)){
				setWaterFlippedX(true);
				applyForce(new Vector2D(-1000,0));
				setAnimation(swimAnim);
			}
		}
		else {
		
		if (!isGrounded() && !(isCeiling() && Game.getViewPort().getLevel().isGravityFlipped())) {
			setSprite(Resources.getImage("player_3"));
		}
		
		if (Input.isKeyPressed(KeyEvent.VK_D)) {
			if (isFlippedX()) {
				setFlippedX(false);
			}
			if (isGrounded() || (isCeiling() && Game.getViewPort().getLevel().isGravityFlipped())) {
				applyForce(new Vector2D(2000, 0));
				setAnimation(walkAnim);
			}
			else {
				applyForce(new Vector2D(400,0));
			}
		}
		
		if (Input.isKeyPressed(KeyEvent.VK_A)) {
			if (!isFlippedX()) {
				setFlippedX(true);
			}
			if (isGrounded() || (isCeiling() && Game.getViewPort().getLevel().isGravityFlipped())) {
				applyForce(new Vector2D(-2000,0));
				setAnimation(walkAnim);
			}
			else {
				applyForce(new Vector2D(-400,0));
			}
		}
		if (Input.isKeyPressed(KeyEvent.VK_SPACE) && (isGrounded() || (isCeiling() && Game.getViewPort().getLevel().isGravityFlipped()))) {
			//System.out.println("jump");
			Input.requireRelease(KeyEvent.VK_SPACE);
			if (Game.getViewPort().getLevel().isGravityFlipped()) {
				applyForce(new Vector2D(0, 30000));
			}
			else {
				applyForce(new Vector2D(0, -30000));
			}
			setGrounded(false);
			setAnimation(null);
			setSprite(Resources.getImage("player_3"));
		}
		if (Input.isKeyPressed(KeyEvent.VK_SPACE) && fuel > 0) {
			jet = true;
			if (Game.getViewPort().getLevel().isGravityFlipped()) {
				applyForce(new Vector2D(0, 900));
			}
			else {
				applyForce(new Vector2D(0, -900));
			}
			setAnimation(jetAnim);
			JukeBox.startLoopingSoundEffect("jetpack");
			fuel-= 0.2d;
		}
		else {
			JukeBox.stopLoopingSoundEffect("jetpack");
			jet = false;
		}
		
		if (!Input.isKeyPressed(KeyEvent.VK_D) && !Input.isKeyPressed(KeyEvent.VK_A) && !Input.isKeyPressed(KeyEvent.VK_SPACE)) {
			setAnimation(null);
		}
		
		if (isGrounded() || (isCeiling() && Game.getViewPort().getLevel().isGravityFlipped())) {
			setSprite(Resources.getImage("player_0"));
		}
		
		if (getAnimation() != null) {
			if (getAnimation().equals(jetAnim) && !jet) {
				setAnimation(null);
			}
		}
		}
		
		super.tick(deltaTime);
	}
	
	@Override
	public void computeCollision(GameObject object) {
		
		if (object.getName().equals("END")) {
			if (!Game.getViewPort().getLevel().isSwitchingLevel()) {
			Game.getViewPort().getLevel().setSwitchingLevel(true);
			Game.getViewPort().transitionLevel(Game.getMapSelect());
			}
			return;
		}
		if (object instanceof Pickup) {
			((Pickup) object).onPickup();
			if (!(object instanceof FlashLight)){
			object.delete();
			}
			return;
		}
		if (object instanceof Liquid) {
			Liquid liquid = (Liquid) object;
			if (liquid.getType() == Liquid.LAVA) {
				damage(health);
				return;
			}
		}
		if (object instanceof Fireball) {
			if (!invulnerable) {
				knockback();
			}
			damage(1);
			return;
		}
		super.computeCollision(object);
		if (object instanceof Enemy) {
			if (!invulnerable) {
				knockback();
			}
			damage(((Enemy) object).getDamage());
		}
		
		
	}
	
	@Override
	public void render(Graphics2D g) {
		if (invulnerable) {
			Composite temp = g.getComposite();
			g.setComposite(invulnerableAnimation.getComposite());
			super.render(g);
			g.setComposite(temp);
		}
		else {
			super.render(g);
		}
	}
	
	public void knockback() {
		stop();
		int dx = 20000;
		int dy = -20000;
		if (getLastXPos() - getXPos() < 0) {
			dx *= -1;
		}
		applyForce(new Vector2D(dx,dy));
		switch (rand.nextInt(5)) {
		case 0:
			JukeBox.playSoundEffect("Jab");
			break;
		case 1:
			JukeBox.playSoundEffect("Left Hook");
			break;
		case 2:
			JukeBox.playSoundEffect("Right Cross");
			break;
		case 3:
			JukeBox.playSoundEffect("Right Hook");
			break;
		case 4:
			JukeBox.playSoundEffect("Upper Cut");
			break;
		default:
			JukeBox.playSoundEffect("Upper Cut");
		}
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getMaxHealth(){
		return maxHealth;
	}
	
	public int getScore(){
		return score;
	}
	
	public double getFuel(){
		return fuel;
	}
	
	public int getMaxFuel() {
		return maxFuel;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public void setMaxHealth(int maxHealth){
		this.maxHealth = maxHealth;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public void setFuel(double fuel){
		this.fuel = fuel;
	}
	
	public void setMaxFuel(int maxFuel){
		this.maxFuel = maxFuel;
	}
	
	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}
	
	
	
	public void turnOnFlashlight(boolean on){
		this.flashlight = on;
	}
	
	public boolean getFlashlightOn(){
		return flashlight;
	}
	
	public Animation getWalkAnimation() {
		return walkAnim;
	}
	
	public Animation getJetAnimation() {
		return jetAnim;
	}
	
	public void damage(int damage) {
		if (!invulnerable) {
			health -= damage;
			if (health < 0) {
				health = 0;
			}
 			invulnerable = true;
		}
	}
	
	public void reset() {
		health = maxHealth;
		fuel = maxFuel;
		Game.getPlayer().setWidth(1);
		Game.getPlayer().setHeight(2);
		setAnimation(null);
	}

	public int getBatteryLife() {
		return batteryLife;
	}

	public void setBatteryLife(int batteryLife) {
		this.batteryLife = batteryLife;
	}

}
