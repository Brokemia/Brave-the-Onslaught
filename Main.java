import javax.swing.JFrame;


public class Main {
	
	public static boolean titleScreen = true;
	public static MainPanel panel;
	public static GameThread thrd;
	public static Player player;
	public static JFrame frame = new JFrame();
	
	public static void main(String[] args) {
		player = new Player();
		frame = new JFrame("Brave the Onslaught");
		frame.setSize(800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new MainPanel();
		frame.add(panel);
		frame.setVisible(true);
		frame.setSize(800 + frame.getInsets().left, 800 + frame.getInsets().top);
		frame.setLocationRelativeTo(null);
		frame.addKeyListener(player);
		thrd = new GameThread();
		thrd.run();
	}

	public static void startGame() {
		titleScreen = false;
	}
	
}
