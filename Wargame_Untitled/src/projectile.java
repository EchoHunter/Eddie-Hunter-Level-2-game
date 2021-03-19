import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class projectile extends GameObject {
	public static BufferedImage image;
	public static boolean needImage = true;
	public static boolean gotImage = false;
	GameManager r = new GameManager();
	int newX;
	int newY;

	projectile(int a, int b, int c, int d, int o, int p) {
		super(a, b, c, d, o, p);
		// TODO Auto-generated constructor stub
		newX = r.newPX;
		newY = r.newPY;
		if (needImage) {
			loadImage("kanife.png");
		}
	}

	int speed = 15;
	int xDiff = newX - super.x;
	int yDiff = newY - super.y;

	void update() {
		if ((Math.abs(xDiff) > speed) || (Math.abs(yDiff) > speed)) {
			double angleRad = Math.atan2(yDiff, xDiff);

			super.x += Math.cos(angleRad) * speed;
			super.y += Math.sin(angleRad) * speed;
		}
	}

	void draw(Graphics g) {

		if (gotImage) {
			g.drawImage(image, x, y, width, height, null);
		} else {
			g.setColor(Color.BLUE);
			g.fillRect(x, y, width, height);
		}
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
