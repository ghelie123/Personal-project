package game.level;

import game.Direction;
import game.Edge;
import game.Game;
import game.JukeBox;
import game.Rectangle;
import game.Resources;
import game.Vector2D;
import game.enemy.Enemy;
import game.object.GameObject;
import game.object.Player;
import game.object.Projectile;
import game.view.BlizzardOverlay;
import game.view.WaterOverlay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Level {
	
	private int id;
	private int width;
	private int height;
	private Rectangle[] bounds;
	private Background background;
	private ArrayList<GameObject> gameObjects;//Stores all objects in the level
	private ArrayDeque<Integer> removedObjects;//Objects queued for removal in order.
	private ArrayList<GameObject> renderOnly;
	private ArrayList<GameObject> initObjects;
	private ArrayList<GameObject> initRenderOnly;
	private Vector2D gravity;
	private Vector2D windForce;
	private double airDensity;
	
	private Rectangle death;
	
	private double playerStartX;
	private double playerStartY;
	
	private boolean switchingLevel;
	
	private boolean blizzard;
	
	private boolean[] fallingRocks;
	
	private boolean gravityFlipped;
	
	private SplashAnimator splashAnim;
	
	public Level(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
		
		gameObjects = new ArrayList<GameObject>();
		initObjects = new ArrayList<GameObject>();
		removedObjects = new ArrayDeque<Integer>();
		renderOnly = new ArrayList<GameObject>();
		initRenderOnly = new ArrayList<GameObject>();
		fallingRocks = new boolean[6];
		
		bounds = new Rectangle[] {new Rectangle(-1,-1,1,height + 2), new Rectangle(width, -1,1, height + 2), new Rectangle(-1,-1, width + 2, 1)};
		death = new Rectangle(-1, height + 2, width + 2, 5);
		airDensity = 1.225;
		
		if (id == 3 || id == 0) {
			if (id == 0) {
				Game.getPlayer().freeze();
				Game.getPlayer().setVisible(false);
			}
			
			BufferedImage layer1 = Resources.getImage("snow_background");
			BufferedImage layer2 = Resources.getImage("snow_clouds");
			background = new Background(new BackgroundLayer[]{new BackgroundLayer(layer1, 0, 0, 16, 9), new LoopingBackgroundLayer(layer2, 0, 0, 16, 1, Direction.LEFT, 0.02), 
				new LoopingBackgroundLayer(layer2, 0, 1, 16, 1, Direction.RIGHT, 0.012)});
		}
		else if (id == -1) {
			background = new Background(new BackgroundLayer[]{new BackgroundLayer(Resources.getImage("black"), 0,0,16,9)});
			
			for (int i = -10; i < 26; i++) {
				addObject(new GameObject(i, 9, 1,1, Resources.getImage("adsadaad")));
			}
			Game.getPlayer().setXPos(-1);
			Game.getPlayer().setYPos(7);
			
			playerStartX = -1;
			playerStartY = 7;
			splashAnim = new SplashAnimator(this);
		}
		else if (id == 2){
			BufferedImage layer1 = Resources.getImage("water_background");
			background = new Background(new BackgroundLayer[] {
					new BackgroundLayer(layer1, 0, 0, 16, 10)});
			airDensity = 100;
		}
		else if (id == 4){
			BufferedImage layer1 = Resources.getImage("cave_background");
			background = new Background(new BackgroundLayer[]{new BackgroundLayer(layer1, 0,0,16,9)});
					
		}
		else if (id == 1) {
			background = new Background(new BackgroundLayer[]{new BackgroundLayer(Resources.getImage("map"), 0,0,16,9)});
		}
		else if (id == 5) {
			BufferedImage layer1 = Resources.getImage("lava_background");
			background = new Background(new BackgroundLayer[]{new BackgroundLayer(layer1, 0,0,16,9)});
		}
		else if (id == 6){
			BufferedImage layer1 = Resources.getImage("jungle_background");
			BufferedImage layer2 = Resources.getImage("snow_clouds");
			background = new Background(new BackgroundLayer[]{new BackgroundLayer(layer1, 0, 0, 16, 9), new LoopingBackgroundLayer(layer2, 0, 0, 16, 1, Direction.LEFT, 0.02), 
				new LoopingBackgroundLayer(layer2, 0, 1, 16, 1, Direction.RIGHT, 0.012)});
		}
		
		gravity = new Vector2D(0,9.8);
		windForce = new Vector2D(0,0);
		
		// Add bounds
		for (int i = 0; i < bounds.length; i++) {
			if (id == -1 && (i == 0 || i == 1)) {
				continue;
			}
			Rectangle rect = bounds[i];
			GameObject object = new GameObject(rect.getXPos(), rect.getYPos(), rect.getWidth(), rect.getHeight(), new BufferedImage((int)(rect.getWidth() * Game.getUnitX()), (int) (rect.getHeight() * Game.getUnitY()), BufferedImage.TYPE_INT_ARGB));
			if (i == 2) {
				object.setName("BOUNDS_TOP");
			}
			gameObjects.add(object);
		}
		
		if (id == -1) {
			saveInitialObjects();
		}
	}
	
	public void tick(int deltaTime){
		//Updates background of all objects.
		if (id == 0){
			Game.getViewPort().getHUD().getHUDElements().get(0).hide();
			Game.getViewPort().getHUD().getHUDElements().get(1).hide();
			Game.getViewPort().getHUD().getHUDElements().get(3).hide();
			Game.getViewPort().getHUD().getHUDElements().get(2).show();
		}
		else if (id == 1) {
			Game.getViewPort().getHUD().getHUDElements().get(0).hide();
			Game.getViewPort().getHUD().getHUDElements().get(1).hide();
			Game.getViewPort().getHUD().getHUDElements().get(2).hide();
			Game.getViewPort().getHUD().getHUDElements().get(3).show();
		}
		else if (id == -1) {
			Game.getViewPort().getHUD().getHUDElements().get(0).hide();
			Game.getViewPort().getHUD().getHUDElements().get(1).hide();
			Game.getViewPort().getHUD().getHUDElements().get(2).hide();
			Game.getViewPort().getHUD().getHUDElements().get(3).hide();
		}
		else {
			if (!MainMenu.inGameMenuOpened){
			Game.getViewPort().getHUD().getHUDElements().get(2).hide();
			}
			Game.getViewPort().getHUD().show();
		}
		background.tick(deltaTime);
		
		
		if (splashAnim != null) {
			splashAnim.tick(deltaTime);
		}
		//System.out.println(gameObjects.size());
		// Update game objects.
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject object = gameObjects.get(i);
			if (object instanceof Enemy && Game.getViewPort().getCollider().inside(object.getCollider())) {
				object.unfreeze();
			}
			
			if (object.isFrozen()) {
				continue;
			}
			if (!object.isDeleted()) {
				gameObjects.get(i).tick(deltaTime);
			}
			
			if (object.isDeleted()) {
				removedObjects.addLast(i);
			}
		} 

		// Remove game objects queued for removal.
		while (!removedObjects.isEmpty()) {
		    gameObjects.remove((int)(removedObjects.pollLast()));
		} 

		// Compute collisions with player
		for (int i = 0; i < gameObjects.size(); i++) {
			Player p = Game.getPlayer();
			GameObject object = gameObjects.get(i);
			
			if (!object.isCollidable()) {
				continue;
			}
			
			if (p.isColliding(object)) {
				p.computeCollision(object);
			}
			
			Edge edge = p.isTouching(object);
		    if (edge != Edge.NONE) {
		        p.computeEdgeTouch(p.getCollider().touchingHorizontal(object.getCollider()),p.getCollider().touchingVertical(object.getCollider()), deltaTime, object);
		    }
		}
		
		if ((death.inside(Game.getPlayer().getCollider()) && !switchingLevel) || (Game.getPlayer().getHealth() <= 0 && !switchingLevel)) {
			switchingLevel = true;
			Game.getViewPort().transitionLevel(this);
			return;
		}
		
		// Compute collisions.
		for (int i = 0; i < gameObjects.size() - 1; i++) {
		    for (int j = i + 1; j < gameObjects.size(); j++) {
		        GameObject object1 = gameObjects.get(i);
		        GameObject object2 = gameObjects.get(j);
		        
		        if (!object1.isCollidable()) {
		        	break;
		        }
		        if (!object2.isCollidable()) {
		        	continue;
		        }
		        
		        if (object1.isColliding(object2)) {
		            object1.computeCollision(object2);
		        }
		        
		        Edge edge = object1.isTouching(object2);
		        if (edge != Edge.NONE) {
		        	object1.computeEdgeTouch(object1.getCollider().touchingHorizontal(object2.getCollider()),object1.getCollider().touchingVertical(object2.getCollider()), deltaTime, object2);
		        }
		        
		    }
		    
		}
		//System.out.println(System.currentTimeMillis() - test);
		
		//TEST
		//System.out.println("x: " + Game.getPlayer().getXPos());
		//System.out.println("y: " + Game.getPlayer().getYPos());
		
		// LEVEL SPECIFIC UPDATES
		if (id == 2){
			Game.getViewPort().setOverlay(new WaterOverlay());
		}
		if (id == 3) {
			if ((Game.getPlayer().getXPos() >= 72 && Game.getPlayer().getXPos() <= 100) || (Game.getPlayer().getXPos() >= 136 && Game.getPlayer().getXPos() <= 157)) {
				if (!blizzard) {
					Game.getViewPort().setOverlay(new BlizzardOverlay());
					windForce = new Vector2D(-300,0);
					blizzard = true;
					JukeBox.startLoopingSoundEffect("wind");
				}
			}
			else {
				blizzard = false;
				if (Game.getViewPort().getOverlay() instanceof BlizzardOverlay) {
					BlizzardOverlay overlay = (BlizzardOverlay)Game.getViewPort().getOverlay();
					overlay.fadeOut();
					windForce = new Vector2D(0,0);
					JukeBox.stopLoopingSoundEffect("wind");
				}
			}
		}
		if (id == 4){
			if (Game.getPlayer().getXPos() >= 85 && Game.getPlayer().getXPos() <= 89 ){
				if (!gravityFlipped){
					flipGravity();
				}
			}
			else if (Game.getPlayer().getXPos() >= 132 && Game.getPlayer().getXPos() <= 134 ){
				if (!gravityFlipped){
					flipGravity();
				}
			}
			
			else{
				if(gravityFlipped){
					flipGravity();
				}
			}
			
			
			
			if(Game.getPlayer().getXPos() >= 72 && Game.getPlayer().getXPos() <= 77 && Game.getPlayer().getYPos() >= 16
					&& Game.getPlayer().getYPos() <= 17){
				airDensity = 100;
			}
			else{
				airDensity = 1.225;
			}
		}
		if (id == 5) {
			if (Game.getPlayer().getXPos() >= 170 && !fallingRocks[0]) {
				addObjectFirst(new Projectile(Projectile.FIREROCK, 0, 177, -1.5, 1.5, 1.5,200, 1, 1, 1, Resources.getImage("lava_rock")));
				fallingRocks[0] = true;
			}
			if (Game.getPlayer().getXPos() >= 176 && !fallingRocks[1]) {
				addObjectFirst(new Projectile(Projectile.FIREROCK, 0, 183, -1.5, 1.5, 1.5,200, 1, 1, 1, Resources.getImage("lava_rock")));
				fallingRocks[1] = true;
			}
			if (Game.getPlayer().getXPos() >= 187 && !fallingRocks[2]) {
				addObjectFirst(new Projectile(Projectile.FIREROCK, 0, 194, -1.5, 1.5, 1.5,200, 1, 1, 1, Resources.getImage("lava_rock")));
				fallingRocks[2] = true;
			}
			if (Game.getPlayer().getXPos() >= 192 && !fallingRocks[3]) {
				addObjectFirst(new Projectile(Projectile.FIREROCK, 0, 199, -1.5, 1.5, 1.5,200, 1, 1, 1, Resources.getImage("lava_rock")));
				fallingRocks[3] = true;
			}
			if (Game.getPlayer().getXPos() >= 260 && !fallingRocks[4]) {
				addObjectFirst(new Projectile(Projectile.FIREROCK, 0, 267, -1.5, 1.5, 1.5,200, 1, 1, 1, Resources.getImage("lava_rock")));
				fallingRocks[4] = true;
			}
			if (Game.getPlayer().getXPos() >= 274 && !fallingRocks[5]) {
				addObjectFirst(new Projectile(Projectile.FIREROCK, 0, 281, -1.5, 1.5, 1.5,200, 1, 1, 1, Resources.getImage("lava_rock")));
				fallingRocks[5] = true;
			}
		}
	}
	
	public void render(Graphics2D g){
		//Renders the background and all objects.
				
		for (int i = 0; i < gameObjects.size(); i++){
			GameObject object = gameObjects.get(i);
			if (Game.getViewPort().getCollider().inside(object.getCollider()) && !object.isInvisible()) {
				object.render(g);
			}
		}
		for (int i = 0; i < renderOnly.size(); i++) {
			GameObject object = renderOnly.get(i);
			if (Game.getViewPort().getCollider().inside(object.getCollider()) && !object.isInvisible()) {
				object.render(g);
			}
		}
		
	}
	
	public void addObject(GameObject object) {
		gameObjects.add(object);
	}
	
	public void addObjectFirst(GameObject object) {
		gameObjects.add(0, object);
	}
	
	public void addRenderOnly(GameObject object) {
		renderOnly.add(object);
	}
	
	public void setPlayerStartX(double playerStartX) {
		this.playerStartX = playerStartX;
	}
	
	public void setPlayerStartY(double playerStartY) {
		this.playerStartY = playerStartY;
	}
	
	public void setSwitchingLevel(boolean switchingLevel) {
		this.switchingLevel = switchingLevel;
	}
	
	public int getId() {
		return id;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public Vector2D getGravity(){
		return gravity;
	}
	
	public Vector2D getWindForce(){
		return windForce;
	}
	
	public double getAirDensity(){
		return airDensity;
	}
	
	public Background getBackground() {
		return background;
	}
	
	public double getPlayerStartX() {
		return playerStartX;
	}
	
	public double getPlayerStartY() {
		return playerStartY;
	}
	
	public void saveInitialObjects() {
		for (int i = 0; i < gameObjects.size(); i++) {
			initObjects.add(gameObjects.get(i));
		}
		for (int i = 0; i < renderOnly.size(); i++) {
			initRenderOnly.add(renderOnly.get(i));
		}
	}
	
	public boolean isSwitchingLevel() {
		return switchingLevel;
	}
	
	public boolean isGravityFlipped() {
		return gravityFlipped;
	}
	
	public void flipGravity() {
		gravity.reverse();
		gravityFlipped = !gravityFlipped;
	}
	
	public void reset() {
		gameObjects.clear();
		renderOnly.clear();
		for (int i = 0; i < initObjects.size(); i++) {
			gameObjects.add(initObjects.get(i));
		}
		for (int i = 0; i < initRenderOnly.size(); i++) {
			renderOnly.add(initRenderOnly.get(i));
		}
		windForce = new Vector2D(0,0);
		gravity = new Vector2D(0, 9.8);
		gravityFlipped = false;
		fallingRocks = new boolean[fallingRocks.length];
	}

}
