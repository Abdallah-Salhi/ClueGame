package clueGame;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	private Board theInstance = Board.getInstance();
	private static Board board;

	private int numRows;
	private int numCols;

	private int panelWidth;
	private int panelHeight;

	private int cellWidth;
	private int cellHeight;

	private int xOffset;
	private int yOffset;
	private int padding;

	private int currentPlayerIndex = 0;
	private boolean humanTurnFinished = false;
	private GameControlPanel controlPanel;

	public BoardPanel(GameControlPanel controlPanel) {
		this.controlPanel = controlPanel;

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		add(panel);
	}

	public void nextTurn() {
		List<Player> players = Board.getInstance().getPlayers();
		Player currentPlayer = players.get(currentPlayerIndex);
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
		int roll = new Random().nextInt(6) + 1;
		
		for (BoardCell[] row : Board.getInstance().getGrid()) {
			for (BoardCell cell : row) {
				cell.setHighlight(false);
			}
		}

		// Set current player and roll in the control panel
		controlPanel.setTurn(currentPlayer, roll);

		BoardCell currentCell = Board.getInstance().getCell(currentPlayer.getRow(), currentPlayer.getColumn());
		Board.getInstance().calcTargets(currentCell, roll);

		// Human player logic
		if (currentPlayer instanceof HumanPlayer) {
			for (BoardCell cell : Board.getInstance().getTargets()) {
				cell.setHighlight(true);
			}
			humanTurnFinished = false;
			repaint();
		} else {
			// Computer player logic
			ComputerPlayer cpu = (ComputerPlayer) currentPlayer;
			BoardCell target = cpu.selectTarget(Board.getInstance().getTargets(), Board.getInstance());
			currentPlayer.row = target.getRow();
			currentPlayer.column = target.getColumn();
			repaint();
		}
	}

	// Draws the board by invoking draw method in boardCells
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		numRows = theInstance.getNumRows();
		numCols = theInstance.getNumColumns();

		padding = 10; // set equal amount of space all around border
		panelWidth = getWidth() - 2 * padding; // Needed to calculate size of cells dynamically 
		panelHeight = getHeight()- 2 * padding;
		cellWidth = panelWidth / numCols;
		cellHeight = panelHeight / numRows;

		// Draw base grid
		BoardCell[][] grid = theInstance.getGrid();
		for(int row = 0; row < grid.length; row++) {
			for(int col = 0; col < grid[row].length; col++) {
				BoardCell cell = grid[row][col];
				cell.draw(g, cellWidth, cellHeight, col * cellWidth + padding, row * cellHeight + padding);
			}
		}

		// Draw Players 
		List<Player> players = theInstance.getPlayers();
		for(Player player : players) {
			g.setColor(player.color);
			g.drawOval(player.column * cellWidth + padding, player.row * cellHeight + padding, cellWidth - 7, cellHeight);
			g.fillOval(player.column * cellWidth + padding, player.row * cellHeight + padding, cellWidth - 7, cellHeight);
		}

		// Draw doors and room labels
		for(int row = 0; row < grid.length; row++) {
			for(int col = 0; col < grid[row].length; col++) {
				if(grid[row][col].isDoorway) {
					DoorDirection dir = grid[row][col].getDoorDirection();
					drawDoorWay(g, dir, row, col, cellWidth, cellHeight);
				}
				if(grid[row][col].isLabel()) {
					String roomLabel = grid[row][col].getRoom();
					String[] words = roomLabel.split(" ");

					Font font = new Font("SansSerif", Font.BOLD, 11);
					g.setFont(font);
					g.setColor(Color.blue);
					FontMetrics metrics = g.getFontMetrics(font);
					int totalHeight = words.length * metrics.getHeight();

					int cellX = col * cellWidth + padding;
					int cellY = row * cellHeight + padding;
					int drawY = cellY + (cellHeight - totalHeight)/2 + metrics.getAscent();

					for(int i = 0; i < words.length; i++) {
						String word = words[i];
						int textWidth = metrics.stringWidth(word);
						int drawX = cellX + (cellWidth - textWidth)/2;
						g.drawString(word, drawX, drawY + i * metrics.getHeight());
					} 
				}
			}
		}
	}

	private void drawDoorWay(Graphics g, DoorDirection direction, int row, int col, int width, int height) {
		int targetRow = row;
		int targetCol = col;

		switch(direction) {
			case DOWN -> targetRow++;
			case UP -> targetRow--;
			case LEFT -> targetCol--;
			case RIGHT -> targetCol++;
			default -> throw new IllegalStateException("Unexpected door direction");
		}

		if (targetRow >= 0 && targetRow < numRows && targetCol >= 0 && targetCol < numCols) {
			int newRow = targetRow * cellHeight + padding;
			int newCol = targetCol * cellWidth + padding;
			g.setColor(Color.BLUE);

			switch(direction) {
				case DOWN -> g.fillRect(newCol, newRow, width, 5);
				case UP -> g.fillRect(newCol, newRow + height - 5, width ,5);
				case LEFT -> g.fillRect(newCol + width - 5, newRow, 5, height);
				case RIGHT -> g.fillRect(newCol, newRow, 5, height);
				default -> throw new IllegalStateException("Unexpected door direction");
			}
		}
	}

	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}

	public static void main(String[] args) {
		setUp();
		JFrame frame = new JFrame();
		GameControlPanel controlPanel = new GameControlPanel();
		BoardPanel panel = new BoardPanel(controlPanel);

		frame.setContentPane(panel);
		frame.setSize(700, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

