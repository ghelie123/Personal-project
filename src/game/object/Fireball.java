package game.object;

import game.Game;
import game.JukeBox;
import game.Resources;
import game.Vector2D;

import java.util.Random;

public class Fireball extends PhysicsObject {

	private boolean launched;
	private boolean delaying;
	
	private double originalYPos;
	private int delay;
	private int timePassed;
	
	private Random rand;
	
	public Fireball(double xPos, double yPos) {
		super(xPos, yPos, 0.75, 1, 20, 1, 1, 1, Resources.getImage("fireball_0"));
		originalYPos = yPos;
		delay = 750;
		rand = new Random();
	}

	
	@Override
	public void tick(int deltaTime) {
		if (delaying) {
			timePassed += deltaTime;
			if (timePassed >= delay) {
				timePassed = 0;
				delaying = false;
				launched = false;
			}
			else {
				return;
			}
		}
		if (!launched) {
			int dy = rand.nextInt(2000) - 1000;
			applyForce(new Vector2D(0,-13000 + dy));
			if (Game.getViewPort().getCollider().inside(getCollider())) {
				JukeBox.playSoundEffect("fireball");
			}
			launched = true;
		}
		
		super.tick(deltaTime);
		
		if (getYPos() >= originalYPos) {
			stop();
			setYPos(originalYPos);
			delaying = true;
		}
		
		if (getVelocity().getYComponent() > 0) {
			setSprite(Resources.getImage("fireball_1"));
		}
		else {
			setSprite(Resources.getImage("fireball_0"));
		}
	}

}
