
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
public class grunt extends GameObject {
	public static BufferedImage image;
	public static boolean needImage = true;
	public static boolean gotImage = false;
	int newX;
	int newY;

	grunt(int a, int b, int c, int d, int o, int p) {
		super(a, b, c, d, o, p);
		// TODO Auto-generated constructor stub
	}
	void draw(Graphics g) {
		if (gotImage) {
			g.drawImage(image, x, y, width, height, null);
		} else {
			g.setColor(Color.RED);
			g.fillRect(x, y, width, height);
		}
	}
	int speed = 10;
	int xDiff;
	int yDiff;
	int moveLimit = 0;
	void update() {
		super.update();
		if(moveLimit<100) {
		if ((Math.abs(xDiff) > speed) || (Math.abs(yDiff) > speed)) {
			double angleRad = Math.atan2(yDiff, xDiff);
			
			super.x += Math.cos(angleRad) * speed;
			super.y += Math.sin(angleRad) * speed;
			moveLimit += speed;
		}
		}

	}
	void takeTurn() {
	moveLimit = 0;
	}
	void setXandY(int newX, int newY) {
		this.newX = newX;
		this.newY = newY;
		xDiff = newX-x;
		yDiff = newY-y;
	}
}
