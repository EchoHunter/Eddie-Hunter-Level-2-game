import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class hero extends GameObject {
	public static BufferedImage image;
	public static boolean needImage = true;
	public static boolean gotImage = false;
	hero(int a, int b, int c, int d, int o, int p) {
		super(a, b, c, d, o, p);
		this.x = a;
		this.y = b;
		this.width = c;
		this.height = d;
		this.originx = o;
		this.originy = p;
	}
	void draw(Graphics g) {
		
		if (gotImage) {
			g.drawImage(image, x, y, width, height, null);
		} else {
			g.setColor(Color.BLUE);
			g.fillRect(x, y, width, height);
		}
	}
	
}
