import java.awt.Graphics;
import java.util.Random;


/**
 *
 * @author Gareth
 */
public abstract class Enemy {
    double x;
    double y;
    double angle = new Random().nextInt(7); //in radians
    public int health = 3;
    
    public Enemy(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public boolean hurt(int damage) {
    	health -= damage;
    	if(health <= 0) {
    		Main.panel.setRemoveEnemy(this);
    		return true;
    	}
    	return false;
    }
    
    public abstract void move();
    public abstract void draw(Graphics g);
}
