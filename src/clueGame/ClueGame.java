package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

/*
 * ClueGame:
 * Represents the entire Clue game GUI, in essence a hub for all GUI JPanels to be grouped in 1 frame. The class itself extends JFrame because it is  
 * the shell for the entire game that will store all the panels needed for GUI. It is also the main entry point for the game and therefore requires
 * a call to setup/initialize the instance from the singleton pattern
 *
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 */
public class ClueGame extends JFrame {
	private static Board board;
	
	// Main constructor which makes an instance of all the necessary panels while also connecting controlPanel to boadPanel. Also intiates the Human Player's (user) turn
	public ClueGame() {
		// Create panels
		GameControlPanel controlPanel = new GameControlPanel();
		KnownCardsPanel knownCardsPanel = new KnownCardsPanel();
		BoardPanel boardPanel = new BoardPanel(controlPanel, knownCardsPanel);


		controlPanel.setBoardPanel(boardPanel);

		// Set up layout
		setLayout(new BorderLayout());
		controlPanel.setPreferredSize(new Dimension(950, 150));
		add(controlPanel, BorderLayout.SOUTH);
		add(boardPanel, BorderLayout.CENTER);
		
		
		JScrollPane scrollPane = new JScrollPane(knownCardsPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(190, 0)); // 
		add(scrollPane, BorderLayout.EAST);
		
		MusicPlayer musicPlayer = new MusicPlayer("data/BackgroundMusic.wav");
		musicPlayer.play();
		boardPanel.nextTurn();
	}
	
	// Must create an instance of board and initialize everything to get going
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}

	// Main entry point for the game
	public static void main(String[] args) {
		setUp(); // Load config files and setup board


		// Launch game window
		JFrame clueGame = new ClueGame();
		clueGame.setTitle("Clue Game");
		clueGame.setSize(1000, 1000);
		clueGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clueGame.setVisible(true);

		// Show splash screen dialog
		JOptionPane.showMessageDialog(
			null,
			"You are Harry Potter.\nCan you find the solution before the Computer players?",
			"Welcome to Clue",
			JOptionPane.INFORMATION_MESSAGE
			);
		
		
		
	}
}
