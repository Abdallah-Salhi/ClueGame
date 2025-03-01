package experiment;

import java.util.HashSet;
import java.util.Set;

/*
 * TestBoardCell 
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 */

public class TestBoardCell {
    private int row;
    private int col;
    private boolean isRoom;
    private boolean isOccupied;
    private Set<TestBoardCell> adjacencyList;
    
    // Constructor
    public TestBoardCell(int row, int col) {
        this.row = row;
        this.col = col;
        this.adjacencyList = new HashSet<>();
    }

    public void addAdjacency(TestBoardCell cell) {
        adjacencyList.add(cell);
    }

    public Set<TestBoardCell> getAdjList() {
        return adjacencyList; // Empty at the moment
    }

    // Setter for checking if the given space is a room
    public void setRoom(boolean isRoom) {
        this.isRoom = isRoom;
    }

    // Getter for checking if the given space is a room
    public boolean isRoom() {
        return isRoom;
    }

    // Setter for checking if the given space is currently occupied by another player
    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    // Getter for checking if the given space is currently occupied by another player
    public boolean getOccupied() {
        return isOccupied;
    }
}
