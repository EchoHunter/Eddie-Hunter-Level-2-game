import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class terrain extends GameObject {
	public static BufferedImage image;
	public static boolean needImage = true;
	public static boolean gotImage = false;
	
	terrain(int a, int b, int c, int d, int o, int p) {
		super(a, b, c, d, o, p);
		// TODO Auto-generated constructor stub
		
	}
	
void draw(Graphics g) {
			
		
			g.setColor(Color.GRAY);
			g.fillRect(x, y, width, height);
			
	}
}
