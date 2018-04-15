import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;


public class Player implements KeyListener {
	
	public ArrayList<Integer> keysPressed = new ArrayList<>(), keysNewlyReleased = new ArrayList<>();
	
	public int quicktimeAttack = 0, maxWeaponDamage = 3, damageCooldown = 0, weaponTimer = 0;
	
	public static int MAX_HEALTH = 20, TIME_TO_QUICK = 20;
	
	public int gems = 0, potions = 1;
	
	public int width = 60, height = 60, x = 5 * 72 + (72-width)/2, y = 5 * 72 + (72-height)/2, roomX = 0, roomY = 0, health = MAX_HEALTH, speed = 5;
	
	public double angle = 0; //radians
	
	public Image img = Util.getImage("assets/player.png");
	
	public void draw(Graphics2D g) {
		AffineTransform tx = AffineTransform.getRotateInstance(angle, 36, 36);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		int w = width, h = height;
		if (angle % (Math.PI / 2) != 0){
			w = (int)(width * 1.2);
			h = (int)(height * 1.2);
		}
		BufferedImage bufOK = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		bufOK.getGraphics().drawImage(img, 0, 0, null);
		Image hurt = Util.getImage("assets/playerHurt.png");
		BufferedImage buf1  = new BufferedImage(hurt.getWidth(null), hurt.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		buf1.getGraphics().drawImage(hurt, 0, 0, null);
		g.drawImage(op.filter((BufferedImage)(damageCooldown > 0 ? buf1 : bufOK), null), x, y, w, h, null);
		if(weaponTimer > 0) {
			g.rotate(weaponTimer * 12, x + 30, y + 30);
			g.drawImage(Util.getImage("assets/attack.png"), x - 5, y - 5, 70, 70, null);
			g.rotate(-weaponTimer * 12, x + 30, y + 30);
		}
		if(potions > 0) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("SansSerif", Font.PLAIN, 30));
			g.drawString(potions + "x", 10, 30);
			g.drawImage(Util.getImage("assets/potion.png"), 35, 0, null);
		}
		if(health > 0) {
			BufferedImage img = new BufferedImage(200, 10, BufferedImage.TYPE_INT_ARGB);
			img.getGraphics().drawImage(Util.getImage("assets/healthFull.png"), 0, 0, null);
			BufferedImage buf = new BufferedImage(200, 10, BufferedImage.TYPE_INT_ARGB);
			buf.getGraphics().drawImage(Util.getImage("assets/healthEmpty.png"), 0, 0, null);
			img.getGraphics().drawImage(buf.getSubimage(0, 0, (int)(200*((MAX_HEALTH-(double)health)/MAX_HEALTH))+1, 10), 0, 0, null);
			g.drawImage(img, 72*11-200, 0, null);
		} else {
			g.drawImage(Util.getImage("assets/healthEmpty.png"), 72*11-200, 0, null);
			g.setColor(new Color(255, 0 , 0, 120));
			g.fillRect(0, 0, 72*11, 72*11);
			g.setColor(Color.WHITE);
			g.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
			g.drawString("The dungeon has claimed another victim...", 36*11 - g.getFontMetrics().stringWidth("The dungeon has claimed another victim...")/2, 400);
		}
		
		if(quicktimeAttack > 0) {
			g.drawImage(Util.getImage("assets/qtBar.png"), x + width + 5, y, null);
			g.drawImage(Util.getImage("assets/qtSlider.png"), x + width + 3, y + (int)(((double)TIME_TO_QUICK-quicktimeAttack)/TIME_TO_QUICK * height), null);
		}
		
		if(gems >= 5) {
			if(!Main.thrd.stopwatch.isStopped())
				Main.thrd.stopwatch.stop();
			g.setColor(new Color(0, 255, 0, 120));
			g.fillRect(0, 0, 72*11, 72*11);
			g.setColor(Color.WHITE);
			g.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
			g.drawString("You have escaped the dungeon and will live to see another day!", 36*11 - g.getFontMetrics().stringWidth("You have escaped the dungeon and will live to see another day!")/2, 400);
			g.drawString("Time: " + Main.thrd.stopwatch.getTime(), 36*11 - g.getFontMetrics().stringWidth("Time: " + Main.thrd.stopwatch.getTime())/2, 500);
		}
	}
	
	public void update() {
		speed = 5;
		
		if(gems >= 5)
			return;
		
		if(quicktimeAttack > 0)
			quicktimeAttack--;
		if(damageCooldown > 0)
			damageCooldown--;
		if(weaponTimer > 0)
			weaponTimer--;
		
		if(keysPressed.size() > 0 && health <= 0) {
			System.exit(0);
		}
		
		if(keysPressed.contains(KeyEvent.VK_W) && !keysPressed.contains(KeyEvent.VK_S)) {
			angle = 0;
			if(y-5 < 0) {
				int newRx = roomX;
				int newRy = roomY == 0 ? RoomManager.rooms.length-1 : roomY-1;
				
				if(RoomManager.rooms[newRx][newRy] == null)
					RoomManager.rooms[newRx][newRy] = new Room();
				
				if(!checkCollide(x, 72*11-height, RoomManager.rooms[newRx][newRy])) {
					y = 72*11-height;
					Main.panel.loadRoom(RoomManager.rooms[newRx][newRy]);
					roomX = newRx;
					roomY = newRy;
				}
			} else if(!checkCollide(x, y-speed)) {
				y -= speed;
			}
		}
		
		if(keysPressed.contains(KeyEvent.VK_S) && !keysPressed.contains(KeyEvent.VK_W)) {
			angle = Math.PI;
			if(y+speed+height > 72*11) {
				int newRx = roomX;
				int newRy = roomY == RoomManager.rooms.length-1 ? 0 : roomY+1;
				
				if(RoomManager.rooms[newRx][newRy] == null)
					RoomManager.rooms[newRx][newRy] = new Room();
				
				if(!checkCollide(x, 0, RoomManager.rooms[newRx][newRy])) {
					y = 0;
					Main.panel.loadRoom(RoomManager.rooms[newRx][newRy]);
					roomX = newRx;
					roomY = newRy;
				}
			} else if(!checkCollide(x, y+speed)) {
				y += speed;
			}
		}
		
		if(keysPressed.contains(KeyEvent.VK_A) && !keysPressed.contains(KeyEvent.VK_D)) {
			angle = 3 * Math.PI / 2;
			if(keysPressed.contains(KeyEvent.VK_W) && !keysPressed.contains(KeyEvent.VK_S)) {
				angle = 7 * Math.PI / 4;
				speed /= Math.sqrt(2);
			} else if(keysPressed.contains(KeyEvent.VK_S) && !keysPressed.contains(KeyEvent.VK_W)) {
				angle = 5 * Math.PI / 4;
				speed /= Math.sqrt(2);
			}
			
			if(x-speed < 0) {
				int newRx = roomX == 0 ? RoomManager.rooms.length-1 : roomX-1;
				int newRy = roomY;
				
				if(RoomManager.rooms[newRx][newRy] == null)
					RoomManager.rooms[newRx][newRy] = new Room();
				
				if(!checkCollide(72*11-width, y, RoomManager.rooms[newRx][newRy])) {
					x = 72*11-width;
					Main.panel.loadRoom(RoomManager.rooms[newRx][newRy]);
					roomX = newRx;
					roomY = newRy;
				}
			} else if(!checkCollide(x-speed, y)) {
				x -= speed;
			}
		}
		
		if(keysPressed.contains(KeyEvent.VK_D) && !keysPressed.contains(KeyEvent.VK_A)) {
			angle = Math.PI / 2;
			if(keysPressed.contains(KeyEvent.VK_W) && !keysPressed.contains(KeyEvent.VK_S)) {
				angle = Math.PI / 4;
				speed /= Math.sqrt(2);
			} else if(keysPressed.contains(KeyEvent.VK_S) && !keysPressed.contains(KeyEvent.VK_W)) {
				angle = 3 * Math.PI / 4;
				speed /= Math.sqrt(2);
			}
			if(x+speed+width > 72*11) {
				int newRx = roomX == RoomManager.rooms.length-1 ? 0 : roomX+1;
				int newRy = roomY;
				
				if(RoomManager.rooms[newRx][newRy] == null)
					RoomManager.rooms[newRx][newRy] = new Room();
				
				if(!checkCollide(0, y, RoomManager.rooms[newRx][newRy])) {
					x = 0;
					Main.panel.loadRoom(RoomManager.rooms[newRx][newRy]);
					roomX = newRx;
					roomY = newRy;
				}
			} else if(!checkCollide(x+speed, y)) {
				x += speed;
			}
		}
		
		if(keysNewlyReleased.contains(KeyEvent.VK_SPACE)) {
			if(quicktimeAttack == 0)
				quicktimeAttack = TIME_TO_QUICK;
			else {
				int damage = maxWeaponDamage*(TIME_TO_QUICK-quicktimeAttack)/TIME_TO_QUICK;
				for(Enemy e : Main.panel.enemies) {
					if(Math.hypot((e.x + 72/2)-(x+width/2), (e.y + 72/2)-(y+height/2)) < 75) {
						if(e.hurt(damage) && new Random().nextInt(4) == 0)
							potions++;
						if(e instanceof Phantom) {
							((Phantom)e).whenAttacked();
						}
							
					}
				}
				weaponTimer = 8;
				quicktimeAttack = 0;
			}
		}
		
		if(keysNewlyReleased.contains(KeyEvent.VK_SHIFT)) {
			if(potions > 0) {
				potions--;
				health = MAX_HEALTH;
			}
		}
		
		keysNewlyReleased.clear();
		
		if(damageCooldown <= 0) {
			if(checkCollideWithTileType(x, y, TileType.lava)) {
				health -= 2;
				damageCooldown = 5;
			}
			
			for(Enemy e : Main.panel.enemies) {
				if(Math.hypot((e.x + 72/2)-(x+width/2), (e.y + 72/2)-(y+height/2)) < 40) {
					if(e instanceof Monster)
						health -= 2;
					else
						health--;
					damageCooldown = 6;
				}
			}
		}
		
		if(checkCollideWithTileType(x, y, TileType.gem)) {
			Tile remove = null;
			for(Tile t : Main.panel.objects) {
				if(t.type == TileType.gem) {
					remove = t;
					break;
				}
			}
			Main.panel.objects.remove(remove);
			Main.panel.objects.add(new Tile(remove.x, remove.y, remove.width, remove.height, Util.getImageForTileType(TileType.dark), TileType.dark));
			gems++;
			RoomManager.rooms[roomX][roomY].startingConfiguration = RoomManager.emptyRoom;
		}
	}
	
	public boolean checkCollideWithTileType(int x, int y, TileType type) {
		for(int i = 0; i < Main.panel.objects.size(); i++) {
			Tile t = Main.panel.objects.get(i);
			if(t.type != type)
				continue;
			if(x >= t.x && x <= t.x + t.width && y >= t.y && y <= t.y + t.height) {
				return true;
			}
			
			if(x+width >= t.x && x+width <= t.x + t.width && y >= t.y && y <= t.y + t.height) {
				return true;
			}
			
			if(x >= t.x && x <= t.x + t.width && y+height >= t.y && y+height <= t.y + t.height) {
				return true;
			}
			
			if(x+width >= t.x && x+width <= t.x + t.width && y+height >= t.y && y+height <= t.y + t.height) {
				return true;
			}
			
			if(t.x >= x && t.x <= x + width && t.y >= y && t.y <= y + width) {
				return true;
			}
			
			if(t.x + t.width >= x && t.x + t.width <= x + width && t.y >= y && t.y <= y + width) {
				return true;
			}
			
			if(t.x >= x && t.x <= x + width && t.y + t.height >= y && t.y + t.height <= y + width) {
				return true;
			}
			
			if(t.x + t.width >= x && t.x + t.width <= x + width && t.y + t.height >= y && t.y + t.height <= y + width) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean checkCollide(int x, int y) {
		for(int i = 0; i < Main.panel.objects.size(); i++) {
			Tile t = Main.panel.objects.get(i);
			if(t.type == TileType.air || t.type == TileType.lava || t.type == TileType.dark || t.type == TileType.gem)
				continue;
			if(x >= t.x && x <= t.x + t.width && y >= t.y && y <= t.y + t.height) {
				return true;
			}
			
			if(x+width >= t.x && x+width <= t.x + t.width && y >= t.y && y <= t.y + t.height) {
				return true;
			}
			
			if(x >= t.x && x <= t.x + t.width && y+height >= t.y && y+height <= t.y + t.height) {
				return true;
			}
			
			if(x+width >= t.x && x+width <= t.x + t.width && y+height >= t.y && y+height <= t.y + t.height) {
				return true;
			}
			
			if(t.x >= x && t.x <= x + width && t.y >= y && t.y <= y + width) {
				return true;
			}
			
			if(t.x + t.width >= x && t.x + t.width <= x + width && t.y >= y && t.y <= y + width) {
				return true;
			}
			
			if(t.x >= x && t.x <= x + width && t.y + t.height >= y && t.y + t.height <= y + width) {
				return true;
			}
			
			if(t.x + t.width >= x && t.x + t.width <= x + width && t.y + t.height >= y && t.y + t.height <= y + width) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean checkCollide(int x, int y, Room r) {
		ArrayList<Tile> obj = r.returnConfigurationAsObjectList();
		for(int i = 0; i < obj.size(); i++) {
			Tile t = obj.get(i);
			if(t.type == TileType.air)
				continue;
			if(x >= t.x && x <= t.x + t.width && y >= t.y && y <= t.y + t.height) {
				return true;
			}
			
			if(x+width >= t.x && x+width <= t.x + t.width && y >= t.y && y <= t.y + t.height) {
				return true;
			}
			
			if(x >= t.x && x <= t.x + t.width && y+height >= t.y && y+height <= t.y + t.height) {
				return true;
			}
			
			if(x+width >= t.x && x+width <= t.x + t.width && y+height >= t.y && y+height <= t.y + t.height) {
				return true;
			}
			
			if(t.x >= x && t.x <= x + width && t.y >= y && t.y <= y + width) {
				return true;
			}
			
			if(t.x + t.width >= x && t.x + t.width <= x + width && t.y >= y && t.y <= y + width) {
				return true;
			}
			
			if(t.x >= x && t.x <= x + width && t.y + t.height >= y && t.y + t.height <= y + width) {
				return true;
			}
			
			if(t.x + t.width >= x && t.x + t.width <= x + width && t.y + t.height >= y && t.y + t.height <= y + width) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(Main.titleScreen)
			Main.startGame();
		keysPressed.add(e.getKeyCode());
	}


	@Override
	public void keyReleased(KeyEvent e) {
		while(keysPressed.contains(e.getKeyCode()))
			keysPressed.remove((Object)e.getKeyCode());
		keysNewlyReleased.add(e.getKeyCode());
	}


	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
}
