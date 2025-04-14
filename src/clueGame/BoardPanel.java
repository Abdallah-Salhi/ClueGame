package clueGame;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardPanel extends JPanel{
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

	public BoardPanel() {

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		add(panel);
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
		//Draw Players 
		List<Player> players = theInstance.getPlayers();
		for(Player player : players) {
			g.setColor(player.color);
			g.drawOval(player.column * cellWidth + padding, player.row * cellHeight + padding, cellWidth - 7, cellHeight);
			g.fillOval(player.column * cellWidth + padding, player.row * cellHeight + padding, cellWidth - 7, cellHeight);
			
		}

		// Draw doors and room labels
		for(int row = 0; row < grid.length; row++) {
			for(int col = 0; col < grid[row].length; col++) {
				// Draw doors
				if(grid[row][col].isDoorway) {
					DoorDirection dir = grid[row][col].getDoorDirection();
					drawDoorWay(g, dir, row, col, cellWidth, cellHeight);
				}
				// Draw room label
				if(grid[row][col].isLabel()) {
					String roomLabel = grid[row][col].getRoom();
					String[] words = roomLabel.split(" "); //split longer labels into words to stack

					
					//calculate style and metrics 
					Font font = new Font("SansSerif", Font.BOLD, 11);
					g.setFont(font);
					g.setColor(Color.blue);
					FontMetrics metrics = g.getFontMetrics(font);
					int totalHeight = words.length * metrics.getHeight();
					
					// Calculate starting position
					int cellX = col * cellWidth + padding;
					int cellY = row * cellHeight + padding;
					int drawY = cellY + (cellHeight - totalHeight)/2 + metrics.getAscent();

					
					for(int i = 0; i < words.length; i++) { // Draw each word seperately
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
		case DOWN:
			targetRow++;
			break;
		case UP:
			targetRow--;
			break;
		case LEFT:
			targetCol--;
			break;
		case RIGHT:
			targetCol++;
			break;
		default:
			throw new IllegalStateException("Unexpected door direction");
		}

		// Make sure we're in bounds
		if (targetRow >= 0 && targetRow < numRows && targetCol >= 0 && targetCol < numCols) {
			
			// Obtain row and col of cell we actually need to draw door in
			int newRow = targetRow * cellHeight + padding;
			int newCol = targetCol * cellWidth + padding;
			
			// Draw door
			
			g.setColor(Color.BLUE);
			
			switch(direction) {
			case DOWN:
				g.fillRect(newCol, newRow, width, 5);
				break;
			case UP:
				g.fillRect(newCol, newRow + height - 5, width ,5);
				break;
			case LEFT:
				g.fillRect(newCol + width - 5, newRow, 5, height);
				break;
			case RIGHT:
				g.fillRect(newCol, newRow, 5, height);
				break;
			default:
				throw new IllegalStateException("Unexpected door direction");
			}
		}

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
		setUp(); // initialize board (needed for size of cells)
		JFrame frame = new JFrame();  // create the frame 
		BoardPanel panel = new BoardPanel();

		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(700, 700);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
	}

}
