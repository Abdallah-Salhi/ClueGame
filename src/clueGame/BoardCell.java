package clueGame;

import java.util.HashSet;
import java.util.Set;

/*
 * BoardCell 
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
	private boolean isRoom = false;
	private boolean isOccupied = false;
	private Set<BoardCell> adjacencyList;
	private Room room;

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
		return adjacencyList; 
	}

	// Setter for checking if the given space is a room
	public void setRoom(Room room) {
		this.room = room;
		isRoom = true;
	}

	// Getter for checking if the given space is a room
	public boolean isRoom() {
		return isRoom;
	}

	// Setter for checking if the given space is currently occupied by another player
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
		isOccupied = true;
	}

	// Getter for checking if the given space is currently occupied by another player
	public boolean isOccupied() {
		return isOccupied;
	}

	public boolean isDoorway() {
		// TODO Auto-generated method stub
		return isDoorway;
	}

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
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	//Should return true or false if 
	public boolean isLabel() {
		return isLabel;
	}

	//Should return true or false if 
	public boolean setLabel() {
		return isLabel = true;
	}

	public boolean isRoomCenter() {
		return isCenter;
	}
	public boolean setRoomCenter() {
		return isCenter = true;
	}

	public void setSecretPassage(char value) {
		this.secretPassage = value;
	}

	public char getSecretPassage() {
		return secretPassage;
	}

	public char getInitial() {
		return initial;
	}

	public void setInitial(char initial) {
		this.initial = initial;
	}







}
