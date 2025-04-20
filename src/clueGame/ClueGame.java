package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	private static Board board;

	public ClueGame() {
		// Create panels
		GameControlPanel controlPanel = new GameControlPanel();
		BoardPanel boardPanel = new BoardPanel(controlPanel);
		KnownCardsPanel knownCardsPanel = new KnownCardsPanel();

		controlPanel.setBoardPanel(boardPanel);

		// Set up layout
		setLayout(new BorderLayout());
		add(controlPanel, BorderLayout.SOUTH);
		add(boardPanel, BorderLayout.CENTER);
		add(knownCardsPanel, BorderLayout.EAST);
		
		knownCardsPanel.setPreferredSize(new Dimension(180, 0));
	}

	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}

	public static void main(String[] args) {
		setUp(); // Load config files and setup board

		// Show splash screen dialog
		JOptionPane.showMessageDialog(
			null,
			"You are Harry Potter.\nCan you find the solution before the Computer players?",
			"Welcome to Clue",
			JOptionPane.INFORMATION_MESSAGE
		);

		// Launch game window
		JFrame clueGame = new ClueGame();
		clueGame.setTitle("Clue Game");
		clueGame.setSize(1000, 1000);
		clueGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clueGame.setVisible(true);
	}
}
