package clueGame;

import java.util.HashSet;
import java.util.Set;

/*
 * BoardCell:
 * Represents a single cell on the Clue game board.
 * A BoardCell may represent a walkway, room, doorway, or special location
 * such as a room label or center. It also maintains information about
 * adjacency, door direction, occupancy, and room association.
 *
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 */


public class BoardCell {
	private int row;
	private int col;
	private char initial;
	private char secretPassage;
	private boolean isLabel = false;
	private boolean isCenter = false;
	private boolean isDoorway = false;
	private DoorDirection doorDirection;
	private boolean isOccupied = false;
	private Set<BoardCell> adjacencyList;
	private Room room;

	// Constructor
	public BoardCell(int row, int col) {
		this.row = row;
		this.col = col;
		this.adjacencyList = new HashSet<>();
	}

	// Adds a cell to this cell's adjacency list.
	public void addAdjacency(BoardCell cell) {
		adjacencyList.add(cell);
	}

	// Setter for checking if the given space is a room
	public void setRoom(Room room) {
		this.room = room;

	}

	// Should return true if it is a label
	public boolean setLabel() {
		return isLabel = true;
	}

	// Setter for checking if the given space is currently occupied by another player
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	// Setter for the initial character representing the room.
	public void setInitial(char initial) {
		this.initial = initial;
	}

	// Setter for isDoorway and Switch for door direction
	public void setDoorDirection(char scndChar) {
		isDoorway = true;
		switch (scndChar) {
		case '<': 
			doorDirection = DoorDirection.LEFT;
			break;
		case '>': 
			doorDirection = DoorDirection.RIGHT;
			break;
		case '^': 
			doorDirection = DoorDirection.UP;
			break;
		case 'v': 
			doorDirection = DoorDirection.DOWN;
			break;
		}
	}

	// Setter: Marks this cell as the center of a room and returns true.
	public boolean setRoomCenter() {
		isCenter = true;
		return isCenter;
	}

	// Setter: Returns the secret passage destination character.
	public void setSecretPassage(char value) {
		this.secretPassage = value;
	}

	// Setter: Returns the secret passage destination character.
	public char getSecretPassage() {
		return secretPassage;
	}

	// Setter: Returns the initial character representing the room.
	public char getInitial() {
		return initial;
	}

	// Setter: Returns the set of cells adjacent to this one.
	public Set<BoardCell> getAdjList() {
		return adjacencyList; 
	}

	// Getter: Checks if the given space is currently occupied by another player
	public boolean isOccupied() {
		return isOccupied;
	}

	// Getter: Returns the direction of the doorway (if this cell is a doorway).
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	// Getter: for checking if the given space is a door way
	public boolean isDoorway() {
		return isDoorway;
	}

	// Getter: returns true if cell is a label
	public boolean isLabel() {
		return isLabel;
	}

	// Getter: Returns true if this cell is the center of a room.
	public boolean isRoomCenter() {
		return isCenter;
	}


}
