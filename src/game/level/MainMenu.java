package game.level;

import game.Game;
import game.Input;
import game.Resources;
import game.view.HUDElement;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class MainMenu extends HUDElement {
	
	private double xPos;
	private double yPos;
	private double width;
	private double height;
	private BufferedImage sprite;
	
	public static  boolean isOnPlay = true;
	public static  boolean isOnOptions;
	public static  boolean isOnExit;
	public static  boolean optionsClicked;
	public static  boolean isOnRes;
	public static  boolean isOnSound;
	public static  boolean isOnOptionsBack;
	public static  boolean isOnPlayBack;
	public static  boolean isOnNew;
	public static  boolean isOnLoad;
	public static  boolean inGameMenuOpened;
	public static  boolean isOnResume;
	public static  boolean isOnMapSelect;
	public static  boolean isOnInGameOptions;
	public static  boolean isOnInGameExit;
	private boolean invisible;
	

	public MainMenu(double xPos, double yPos, double width, double height, BufferedImage sprite) {
		super(xPos, yPos, width, height, sprite);
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.sprite = sprite;

	}
	public void setSprite(BufferedImage sprite){
		this.sprite = sprite;
	}
	
	public void setXPos(double xPos) {
		this.xPos = xPos;
	}

	public void setYPos(double yPos) {
		this.yPos = yPos;
	}
	
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public void show() {
		invisible = false;
	}
	
	public void hide() {
		invisible = true;
	}

	public void tick(int deltaTime) {
		if (!invisible){
			
		if (isOnResume){
			setYPos(0.5);
			setXPos(5.2);
			setHeight(8);
			setWidth(6);
			setSprite(Resources.getImage("InGame menu 2"));
			
			
		}
		if (Input.isKeyPressed(KeyEvent.VK_S)) {
			if (isOnPlay) {
				//this.sprite = Resources.getImage("Options");
				setSprite(Resources.getImage("Options"));
				isOnPlay = false;
				isOnOptions = true;
			} else if (isOnOptions) {
				setSprite(Resources.getImage("Exit"));
				isOnOptions = false;
				isOnExit = true;
			}
			else if (isOnResume){
				setSprite(Resources.getImage("InGame menu 3"));
				isOnResume = false;
				isOnMapSelect = true;
			}
			else if (isOnMapSelect){
				setSprite(Resources.getImage("InGame menu 4"));
				isOnMapSelect = false;
				isOnInGameOptions = true;
			}
			else if (isOnInGameOptions){
				setSprite(Resources.getImage("InGame menu 5"));
				isOnInGameExit = true;
				isOnInGameOptions = false;
			}
			Input.clearInput();
		} 
		
		
		else if (Input.isKeyPressed(KeyEvent.VK_W)) {
			if (isOnOptions) {
				setSprite(Resources.getImage("Play"));
				isOnOptions = false;
				isOnPlay = true;
			} else if (isOnExit) {
				setSprite(Resources.getImage("Options"));
				isOnExit = false;
				isOnOptions = true;
			}
			else if (isOnMapSelect){
				setSprite(Resources.getImage("InGame menu 2"));
				isOnMapSelect = false;
				isOnResume = true;
			}
			else if (isOnInGameOptions){
				setSprite(Resources.getImage("InGame menu 3"));
				isOnMapSelect = true;
				isOnInGameOptions = false;
			}
			else if (isOnInGameExit){
				setSprite(Resources.getImage("InGame menu 4"));
				isOnInGameOptions = true;
				isOnInGameExit = false;
			}
			
			Input.clearInput();
		}
		
		else if (Input.isKeyPressed(KeyEvent.VK_ENTER)){
			if (isOnOptions ){
				setXPos(0);
				setYPos(0);
				setWidth(16);
				setHeight(10);
				setSprite(Resources.getImage("option button 1"));
				isOnOptionsBack = true;
				isOnOptions = false;
			}
			else if (isOnMapSelect){
				Game.getViewPort().transitionLevel(Game.getMapSelect());
				Game.getViewPort().unpause();
				this.hide();
				isOnMapSelect = false;
			}
			else if (isOnInGameOptions){
				setXPos(0);
				setYPos(0);
				setWidth(16);
				setHeight(10);
				setSprite(Resources.getImage("option button 1"));
				isOnOptionsBack = true;
				isOnInGameOptions = false;
			}
			else if (isOnOptionsBack){
				if (!inGameMenuOpened){
				setXPos(4);
				setYPos(1);
				setWidth(8);
				setHeight(8);
				setSprite(Resources.getImage("Options"));
				isOnOptionsBack = false;
				isOnOptions = true;
				}
				else{
					setYPos(0.5);
					setXPos(5.2);
					setHeight(8);
					setWidth(6);
					setSprite(Resources.getImage("InGame menu 4"));
					isOnInGameOptions = true;
					isOnOptionsBack = false;
				}
			}
			else if (isOnPlayBack){
				setXPos(4);
				setYPos(1);
				setWidth(8);
				setHeight(8);
				setSprite(Resources.getImage("Play"));
				isOnPlayBack = false;
				isOnPlay = true;
			}
			else if (isOnNew){
				//Game.getViewPort().transitionLevel(Resources.getLevel("test"));
				Game.getViewPort().transitionLevel(Game.getMapSelect());
				
			}
			else if (isOnLoad){
				Resources.loadProgress(Resources.getFile("SavedData"));
			}
			else if (isOnResume){
				//Resources.loadProgress(Resources.getFile("SavedData"));
				Game.getViewPort().unpause();
				inGameMenuOpened = false;
			}
			
			else if (isOnInGameExit){
				//Resources.saveProgress(Game.getViewPort().getLevel().getId(),1,1,Resources.getFile("SavedData"));
				System.exit(1);
			}
			
			else if (isOnExit){
				System.exit(0);
				
				
			}
			else if (isOnPlay){
				setXPos(0);
				setYPos(0);
				setWidth(16);
				setHeight(10);
				setSprite(Resources.getImage("play button 1"));
				isOnPlayBack = true;
			    isOnPlay = false;
			}
			
			Input.clearInput();
		}
		
		else if (Input.isKeyPressed(KeyEvent.VK_D)){
			if (isOnOptionsBack){
				setSprite(Resources.getImage("option button 2"));
				isOnRes = true;
				isOnOptionsBack = false;
			}
			else if (isOnRes){
				setSprite(Resources.getImage("option button 3"));
				isOnRes = false;
				isOnSound = true;
			}
			else if (isOnPlayBack){
				setSprite(Resources.getImage("play button 2"));
				isOnPlayBack = false;
				isOnNew = true;
			}
			else if (isOnNew){
				setSprite(Resources.getImage("play button 3"));
				isOnNew = false;
				isOnLoad = true;
			}
			
			Input.clearInput();
		}
		
		else if (Input.isKeyPressed(KeyEvent.VK_A)){
			if (isOnSound){
				setSprite(Resources.getImage("option button 2"));
				isOnRes = true;
				isOnSound = false;
			}
			else if (isOnRes){
				setSprite(Resources.getImage("option button 1"));
				isOnOptionsBack = true;
				isOnRes = false;
			}
			else if (isOnLoad){
				setSprite(Resources.getImage("play button 2"));
				isOnLoad = false;
				isOnNew = true;
			}
			else if (isOnNew){
				setSprite(Resources.getImage("play button 1"));
				isOnNew = false;
				isOnPlayBack = true;
			}
			Input.clearInput();
			
			
		}
		}
		//super.tick(deltaTime);
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		if (!invisible){
		//System.out.println("hi");
		int x = (int)((xPos  * Game.getUnitX()));
		int y = (int)((yPos  * Game.getUnitY()));
		
		
		
		g.drawImage(this.sprite, x, y, (int)(width * Game.getUnitX()), (int)(height * Game.getUnitY()), null);
		
		
		}
	}


}
