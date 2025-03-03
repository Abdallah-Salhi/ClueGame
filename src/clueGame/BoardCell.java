package clueGame;

import java.util.HashSet;
import java.util.Set;



public class BoardCell {
	private int row;
    private int col;
    private char initial;
    private char secretPassage;
    private boolean roomLabel;
    private boolean roomCenter;
    private DoorDirection doorDirection;
    private boolean isRoom;
    private boolean isOccupied;
    private Set<BoardCell> adjacencyList;
    
    // Constructor
    public BoardCell(int row, int col) {
        this.row = row;
        this.col = col;
        this.adjacencyList = new HashSet<>();
    }

    public void addAdjacency(BoardCell cell) {
        adjacencyList.add(cell);
    }

    public Set<BoardCell> getAdjList() {
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
    public boolean isOccupied() {
        return isOccupied;
    }

	public boolean isDoorway() {
		// TODO Auto-generated method stub
		return true;
	}

	public Object[] getDoorDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isLabel() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRoomCenter() {
		// TODO Auto-generated method stub
		return false;
	}

	public char getSecretPassage() {
		// TODO Auto-generated method stub
		return 0;
	}

}
