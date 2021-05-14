
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
public class grunt extends GameObject {
	public static BufferedImage image;
	public static boolean needImage = true;
	public static boolean gotImage = false;
	int newX;
	int newY;
	Rectangle movementRange ;
	long startCount = 0;
	
	grunt(int a, int b, int c, int d, int o, int p) {
		super(a, b, c, d, o, p);
		movementRange = new Rectangle (x-10,y-10,width+20,height+20);
		// TODO Auto-generated constructor stub
	}
	int frameNum = 0;
	void draw(Graphics g) {
//		if((frameNum >= 0)||(frameNum <= 5)) {
//			loadImage("frame_0_delay-0.1s.gif");
//		frameNum++;
//		}
//		else if((frameNum >= 6)||(frameNum <= 12)) {
//			loadImage("frame_1_delay-0.1s.gif");
//			frameNum++;
//		}
//		else if((frameNum >= 13)||(frameNum <= 25)) {
//			loadImage("frame_2_delay-0.2s.gif");
//			frameNum++;
//		}
//		else if((frameNum >= 26)||(frameNum <= 35)) {
//			loadImage("frame_3_delay-0.15s.gif");
//		frameNum++;
//		}
//		else if((frameNum >= 37)||(frameNum <= 46)) {
//			loadImage("frame_4_delay-0.15s.gif");
//		frameNum++;
//		}
//		else {
//			frameNum =0;
//		}
		if(GameManager.getCounter()-startCount > 60) {
			
		}
		g.drawImage(image, x, y, width, height, null);
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
		movementRange.setBounds(x-17,y-17,20+width,20+height);
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
	void loadImage(String imageFile) {
		if (needImage) {
			try {
				image = ImageIO.read(this.getClass().getResourceAsStream(imageFile));
				gotImage = true;
			} catch (Exception e) {

			}
			needImage = false;
		}
	}
}
