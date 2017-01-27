package test;

import game.Vector2D;

public class VectorTest {

	public static void main(String[] args) {
		// Add case
		Vector2D v = new Vector2D(4,7);
		Vector2D u = new Vector2D(-3,16);
		boolean b = false;
		v.add(u);
		if (v.getXComponent() == 1 && v.getYComponent() == 23 && v.getMagnitude() == Math.sqrt(530) && v.getDirection() == Math.atan(23)) {
			b = true;
		}
		System.out.println("Addition:\n1, 23, 23.02172887, 1.527345431\t:\t" + v.getXComponent() + ", " + v.getYComponent() + ", " + v.getMagnitude() + ", " + v.getDirection() + "\t" + ((b) ? "PASSED":"FAILED"));
		
		v = new Vector2D(0,0);
		System.out.println(v);
		v.setXComponent(5);
		System.out.println(v);
		v.setXComponent(-5);
		System.out.println(v);
		v = new Vector2D(0,5);
		System.out.println(v);
		v.setYComponent(-5);
		System.out.println(v);
		System.out.println();
		v = new Vector2D(5,-7);
		System.out.println(v);
		v.reverse();
		System.out.println(v);
		
		System.out.println();
		v = new Vector2D(4,9);
		System.out.println(v);
		v.setMagnitude(1);
		System.out.println(v);
		
	}

}
