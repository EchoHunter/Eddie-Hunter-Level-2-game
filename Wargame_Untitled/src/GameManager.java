import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameManager extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener {
	public static BufferedImage image;
	public static BufferedImage DamagedStone;
	public static boolean needImage = true;
	public static boolean gotImage = false;
	Timer frameDraw;
	int currentImage = 0;
	Random rand = new Random();
	Font titleFont = new Font("Arial", Font.PLAIN, 48);
	Font lowerFont = new Font("Arial", Font.PLAIN, 25);
	hero h = new hero(50, 50, 50, 50, 50, 50);
	ArrayList<terrain> t = new ArrayList<terrain>();
	ArrayList<grunt> g = new ArrayList<grunt>();
	GameManager() {
		frameDraw = new Timer(1000 / 60, this);
		frameDraw.start();
	}

	void drawOpeningState(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(0, 0, GameRunner.w, GameRunner.h);
		g.setFont(titleFont);
		g.setColor(Color.YELLOW);
		g.drawString("Working Title", 55, 70);

		g.setFont(lowerFont);
		g.setColor(Color.YELLOW);
		g.drawString("Press ENTER to start the game", 55, 400);
		g.setColor(Color.YELLOW);
		g.setFont(lowerFont);
		g.setColor(Color.YELLOW);
		g.drawString("Press Space to see instructions", 55, 650);
	}

	void drawGameState(Graphics g) {
		loadImage("BackgroundButGood.jpg");
		if (gotImage) {
			Generate(g);

		} else {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, GameRunner.w, GameRunner.h);
		}
		h.draw(g);
		drawTerrain(g);
	}

	void drawTerrain(Graphics g) {
		for (terrain f : t) {
			f.draw(g);
		}
	}

	void drawEndState(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GameRunner.w, GameRunner.h);
		g.setFont(titleFont);
		g.setColor(Color.RED);
		g.drawString("FAILURE", 55, 70);

		g.setFont(lowerFont);
		g.setColor(Color.RED);
		g.drawString("Press ENTER to return to the menu", 55, 400);

	}

	void updateOpeningState() {

	}

	void updateGameState() {
		if(checkWall()) {
		h.x = h.originx;
		h.y = h.originy;
		h.clicked = false;
		}
		h.update();
		
	}

	void updateEndState() {

	}

	final int MENU = 1;
	final int GAME = 2;
	final int END = 3;

	int enterTimes = 0;
	int currentState = MENU;
	int spacePressed = 0;
	boolean attackMode = false;
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {

			if (currentState == END) {
				currentState = MENU;
			}

			else if (currentState == MENU) {
				if (enterTimes == 0) {
					JOptionPane.showMessageDialog(null, "Are you sure? If you fail you must start again");
					enterTimes++;
				}
				if (enterTimes == 1) {
					currentState++;
					startGame();
					enterTimes = 0;
				}
			}

			else if (currentState == GAME) {
				currentState++;
				endGame();
			}
		}

		if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
			if (currentState == MENU) {
				JOptionPane.showMessageDialog(null, "click and drag on your character to move, press space to enter attack mode and press space to leave it. When in attack mode, click in the direction you want to throw a knife and a knife will move until it hits something(or someone)");
			}
			if (currentState == GAME) {
				spacePressed++;
				if(spacePressed == 1) {
					attackMode = true;
				}
				if(spacePressed == 2) {
					spacePressed = 0;
					attackMode = false;
				}
			System.out.println(attackMode);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	void startGame() {
		for (int i = 0; i < 35; i++) {
			// t.add(new terrain( rand.nextInt(1400)+100, rand.nextInt(900)+100,
			// rand.nextInt(150)+20, rand.nextInt(50)+10, 10, 10));
		}

		while (t.size() < 35) {
			int terrainX = rand.nextInt(1400) + 100;
			int terrainY = rand.nextInt(900) + 100;
			int terrainWidth = rand.nextInt(200) + 20;
			int terrainHeight = rand.nextInt(100) + 10;
			Rectangle terrainBox = new Rectangle(terrainX, terrainY, terrainWidth, terrainHeight);
			boolean intersecting = false;
			for (terrain z : t) {
				if (terrainBox.intersects(z.collisionBox)) {
					intersecting = true;

				} else {
					intersecting = false;
				}

			}
			if (intersecting == false) {
				t.add(new terrain(terrainX, terrainY, terrainWidth, terrainHeight, 10, 10));
			}
		}
	}

	void endGame() {

	}

	boolean checkWall() {
		for (terrain z : t) {
			if (h.collisionBox.intersects(z.collisionBox)) {
				return true;
				
			}
			
		}
		return false;
		
	}

	public void paintComponent(Graphics g) {

		if (currentState == MENU) {
			drawOpeningState(g);
		} else if (currentState == GAME) {
			drawGameState(g);

		} else if (currentState == END) {
			drawEndState(g);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (currentState == MENU) {
			updateOpeningState();
		}

		if (currentState == GAME) {
			updateGameState();
		}
		if (currentState == END) {
			updateEndState();
		}
		repaint();

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mouseX = arg0.getX() - this.getLocationOnScreen().x;
		mouseY = arg0.getY() - this.getLocationOnScreen().y;
		checkButton();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		h.clicked = false;
		h.originx = h.x;
		h.originy = h.y;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mouseX = arg0.getX() - this.getLocationOnScreen().x;
		mouseY = arg0.getY() - this.getLocationOnScreen().y;
		if (h.clicked) {
			h.x = mouseX - h.width / 2;
			h.y = mouseY - h.height / 2;
		}

	}

	int mouseX = 0;
	int mouseY = 0;

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseX = e.getX() - this.getLocationOnScreen().x;
		mouseY = e.getY() - this.getLocationOnScreen().y;
	}

	void loadImage(String imageFile) {
		if (needImage) {
			try {
				image = ImageIO.read(this.getClass().getResourceAsStream(imageFile));
				DamagedStone = ImageIO.read(this.getClass().getResourceAsStream("damagedStones.jpg"));
				gotImage = true;
			} catch (Exception e) {

			}
			needImage = false;
		}
	}

	void checkButton() {
		System.out.println(mouseX + " " + mouseY + " " + h.x + " " + h.y);
		if ((mouseX > h.x) && (mouseX < h.x + h.width)) {
			if ((mouseY > h.y) && (mouseY < h.y + h.height)) {
				System.out.println("clicked");
				h.x = mouseX - h.width / 2;
				h.y = mouseY - h.height / 2;
				repaint();
				h.clicked = true;
			}
		}
		System.out.println("button checked");
	}

	void Generate(Graphics g) {

		g.drawImage(image, 0, 0, 1500, 1000, null);
		h.draw(g);
	}
}
