import java.util.ArrayList;


public class Room {
	
	public TileType[][] startingConfiguration;
	
	public Room() {
		startingConfiguration = RoomManager.generateRoomStartingConfiguration();
	}
	
	public Room(TileType[][] tiles) {
		startingConfiguration = tiles;
	}

	public ArrayList<Tile> returnConfigurationAsObjectList() {
		ArrayList<Tile> obj = new ArrayList<>();
		
		for(int i = 0; i < startingConfiguration.length; i++) {
			for(int j = 0; j < startingConfiguration[i].length; j++) {
				if(startingConfiguration[i][j] == TileType.monsterSpawn) {
					obj.add(new Tile(i * 72, j * 72, 72, 72, Util.getImageForTileType(TileType.air), TileType.air));
				} else
					obj.add(new Tile(i * 72, j * 72, 72, 72, Util.getImageForTileType(startingConfiguration[i][j]), startingConfiguration[i][j]));
			}
			
		}
		
		return obj;
	}
	
}
