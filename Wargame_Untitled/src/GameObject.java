import java.awt.Rectangle;
import javax.swing.Timer;
public class GameObject {
	int x;
	int y;
	int width;
	int height;
	Rectangle collisionBox;

	GameObject(int a, int b, int c, int d, int o, int p) {
		this.x = a;
		this.y = b;
		this.width = c;
		this.height = d;
		this.originx = o;
		this.originy = p;
		collisionBox = new Rectangle(a, b, c, d);
		this.clicked = false;
	}
	int originx;
	int originy;
	int move = 0;
	boolean clicked;
	boolean onTable = true;

	void update() {
		collisionBox.setBounds(x, y, width, height);
		
	}
}
