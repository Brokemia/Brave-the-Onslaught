import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;


public class Util {
	
	@SuppressWarnings("deprecation")
	public static Image getImage(String str) {
		return new ImageIcon(Util.class.getClassLoader().getResource(str)).getImage();
	}

	public static Image getImageForTileType(TileType tileType) {
		return getImage("assets/"+tileType+".png");
	}
	
}
