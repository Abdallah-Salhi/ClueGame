package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class ClueGame extends JFrame {
	private Board theInstance = Board.getInstance();
	private static Board board;
	
	public ClueGame() {
		KnownCardsPanel knownCardsPanel = new KnownCardsPanel();
		knownCardsPanel.setPreferredSize(new Dimension(180, 0));
		
		GameControlPanel gameControlPanel = new GameControlPanel();
		
		BoardPanel boardPanel = new BoardPanel();
		
		add(knownCardsPanel, BorderLayout.EAST);
		add(gameControlPanel, BorderLayout.SOUTH);
		add(boardPanel, BorderLayout.CENTER);

	}
	
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	public static void main(String[] args) {
		JFrame clueGame = new ClueGame();  // create the frame 
		setUp(); // initialize board 
		clueGame.setTitle("Clue Game");
		clueGame.setSize(1000, 1000);  // size the frame
		clueGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		clueGame.setVisible(true); // make it visible

	}
}
