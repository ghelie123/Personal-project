package game;

import javax.sound.sampled.Clip;

public final class JukeBox {
	
	private static Clip backgroundMusic;

	public static void playSoundEffect(String fileName) {
		Clip clip = Resources.getSound(fileName);
		clip.setFramePosition(0);
		clip.start();
	}
	
	public static void startLoopingSoundEffect(String fileName) {
		Clip clip = Resources.getSound(fileName);
		if (!clip.isActive()) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	public static void stopLoopingSoundEffect(String fileName) {
		Clip clip = Resources.getSound(fileName);
		clip.stop();
		clip.setFramePosition(0);
	}
	
	public static void setBackgroundMusic(String fileName) {
		if (fileName == null) {
			if (backgroundMusic == null) {
				return;
			}
			backgroundMusic.stop();
			backgroundMusic = null;
			return;
		}
		Clip clip = Resources.getSound(fileName);
		if (backgroundMusic == null) {
			backgroundMusic = clip;
			backgroundMusic.setFramePosition(0);
			backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
		}
		else if (!backgroundMusic.equals(clip)) {
			backgroundMusic.stop();
			backgroundMusic = clip;
			backgroundMusic.setFramePosition(0);
			backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
		}

	}
}
