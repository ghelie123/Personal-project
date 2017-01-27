package game.object;

import game.Game;
import game.Input;
import game.Resources;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class PlayerMap extends GameObject {

	private int currentLevel;
	
	private double[][] positions;
	
	private MapIcon[] icons;;
	
	public PlayerMap(int currentLevel, MapIcon[] icons) {
		super(0, 0, 0.6, 1.7, Resources.getImage("map_player_0"));
		this.currentLevel = currentLevel;
		this.icons = icons;
		
		positions = new double[][]{{1.75, 0.375},
				{10.65, 0.375},
				{10.65, 1.75},
				{10.65, 3.95},
				{10.65, 6.65}};
		setXPos(positions[currentLevel - 2][0]);
		setYPos(positions[currentLevel - 2][1]);
		
		BufferedImage[] images = new BufferedImage[]{Resources.getImage("map_player_0"),
				Resources.getImage("map_player_1"),
				Resources.getImage("map_player_2"),
				Resources.getImage("map_player_3"),
				Resources.getImage("map_player_4"),
				Resources.getImage("map_player_3"),
				Resources.getImage("map_player_2"),
				Resources.getImage("map_player_1"),
				Resources.getImage("map_player_0"),};
		int[] delays = new int[]{100,100,100,100,100,100,100,100,50};
		Animation anim = new Animation(images, delays);
		setAnimation(anim);
		
	}
	
	@Override
	public void tick(int deltaTime) {
		super.tick(deltaTime);
		
		if (Input.isKeyPressed(KeyEvent.VK_ENTER)) {
			switch (currentLevel) {
			
			case 2:
				Game.getViewPort().transitionLevel(Resources.getLevel("water"));
				break;
				
			case 3:
				Game.getViewPort().transitionLevel(Resources.getLevel("snow"));
				break;
				
			case 4:
				Game.getViewPort().transitionLevel(Resources.getLevel("cave"));
				break;
			case 5:
				Game.getViewPort().transitionLevel(Resources.getLevel("lava"));
				break;
			case 6: Game.getViewPort().transitionLevel(Resources.getLevel("jungle"));
					break;
			default:
				Game.getViewPort().transitionLevel(Game.getMapSelect());
			}
		}
		
		if (Input.isKeyPressed(KeyEvent.VK_D) || Input.isKeyPressed(KeyEvent.VK_S)) {
			if (currentLevel == 2 && !icons[1].isLocked()) {
				currentLevel = 3;
				setXPos(positions[currentLevel - 2][0]);
				setYPos(positions[currentLevel - 2][1]);
			}
			else if (currentLevel == 3 && !icons[2].isLocked()) {
				currentLevel = 4;
				setXPos(positions[currentLevel - 2][0]);
				setYPos(positions[currentLevel - 2][1]);
			}
			else if (currentLevel == 4 && !icons[3].isLocked()) {
				currentLevel = 5;
				setXPos(positions[currentLevel - 2][0]);
				setYPos(positions[currentLevel - 2][1]);
			}
			else if (currentLevel == 5 && !icons[4].isLocked()) {
				currentLevel = 6;
				setXPos(positions[currentLevel - 2][0]);
				setYPos(positions[currentLevel - 2][1]);
			}
			Input.requireRelease(KeyEvent.VK_D);
			Input.requireRelease(KeyEvent.VK_S);
		}
		
		if (Input.isKeyPressed(KeyEvent.VK_A) || Input.isKeyPressed(KeyEvent.VK_W)) {
			if (currentLevel == 3) {
				currentLevel = 2;
				setXPos(positions[currentLevel - 2][0]);
				setYPos(positions[currentLevel - 2][1]);
			}
			else if (currentLevel == 4) {
				currentLevel = 3;
				setXPos(positions[currentLevel - 2][0]);
				setYPos(positions[currentLevel - 2][1]);
			}
			else if (currentLevel == 5) {
				currentLevel = 4;
				setXPos(positions[currentLevel - 2][0]);
				setYPos(positions[currentLevel - 2][1]);
			}
			else if (currentLevel == 6) {
				currentLevel = 5;
				setXPos(positions[currentLevel - 2][0]);
				setYPos(positions[currentLevel - 2][1]);
			}
			Input.requireRelease(KeyEvent.VK_A);
			Input.requireRelease(KeyEvent.VK_W);
		}
	}

}
