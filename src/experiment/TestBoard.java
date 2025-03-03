package experiment;

import java.util.HashSet;
import java.util.Set;

/*
 * TestBoard contains board and includes methods to calculate targets and perform operations on board, along with getters and setters
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 */
public class TestBoard {

    private TestBoardCell[][] grid;
    private Set<TestBoardCell> targets;
    private Set<TestBoardCell> visited;

    public static final int COLS = 4;
    public static final int ROWS = 4;

    // Constructor - initializes the board
    public TestBoard() {
        grid = new TestBoardCell[ROWS][COLS];
        targets = new HashSet<>();
        visited = new HashSet<>();

        // Fill the board with TestBoardCell instances
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = new TestBoardCell(row, col);
            }
        }

        // Calculate adjacency lists for each cell
        calcAdjacencies();
    }

    // Returns the cell at the specified row and column
    public TestBoardCell getCell(int row, int column) {
        return grid[row][column];
    }

    // Calculates adjacency lists for all cells on the board
    private void calcAdjacencies() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                TestBoardCell cell = grid[row][col];

                // Add the valid adjacent cells :)
                if (row - 1 >= 0) cell.addAdjacency(grid[row - 1][col]); // Up
                if (row + 1 < ROWS) cell.addAdjacency(grid[row + 1][col]); // Down
                if (col - 1 >= 0) cell.addAdjacency(grid[row][col - 1]); // Left
                if (col + 1 < COLS) cell.addAdjacency(grid[row][col + 1]); // Right
            }
        }
    }

    // Calculates possible move targets given a dice roll
    public void calcTargets(TestBoardCell startCell, int pathlength) {
        targets.clear();
        visited.clear();
        findAllTargets(startCell, pathlength);
    }

    // Recursive function to find all valid move targets
    private void findAllTargets(TestBoardCell currentCell, int stepsRemaining) {
        visited.add(currentCell);

        if (stepsRemaining == 0) {
            targets.add(currentCell);
        } else {
            for (TestBoardCell adj : currentCell.getAdjList()) {
                if (!visited.contains(adj) && !adj.isOccupied()) {
                    findAllTargets(adj, stepsRemaining - 1);
                }
            }
        }

        visited.remove(currentCell); // Backtracking step
    }

    public Set<TestBoardCell> getTargets() {
        return targets;
    }
}