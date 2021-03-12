import javax.swing.JFrame;

public class GameRunner {
public final static int w = 1500;
public final static int h = 1000;
GameManager g = new GameManager();
JFrame j = new JFrame();
public static void main(String[] args) {
	
	
	GameRunner l = new GameRunner();
	l.setup();
}
public void setup() {
	j.add(g);
	j.addKeyListener(g);
	j.addMouseListener(g);
	j.addMouseMotionListener(g);
	j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	j.setSize(w,h);
	j.setVisible(true);
	
}

}
