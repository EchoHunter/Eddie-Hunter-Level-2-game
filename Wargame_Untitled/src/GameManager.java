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
	public static BufferedImage knife;
	public static boolean needImage = true;
	public static boolean gotImage = false;
	Timer frameDraw;
	Timer GruntAnimate;
	int currentImage = 0;
	Random rand = new Random();
	Font titleFont = new Font("Arial", Font.PLAIN, 48);
	Font lowerFont = new Font("Arial", Font.PLAIN, 25);
	hero h = new hero(50, 50, 50, 50, 50, 50);
	ArrayList<terrain> t = new ArrayList<terrain>();
	ArrayList<grunt> grunts = new ArrayList<grunt>();
	ArrayList<projectile> p = new ArrayList<projectile>();
	boolean gruntMove = false;
	static long counter =0;
	GameManager() {
		frameDraw = new Timer(1000 / 44, this);
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
		
		if (gotImage) {
			if(knivesUsed ==0) {
			g.drawImage(knife,1150 ,50,100, 100, null );
			g.drawImage(knife,1200 ,50,100, 100, null );
			g.drawImage(knife,1250 ,50,100, 100, null );
			}
			else if (knivesUsed ==1) {
				g.drawImage(knife,1150 ,50,100, 100, null );
				g.drawImage(knife,1200 ,50,100, 100, null );
			}
			else if(knivesUsed ==2) {
				g.drawImage(knife,1150 ,50,100, 100, null );
			}
			}
		h.draw(g);
		drawTerrain(g);
		drawProjectiles(g);
		drawGrunt(g);
	}

	void drawTerrain(Graphics g) {
		for (terrain f : t) {
			f.draw(g);
			
			g.setColor(Color.BLACK);
			//g.drawRect(f.collisionBox.x, f.collisionBox.y, f.collisionBox.width, f.collisionBox.height);
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
			if (c.isActive == false) {
				m.remove();
				System.out.println("removed knife");
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
			if ((m.x > GameRunner.w) || (m.x < 0)) {
				m.isActive = false;
			}
			if ((m.y > GameRunner.h) || (m.y < 0)) {
				m.isActive = false;
			}
		}
		for (grunt g : grunts) {
			for (projectile m : p) {
				if (g.collisionBox.intersects(m.collisionBox)) {
					g.isActive = false;
					m.isActive = false;
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

	int gruntCount = 0;

	void addGrunt() {
		boolean addgrunt = true;

		grunt testGrunt = new grunt(rand.nextInt(GameRunner.w - 235) + 200, rand.nextInt(GameRunner.h - 235) + 200, 35,
				35, 0, 0);
		while (addgrunt) {
			boolean gruntIntersects = false;
			if (!gruntIntersects) {
				for (terrain x : t) {
					if (testGrunt.collisionBox.intersects(x.collisionBox)) {
						gruntIntersects = true;
						testGrunt = new grunt(rand.nextInt(GameRunner.w - 235) + 200,
								rand.nextInt(GameRunner.h - 235) + 200, 35, 35, 0, 0);
						break;
					}
				}
			}
			if (gruntIntersects == false) {
				grunts.add(testGrunt);
				addgrunt = false;
			}
		}
		gruntCount++;
	}

	void removeGrunts() {
		Iterator<grunt> z = grunts.iterator();
		while (z.hasNext()) {
			grunt c = z.next();
			if (c.isActive == false) {
				z.remove();
				System.out.println("removed knife");
			}
		}
	}

	void updateGrunt() {
		for (grunt g : grunts) {
			g.update();
		}
	}

	void drawGrunt(Graphics c) {
		for (grunt g : grunts) {
			g.draw(c);
			
//			c.setColor(Color.BLACK);
//			c.drawRect(g.collisionBox.x, g.collisionBox.y, g.collisionBox.width, g.collisionBox.height);
		}
	}
	
	void checkGruntCollision() {
	    /*
	     * This variable is only set upon entering the grunt's turn
	     */
	    if(gruntMove) {
	        /*
	         * Destination of all the grunts set here and their positions
	         * are updated in the update method afterwards
	         */
	        for( grunt g : this.grunts ) {
	            
	            int[] move = getNextMove(g);
	            g.setXandY(move[0], move[1]);
	            g.takeTurn();
	            
	        }
	        
	        /*
	         * Must put this after the loop through all the grunts so they
	         * only move once per turn
	         */
	        gruntMove = false;
	    }
	}
	
	int[] getNextMove(grunt g) {
	    int attempts = 0;
	    double angleToHero = Math.atan2(h.y - g.y, h.x - g.x);
	    boolean haveMove = false;
	    
	    /*
	     * This is the expected move distance. 100 isn't used because the
	     * grunt move distance isn't calculated to an exact value. 100 plus a
	     * tolerance of 50 works good enough.
	     */
	    int moveDistance = 115;
	    
	    while( attempts < 32 && !haveMove ) {
	        attempts++;
	        haveMove = true;
	        
	        for( terrain eachTerrain : this.t ) {
	            if( isCollisionOnPath(eachTerrain, g, angleToHero, moveDistance) ) {
	                /*
	                 * Collision with terrain, change the angle slightly to
	                 * try and stay moving toward the hero
	                 */
                    if( rand.nextBoolean() ) {
                        angleToHero = angleToHero + Math.PI / 16;
                    } else {
                        angleToHero = angleToHero - Math.PI / 16;
                    }
	                haveMove = false;
	                break;
	            }
	        }
	    }
	    
	    int x = g.x;
	    int y = g.y;
	    
	    if( attempts < 32 ) {
	        x = g.x + (int)(moveDistance * Math.cos(angleToHero));
	        y = g.y + (int)(moveDistance * Math.sin(angleToHero));
	    }
	    
	    return new int[] {x, y};
	}
	
	boolean isCollisionOnPath(terrain terr, grunt g, double angleRad, int moveDistance) {
	    boolean isCollision = false;
	    Rectangle newMove = null;
	    int currentDistance = 5;
	    
        while(currentDistance <= moveDistance) {
            int x = g.x + (int)(currentDistance * Math.cos(angleRad));
            int y = g.y + (int)(currentDistance * Math.sin(angleRad));
            newMove = new Rectangle(x, y, g.width, g.height);
            
            if(terr.collisionBox.intersects(newMove)) {
                isCollision = true;
                break;
            }
            
            /*
             * No collision so far, test the next position on the path
             */
            currentDistance += 5;
        }
        
        return isCollision;
	}

//	Rectangle newMove;
//	boolean moveIntersects = false;
	
//	void checkGruntCollision() {
//		if (gruntMove) {
//			for (grunt g : grunts) {
//				int xBias = -10;
//				int yBias = -10;
//
//				System.out.println("x and y set");
//				if (g.x > h.x) {
//					xBias = 8;
//				}
//				if (g.x < h.x) {
//					xBias = 12;
//				}
//				if (g.y > h.y) {
//					yBias = 8;
//				}
//				if (g.y < h.y) {
//					yBias = 12;
//				}
//
//				int moveX = g.x + (g.width) / 2 + rand.nextInt(21) - xBias;
//				int moveY = g.y + (g.height / 2) + rand.nextInt(21) - yBias;
//				if (moveX < -10) {
//					moveX = g.x + (g.width) / 2 - 10;
//				}
//				if (moveX > 10) {
//					moveX = g.x + (g.width) / 2 + 10;
//				}
//				if (moveY < -10) {
//					moveY = g.y + (g.height) / 2 - 10;
//				}
//				if (moveY > 10) {
//					moveY = g.y + (g.height) / 2 + 10;
//				}
//				newMove = new Rectangle(g.x, g.y, g.width, g.height);
//
//				if ((g.x < h.x) && (g.y < h.y)) {
//					newMove = new Rectangle(g.x + 10, g.y + 10, g.width, g.height);
//				} else if ((g.x > h.x) && (g.y < h.y)) {
//					newMove = new Rectangle(g.x - 10, g.y + 10, g.width, g.height);
//				} else if ((g.x < h.x) && (g.y > h.y)) {
//					newMove = new Rectangle(g.x + 10, g.y - 10, g.width, g.height);
//				} else if ((g.x > h.x) && (g.y > h.y)) {
//					newMove = new Rectangle(g.x - 10, g.y - 10, g.width, g.height);
//				}
//
//				checkMove(g);
//				while (moveIntersects) {
//					for (terrain c : t) {
//						if (newMove.intersects(c.collisionBox)) {
//							moveIntersects = true;
//							break;
//						} else {
//							moveIntersects = false;
//						}
//
//					}
//					if (moveIntersects) {
//						moveX = g.x + (g.width) / 2 + rand.nextInt(21) - xBias;
//						moveY = g.y + (g.height / 2) + rand.nextInt(21) - yBias;
//						if (moveX < -10) {
//							moveX = g.x + (g.width) / 2 - 10;
//						}
//						if (moveX > 10) {
//							moveX = g.x + (g.width) / 2 + 10;
//						}
//						if (moveY < -10) {
//							moveY = g.y + (g.height) / 2 - 10;
//						}
//						if (moveY > 10) {
//							moveY = g.y + (g.height) / 2 + 10;
//						}
//						newMove.setBounds(moveX, moveY, g.width, g.height);
//						moveIntersects = false;
//						for (terrain c : t) {
//							if (newMove.intersects(c.collisionBox)) {
//								moveX = g.x + (g.width) / 2 + rand.nextInt(21) - xBias;
//								moveY = g.y + (g.height / 2) + rand.nextInt(21) - yBias;
//								if (moveX < -10) {
//									moveX = g.x + (g.width) / 2 - 10;
//								}
//								if (moveX > 10) {
//									moveX = g.x + (g.width) / 2 + 10;
//								}
//								if (moveY < -10) {
//									moveY = g.y + (g.height) / 2 - 10;
//								}
//								if (moveY > 10) {
//									moveY = g.y + (g.height) / 2 + 10;
//								}
//								newMove.setBounds(moveX, moveY, g.width, g.height);
//								moveIntersects = true;
//								break;
//							}
//
//						}
//						if (!moveIntersects) {
//							g.takeTurn();
//							g.setXandY(moveX, moveY);
//						}
//
//					}
//
//				}
//			}
//			gruntMove = false;
//			moveIntersects = false;
//		}
//	}

//	void checkMove(grunt g) {
//
//		moveIntersects = false;
//		for (terrain v : t) {
//
//			if (newMove.intersects(v.collisionBox)) {
//				moveIntersects = true;
//				break;
//			}
//		}
//		if(!moveIntersects) {
//			g.takeTurn();
//			g.setXandY(newMove.x, newMove.y);
//			}
//		}

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
		h.update();
		updateProjectiles();
		checkProjectileMove();
		checkProjectileIntersect();
		removeProjectiles();
		checkGruntCollision();
		updateGrunt();
		for (grunt g : grunts) {
			if (h.collisionBox.intersects(g.collisionBox)) {
				currentState = END;
			}
		}
		removeGrunts();
	}

	void updateEndState() {

	}

	void checkProjectileMove() {
		Iterator<projectile> g = p.iterator();
		while (g.hasNext()) {
			projectile c = g.next();
			if ((c.x == c.newX) && (c.y == c.newY)) {
				g.remove();
				System.out.println("removed knife");
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
					gruntMove = true;
					for (grunt g : grunts) {
						if (h.collisionBox.intersects(g.collisionBox)) {
							currentState = END;
						}
					}
				}
				turn = playerTurn;
				removeProjectiles();
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
	    t.clear();
	    grunts.clear();
	    h.x = 50;
	    h.y = 50;
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
//		t.add(new terrain(900, 700, 100, 100, 10, 10));
//		t.add(new terrain(600, 300, 100, 100, 10, 10));
//		t.add(new terrain(1000, 300, 100, 100, 10, 10));
//
//        t.add(new terrain(1000, 500, 100, 100, 10, 10));
//		t.add(new terrain(300, 200, 100, 100, 10, 10));
//		t.add(new terrain(900, 500, 100, 100, 10, 10));
//		grunts.add(new grunt(1110, 610, 25, 25, 10, 10));
//		addGrunt();
		for (int i = 0; i < 13; i++) {
			addGrunt();
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

	@Override
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
			counter++;
		}
		if (currentState == END) {
			updateEndState();
		}
		repaint();

	}
public static long getCounter() {
	return counter;
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

	int knivesUsed =0;
	@Override
	public void mousePressed(MouseEvent arg0) {
		mouseX = arg0.getX() - this.getLocationOnScreen().x;
		mouseY = arg0.getY() - this.getLocationOnScreen().y;
		checkButton();
		if (turn == playerTurn) {
			if (attackMode) {
if(knivesUsed>= 3) {
	knivesUsed = 0;
	turn = enemyTurn;
	
	attackMode = false;
}
else {
				addProjectile(mouseX, mouseY);
				knivesUsed++;
}			
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
			spacePressed++;
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
						 double[] position = h.getFutureMovementPosition(mouseX, mouseY);
						 h.futureX = (int)position[0];
						 h.futureY = (int)position[1];

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
				knife = ImageIO.read(this.getClass().getResourceAsStream("kanife.png"));
				gotImage = true;
			} catch (Exception e) {

			}
			needImage = false;
		}
	}

	void checkButton() {
		System.out.println(mouseX + " " + mouseY + " " + h.x + " " + h.y);
		if (attackMode == false) {
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
	}

	void Generate(Graphics g) {

		g.drawImage(image, 0, 0, 1500, 1000, null);
		h.draw(g);
	}
}
