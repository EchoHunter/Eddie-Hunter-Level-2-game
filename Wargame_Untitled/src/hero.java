import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class hero extends GameObject {
    public static BufferedImage image;
    public static boolean needImage = true;
    public static boolean gotImage = false;
    
    /*
     *  DMC New variables
     */
    int movementRadius = 100;
    Integer movementX = null;
    Integer movementY = null;
    Integer futureX = null;
    Integer futureY = null;
    
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
        
        // DMC Draw the movement circle and future location of the hero
        if( this.clicked && haveMovementCircle() ) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(2f));
            g2d.setColor(Color.BLACK);
            g2d.drawOval(this.movementX, this.movementY, 2 * this.movementRadius, 2 * this.movementRadius);
            
            // Shaded circular movement zone
            g2d.setColor(Color.BLUE);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f));
            g2d.fillOval(this.movementX, this.movementY, 2 * this.movementRadius, 2 * this.movementRadius);

            // Future hero location
            g2d.setColor(Color.BLUE);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            g2d.fillRect(this.futureX, this.futureY, this.width, this.height);
            
            // Reset transparency so it doesn't affect other drawn objects
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }
    
    /*
     * DMC Use this if you need to draw the movement circle right after the
     * first time the hero is clicked  
     */
    public boolean haveMovementCircle() {
        return this.movementX != null && this.movementY != null;
    }
    
    /*
     * DMC Call this the first time the hero is clicked
     */
    void heroClicked() {
        this.clicked = true;
        
        this.movementX = this.x + (this.width / 2) - this.movementRadius;
        this.movementY = this.y + (this.height / 2) - this.movementRadius;
        this.futureX = this.x;
        this.futureY = this.y;
    }
    
    /*
     * Call this when the mouse is released to reset the movement variables
     */
    public void reset() {
        this.clicked = false;
        this.movementX = null;
        this.movementY = null;
        this.futureX = null;
        this.futureY = null;
    }

    /*
     * DMC Check if inside the movement area circle
     */
    public boolean isMouseInsideMovementArea(int mouseX, int mouseY) {
        int distX = mouseX - (this.x + (this.width / 2));
        int distY = mouseY - (this.y + (this.height / 2));
        double distance = Math.sqrt( (distX * distX) + (distY * distY) );

        if( distance <= this.movementRadius ) {
            return true;
        }

        return false;
    }

    /*
     * DMC Gets the position when the mouse is outside the movement circle 
     */
    public double[] getFutureMovementPosition(int mouseX, int mouseY) {
        double[] position = {0.0, 0.0};
        int movementCenterX = this.x + (this.width / 2);
        int movementCenterY = this.y + (this.height / 2);

        double angle = Math.atan2( mouseY - movementCenterY, mouseX - movementCenterX );

        position[0] = movementCenterX + ((this.movementRadius - 0) * Math.cos(angle));
        position[1] = movementCenterY + ((this.movementRadius - 0) * Math.sin(angle));

        position[0] -= this.width / 2;
        position[1] -= this.height / 2;

        return position;
    }
}
