import java.util.Arrays;
import java.util.Random;

public class RoomManager {

	public static Room[][] rooms = new Room[8][8];
	
	private static Random r = new Random();
    
    private final static int ROOM_SIZE = 11;
    private final static int AVG_WALL_NUM = 8;
    public final static TileType[][] gemRoom = {{TileType.brick, TileType.brick, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.brick, TileType.brick},
    											{TileType.brick, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.brick},
    											{TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air},
    											{TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.monsterSpawn, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air},
    											{TileType.air, TileType.air, TileType.air, TileType.air, TileType.dark, TileType.dark, TileType.dark, TileType.air, TileType.air, TileType.air, TileType.air},
    											{TileType.air, TileType.air, TileType.air, TileType.monsterSpawn, TileType.dark, TileType.gem, TileType.dark, TileType.monsterSpawn, TileType.air, TileType.air, TileType.air},
    											{TileType.air, TileType.air, TileType.air, TileType.air, TileType.dark, TileType.dark, TileType.dark, TileType.air, TileType.air, TileType.air, TileType.air},
    											{TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.monsterSpawn, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air},
    											{TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air},
    											{TileType.brick, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.brick},
    											{TileType.brick, TileType.brick, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.brick, TileType.brick}};
    
    public final static TileType[][] emptyRoom = {{TileType.brick, TileType.brick, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.brick, TileType.brick},
												{TileType.brick, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.brick},
												{TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air},
												{TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.monsterSpawn, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air},
												{TileType.air, TileType.air, TileType.air, TileType.air, TileType.dark, TileType.dark, TileType.dark, TileType.air, TileType.air, TileType.air, TileType.air},
												{TileType.air, TileType.air, TileType.air, TileType.monsterSpawn, TileType.dark, TileType.dark, TileType.dark, TileType.monsterSpawn, TileType.air, TileType.air, TileType.air},
												{TileType.air, TileType.air, TileType.air, TileType.air, TileType.dark, TileType.dark, TileType.dark, TileType.air, TileType.air, TileType.air, TileType.air},
												{TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.monsterSpawn, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air},
												{TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air},
												{TileType.brick, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.brick},
												{TileType.brick, TileType.brick, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.air, TileType.brick, TileType.brick}};
	
	static {
		rooms[0][0] = new Room();
		rooms[0][0].startingConfiguration[5][5] = TileType.air;
		for(int i = 0; i < 10; i++) {
			int xPos = r.nextInt(rooms.length), yPos = r.nextInt(rooms.length);
			while(xPos == 0 || yPos == 0 || rooms[xPos][yPos] != null) {
				xPos = r.nextInt(rooms.length);
				yPos = r.nextInt(rooms.length);
			}
			rooms[xPos][yPos] = new Room(gemRoom);
		}
	}
	
	private static int randomGaussian(double mean, double stdev) {
        int result = (int)(stdev * r.nextGaussian() + mean);
        return result < 0? 0: result;
    }
	
	public static TileType[][] generateRoomStartingConfiguration() {
        TileType[][] room = new TileType[ROOM_SIZE][ROOM_SIZE];
        for(int i = 0; i < ROOM_SIZE; i++) {
            Arrays.fill(room[i], TileType.air);
        }
        int numWalls = randomGaussian(AVG_WALL_NUM, 5);
        
        for(int i = 0; i < numWalls; i++) {
            int direction = r.nextInt(4);
            int length = randomGaussian(6, 3);
            int x = r.nextInt(ROOM_SIZE);;
            int y = r.nextInt(ROOM_SIZE);;
            for (int j = 1; j <= length; j++) {
                if(x < 0 || x >= ROOM_SIZE || y < 0 || y >= ROOM_SIZE) {
                    break;
                }
                room[y][x] = TileType.brick;
                switch(direction) {
                    case 0:
                        x += 1;
                        break;
                    case 1:
                        x -= 1;
                        break;
                    case 2:
                        y += 1;
                        break;
                    case 3:
                        y -= 1;
                        break;
                }
            }
        }
        int numMonsters = r.nextInt(6) + 1;
        for (int i = 0; i < numMonsters; i++) {
            room[r.nextInt(ROOM_SIZE)][r.nextInt(ROOM_SIZE)] = TileType.monsterSpawn;
        }
        
        int numLavaPits = r.nextInt(3);
        for (int i = 0; i < numLavaPits; i++) {
            final int startX = r.nextInt(11);
            final int startY = r.nextInt(11);
            int[][] lavaTiles = new int[121][2];
            lavaTiles[0] = new int[] {startX, startY};
            
            for (int j = 1; j < 20; j++) {
                int[] chosen = lavaTiles[r.nextInt(j)];
                switch (r.nextInt(4)) {
                    case 0:
                        lavaTiles[j] = new int[] {chosen[0] + 1, chosen[1]};
                        break;
                    case 1:
                        lavaTiles[j] = new int[] {chosen[0] - 1, chosen[1]};
                        break;
                    case 2:
                        lavaTiles[j] = new int[] {chosen[0], chosen[1] + 1};
                        break;
                    case 3:
                        lavaTiles[j] = new int[] {chosen[0], chosen[1] - 1};
                        break;
                }
            }
            
            for (int j = 0; j < 20; j++) {
                int x = lavaTiles[j][0];
                int y = lavaTiles[j][1];
                if (0 <= x && x < 11 && 0 <= y && y < 11) {
                    room[y][x] = TileType.lava;
                }
            }
        }
        return room;
	}
	
}
