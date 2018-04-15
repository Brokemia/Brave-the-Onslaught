import java.awt.Graphics2D;
import java.awt.Image;


public class Tile {
	
	public int x, y, width, height;
	
	public Image img;
	
	public TileType type;
	
	public Tile(int x, int y, int w, int h, TileType t) {
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		type = t;
	}
	
	public Tile(int x, int y, int w, int h, Image img, TileType t) {
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		this.img = img;
		type = t;
	}
	
	public Tile(int x, int y, int w, int h, String path, TileType t) {
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		img = Util.getImage(path);
		type = t;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(img, x, y, width, height, null);
	}
	
}
