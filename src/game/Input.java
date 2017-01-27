package game;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

public final class Input implements KeyListener, MouseListener, FocusListener, ComponentListener{

	private static HashMap<Integer, Boolean> keys = new HashMap<Integer, Boolean>();
	private static ArrayList<Integer> waitingForRelease = new ArrayList<Integer>();
	
	private static boolean leftClickPressed;
	private static boolean listening = true;
	
	@Override
	public void componentHidden(ComponentEvent arg0) {}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		if (listening) {
			clearInput();
		}
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		if (listening) {
			clearInput();
		}
	}

	@Override
	public void componentShown(ComponentEvent arg0) {}

	@Override
	public void focusGained(FocusEvent arg0) {}

	@Override
	public void focusLost(FocusEvent arg0) {
		if (listening) {
			clearInput();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		if (listening) {
			leftClickPressed = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (listening) {
			leftClickPressed = false;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (listening && keys.containsKey(e.getKeyCode())) {
			keys.put(e.getKeyCode(), true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (listening && keys.containsKey(e.getKeyCode())) {
			keys.put(e.getKeyCode(), false);
			waitingForRelease.remove((Object)e.getKeyCode());
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	public static void registerKey(int keyCode) {
		keys.put(keyCode, false);
	}
	
	public static boolean isKeyPressed(int keyCode) {
		Boolean pressed = keys.get(keyCode);
		if (pressed == null || waitingForRelease.contains(keyCode)) {
			return false;
		}
		else {
			return pressed;
		}
	}
	
	public static void requireRelease(int keyCode) {
		Boolean pressed = keys.get(keyCode);
		if (pressed != null) {
			if (!pressed.booleanValue()) {
				return;
			}
			if (!waitingForRelease.contains(keyCode)) {
				waitingForRelease.add(keyCode);
			}
		}
	}
	
	public static boolean isLeftPressed() {
		return leftClickPressed;
	}
	
	public static void setListening(boolean listening) {
		Input.listening = listening;
		clearInput();
		leftClickPressed = false;
	}
	
	public static void clearInput() {
		for (Integer ints : keys.keySet()) {
			keys.put(ints, false);
		}
	}
}
