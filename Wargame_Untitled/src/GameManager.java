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
import java.util.Iterator;
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
	ArrayList<projectile> p = new ArrayList<projectile>();

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
		drawProjectiles(g);
	}

	void drawTerrain(Graphics g) {
		for (terrain f : t) {
			f.draw(g);
		}
	}

	void drawProjectiles(Graphics g) {

		for (projectile h : p) {
			if (h.isActive) {
				h.draw(g);
			}
		}
	}

	void removeProjectiles() {
		Iterator<projectile> m = p.iterator();
		while (m.hasNext()) {
			projectile c = m.next();
			if (!c.isActive) {
				m.remove();
				
			}
		}
	}

	void checkProjectileIntersect() {
		for (projectile m : p) {
			for (terrain d : t) {
				if (m.collisionBox.intersects(d.collisionBox)) {
					m.isActive = false;
					System.out.println("checked");
				}
			}
		}
	}

	void updateProjectiles() {

		for (projectile m : p) {
			m.update();
		}
	}

	void addProjectile(int newX, int newY) {
		projectile pro = new projectile(h.x, h.y, 35, 35, h.x, h.y);
		pro.setNewXNewY(newX, newY);
		p.add(pro);
		System.out.println(p.size());
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

	int enemyTurnTime = 0;

	void updateGameState() {
		h.update();
		updateProjectiles();
		checkProjectileMove();
		checkProjectileIntersect();
		removeProjectiles();
		
		if ((turn == enemyTurn) && (enemyTurnTime < 180)) {
			enemyTurnTime++;
		} else if ((turn == enemyTurn) && (enemyTurnTime >= 180)) {
			turn = playerTurn;
		}
	}

	void updateEndState() {

	}

	void checkProjectileMove() {
		for (projectile g : p) {
			if ((g.x == g.newX) && (g.y == g.newY)) {
				p.remove(g);
			}
		}
	}

	final int MENU = 1;
	final int GAME = 2;
	final int END = 3;

	final int playerTurn = 0;
	final int enemyTurn = 1;
	int turn = playerTurn;
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
				JOptionPane.showMessageDialog(null,
						"click and drag on your character to move, press space to enter attack mode and press space to leave it (when you leave attack mode, your turn ends). When in attack mode, click in the direction you want to throw a knife and a knife will move until it hits something(or someone)");
			}
			if (currentState == GAME) {
				spacePressed++;
				if (spacePressed == 1) {
					attackMode = true;
				}
				if (spacePressed == 2) {
					spacePressed = 0;
					attackMode = false;
					turn = enemyTurn;
					removeProjectiles();
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
		h.update();
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
		mouseX = arg0.getX() - this.getLocationOnScreen().x;
		mouseY = arg0.getY() - this.getLocationOnScreen().y;
		checkButton();
		if (turn == playerTurn) {
			if (attackMode) {

				addProjectile(mouseX, mouseY);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		/*
		 * DMC Update hero's position
		 */
		if (h.clicked) {
			h.x = h.futureX;
			h.y = h.futureY;

		}

		/*
		 * DMC Move the hero back to the original position if dropped on a terrain
		 * object
		 */
		if (checkWall()) {
			h.x = h.originx;
			h.y = h.originy;
			h.clicked = false;
		} else if (h.clicked) {
			attackMode = true;
		}

		/*
		 * DMC Must be done after checking for terrain wall collisions
		 */
		h.originx = h.x;
		h.originy = h.y;

		/*
		 * DMC Put this as the last line in the method
		 */
		h.reset();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		mouseX = arg0.getX() - this.getLocationOnScreen().x;
		mouseY = arg0.getY() - this.getLocationOnScreen().y;

		/*
		 * DMC Move to future location
		 */
		if (turn == playerTurn) {
			if (attackMode == false) {
				if (h.clicked && h.haveMovementCircle()) {
					if (h.isMouseInsideMovementArea(mouseX, mouseY)) {
						h.futureX = mouseX - (h.width / 2);
						h.futureY = mouseY - (h.height / 2);
					} else {
						/*
						 * Clamp cast
						 */
						// double[] position = h.getFutureMovementPosition(mouseX, mouseY);
						// h.futureX = (int)position[0];
						// h.futureY = (int)position[1];

						/*
						 * Return back to starting position
						 */
						h.futureX = h.x;
						h.futureY = h.y;
					}
				}
			}
		}
	}

	int mouseX = 0;
	int mouseY = 0;

	@Override
	public void mouseMoved(MouseEvent e) {
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

				h.heroClicked();
			}
		}
		System.out.println("button checked");
	}

	void Generate(Graphics g) {

		g.drawImage(image, 0, 0, 1500, 1000, null);
		h.draw(g);
	}
}
