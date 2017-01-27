package game.view;

import game.Direction;
import game.Game;
import game.Input;
import game.JukeBox;
import game.Rectangle;
import game.Resources;
import game.enemy.Archer;
import game.enemy.Bat;
import game.enemy.Golum;
import game.enemy.Monkey;
import game.enemy.Octopus;
import game.enemy.Penguin;
import game.enemy.Snake;
import game.enemy.Squid;
import game.enemy.Worm;
import game.level.Level;
import game.level.MainMenu;
import game.object.FallingSpike;
import game.object.Fireball;
import game.object.FlashLight;
import game.object.FuelPickup;
import game.object.GameObject;
import game.object.HealthPickup;
import game.object.Projectile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JPanel;

public class ViewPort extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Lock lock = new ReentrantLock();
	
	private double xPos;//of the view
	private double yPos;//of the view
	private int width;
	private int height;
	private BufferedImage screen;
	private BufferedImage spotlight;
	private HUD hud;
	private Level level;
	private Rectangle collider;
	
	private boolean changeSpotlight;
	
	private boolean paused;
	
	private FadeTransition transition;
	
	private RectangleOverlay overlay = null;
	
	
	public ViewPort(double xPos , double yPos, int width , int height){
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		collider = new Rectangle(xPos,yPos, width / Game.getUnitX(), height / Game.getUnitY());
		screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		spotlight = new BufferedImage(width * 3, height * 3, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = spotlight.createGraphics();
		g.drawImage(Resources.getImage("overlay"), width, height, width, height, null);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		g.fillRect(width, 0, width, height);
		g.fillRect(width * 2, 0, width, height);
		g.fillRect(0, height, width, height);
		g.fillRect(0, height*2, width, height);
		g.fillRect(width * 2, height, width, height);
		g.fillRect(width * 2, height * 2, width, height);
		g.fillRect(width, height * 2, width, height);
		g.dispose();
		hud = new HUD();
		hud.addHUDElement(new HealthBar(0.2,0.2));
		hud.addHUDElement(new FuelBar(0.2, 7.15));
		hud.addHUDElement(new MainMenu(4,1,8,8,Resources.getImage("Play")));
		hud.addHUDElement(new Score(13,0.6));
		
		level = new Level(-1, 16, 9);
		
		setPreferredSize(new Dimension(width,height));
	}
	
	public void render(){
		//Renders level and HUD.
		screen = new BufferedImage(screen.getWidth(), screen.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = screen.createGraphics();
		
		level.getBackground().render(g);
		if (!Game.getPlayer().isInvisible()) {
			Game.getPlayer().render(g);
		}
		level.render(g);
		
		
		if (overlay != null) {
			overlay.render(g);
		}
		
		if (getLevel().getId() == 4){
			if (Game.getPlayer().getFlashlightOn() == true && changeSpotlight){
				changeSpotlight(false);
				spotlight = new BufferedImage(width * 3, height * 3, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2 = spotlight.createGraphics();
				g2.drawImage(Resources.getImage("BigOverlay"), width, height, width, height, null);
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, width, height);
				g2.fillRect(width, 0, width, height);
				g2.fillRect(width * 2, 0, width, height);
				g2.fillRect(0, height, width, height);
				g2.fillRect(0, height*2, width, height);
				g2.fillRect(width * 2, height, width, height);
				g2.fillRect(width * 2, height * 2, width, height);
				g2.fillRect(width, height * 2, width, height);
				g2.dispose();
			}
			
			
			else if (Game.getPlayer().getFlashlightOn() == false && changeSpotlight){
				changeSpotlight(false);
				spotlight = new BufferedImage(width * 3, height * 3, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2 = spotlight.createGraphics();
				g2.drawImage(Resources.getImage("overlay"), width, height, width, height, null);
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, width, height);
				g2.fillRect(width, 0, width, height);
				g2.fillRect(width * 2, 0, width, height);
				g2.fillRect(0, height, width, height);
				g2.fillRect(0, height*2, width, height);
				g2.fillRect(width * 2, height, width, height);
				g2.fillRect(width * 2, height * 2, width, height);
				g2.fillRect(width, height * 2, width, height);
				g2.dispose();
			}
				
		g.drawImage(spotlight,(int)((Game.getPlayer().getXPos() - Game.getViewPort().getXPos()) * Game.getUnitX()
				+ ((Game.getPlayer().getWidth()*Game.getUnitX())/2) - (Game.getViewPort().getWidth())*1.5) ,(int)((Game.getPlayer().getYPos() - Game.getViewPort().getYPos()) * Game.getUnitY()
						+ (((Game.getPlayer().getHeight() * Game.getUnitY()) / 2)) - (Game.getViewPort().getHeight())*1.5),this);
			
		
		}
		
		hud.render(g);
		
		if (transition != null) {
			transition.render(g);
		}
		
		// Draw grid
		g.setColor(Color.BLACK);
		if (Game.DRAW_GRID) {
			for (int i = 0; i < 16; i++) {
				g.drawLine(i * Game.getUnitX(), 0, i * Game.getUnitX(),
						screen.getHeight());
			}
			for (int i = 0; i < 9; i++) {
				g.drawLine(0, i * Game.getUnitY(), screen.getWidth(),
						i * Game.getUnitY());
			}
		}
		
		if (Game.DRAW_FPS) {
			// Display frame rate
			g.setFont(Resources.getFont("pixelmix").deriveFont(16f));
			g.drawString("FPS: " + String.valueOf(Game.getFrameRate()), 10, 25);
		}
		g.dispose();
		
		paintImmediately(0, 0, getWidth(), getHeight());
		//repaint();
	}
	
	public void tick(int deltaTime){
		if (!paused) {
			//Updates level and HUD.
			if (!Game.getPlayer().isFrozen()) {
				Game.getPlayer().tick(deltaTime);
			}
			level.tick(deltaTime);
		}
		// Set view port's position
		snapToPlayer();
		
		hud.tick(deltaTime);
		
		if (Input.isKeyPressed(KeyEvent.VK_P)) {
			paused = !paused;
			Input.requireRelease(KeyEvent.VK_P);
		}
		
		if (transition != null) {
			transition.tick(deltaTime);
			if (transition.isHalf()) {
				changeLevel(transition.getLevel());
				Input.setListening(true);
				transition.setHalf(false);
			}
			if (transition.isFinished()) {
				transition = null;
			}
		}
		
		if (overlay != null) {
			if (overlay instanceof BlizzardOverlay) {
				BlizzardOverlay b = (BlizzardOverlay) overlay;
				if (b.isFinished()) {
					overlay = null;
				}
			}
			
			if (overlay != null) {
				overlay.tick(deltaTime);
			}
		}
	}
	
	public void addToHUD(HUDElement HUD){
		//Adds a new HUD element to the HUD
		
		hud.addHUDElement(HUD);
	}
	
	public void snapToPlayer() {
		xPos = Game.getPlayer().getXPos() - 7.5;
		yPos = Game.getPlayer().getYPos() - 4;
		
		if (xPos + (width / Game.getUnitX()) > level.getWidth()) {
			xPos = level.getWidth() - (width / Game.getUnitX());
		}
		if (xPos < 0) {
			xPos = 0;
		}
		if (yPos < 0) {
			yPos = 0;
		}
		if (yPos + (height / Game.getUnitY()) > level.getHeight()) {
			yPos = level.getHeight() - (height / Game.getUnitY());
		}
		
		collider.setBounds(xPos, yPos, width / Game.getUnitX(), height / Game.getUnitY());
	}
	
	public void transitionLevel(Level level) {
		transition = new FadeTransition(1500, level);
		Input.setListening(false);
	}
	
	public void changeLevel(Level level) {
		this.level = level;
		Game.getPlayer().stop();
		Game.getPlayer().reset();
		Game.getPlayer().setXPos(level.getPlayerStartX());
		Game.getPlayer().setYPos(level.getPlayerStartY());
		if (level.getId() == 4){
			Game.getPlayer().setXPos(144);
			Game.getPlayer().setYPos(19);
		}
		if (level.getId() == 6){
			Game.getPlayer().setXPos(148);
			Game.getPlayer().setYPos(21);
		}
		level.getBackground().recompute();
		level.setSwitchingLevel(false);
		level.reset();
		snapToPlayer();
		overlay = null;
		if (Game.getPlayer().isWaterFlippedX()){
			Game.getPlayer().setWaterFlippedX(false);
		}
		if (Game.getPlayer().isFlippedX()){
			Game.getPlayer().setFlippedX(false);
		}
		
		if (level.getId() == 0 || level.getId() == 1) {
			Game.getPlayer().freeze();
			Game.getPlayer().setVisible(false);
		}
		else {
			Game.getPlayer().unfreeze();
			Game.getPlayer().setVisible(true);
		}
		
		// Overlays
		if (level.getId() == 5) {
			overlay = new RectangleOverlay(new Color(0xFF2A00), 0.4f);
		}
		else {
			overlay = null;
		}
		
		// Stop sound effect loops
		JukeBox.stopLoopingSoundEffect("wind");
		
		
		// Set background music
		if (level.getId() == 5) {
			JukeBox.setBackgroundMusic("lava_bgm");
		}
		else if (level.getId() == 0) {
			JukeBox.setBackgroundMusic("main_bgm");
		}
		else if (level.getId() == 1) {
			JukeBox.setBackgroundMusic("map_bgm");
		}
		else if (level.getId() == 3) {
			JukeBox.setBackgroundMusic("snow_bgm");
		}
		else {
			JukeBox.setBackgroundMusic(null);
		}
		
		if (level.getId() == 2) {
			Squid squid1 = new Squid(2,1,17,4,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid2 = new Squid(2,1,17,12,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid3 = new Squid(2,1,35,10,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid4 = new Squid(2,1,28,4,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid5 = new Squid(2,1,44,2,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid6 = new Squid(2,1,55,2,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid7 = new Squid(2,1,55,13,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid8 = new Squid(2,1,69,2,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid9 = new Squid(2,1,69,13,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid10 = new Squid(2,1,80,9,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid11 = new Squid(2,1,80,1,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid12 = new Squid(2,1,80,26,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid13 = new Squid(2,1,88,1,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid14 = new Squid(2,1,88,9,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid15 = new Squid(2,1,84,19,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid16 = new Squid(2,1,91,15,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			Squid squid17 = new Squid(2,1,94,21,1,1.8,23,1,1,1,Resources.getImage("squid_0"));
			
			squid1.setDirection(Direction.RIGHT);
			squid2.setDirection(Direction.RIGHT);
			squid3.setDirection(Direction.RIGHT);
			squid4.setDirection(Direction.RIGHT);
			squid5.setDirection(Direction.DOWN);
			squid6.setDirection(Direction.DOWN);
			squid7.setDirection(Direction.DOWN);
			squid8.setDirection(Direction.DOWN);
			squid9.setDirection(Direction.DOWN);
			squid10.setDirection(Direction.DOWN);
			squid11.setDirection(Direction.DOWN);
			squid12.setDirection(Direction.DOWN);
			squid13.setDirection(Direction.DOWN);
			squid14.setDirection(Direction.DOWN);
			squid15.setDirection(Direction.LEFT);
			squid16.setDirection(Direction.LEFT);
			squid17.setDirection(Direction.RIGHT);
			
			level.addObject(squid1);
			level.addObject(squid2);
			level.addObject(squid3);
			level.addObject(squid4);
			level.addObject(squid5);
			level.addObject(squid6);
			level.addObject(squid7);
			level.addObject(squid8);
			level.addObject(squid9);
			level.addObject(squid10);
			level.addObject(squid11);
			level.addObject(squid12);
			level.addObject(squid13);
			level.addObject(squid14);
			level.addObject(squid15);
			level.addObject(squid16);
			level.addObject(squid17);
			
			
			Octopus oct1 = new Octopus(2,1,96,7,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct2 = new Octopus(2,1,108,2,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct3 = new Octopus(2,1,103,10,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct4 = new Octopus(2,1,103,23,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct5 = new Octopus(2,1,106,6,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct6 = new Octopus(2,1,106,15,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct7 = new Octopus(2,1,106,20,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct8 = new Octopus(2,1,108,23,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct9 = new Octopus(2,1,108,17,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct10 = new Octopus(2,1,108,2,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct11 = new Octopus(2,1,113,2,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct12 = new Octopus(2,1,116,5,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct13 = new Octopus(2,1,113,10,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct14 = new Octopus(2,1,113,23,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct15 = new Octopus(2,1,116,16,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct16 = new Octopus(2,1,119,19,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct17 = new Octopus(2,1,121,8,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct18 = new Octopus(2,1,129,1,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct19 = new Octopus(2,1,135,1,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct20 = new Octopus(2,1,141,1,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct21 = new Octopus(2,1,129,8,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct22 = new Octopus(2,1,135,8,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct23 = new Octopus(2,1,141,8,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct24 = new Octopus(2,1,147,1,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			Octopus oct25 = new Octopus(2,1,147,8,1,1.8,23,1,1,1,Resources.getImage("octopus_0"));
			
			oct1.setDirection(Direction.DOWN);
			oct2.setDirection(Direction.DOWN);
			oct3.setDirection(Direction.DOWN);
			oct4.setDirection(Direction.DOWN);
			oct5.setDirection(Direction.RIGHT);
			oct6.setDirection(Direction.RIGHT);
			oct7.setDirection(Direction.RIGHT);
			oct8.setDirection(Direction.DOWN);
			oct9.setDirection(Direction.DOWN);
			oct10.setDirection(Direction.DOWN);
			oct11.setDirection(Direction.DOWN);
			oct12.setDirection(Direction.RIGHT);
			oct13.setDirection(Direction.DOWN);
			oct14.setDirection(Direction.DOWN);
			oct15.setDirection(Direction.RIGHT);
			oct16.setDirection(Direction.LEFT);
			oct17.setDirection(Direction.DOWN);
			oct18.setDirection(Direction.DOWN);
			oct19.setDirection(Direction.DOWN);
			oct20.setDirection(Direction.DOWN);
			oct21.setDirection(Direction.DOWN);
			oct22.setDirection(Direction.UP);
			oct23.setDirection(Direction.DOWN);
			oct24.setDirection(Direction.DOWN);
			oct25.setDirection(Direction.UP);
			
			
			
			level.addObject(oct1);
			level.addObject(oct2);
			level.addObject(oct3);
			level.addObject(oct4);
			level.addObject(oct5);
			level.addObject(oct6);
			level.addObject(oct7);
			level.addObject(oct8);
			level.addObject(oct9);
			level.addObject(oct10);
			level.addObject(oct11);
			level.addObject(oct12);
			level.addObject(oct13);
			level.addObject(oct14);
			level.addObject(oct15);
			level.addObject(oct16);
			level.addObject(oct17);
			level.addObject(oct18);
			level.addObject(oct19);
			level.addObject(oct20);
			level.addObject(oct21);
			level.addObject(oct22);
			level.addObject(oct23);
			level.addObject(oct24);
			level.addObject(oct25);
			
			
			GameObject abyss1 = new GameObject(155.5,39,40,1,Resources.getImage("abyss"));
			
			Worm worm = new Worm(2,2,3000,156,39,1,3,23,1,1,1,Resources.getImage("worm_0"));
			Worm worm1 = new Worm(2,2,3000,160,39,1,3,23,1,1,1,Resources.getImage("worm_0"));
			Worm worm2 = new Worm(2,2,3000,164,39,1,3,23,1,1,1,Resources.getImage("worm_0"));
			Worm worm3 = new Worm(2,2,3000,168,39,1,3,23,1,1,1,Resources.getImage("worm_0"));
			Worm worm4 = new Worm(2,2,3000,172,39,1,3,23,1,1,1,Resources.getImage("worm_0"));
			Worm worm5 = new Worm(2,2,3000,176,39,1,3,23,1,1,1,Resources.getImage("worm_0"));
			Worm worm6 = new Worm(2,2,3000,180,39,1,3,23,1,1,1,Resources.getImage("worm_0"));
			Worm worm7 = new Worm(2,2,3000,184,39,1,3,23,1,1,1,Resources.getImage("worm_0"));
			
			
			
			
			abyss1.setCollidable(false);
			
			
			level.addObjectFirst(abyss1);
			level.addObjectFirst(worm);
			level.addObjectFirst(worm1);
			level.addObjectFirst(worm2);
			level.addObjectFirst(worm3);
			level.addObjectFirst(worm4);
			level.addObjectFirst(worm5);
			level.addObjectFirst(worm6);
			level.addObjectFirst(worm7);
			
			
			
			level.addObject(new HealthPickup(2, -1, 90, 27, Resources.getImage("Health Pickup")));
			level.addObject(new HealthPickup(2, -1, 109, 13, Resources.getImage("Health Pickup")));
			level.addObject(new HealthPickup(2, -1, 153, 38, Resources.getImage("Health Pickup")));
			
			
			

			
			
			
			//END LEVEL
			GameObject end = new GameObject(187, 38, 0.5, 1, Resources.getImage("bottle"));
			end.setName("END");
			level.addObjectFirst(end);
			
			
			
			
		}
		
		if (level.getId() == 3) {
			Penguin pen1 = new Penguin(2, 1, 17.1, 6.2, 1, 1.8, 23, 1, 1, 1, Resources.getImage("penguin_0"));
			Penguin pen2 = new Penguin(2, 1, 19, 6.2, 1, 1.8, 23, 1, 1, 1, Resources.getImage("penguin_0"));
			Penguin pen3 = new Penguin(2, 1, 107, 3.2, 1, 1.8, 23, 1, 1, 1, Resources.getImage("penguin_0"));
			Penguin pen4 = new Penguin(2, 1, 179, 6.2, 1, 1.8, 23, 1, 1, 1, Resources.getImage("penguin_0"));
			pen4.setDirection(Direction.LEFT);
			pen1.setDirection(Direction.LEFT);
			pen3.setDirection(Direction.LEFT);
			level.addObject(pen1);
			level.addObject(pen2);
			level.addObject(pen3);
			level.addObject(pen4);
			//Resources.loadImage("crate");
			//level.addObject(new PhysicsObject(17,0,1,1,20, 0.62, 0.62, 0.8, Resources.getImage("crate")));
			//level.addObject(new PhysicsObject(19,0,1,1,20, 0.62, 0.62, 0.8, Resources.getImage("crate")));
			//level.addObject(new PhysicsObject(20.4,0,1,1,20, 0.62, 0.62, 0.8, Resources.getImage("crate")));
			//level.addObject(new PhysicsObject(22,0,1,1,20, 0.62, 0.62, 0.8, Resources.getImage("crate")));
			
			level.addObject(new FuelPickup(50, -1, 89, 4, Resources.getImage("pickup_fuel_0")));
			level.addObject(new FuelPickup(50, -1, 160, 7, Resources.getImage("pickup_fuel_0")));
			level.addObject(new HealthPickup(2, -1, 123, 7, Resources.getImage("Health Pickup")));
			
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 169, 1.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 171, 1.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 173, 1.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 174, 1.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 180, 1.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 183, 1.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 184, 1.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			
			
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 194, 0.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 198, 0.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 200, 0.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 201, 0.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 205, 0.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 207, 0.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 216, 0.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 218, 0.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 219, 0.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ICE, 223, 0.3, 1, 1, 500, 0, 0, 0, Resources.getImage("spike_ice")));
			
			// End of level
			GameObject end = new GameObject(244, 2, 4, 6, Resources.getImage("snow_final"));
			end.setName("END");
			level.addObjectFirst(end);
		}
		if (level.getId() == 4){
			FlashLight flash1 = new FlashLight(500000,5000,21,4,1,1,Resources.getImage("FlashLight"));
			//level.addObject(new HealthPickup(2, -1, 40, 7, Resources.getImage("Health Pickup")));
			FlashLight flash2 = new FlashLight(500000,5000,30,4,1,1,Resources.getImage("FlashLight"));
			FlashLight flash3 = new FlashLight(500000,5000,40,7,1,1,Resources.getImage("FlashLight"));
			FlashLight flash4 = new FlashLight(500000,5000,48,3,1,1,Resources.getImage("FlashLight"));
			FlashLight flash5 = new FlashLight(500000,5000,60,4,1,1,Resources.getImage("FlashLight"));
			FlashLight flash6 = new FlashLight(500000,5000,40,7,1,1,Resources.getImage("FlashLight"));
			FlashLight flash7 = new FlashLight(500000,5000,64,15,1,1,Resources.getImage("FlashLight"));
			FlashLight flash8 = new FlashLight(500000,5000,80,23,1,1,Resources.getImage("FlashLight"));
			FlashLight flash9 = new FlashLight(500000,5000,89,7,1,1,Resources.getImage("FlashLight"));
			FlashLight flash10 = new FlashLight(500000,5000,98,26,1,1,Resources.getImage("FlashLight"));
			FlashLight flash11 = new FlashLight(500000,5000,106,22,1,1,Resources.getImage("FlashLight"));
			FlashLight flash12 = new FlashLight(500000,5000,101,15,1,1,Resources.getImage("FlashLight"));
			FlashLight flash13 = new FlashLight(500000,5000,109,10,1,1,Resources.getImage("FlashLight"));
			FlashLight flash14 = new FlashLight(500000,5000,113,28,1,1,Resources.getImage("FlashLight"));
			FlashLight flash15 = new FlashLight(500000,5000,120,28,1,1,Resources.getImage("FlashLight"));
			FlashLight flash16 = new FlashLight(500000,5000,126,28,1,1,Resources.getImage("FlashLight"));
			
			level.addObject(flash1);
			level.addObject(flash2);
			level.addObject(flash3);
			level.addObject(flash4);
			level.addObject(flash5);
			level.addObject(flash6);
			level.addObject(flash7);
			level.addObject(flash8);
			level.addObject(flash9);
			level.addObject(flash10);
			level.addObject(flash11);
			level.addObject(flash12);
			level.addObject(flash13);
			level.addObject(flash14);
			level.addObject(flash15);
			level.addObject(flash16);
			
			level.addObject(new FuelPickup(50, -1, 64, 6, Resources.getImage("pickup_fuel_0")));
			level.addObject(new HealthPickup(2, -1, 66, 10, Resources.getImage("Health Pickup")));
			level.addObject(new HealthPickup(2, -1, 114, 28, Resources.getImage("Health Pickup")));
			
			Bat bat1 = new Bat(2, 1, 42, 5, 0.8, 0.8, 10, 1, 1, 1, Resources.getImage("bat_0"));
			Bat bat2 = new Bat(2, 1, 55, 5, 0.8, 0.8, 10, 1, 1, 1, Resources.getImage("bat_0"));
			Bat bat3 = new Bat(2, 1, 64, 11, 0.8, 0.8, 10, 1, 1, 1, Resources.getImage("bat_0"));
			Bat bat4 = new Bat(2, 1, 68, 13, 0.8, 0.8, 10, 1, 1, 1, Resources.getImage("bat_0"));
			Bat bat5 = new Bat(2, 1, 83, 21, 0.8, 0.8, 10, 1, 1, 1, Resources.getImage("bat_0"));
			Bat bat6 = new Bat(2, 1, 92, 4, 0.8, 0.8, 10, 1, 1, 1, Resources.getImage("bat_0"));
			Bat bat7 = new Bat(2, 1, 96, 4, 0.8, 0.8, 10, 1, 1, 1, Resources.getImage("bat_0"));
			
			bat1.setDirection(Direction.UP);
			bat2.setDirection(Direction.LEFT);
			bat3.setDirection(Direction.RIGHT);
			bat4.setDirection(Direction.DOWN);
			bat5.setDirection(Direction.DOWN);
			bat6.setDirection(Direction.UP);
			bat7.setDirection(Direction.DOWN);
			
			level.addObject(bat1);
			level.addObject(bat2);
			level.addObject(bat3);
			level.addObject(bat4);
			level.addObject(bat5);
			level.addObject(bat6);
			//level.addObject(bat7);
			
			Snake snake1 = new Snake(true,2, 1, 101, 21, 1, 1, 23, 1, 1, 1, Resources.getImage("snake"));
			Snake snake2 = new Snake(false,2, 1, 106, 13, 1, 1, 23, 1, 1, 1, Resources.getImage("snake"));
			
			snake1.setDirection(Direction.STATIC);
			snake2.setDirection(Direction.STATIC);
			
			level.addObject(snake1);
			level.addObject(snake2);
			
			level.addObjectFirst(new FallingSpike(FallingSpike.ROCK, 119, 22.3, 1, 1, 500, 0, 0, 0, Resources.getImage("stalactitte")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ROCK, 120, 22.3, 1, 1, 500, 0, 0, 0, Resources.getImage("stalactitte")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ROCK, 121, 22.3, 1, 1, 500, 0, 0, 0, Resources.getImage("stalactitte")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ROCK, 122, 22.3, 1, 1, 500, 0, 0, 0, Resources.getImage("stalactitte")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ROCK, 123, 22.3, 1, 1, 500, 0, 0, 0, Resources.getImage("stalactitte")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ROCK, 124, 22.3, 1, 1, 500, 0, 0, 0, Resources.getImage("stalactitte")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ROCK, 125, 22.3, 1, 1, 500, 0, 0, 0, Resources.getImage("stalactitte")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ROCK, 126, 22.3, 1, 1, 500, 0, 0, 0, Resources.getImage("stalactitte")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ROCK, 127, 22.3, 1, 1, 500, 0, 0, 0, Resources.getImage("stalactitte")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ROCK, 128, 22.3, 1, 1, 500, 0, 0, 0, Resources.getImage("stalactitte")));
			level.addObjectFirst(new FallingSpike(FallingSpike.ROCK, 129, 22.3, 1, 1, 500, 0, 0, 0, Resources.getImage("stalactitte")));
			
		
			// End of level
						GameObject end = new GameObject(150, 18, 4, 4, Resources.getImage("cave_end"));
						end.setName("END");
						level.addObjectFirst(end);
		
		
		}
		
		if (level.getId() == 5) {
			
			Golum golum1 = new Golum(23,3.5);
			golum1.setWidth(1.5);
			golum1.setHeight(1.5);
			Golum golum2 = new Golum(48,4);
			Golum golum3 = new Golum(59.2,6.5);
			golum3.setWidth(1.5);
			golum3.setHeight(1.5);
			Golum golum4 = new Golum(62,6);
			Golum golum5 = new Golum(69,6);
			Golum golum6 = new Golum(106,6);
			
			Golum golum7 = new Golum(151,6);
			Golum golum8 = new Golum(157,6);
			Golum golum9 = new Golum(163,6);
			
			golum1.setDirection(Direction.LEFT);
			golum2.setDirection(Direction.LEFT);
			golum3.setDirection(Direction.LEFT);
			golum4.setDirection(Direction.LEFT);
			golum5.setDirection(Direction.LEFT);
			golum6.setDirection(Direction.LEFT);
			golum7.setDirection(Direction.LEFT);
			golum8.setDirection(Direction.LEFT);
			golum9.setDirection(Direction.LEFT);
			
			level.addObjectFirst(golum1);
			level.addObjectFirst(golum2);
			level.addObjectFirst(golum3);
			level.addObjectFirst(golum4);
			level.addObjectFirst(golum5);
			level.addObjectFirst(golum6);
			level.addObjectFirst(golum7);
			level.addObjectFirst(golum8);
			level.addObjectFirst(golum9);
			
			level.addObjectFirst(new Fireball(16,8.2));
			level.addObjectFirst(new Fireball(18,8.2));
			level.addObjectFirst(new Fireball(28,8.2));
			level.addObjectFirst(new Fireball(30,8.2));
			level.addObjectFirst(new Fireball(32,8.2));
			level.addObjectFirst(new Fireball(39.5,8.2));
			level.addObjectFirst(new Fireball(125,8.2));
			level.addObjectFirst(new Fireball(127,8.2));
			level.addObjectFirst(new Fireball(117,8.2));
			level.addObjectFirst(new Fireball(169,8.2));
			level.addObjectFirst(new Fireball(172,8.2));
			level.addObjectFirst(new Fireball(205,8.2));
			level.addObjectFirst(new Fireball(219,8.2));
			level.addObjectFirst(new Fireball(221,8.2));
			level.addObjectFirst(new Fireball(231.5,8.2));
			level.addObjectFirst(new Fireball(242.5,8.2));
			level.addObjectFirst(new Fireball(244.5,8.2));
			level.addObjectFirst(new Fireball(258.5,8.2));
			
			level.addObject(new FuelPickup(50,-1,71,7, Resources.getImage("pickup_fuel_0")));
			level.addObject(new HealthPickup(2, -1, 139, 7, Resources.getImage("Health Pickup")));
			level.addObject(new HealthPickup(2, -1, 237.5, 4, Resources.getImage("Health Pickup")));
			
			// End of level
			GameObject end = new GameObject(296, 4, 4, 4, Resources.getImage("lava_end"));
			end.setName("END");
			level.addObjectFirst(end);
		}
		if (level.getId() == 6){
			Monkey monkey1 = new Monkey(true,2, 1, 29, 15, 1.5, 2, 10, 1, 1, 1, Resources.getImage("monkey_0"));
			Monkey monkey2 = new Monkey(true,2, 1, 38, 15, 1.5, 2, 10, 1, 1, 1, Resources.getImage("monkey_0"));
			Monkey monkey3 = new Monkey(true,2, 1, 53, 9, 1.5, 2, 10, 1, 1, 1, Resources.getImage("monkey_0"));
			Monkey monkey4 = new Monkey(true,2, 1, 65, 9, 1.5, 2, 10, 1, 1, 1, Resources.getImage("monkey_0"));
			Monkey monkey5 = new Monkey(true,2, 1, 109, 2, 1.5, 2, 10, 1, 1, 1, Resources.getImage("monkey_0"));
			Monkey monkey6 = new Monkey(true,2, 1, 116, 2, 1.5, 2, 10, 1, 1, 1, Resources.getImage("monkey_0"));
			Monkey monkey7 = new Monkey(false,2, 1, 144, 16, 1.5, 2, 10, 1, 1, 1, Resources.getImage("monkey_0"));
			Monkey monkey8 = new Monkey(false,2, 1, 144, 19, 1.5, 2, 10, 1, 1, 1, Resources.getImage("monkey_0"));
			Monkey monkey9 = new Monkey(true,2, 1, 160, 16, 1.5, 2, 10, 1, 1, 1, Resources.getImage("monkey_0"));
			Monkey monkey10 = new Monkey(true,2, 1, 160, 19, 1.5, 2, 10, 1, 1, 1, Resources.getImage("monkey_0"));
			
			monkey1.setDirection(Direction.STATIC);
			monkey2.setDirection(Direction.STATIC);
			monkey3.setDirection(Direction.STATIC);
			monkey4.setDirection(Direction.STATIC);
			monkey5.setDirection(Direction.STATIC);
			monkey6.setDirection(Direction.STATIC);
			monkey7.setDirection(Direction.STATIC);
			monkey8.setDirection(Direction.STATIC);
			monkey9.setDirection(Direction.STATIC);
			monkey10.setDirection(Direction.STATIC);
			
			level.addObject(monkey1);
			level.addObject(monkey2);
			level.addObject(monkey3);
			level.addObject(monkey4);
			level.addObject(monkey5);
			level.addObject(monkey6);
			level.addObject(monkey7);
			level.addObject(monkey8);
			level.addObject(monkey9);
			level.addObject(monkey10);
			
			Archer archer1 = new Archer(true,2, 1, 84, 8, 1, 2, 10, 1, 1, 1, Resources.getImage("archer_0"));
			Archer archer2 = new Archer(false,2, 1, 95, 5, 1, 2, 10, 1, 1, 1, Resources.getImage("archer_0"));
			Archer archer3 = new Archer(true,2, 1, 84, 4, 1, 2, 10, 1, 1, 1, Resources.getImage("archer_0"));
			Archer archer4 = new Archer(true,2, 1, 117, 12, 1, 2, 10, 1, 1, 1, Resources.getImage("archer_0"));
			Archer archer5 = new Archer(true,2, 1, 117, 17, 1, 2, 10, 1, 1, 1, Resources.getImage("archer_0"));
			Archer archer6 = new Archer(false,2, 1, 134, 13, 1, 2, 10, 1, 1, 1, Resources.getImage("archer_0"));
			Archer archer7 = new Archer(false,2, 1, 134, 20, 1, 2, 10, 1, 1, 1, Resources.getImage("archer_0"));
			
			archer1.setDirection(Direction.STATIC);
			archer2.setDirection(Direction.STATIC);
			archer3.setDirection(Direction.STATIC);
			archer4.setDirection(Direction.STATIC);
			archer5.setDirection(Direction.STATIC);
			archer6.setDirection(Direction.STATIC);
			archer7.setDirection(Direction.STATIC);
			
			
			
			level.addObject(archer1);
			level.addObject(archer2);
			level.addObject(archer3);
			level.addObject(archer4);
			level.addObject(archer5);
			level.addObject(archer6);
			level.addObject(archer7);
			
			level.addObject(new FuelPickup(50,-1,93,15, Resources.getImage("pickup_fuel_0")));
			level.addObject(new FuelPickup(50,-1,122,10, Resources.getImage("pickup_fuel_0")));
			level.addObject(new HealthPickup(2, -1, 93, 2, Resources.getImage("Health Pickup")));
			level.addObject(new HealthPickup(2, -1, 127, 18, Resources.getImage("Health Pickup")));
			
			// End of level
			GameObject end = new GameObject(150, 22, 2, 1, Resources.getImage("jungle_end"));
			end.setName("END");
			level.addObjectFirst(end);
			
			
			
			
			
		}
	}
	
	public double getXPos(){
		return xPos;
	}
	
	public double getYPos(){
		return yPos;
	}
	
	public Level getLevel(){
		return level;
	}
	
	public Rectangle getCollider() {
		return collider;
	}
	
	public HUD getHUD() {
		return hud;
	}
	
	public RectangleOverlay getOverlay() {
		return overlay;
	}
	
	public void setXPos(double xPos){
		this.xPos = xPos;
	}
	
	public void setYPos(double yPos){
		this.yPos = yPos;
	}
	
	public void setWidth(int width) {
		this.width = width;
		screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public void setHeight(int height) {
		this.height = height;
		screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public void setOverlay(RectangleOverlay overlay) {
		this.overlay = overlay;
	}
	
	public void pause() {
		paused = true;
	}
	
	public void unpause() {
		paused = false;
	}
	
	public void lock() {
		lock.lock();
	}
	
	public void unlock() {
		lock.unlock();
	}
	
	public void changeSpotlight(boolean change){
		changeSpotlight = change;
	}
		
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(screen, 0, 0, getWidth(), getHeight(), null);
		
	}

}
