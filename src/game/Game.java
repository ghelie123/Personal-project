package game;

import game.level.Level;
import game.object.MapIcon;
import game.object.Player;
import game.object.PlayerMap;
import game.view.ViewPort;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;


public class Game extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Journey to the Center of the Earth";
	public static final int FPS = 60;
	public static final int WIDTH = 854;
	public static final int HEIGHT = 480;
	public static final boolean RESIZABLE = false;
	public static final boolean DRAW_GRID = false;
	public static final boolean DRAW_COLLIDER = false;
	public static final boolean DRAW_FPS = true;
	public static final boolean DRAW_EDGE_DETECT = false;
	public static final boolean DRAW_VELOCITY = false;
	
	
	private static int unitX;
	private static int unitY;
	private static int frameRate;
	private static boolean running;
	private static ViewPort view;
	private static Player player;
	private static Level mapSelect;
	
	public Game() {
		super(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(RESIZABLE);
		
		Input input = new Input();
		addKeyListener(input);
		addMouseListener(input);
		addComponentListener(input);
		addFocusListener(input);
		setFocusable(true);
		
		Input.registerKey(KeyEvent.VK_A);
		Input.registerKey(KeyEvent.VK_D);
		Input.registerKey(KeyEvent.VK_SPACE);
		Input.registerKey(KeyEvent.VK_S);
		Input.registerKey(KeyEvent.VK_W);
		Input.registerKey(KeyEvent.VK_Q);
		Input.registerKey(KeyEvent.VK_ENTER);
		Input.registerKey(KeyEvent.VK_ESCAPE);
		Input.setListening(false);
		
		addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				view.lock();
				unitX = getWidth() / 16;
				unitY = getHeight() / 9;
				view.setPreferredSize(new Dimension(getWidth(), getHeight()));
				view.setWidth(16 * unitX);
				view.setHeight(9 * unitY);
				view.getLevel().getBackground().recompute();
				view.unlock();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// Compute game unit
		unitX = WIDTH / 16;
		unitY = HEIGHT / 9;
		
		// Set icon
		Resources.loadImage("icon");
		setIconImage(Resources.getImage("icon"));
		
		Resources.loadFont("pixelmix");
		Resources.loadSound("snow_step");
		Resources.loadSound("Upper Cut");
		Resources.loadSound("Jab");
		Resources.loadSound("Left Hook");
		Resources.loadSound("Right Cross");
		Resources.loadSound("Right Hook");
		Resources.loadSound("fireball");
		Resources.loadSound("lava_bgm");
		Resources.loadSound("main_bgm");
		Resources.loadSound("map_bgm");
		Resources.loadSound("wind");
		Resources.loadSound("snow_bgm");
		Resources.loadSound("jetpack");
		Resources.loadSound("ice_shatter");
		Resources.loadImage("map");
		Resources.loadImage("heart_empty");
		Resources.loadImage("heart_full");
		Resources.loadImage("heart_half");
		Resources.loadImage("can_empty");
		Resources.loadImage("can_glass");
		Resources.loadImage("FlashLight");
		Resources.loadImage("pickup_fuel_0");
		Resources.loadImage("pickup_fuel_0");
		Resources.loadImage("Play");
		Resources.loadImage("Options");
		Resources.loadImage("Exit");
		Resources.loadImage("option button 1");
		Resources.loadImage("option button 2");
		Resources.loadImage("option button 3");
		Resources.loadImage("play button 1");
		Resources.loadImage("play button 2");
		Resources.loadImage("play button 3");
		Resources.loadImage("snow_background");
		Resources.loadImage("snow_clouds");
		Resources.loadImage("jungle_background");
		Resources.loadImage("snow_final");
		Resources.loadImage("black");
		Resources.loadImage("stalactitte");
		Resources.loadImage("abyss");
		Resources.loadImage("bottle");
		Resources.loadImage("lava_rock");
		Resources.loadImage("lava_end");
		Resources.loadImage("jungle_end");
		Resources.loadSpriteSheet("map_player", "map_player", 15, 37, new int[0]);
		//Resources.loadTextFile("SavedData");
		Resources.loadImage("InGame menu 1");
		Resources.loadImage("InGame menu 2");
		Resources.loadImage("InGame menu 3");
		Resources.loadImage("InGame menu 4");
		Resources.loadImage("InGame menu 5");
		Resources.loadImage("Health Pickup");
		Resources.loadImage("spike_ice");
		Resources.loadImage("snake");
		Resources.loadImage("venom");
		Resources.loadImage("arrow");
		Resources.loadImage("arrow2");
		Resources.loadImage("cave_end");
		Resources.loadImage("map_icon_locked");
		Resources.loadImage("overlay");
		Resources.loadImage("BigOverlay");
		Resources.loadImage("cave_background");
		Resources.loadImage("lava_background");
		Resources.loadSpriteSheet("map_player", "map_player", 15, 37, new int[0]);
		Resources.loadSpriteSheet("bat", "bat",23 , 18, new int[0]);
		Resources.loadSpriteSheet("player", "player", 22, 43, new int[0]);
		Resources.loadSpriteSheet("penguin", "penguin", 14, 25, new int[0]);
		Resources.loadSpriteSheet("snow_level", "snow", 32, 32, new int[0]);
		Resources.loadSpriteSheet("spike_ice_shatter", "ice_shatter", 32, 32, new int[0]);
		Resources.loadSpriteSheet("rock_shatter", "rock_shatter", 32, 32, new int[0]);
		Resources.loadSpriteSheet("venom shatter", "venom shatter", 13 , 10, new int[0]);
		Resources.loadSpriteSheet("map_icon", "map_icon", 54, 18, new int[0]);
		Resources.loadSpriteSheet("water_level", "water", 32, 32, new int[0]);
		Resources.loadSpriteSheet("swim_test", "swim", 43, 29, new int[0]);
		Resources.loadSpriteSheet("octopus", "octopus", 39,45, new int[0]);
		Resources.loadSpriteSheet("monkey","monkey",32,32,new int[0]);
		Resources.loadSpriteSheet("archer", "archer", 21, 28, new int[0]);
		Resources.loadSpriteSheet("squid", "squid", 22,27, new int[0]);
		Resources.loadSpriteSheet("banana", "banana", 8, 8, new int[0]);
		Resources.loadSpriteSheet("lava_level", "lava", 32, 32, new int[0]);
		Resources.loadSpriteSheet("golum", "golum", 29, 29, new int[0]);
		Resources.loadSpriteSheet("fireball", "fireball", 38, 51, new int[0]);
		Resources.loadSpriteSheet("lava_rock_shatter", "lava_rock_shatter", 30, 30, new int[0]);
		Resources.loadSpriteSheet("worm", "worm", 32, 32, new int[0]);
		Resources.loadSpriteSheet("cave_level", "cave", 32, 32, new int[0]);
		Resources.loadSpriteSheet("jungle_level", "jungle", 32, 32, new int[0]);
		Resources.loadImage("submarine");
		Resources.loadImage("fish");
		Resources.loadImage("water_background");
		Resources.loadLevel("snow", 3);
		Resources.loadLevel("water", 2);
		Resources.loadLevel("test2", 3);
		Resources.loadLevel("cave", 4);
		Resources.loadLevel("lava", 5);
		Resources.loadLevel("jungle",6);

		
		player = new Player(5, 6, 6, 100);
		//player.setVisible(false);
		view = new ViewPort(0,0, unitX * 16, unitY * 9);
		mapSelect = new Level(1, 16,9);
		
		MapIcon mapIcon1 = new MapIcon(1.7, 1.9);
		MapIcon mapIcon2 = new MapIcon(10.6, 1.9);
		MapIcon mapIcon3 = new MapIcon(10.6, 3.3);
		MapIcon mapIcon4 = new MapIcon(10.6, 5.5);
		MapIcon mapIcon5 = new MapIcon(10.6, 8.2);
		
		mapIcon1.unlock();
		mapIcon2.unlock();
		mapIcon3.unlock();
		mapIcon4.unlock();
		mapIcon5.unlock();
		
		mapSelect.addObject(mapIcon1);
		mapSelect.addObject(mapIcon2);
		mapSelect.addObject(mapIcon3);
		mapSelect.addObject(mapIcon4);
		mapSelect.addObject(mapIcon5);
		
		MapIcon[] icons = new MapIcon[]{mapIcon1, mapIcon2, mapIcon3, mapIcon4, mapIcon5};
		
		mapSelect.addObject(new PlayerMap(2, icons));
		mapSelect.saveInitialObjects();
		//GameObject mapImage = new GameObject(0, 0, 16, 9, Resources.getImage("map"));
		//mapImage.setCollidable(false);
		//mapSelect.addObject(mapImage);
		
		//player = new Player(0, 7, 6, 10);
		//player.applyForce(new Vector2D(35000, -40000));
		
		//player = new Player(15, 7, 6, 10);
		//player.applyForce(new Vector2D(-35000, -40000));
		
		
		
		//menu = new MainMenu(4,1,8,8,Resources.getImage("Play"));
		
		//player = new Player(5, 9, 6, 10);
		//player.applyForce(new Vector2D(0, -40000));
		
		add(view);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		JukeBox.setBackgroundMusic("main_bgm");
	}

	public static void main(String[] args) {
		File file = new File("save.dat");
		if (!file.exists()) {
			try {
				DataOutputStream output = new DataOutputStream(new FileOutputStream(file));
				output.writeInt(2);
				output.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Game game = new Game();
		Thread mainThread = new Thread(game);
		mainThread.setName("Game");
		mainThread.start();
	}

	@Override
	public void run() {
		// Game loop
		running = true;
		long lastTime = System.nanoTime();
		int targetTime = 1000000000 / FPS;
		long deltaTime = 0;
		int frames = 0;
		long counter = 0;
		long delay = 0;

		while (running) {
			deltaTime = System.nanoTime() - lastTime;
			//System.out.print(deltaTime / 1000000);

			// Delay until target time is reached.
			if (deltaTime < targetTime) {
				delay = targetTime - deltaTime;
				//System.out.print(" delay: " + (delay/1000000));
				try {
					long test = System.currentTimeMillis();
					Thread.sleep(delay / 1000000);
					//System.out.println(" slept: " + (System.currentTimeMillis() - test));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				deltaTime += delay;
			}

			lastTime = System.nanoTime();

			// Update and render.
			long test = System.currentTimeMillis();
			tick((int) (deltaTime / 1000000));
			//System.out.println("TICK: " + (System.currentTimeMillis() - test));
			//System.out.println("DELTATIME: " + (deltaTime / 1000000));
			
			test = System.currentTimeMillis();
			render();
			//System.out.println("RENDER: " + (System.currentTimeMillis() - test));
			//System.out.println(((System.nanoTime() - lastTime) / 1000000) + "ms");
			//System.out.println(deltaTime);
			// Frame Counter.
			counter += deltaTime;
			if (counter >= 1000000000) {
				frameRate = frames;
				frames = 0;
				counter = 0;
			} else {
				frames++;
			}
		}

	}

	public void tick(int deltaTime) {
		// Called by game loop. Calls tick of the ViewPort.
		//long t = System.currentTimeMillis();
		view.lock();
		view.tick(deltaTime);
		view.unlock();
		//System.out.println("TICK: " + (System.currentTimeMillis() - t) + "ms");
		
	}

	public void render() {
		// Called by game loop. Renders the ViewPort.
		//long t = System.currentTimeMillis();
		view.lock();
		view.render();
		//System.out.println("RENDER: " + (System.currentTimeMillis() - t) + "ms");
		view.unlock();
	}

	public static int  getUnitX() {
		return unitX;
	}
	
	public static int getUnitY() {
		return unitY;
	}

	public static int  getFrameRate() {
		return frameRate;
	}

	public static ViewPort getViewPort() {
		return view;
	}

	public static Player getPlayer() {
		return player;
	}
	
	public static Level getMapSelect() {
		return mapSelect;
	}

	public static void stop() {
		// Stops the game.
		running = false;
	}

}
