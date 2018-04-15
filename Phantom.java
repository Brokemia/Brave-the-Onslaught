import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


/**
 *
 * @author Gareth
 */
public class Phantom extends Enemy {
    final double acceleration = 0.8;
    double xVelocity = 0;
    double yVelocity = 0;
    public Phantom(double x, double y) {
    	super(x, y);
    	health = 1;
    }
    
    public void whenAttacked() {
        xVelocity += (x - Main.player.x)/2;
        yVelocity += (y - Main.player.y)/2;
    }
    
    public void move() {
        double theta = Math.atan2((Main.player.y - y), (Main.player.x - x)); 
        xVelocity += acceleration * Math.cos(theta);
        yVelocity += acceleration * Math.sin(theta);
        x += xVelocity;
        y += yVelocity;
        angle = theta;
    }

	@Override
	public void draw(Graphics g) {
		if(health == 0)
			return;
		AffineTransform tx = AffineTransform.getRotateInstance(angle + Math.PI/2, 36, 36);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		Image img = Util.getImage("assets/phantom.png");
		BufferedImage buf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		buf.getGraphics().drawImage(img, 0, 0, null);
		try {
		g.drawImage(op.filter(buf, null), (int)x, (int)y, null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
    
}
