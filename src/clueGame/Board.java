package clueGame;

import java.util.HashSet;
import java.util.Set;



public class Board {
	private BoardCell[][] grid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;

	public static final int COLS = 4;
	public static final int ROWS = 4;

	/*
	 * variable and methods used for singleton pattern
	 */
	private static Board theInstance = new Board();
	// constructor is private to ensure only one can be created
	private Board() {
		super() ;
	}
	// this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}
	/*
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize(){
		grid = new BoardCell[ROWS][COLS];
		targets = new HashSet<>();
		visited = new HashSet<>();

		// Fill the board with TestBoardCell instances
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				grid[row][col] = new BoardCell(row, col);
			}
		}

		// Calculate adjacency lists for each cell
		calcAdjacencies();
	}

	public void setConfigFiles(String string, String string2) {
		// TODO Auto-generated method stub


	}
	public void loadSetupConfig() {
		// TODO Auto-generated method stub


	}
	public void loadLayoutConfig() {
		// TODO Auto-generated method stub

	}

	// Returns the cell at the specified row and column
	public BoardCell getCell(int row, int column) {
		return grid[row][column];
	}
	
	// Calculates adjacency lists for all cells on the board
    private void calcAdjacencies() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                BoardCell cell = grid[row][col];

                // Add the valid adjacent cells :)
                if (row - 1 >= 0) cell.addAdjacency(grid[row - 1][col]); // Up
                if (row + 1 < ROWS) cell.addAdjacency(grid[row + 1][col]); // Down
                if (col - 1 >= 0) cell.addAdjacency(grid[row][col - 1]); // Left
                if (col + 1 < COLS) cell.addAdjacency(grid[row][col + 1]); // Right
            }
        }
    }
	public Object getRoom(char c) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getNumRows() {
		// TODO Auto-generated method stub
		return 0;
	}
	public int getNumColumns() {
		// TODO Auto-generated method stub
		return 0;
	}
}
