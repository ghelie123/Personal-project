package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Fix {
	
	public static final int[] COLUMNS = new int[]{14,26,33,32,42,43,44,54,55,68,69,80,81,88,89,96,97,103,104,108,109,113,114,121,122,129,130,135,136,141,142,147,148,155,150, 25,27,28,29,30,31,34,35,36,37,38,39,40,41,63,64,65,66,67,70,71};
	public static final String STRING = "water_4";
	public static final String PATH = "water.level";
	
	public static void main(String[] args) {
		
		File file = new File(PATH);
		if (!file.exists()) {
			System.out.println("file doesnt exist");
			System.exit(0);
		}
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		try {
			DataInputStream input = new DataInputStream(new FileInputStream(file));
			input.readChar();
			input.readChar();
			input.readChar();
			input.readChar();
			input.readChar();
			int width = input.readInt();
			int height = input.readInt();
			int numObjects = input.readInt();
			
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
				tiles.add(new Tile(x,y,w,h,string));
			}
			int startX = input.readInt();
			int startY = input.readInt();
			input.close();
			
			
			//
			ArrayList<Integer> removed = new ArrayList<Integer>();
			for (int i = 0; i < tiles.size(); i++) {
				Tile tile = tiles.get(i);
				if (tile.getImagePath().equals(STRING)) {
					boolean remove = false;
					for (int j = 0; j < COLUMNS.length; j++) {
						if (COLUMNS[j] == tile.getXPos()) {
							remove = true;
							break;
						}
					}
					
					if (remove) {
						removed.add(i);
					}
				}
			}
			
			for (int i = removed.size() - 1; i > 0; i--) {
				tiles.remove(((int)removed.get(i)));
			}
			
			// output
			try {
				DataOutputStream output = new DataOutputStream(new FileOutputStream(file));
				output.writeChars("LEVEL");
				output.writeInt(width);
				output.writeInt(height);
				
				output.writeInt(tiles.size());
				
				for (int i = 0; i < tiles.size(); i++) {
					Tile tile = tiles.get(i);
					output.writeInt(tile.getXPos());
					output.writeInt(tile.getYPos());
					output.writeInt(tile.getWidth());
					output.writeInt(tile.getHeight());
					output.writeInt(tile.getImagePath().length());
					output.writeChars(tile.getImagePath());
				}
				output.writeInt(startX);
				output.writeInt(startY);
				
				output.close();
				System.out.println("done");
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
