package clueGame;

import java.awt.Color;
import java.awt.Graphics;
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
	protected boolean isDoorway = false;
	private boolean isWalkway = false;
	public boolean isRoom = false;
	private boolean isUnused = false;
	private DoorDirection doorDirection;
	private boolean isOccupied = false;
	private Set<BoardCell> adjacencyList;
	private Room room;
	private boolean highlight = false;

	// Constructor
	public BoardCell(int row, int col) {
		this.row = row;
		this.col = col;
		this.adjacencyList = new HashSet<>();
	}

	// Uses Graphics object in paintCompnent to draw the board in JPanel
	public void draw(Graphics g, int cellWidth, int cellHeight, int posX, int posY) {
		// Draw highlighted cell first
		if (this.highlight) {
			g.setColor(Color.cyan);
			g.fillRect(posX, posY, cellWidth, cellHeight);
			g.setColor(Color.BLACK);
			g.drawRect(posX, posY, cellWidth, cellHeight);
			return;
		}

		// Draw cell background then border
		if (this.isRoom()) {
			g.setColor(new Color(192, 192, 152));
			g.fillRect(posX, posY, cellWidth, cellHeight);
			g.drawRect(posX, posY, cellWidth, cellHeight);
		} else if (this.isWalkway()) {
			g.setColor(	new Color(112, 128, 144));
			g.fillRect(posX, posY, cellWidth, cellHeight);
			g.setColor(Color.BLACK);
			g.drawRect(posX, posY, cellWidth, cellHeight);
		} else if (this.isUnused) {
			g.setColor(new Color(87, 166, 57));
			g.fillRect(posX, posY, cellWidth, cellHeight);
			g.drawRect(posX, posY, cellWidth, cellHeight);
		} else {
			g.setColor(Color.RED); // highlight errors
			g.fillRect(posX, posY, cellWidth, cellHeight);
			g.drawRect(posX, posY, cellWidth, cellHeight);
		}
	}


	// Adds a cell to this cell's adjacency list.
	public void addAdjacency(BoardCell cell) {
		adjacencyList.add(cell);
	}

	// Setter for checking if the given space is a room
	public void setRoom(Room room) {
		isRoom = true;
		this.room = room;
	}

	// Should return true if it is a label
	public boolean setLabel() {
		return isLabel = true;
	}

	// Setter for making cell occupied by another player
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

	// Setter: Used for walkway cells
	public void setWalkway() {
		isWalkway = true;		
	}

	// Setter: For Unused spaces (Important for draw method)
	public void setUnused() {
		isUnused = true;
	}
	
	public void setHighlight(boolean value) {
		this.highlight = value;
	}

	// Getter: Checks if the given space is currently occupied by another player
	public boolean isOccupied() {
		return isOccupied;
	}

	// Getter: Returns the direction of the doorway (if this cell is a doorway).
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	// Getter: Uused for checking if the given space is a door way
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

	// Getter: Used for returning walkway cells
	private boolean isWalkway() {
		return isWalkway;
	}

	// Getter: Used for returning room cells
	private boolean isRoom() {
		return isRoom;
	}

	// Getter: Used for Unused spaces (Important for draw method)
	public boolean isUnused() {
		return isUnused;
	}

	// Getter: Used for room name (Important for draw method)
	public String getRoom() {
		return room.getName();
	}


	public boolean isHighlighted() {
		return highlight;
	}
	
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return col;
	}

}
