package game.level;

import game.Game;

import java.awt.Graphics2D;

public class Background {
	
	private BackgroundLayer[] layers;
	
	public Background(BackgroundLayer[] layers) {
		this.layers = layers;
	}
	
	public void tick(int deltaTime) {
		for (int i = 0; i < layers.length; i++) {
			if (layers[i] instanceof LoopingBackgroundLayer) {
				LoopingBackgroundLayer layer = (LoopingBackgroundLayer) layers[i];
				layer.tick(deltaTime);
			}
		}
	}
	
	public void render(Graphics2D g) {	
		for (int i = 0; i < layers.length; i++) {
			BackgroundLayer layer = layers[i];
			g.drawImage(layer.getImage(), (int)(layer.getXPos() * Game.getUnitX()), (int)(layer.getYPos() * Game.getUnitY()), (int)(layer.getWidth() * Game.getUnitX()), (int)(layer.getHeight() * Game.getUnitY()), null);
		}

	}
	
	public void recompute() {
		for (int i = 0; i < layers.length; i++) {
			if (layers[i] instanceof LoopingBackgroundLayer) {
				LoopingBackgroundLayer layer = (LoopingBackgroundLayer) layers[i];
				layer.recompute();
			}
		}
	}

}
