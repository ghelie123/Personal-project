package game;

import game.level.Level;
import game.object.GameObject;
import game.object.Liquid;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public final class Resources {
	
	private static final String RESOURCE_PATH = "res/";
	private static final BufferedImage DEFAULT_IMAGE = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
	private static final Font DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

	private static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	private static HashMap<String, Clip> sounds = new HashMap<String, Clip>();
	private static HashMap<String, Font> fonts = new HashMap<String, Font>();
	private static HashMap<String, String[]> sheets = new HashMap<String, String[]>();
	private static HashMap<String, File> textFiles = new HashMap<String , File>();
	private static HashMap<String, Level> levels = new HashMap<String, Level>();
		
	static {
		DEFAULT_IMAGE.setRGB(0, 0, 0x606060);
		DEFAULT_IMAGE.setRGB(1, 1, 0x606060);
		DEFAULT_IMAGE.setRGB(0, 1, 0xFF6A00);
		DEFAULT_IMAGE.setRGB(1, 0, 0xFF6A00);
	}
	
	private Resources() {}
	
	public static void loadImage(String fileName) {
		
		// Check if already loaded
		if (images.get(fileName) != null) {
			return;
		}
		
		try {
			URL path = Resources.class.getResource(RESOURCE_PATH + "image/" + fileName + ".png");
			if (path == null) {
				return;
			}
			System.out.println("Loading image " + fileName + "...");
			BufferedImage image = ImageIO.read(path);
			images.put(fileName, image);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadSound(String fileName) {
		
		// Check if already loaded
		if (sounds.get(fileName) != null) {
			return;
		}
		
		try {
			System.out.println("Loading sound " + fileName + "...");
			Clip sound = AudioSystem.getClip();
			sound.open(AudioSystem.getAudioInputStream(Resources.class.getResource(RESOURCE_PATH + "sound/" + fileName + ".wav")));
			sounds.put(fileName, sound);
		} 
		catch (LineUnavailableException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadFont(String fileName) {
		
		// Check if already loaded
		if (fonts.get(fileName) != null) {
			return;
		}
		
		try {
			System.out.println("Loading font " + fileName + "...");
			Font font = Font.createFont(Font.TRUETYPE_FONT, Resources.class.getResourceAsStream(RESOURCE_PATH + "font/" + fileName + ".ttf"));
			fonts.put(fileName, font);
		} 
		catch (FontFormatException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadSpriteSheet(String fileName, String name, int width, int height, int[] ignoreIndex) {
		System.out.println("Loading sprite sheet " + fileName + "...");
		int index = 0;
		int count = 0;
		
		ArrayList<String> names = new ArrayList<String>();
		BufferedImage sheet = null;
		
		try {
			sheet = ImageIO.read(Resources.class.getResource(RESOURCE_PATH + "image/" + fileName + ".png"));
		} 
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		int rows = sheet.getHeight() / height;
		int columns = sheet.getWidth() / width;
		
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				
				// Check ignore
				boolean ignore = false;
				for (int i = 0; i < ignoreIndex.length; i++) {
					if (ignoreIndex[i] == index) {
						ignore = true;
						break;
					}
				}
				
				if (!ignore) {
					images.put(name + "_" + count, sheet.getSubimage(column * width, row * height, width, height));
					names.add(name + "_" + count);
					count++;
				}
				index++;
			}
		}
		
		sheets.put(fileName, names.toArray(new String[0]));
	}
	
	public static void loadTextFile(String fileName){
		
		
		URL path = Resources.class.getResource(RESOURCE_PATH + "File/" + fileName + ".dat");
		
		File file = null;
		try {
			file = new File(path.toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		textFiles.put(fileName, file);
	
	}
	
	public static void loadLevel(String fileName, int id) {
		// Check if already loaded
		if (levels.get(fileName) != null) {
			return;
		}
		
		Level level = null;
		try {
			System.out.println("Loading level " + fileName + "...");
			DataInputStream input = new DataInputStream(Resources.class.getResourceAsStream(RESOURCE_PATH + "level/" + fileName + ".level"));
			input.readChar();
			input.readChar();
			input.readChar();
			input.readChar();
			input.readChar();
			int width = input.readInt();
			int height = input.readInt();
			int numObjects = input.readInt();
			level = new Level(id,width,height);
			
			for (int i = 0; i < numObjects; i++) {
				int x = input.readInt();
				int y = input.readInt();
				int w = input.readInt();
				int h = input.readInt();
				String string = "";
				int sw = input.readInt();
				for (int j = 0; j < sw; j++) {
					string += input.readChar();
				}
				GameObject obj = new GameObject(x, y, w, h, Resources.getImage(string));
				if (string.equals("snow_1") || string.equals("snow_4") || string.equals("snow_9")) {
					obj.setName("ICE");
				}
				else if (string.equals("snow_6") || string.equals("snow_7") || string.equals("snow_11") || string.equals("water_5")) {
					obj.setName("SNOW_CEILING");
				}
				
				if (string.equals("water_4") || string.equals("water_10") || string.equals("snow_2") || string.equals("lava_16")) {
					level.addRenderOnly(obj);
				}
				else if (string.equals("lava_8")) {
					level.addObject(new Liquid(x, y, Liquid.LAVA));
				}
				else if (string.equals("cave_0")) {
					level.addObject(new Liquid(x, y, Liquid.WATER));
				}
				else {
					level.addObject(obj);
				}
				
			}
			level.setPlayerStartX(input.readInt());
			level.setPlayerStartY(input.readInt());
			input.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		level.saveInitialObjects();
		levels.put(fileName, level);
	}
	
	public static void saveProgress(int data1 , int data2 , int data3 , File fileName){
		try{
		//textFiles.get
		
		DataOutputStream output = new DataOutputStream(new FileOutputStream(fileName,false));
		
		output.writeInt(data1);
		output.writeInt(data2);
		output.writeInt(data3);
		
		

		output.close();
		
		
		
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static int[] loadProgress(File file){
		int[] data = new int[3];
		try{
			DataInputStream input = new DataInputStream(new FileInputStream(file));

			
			data[0] = input.readInt();
			data[1] = input.readInt();
			data[2] = input.readInt();
			 //Game.getViewPort().changeLevel(data[0]);
			 Game.getPlayer().setXPos(data[1]);
			 Game.getPlayer().setYPos(data[2]);
			 System.out.println(data[0]);
		 
			
		
		 
			input.close();
			
			
			
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return data;
	}
	
	public static void unloadImage(String fileName) {
		images.remove(fileName);
	}
	
	public static void unloadSound(String fileName) {
		sounds.remove(fileName);
	}
	
	public static void unloadFont(String fileName) {
		fonts.remove(fileName);
	}
	
	public static void unloadSpriteSheet(String fileName) {
		String[] names = sheets.remove(fileName);
		
		if (names == null) {
			return;
		}
		
		for (int i = 0; i < names.length; i++) {
			images.remove(names[i]);
		}
	}
	
	public static void unloadAll() {
		images.clear();
		sounds.clear();
		fonts.clear();
		sheets.clear();
	}
	
	public static BufferedImage getImage(String fileName) {
		BufferedImage image = images.get(fileName);
		
		return (image == null) ? DEFAULT_IMAGE:image;
	}
	
	public static Clip getSound(String fileName) {
		Clip sound = sounds.get(fileName);
		
		// TODO find better way of doing this.
		if (sound == null) {
			try {
				sound = AudioSystem.getClip();
			} 
			catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
		
		return sound;
	}
	
	public static File getFile(String fileName){
		File file = textFiles.get(fileName);
		
		return file;
	}
	
	
	public static Font getFont(String fileName) {
		Font font = fonts.get(fileName);
		
		return (font == null) ? DEFAULT_FONT:font;
	}
	
	public static Level getLevel(String fileName) {
		return levels.get(fileName);
	}

}
