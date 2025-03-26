package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import experiment.TestBoardCell;

/*
 * Board
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 */

public class Board {

	private int ROWS,COLS;
	private String layoutConfigFile, setupConfigFile;
	private Map<Character,Room> roomMap;
	private BoardCell[][] grid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private Set<BoardCell> templist;
	private ArrayList<String> temp;
	private Set<BoardCell> doorwayList = new HashSet<>();




	// Singleton Pattern
	private static Board theInstance = new Board();


	// constructor is private to ensure only one can be created
	private Board() {
		super() ;
	}

	// This method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}

	public void initialize() {
	    // Always reset in case tests re-call this (common in JUnit test suites)
	    ROWS = 0;
	    COLS = 0;
	    targets = new HashSet<>();
	    visited = new HashSet<>();
	    roomMap = new HashMap<>();
	    grid = null;
	    doorwayList = new HashSet<>();

	    try {
	        loadSetupConfig();
	        loadLayoutConfig();
	    } catch (BadConfigFormatException e) {
	        System.out.println("Error: The config files are corrupt");
	    } catch (FileNotFoundException e) {
	        System.out.println("Error: Config files not found");
	    }

	    calcAdjacencies();
	}

	public void setConfigFiles(String layout, String setup) {
		// TODO Auto-generated method stub
		layoutConfigFile = "data/" + layout;
		setupConfigFile = "data/" + setup;


	}
	//Load setup.txt file and insert value/key in hash map for future access
	public void loadSetupConfig() throws BadConfigFormatException, FileNotFoundException{
		roomMap = new HashMap<>(); // Contains rooms
		
		//Fill hashMap
		try (Scanner scanner = new Scanner(new File(setupConfigFile))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty() || line.startsWith("/")) continue; // Skip empty and comment lines

				String[] values = line.split(", "); // Separate room/space names and character

				if(values[0].equals("Room") || values[0].equals("Space")) { //only input rooms

					String roomCharStr = values[2]; // convert room character to char
					char roomChar = roomCharStr.charAt(0);

					String roomNameStr = values[1];
					Room room = new Room(roomNameStr); // create room object

					roomMap.put(roomChar, room);
				}else { 
					throw new BadConfigFormatException("Error: Setup txt file not configured correctly");
				}
			}
		}
	}

	// Load layout.csv file and read through it ensuring it is not corrupted
	public void loadLayoutConfig() throws BadConfigFormatException, FileNotFoundException {
	    try (Scanner scanner = new Scanner(new File(layoutConfigFile))) {
	        ArrayList<String[]> tempList = new ArrayList<>();

	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine().trim();
	            if (line.isEmpty()) continue;
	            String[] values = line.split(",");
	            if (COLS == 0) {
	                COLS = values.length;
	            } else if (COLS != values.length) {
	                throw new BadConfigFormatException("Inconsistent column count in layout");
	            }
	            tempList.add(values);
	        }

	        ROWS = tempList.size();
	        grid = new BoardCell[ROWS][COLS];

	        for (int row = 0; row < ROWS; row++) {
	            for (int col = 0; col < COLS; col++) {
	                grid[row][col] = new BoardCell(row, col);
	            }
	        }


	        processCell(tempList, ROWS, COLS);
	    }
	}

	// Check if character is in setup config and populate board cells with info
	private void processCell(ArrayList<String[]>tempList, int ROWS, int COLS) throws BadConfigFormatException {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				char firstChar = tempList.get(row)[col].charAt(0); //get just the first Character not * or #

				if(roomMap.containsKey(firstChar)) {
					//Populate boardCells with info
					grid[row][col].setInitial(firstChar); //set the initial of the cell
					grid[row][col].setRoom(roomMap.get(firstChar)); //link to room object
					grid[row][col].isRoom(); //returns true because it is a room cell

					if(tempList.get(row)[col].length() > 1) { //check if cell has other characters
						char scndChar = tempList.get(row)[col].charAt(1); //get the second Character( * or # or letters)

						if(scndChar == '*') {
							grid[row][col].setRoomCenter();
							Room room = roomMap.get(firstChar);
							room.setCenterCell(grid[row][col]);

						}else if(scndChar == '#') {
							grid[row][col].setLabel();
							Room room = roomMap.get(firstChar);
							room.setLabelCell(grid[row][col]);

						}else if(scndChar == '<' ||scndChar == '>' || scndChar == 'v'|| scndChar == '^') {
							grid[row][col].setDoorDirection(scndChar);
							doorwayList.add(grid[row][col]); // Add to doorway list

						}else if(roomMap.containsKey(scndChar)) {
							grid[row][col].setSecretPassage(scndChar);
							Room room = roomMap.get(firstChar);
							room.setSecretPassage(grid[row][col]);

						}else {
							throw new BadConfigFormatException("Error: Second character in cell unknown");
						}
					}
				}else {
					throw new BadConfigFormatException("Error: CSV layout contains unknown symbol");
				}
			}
		}
	}
	
	// Calculates adjacency lists for all cells on the board
	private void calcAdjacencies() {

	    for (int row = 0; row < ROWS; row++) {
	        for (int col = 0; col < COLS; col++) {
	            BoardCell cell = grid[row][col];

	            if (cell.getInitial() == 'W') {
	                // Walkway adjacency
	                if (row - 1 >= 0 && grid[row - 1][col].getInitial() == 'W')
	                    cell.addAdjacency(grid[row - 1][col]);
	                if (row + 1 < ROWS && grid[row + 1][col].getInitial() == 'W')
	                    cell.addAdjacency(grid[row + 1][col]);
	                if (col - 1 >= 0 && grid[row][col - 1].getInitial() == 'W')
	                    cell.addAdjacency(grid[row][col - 1]);
	                if (col + 1 < COLS && grid[row][col + 1].getInitial() == 'W')
	                    cell.addAdjacency(grid[row][col + 1]);
	            }

	            if (cell.isDoorway()) {
	                Room room = null;
	                switch (cell.getDoorDirection()) {
	                    case DOWN:
	                        room = roomMap.get(grid[row + 1][col].getInitial());
	                        break;
	                    case UP:
	                        room = roomMap.get(grid[row - 1][col].getInitial());
	                        break;
	                    case LEFT:
	                        room = roomMap.get(grid[row][col - 1].getInitial());
	                        break;
	                    case RIGHT:
	                        room = roomMap.get(grid[row][col + 1].getInitial());
	                        break;
	                }
	                if (room != null && room.getCenterCell() != null) {
	                    cell.addAdjacency(room.getCenterCell());
	                }
	            }
	        }
	    }

	    // Second pass: update room center adjacencies
	    for (int row = 0; row < ROWS; row++) {
	        for (int col = 0; col < COLS; col++) {
	            BoardCell cell = grid[row][col];
	            if (cell.isRoomCenter()) {
	                Room currentRoom = roomMap.get(cell.getInitial());
	                if (currentRoom != null && currentRoom.getSecretPassage() != null) {
	                    char passageInitial = currentRoom.getSecretPassage().getSecretPassage();
	                    Room targetRoom = roomMap.get(passageInitial);
	                    if (targetRoom != null && targetRoom.getCenterCell() != null) {
	                        BoardCell targetCenter = targetRoom.getCenterCell();   
	                        cell.addAdjacency(targetCenter);
	                    }
	                }

	                // Add doorway cells connected to this room
	                for (BoardCell doorway : doorwayList) {
	                    for (BoardCell adj : doorway.getAdjList()) {
	                        if (adj.isRoomCenter() && adj.getInitial() == cell.getInitial()) {
	                            cell.addAdjacency(doorway);
	                        }
	                    }
	                }
	            }
	        }
	    }

	}
	
	// Calculates possible move targets given a dice roll
	public void calcTargets(BoardCell startCell, int pathlength) {
		targets.clear();
		visited.clear();
		findAllTargets(startCell, pathlength);
	}

	// Recursive function to find all valid move targets
	private void findAllTargets(BoardCell currentCell, int stepsRemaining) {
		visited.add(currentCell);

		if (stepsRemaining == 0) {
			targets.add(currentCell);
		} else {
			for (BoardCell adj : currentCell.getAdjList()) {
				if (!visited.contains(adj) && (!adj.isOccupied() || adj.isRoomCenter())) {
					if(adj.isRoomCenter()) {
						findAllTargets(adj, 0);
					}else {
						findAllTargets(adj, stepsRemaining - 1);
					}
					
				}
			}
		}

		visited.remove(currentCell); // Backtracking step
	}

	public Set<BoardCell> getAdjList(int row, int col) {
		return getCell(row,col).getAdjList(); 
		//return templist = new HashSet<BoardCell>(); //temporary for failure
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	// Returns the cell at the specified row and column
	public BoardCell getCell(int row, int column) {
		return grid[row][column];
	}

	//getRoom can either take a boardCell or character as a parameter
	public Room getRoom(BoardCell cell) {
		char key = cell.getInitial();
		return roomMap.get(key);
	}

	public Room getRoom(char value) {
		return roomMap.get(value);
	}

	public int getNumRows() {
		return ROWS;
	}
	public int getNumColumns() {
		return COLS;
	}
}



