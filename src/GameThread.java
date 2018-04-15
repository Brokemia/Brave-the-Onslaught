import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.apache.commons.lang3.time.StopWatch;


public class GameThread extends Thread {
	
	public StopWatch stopwatch;
	
	@Override
	public void run() {
		while(Main.titleScreen) {
			BufferedImage img = new BufferedImage(900, 900, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			Main.panel.draw(g);
			Main.panel.getGraphics().drawImage(img, 0, 0, 900, 900, null);
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		stopwatch = new StopWatch();
		stopwatch.start();
		Main.panel.loadRoom(RoomManager.rooms[0][0]);
		while(true) {
			BufferedImage img = new BufferedImage(900, 900, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 900, 900);
			Main.panel.update();
			Main.player.update();
			Main.panel.draw(g);
			Main.player.draw(g);
			Main.panel.getGraphics().drawImage(img, 0, 0, 900, 900, null);
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
