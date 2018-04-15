import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;


public class MainPanel extends JPanel {
	
	public ArrayList<Tile> objects = new ArrayList<>();
	public ArrayList<Enemy> enemies = new ArrayList<>();
	Random rand = new Random();
	
	ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
	
	public void loadRoom(Room r) {
		objects.clear();
		enemies.clear();
		for(int i = 0; i < r.startingConfiguration.length; i++) {
			for(int j = 0; j < r.startingConfiguration[i].length; j++) {
				if(r.startingConfiguration[i][j] == TileType.monsterSpawn) {
					enemies.add(rand.nextInt(3) == 0 ? new Phantom(i * 72, j * 72) : new Monster(i * 72 + 6, j * 72 + 6, rand.nextInt(4)+1));
					objects.add(new Tile(i * 72, j * 72, 72, 72, Util.getImageForTileType(TileType.air), TileType.air));
				} else
					objects.add(new Tile(i * 72, j * 72, 72, 72, Util.getImageForTileType(r.startingConfiguration[i][j]), r.startingConfiguration[i][j]));
			}
			
		}
	}
	
	public void update() {
		enemies.removeAll(enemiesToRemove);
		enemiesToRemove.clear();
		for(Enemy e : enemies) {
			e.move();
		}
	}
	
	public void draw(Graphics2D g) {
		if(Main.titleScreen) {
	        g.setColor(Color.BLACK);
	        g.fillRect(0, 0, 72*11, 72*11);
	        g.setFont(new Font("Papyrus", Font.PLAIN, 40));
	        g.setColor(Color.WHITE);
	        g.drawString("Brave the Onslaught", 36*11 - g.getFontMetrics().stringWidth("Brave the Onslaught")/2, 300);
	        g.setFont(new Font("Serif", Font.PLAIN, 20));
	        g.drawString("Press any key to start", 36*11 - g.getFontMetrics().stringWidth("Press any key to start")/2, 450);
	        return;
		}
		
		for(Tile t : objects) {
			t.draw(g);
		}
		for(Enemy e : enemies) {
			e.draw(g);
		}
	}
	
	public void setRemoveEnemy(Enemy e) {
		enemiesToRemove.add(e);
	}
	
}
