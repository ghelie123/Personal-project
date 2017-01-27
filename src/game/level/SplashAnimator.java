package game.level;

import game.Game;
import game.Resources;
import game.Vector2D;
import game.enemy.Enemy;
import game.enemy.Golum;
import game.enemy.Octopus;
import game.enemy.Penguin;
import game.enemy.Squid;
import game.object.PhysicsObject;

public class SplashAnimator {

	private int timePassed;
	
	private PhysicsObject[] actors;
	
	private boolean playerJumped;
	private boolean jetPacked;
	private boolean switchingLevel;
	
	public SplashAnimator(Level level) {
		Game.getPlayer().setAnimation(Game.getPlayer().getWalkAnimation());
		actors = new PhysicsObject[]{Game.getPlayer(), 
				new Penguin(2, 1, -5, 7.2, 1, 1.8, 23, 1, 1, 1, Resources.getImage("penguin_0")),
				new Golum(-5, 8),
				new Octopus(2,1,-5,7,1,1.8,23,1,1,1,Resources.getImage("octopus_0")),
				new Squid(2,1,-5,7,1,1.8,23,1,1,1,Resources.getImage("squid_0"))};
		
		for (int i = 1; i < actors.length; i++) {
			level.addObject(actors[i]);
			actors[i].unfreeze();
		}
	} 
	
	public void tick(int deltaTime) {
		timePassed += deltaTime;
		
		
		// Player animations
		PhysicsObject player = actors[0];
		if (!playerJumped) {
			player.applyForce(new Vector2D(1000,0));
		}
		if (timePassed >= 1500 && !playerJumped) {
			player.applyForce(new Vector2D(0, -30000));
			player.setGrounded(false);
			player.setAnimation(null);
			player.setSprite(Resources.getImage("player_3"));
			playerJumped = true;
		}
		if (timePassed >= 2200) {
			jetPacked = true;
			player.setAnimation(Game.getPlayer().getJetAnimation());
		}
		if (jetPacked) {
			player.applyForce(new Vector2D(0, -900));
		}
		
		// Enemy animations
		Enemy penguin = (Enemy) actors[1];
		penguin.applyForce(new Vector2D(190,0));
		
		Enemy golum = (Enemy) actors[2];
		golum.applyForce(new Vector2D(500,0));
		
		Enemy octo = (Enemy) actors[3];
		octo.applyForce(new Vector2D(-100,0));
		
		Enemy squid = (Enemy) actors[4];
		squid.applyForce(new Vector2D(-120,0));
		
		if (timePassed >= 4000 && !switchingLevel) {
			Game.getViewPort().transitionLevel(new Level(0,16,9));
			switchingLevel = true;
		}
	}
	
}
