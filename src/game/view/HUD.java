package game.view;

import game.Game;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class HUD {

	private ArrayList<HUDElement> elements = new ArrayList<HUDElement>();
	private boolean invisible;

	public HUD() {

	}

	public void render(Graphics2D g) {
		// Renders elements in the HUD
		if (!invisible) {
			for (int i = 0; i < elements.size(); i++) {
				elements.get(i).render(g);
			}
		}
	}

	public void tick(int deltaTime) {
		// Updates elements in the HUD

		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).tick(deltaTime);
		}
	}

	public void addHUDElement(HUDElement element) {

		elements.add(element);
	}
	
	public ArrayList<HUDElement> getHUDElements(){
		
		return elements;
		
	}
	
	public void show() {
		invisible = false;
		Game.getViewPort().getHUD().getHUDElements().get(0).show();
		Game.getViewPort().getHUD().getHUDElements().get(1).show();
		Game.getViewPort().getHUD().getHUDElements().get(3).show();
	}
	
	public void hide() {
		invisible = true;
		Game.getViewPort().getHUD().getHUDElements().get(0).hide();
		Game.getViewPort().getHUD().getHUDElements().get(1).hide();
		Game.getViewPort().getHUD().getHUDElements().get(3).hide();
	}

}
