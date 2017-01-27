package game.view;

import game.Game;
import game.Resources;

import java.awt.Color;
import java.awt.Graphics2D;

public class Score extends HUDElement {
	
	private boolean invisible;
	
	public Score(double xPos, double yPos) {
		super(xPos, yPos, 0, 0, null);
		
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		if (!invisible){
		g.setColor(Color.black);
		g.setFont(Resources.getFont("pixelmix").deriveFont(24f));
		g.drawString("SCORE: " + Game.getPlayer().getScore(),(int)(getXPos() * Game.getUnitX()) , (int)(getYPos() * Game.getUnitY()));
		//g.dispose();
		}
	}
	

	@Override
	public void tick(int deltaTime) {
		// TODO Auto-generated method stub
		
	}
	
	public void show() {
		invisible = false;
	}
	
	public void hide() {
		invisible = true;
	}

}
