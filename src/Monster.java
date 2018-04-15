import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


/**
 *
 * @author Gareth
 */
public class Monster extends Enemy {
    public double speed;
    
    public Monster(double x, double y, double speed) {
        super(x, y);
        this.speed = speed;
    }
    
    public void move() {
    	boolean canSeePlayer = true;
    	if(x - Main.player.x == 0)
    		x++;
        double m = (y - Main.player.y) / (x - Main.player.x);
        for(Tile tile : Main.panel.objects) {
        	if(tile.type == TileType.air || tile.type == TileType.dark || tile.type == TileType.gem)
        		continue;
            double leftIntersect = m * (tile.x - (x + 36)) + (y + 36);
            double rightIntersect = m * (tile.x + 72 - (x + 36)) + (y + 36);
            double topIntersect = (tile.y - (y + 36)) / m + (x + 36);
            double bottomIntersect = (tile.y + 72 - (y + 36)) / m + (x + 36);
            if(tile.y <= leftIntersect && leftIntersect <= tile.y + 72 && Math.min((x + 36), (Main.player.x + Main.player.width/2)) < tile.x && tile.x < Math.max((x + 36), (Main.player.x + Main.player.width/2)) || 
               tile.y <= rightIntersect && rightIntersect <= tile.y + 72 && Math.min((x + 36), (Main.player.x + Main.player.width/2)) < tile.x + 72 && tile.x + 72 < Math.max((x + 36), (Main.player.x + Main.player.width/2)) ||
               tile.x <= topIntersect && topIntersect <= tile.x + 72 && Math.min((y + 36), (Main.player.y + Main.player.width/2)) < tile.y && tile.y < Math.max((y + 36), (Main.player.y + Main.player.width/2)) ||
               tile.x <= bottomIntersect && bottomIntersect <= tile.x + 72 && Math.min((y + 36), (Main.player.y + Main.player.width/2)) < tile.y + 72 && tile.y < Math.max((y + 36), (Main.player.y + Main.player.width/2))) {
                canSeePlayer = false;
                break;
            }
        }
        
        if(canSeePlayer) {
            double sign = Math.signum(Main.player.x - x);
            double newX = x + sign * speed / Math.sqrt(Math.pow(m, 2) + 1);
            double newY = y + m * sign * speed / Math.sqrt(Math.pow(m, 2) + 1);
            if(checkCollide((int)newX, (int)newY)) {
            	if(checkCollide((int)x, (int)newY)) {
            		newY = y;
            	}
            	if(checkCollide((int)newX, (int)y)) {
            		newX = x;
           		}
            }
            x = newX;
            y = newY;
            angle = Math.atan2(Main.player.y - y, Main.player.x - x);
        }
    }
    
    public boolean checkCollide(int x, int y) {
		for(int i = 0; i < Main.panel.objects.size(); i++) {
			Tile t = Main.panel.objects.get(i);
			if(t.type == TileType.air || t.type == TileType.dark || t.type == TileType.gem)
				continue;
			if(x >= t.x && x <= t.x + t.width && y >= t.y && y <= t.y + t.height) {
				return true;
			}
			
			if(x+60 >= t.x && x+60 <= t.x + t.width && y >= t.y && y <= t.y + t.height) {
				return true;
			}
			
			if(x >= t.x && x <= t.x + t.width && y+60 >= t.y && y+60 <= t.y + t.height) {
				return true;
			}
			
			if(x+60 >= t.x && x+60 <= t.x + t.width && y+60 >= t.y && y+60 <= t.y + t.height) {
				return true;
			}
			
			if(t.x >= x && t.x <= x + 60 && t.y >= y && t.y <= y + 60) {
				return true;
			}
			
			if(t.x + t.width >= x && t.x + t.width <= x + 60 && t.y >= y && t.y <= y + 60) {
				return true;
			}
			
			if(t.x >= x && t.x <= x + 60 && t.y + t.height >= y && t.y + t.height <= y + 60) {
				return true;
			}
			
			if(t.x + t.width >= x && t.x + t.width <= x + 60 && t.y + t.height >= y && t.y + t.height <= y + 60) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void draw(Graphics g) {
		if(health <= 0)
			return;
		AffineTransform tx = AffineTransform.getRotateInstance(angle + Math.PI/2, 36, 36);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		Image img = Util.getImage("assets/monster" + (3-health) + ".png");
		BufferedImage buf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		buf.getGraphics().drawImage(img, 0, 0, null);
		try {
		g.drawImage(op.filter(buf, null), (int)x, (int)y, null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
