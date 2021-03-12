
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
public class grunt extends GameObject {
	public static BufferedImage image;
	public static boolean needImage = true;
	public static boolean gotImage = false;
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
}
