package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.List;
import java.util.Collections;

import experiment.TestBoardCell;

/*
 * Board:
 * Represents the Clue game board and manages its configuration, layout,
 * room data, and movement logic. This class is implemented as a Singleton,
 * meaning only one instance of Board exists throughout the program.
 *
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 */

public class Board {

	private int MAX_ROWS,MAX_COLS;
	private String layoutConfigFile, setupConfigFile;
	private Map<Character,Room> roomMap;
	private BoardCell[][] grid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private Set<BoardCell> doorwayList;
    private Solution solution;
    private List<Player> players;
    private Set<Card> deck;

	// Singleton instance of the Board (only one Board exists in the game)
	private static Board theInstance = new Board();


	// Constructor is private to ensure only one can be created
	private Board() {
		super() ;
	}

	// This method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}

	public void initialize() {
		// Always reset in case tests re-call this (common in JUnit test suites)
		MAX_ROWS = 0;
		MAX_COLS = 0;
		targets = new HashSet<>();
		visited = new HashSet<>();
		roomMap = new HashMap<>();
		grid = null;
		doorwayList = new HashSet<>();
		deck = new HashSet<>();
		players = new ArrayList<>();

		// Load configuration files
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (BadConfigFormatException e) {
			System.out.println("Error: The config files are corrupt");
		} catch (FileNotFoundException e) {
			System.out.println("Error: Config files not found");
		}
		
		// After files are loaded, calculate adjacencies for movement logic
	    calcAdjacencies();
	    
	    selectAnswerAndDealCards();
	}
	
	// Ensures correct pathway for file reading
	public void setConfigFiles(String layout, String setup) {
		layoutConfigFile = "data/" + layout;
		setupConfigFile = "data/" + setup;
	}
	

	//Load setup.txt file and insert value/key in hash map for future access
	public void loadSetupConfig() throws BadConfigFormatException, FileNotFoundException {
	    roomMap = new HashMap<>();
	    
	    try (Scanner scanner = new Scanner(new File(setupConfigFile))) {
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine().trim();
	            if (line.isEmpty() || line.startsWith("/")) continue;

	            String[] values = line.split(", ");

	            switch (values[0]) {
	                case "Room", "Space" -> {
	                    String roomName = values[1];
	                    char roomChar = values[2].charAt(0);
	                    Room room = new Room(roomName);
	                    roomMap.put(roomChar, room);

	                    if (values[0].equals("Room")) {
	                        deck.add(new Card(roomName, CardType.ROOM)); // Only actual rooms get cards
	                    }
	                }

	                case "Weapon" -> {
	                    String weaponName = values[1];
	                    deck.add(new Card(weaponName, CardType.WEAPON));
	                }

	                case "Player" -> {
	                    String name = values[1];
	                    String colorName = values[2];
	                    String type = values[3];
	                    int row = Integer.parseInt(values[4]);
	                    int col = Integer.parseInt(values[5]);

	                    java.awt.Color color = convertColor(colorName);

	                    Player player;
	                    if (type.equalsIgnoreCase("Human")) {
	                        player = new HumanPlayer(name, color, row, col);
	                    } else {
	                        player = new ComputerPlayer(name, color, row, col);
	                    }

	                    players.add(player);
	                    deck.add(new Card(name, CardType.PERSON));
	                }

	                default -> throw new BadConfigFormatException("Unrecognized type in setup file: " + values[0]);
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
				if (MAX_COLS == 0) {
					MAX_COLS = values.length;
				} else if (MAX_COLS != values.length) {
					throw new BadConfigFormatException("Inconsistent column count in layout");
				}
				tempList.add(values);
			}

			MAX_ROWS = tempList.size();
			grid = new BoardCell[MAX_ROWS][MAX_COLS];

			for (int row = 0; row < MAX_ROWS; row++) {
				for (int col = 0; col < MAX_COLS; col++) {
					grid[row][col] = new BoardCell(row, col);
				}
			}


			processCell(tempList, MAX_ROWS, MAX_COLS);
		}
	}

	// Moves through the board layout and populates grid cells with room and modifier info.
	private void processCell(ArrayList<String[]> tempList, int ROWS, int COLS) throws BadConfigFormatException {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				String cellCode = tempList.get(row)[col];
				if (cellCode.isEmpty()) {
					throw new BadConfigFormatException("Empty cell at (" + row + ", " + col + ")");
				}

				char firstChar = cellCode.charAt(0);
				if (!roomMap.containsKey(firstChar)) {
					throw new BadConfigFormatException("Unknown room symbol '" + firstChar + "' at (" + row + ", " + col + ")");
				}

				Room room = roomMap.get(firstChar);
				grid[row][col].setInitial(firstChar);
				grid[row][col].setRoom(room);

				if (cellCode.length() > 1) {
					processCellModifiers(cellCode.charAt(1), grid[row][col], room, row, col);
				}
			}
		}
	}

	// Helper function, applies cell-specific modifiers like doors, labels, centers, or secret passages.
	private void processCellModifiers(char modifier, BoardCell cell, Room room, int row, int col) throws BadConfigFormatException {
		switch (modifier) {
			case '*':
				cell.setRoomCenter();
				room.setCenterCell(cell);
				break;
			case '#':
				cell.setLabel();
				room.setLabelCell(cell);
				break;
			case '<': case '>': case '^': case 'v':
				cell.setDoorDirection(modifier);
				doorwayList.add(cell);
				break;
			default:
				if (roomMap.containsKey(modifier)) {
					cell.setSecretPassage(modifier);
					room.setSecretPassage(cell);
				} else {
					throw new BadConfigFormatException("Unknown cell modifier '" + modifier + "' at (" + row + ", " + col + ")");
				}
		}
	}

	// Calculates adjacency lists for all cells on the board
	private void calcAdjacencies() {

		for (int row = 0; row < MAX_ROWS; row++) {
			for (int col = 0; col < MAX_COLS; col++) {
				BoardCell cell = grid[row][col];

				if (cell.getInitial() == 'W') {
					// Walkway adjacency
					if (row - 1 >= 0 && grid[row - 1][col].getInitial() == 'W')
						cell.addAdjacency(grid[row - 1][col]);
					if (row + 1 < MAX_ROWS && grid[row + 1][col].getInitial() == 'W')
						cell.addAdjacency(grid[row + 1][col]);
					if (col - 1 >= 0 && grid[row][col - 1].getInitial() == 'W')
						cell.addAdjacency(grid[row][col - 1]);
					if (col + 1 < MAX_COLS && grid[row][col + 1].getInitial() == 'W')
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
					case NONE:
			        default:
			            throw new IllegalStateException("Unexpected door direction: " + cell.getDoorDirection());
					}
					
						
					if (room != null && room.getCenterCell() != null) {
						cell.addAdjacency(room.getCenterCell());
					}
				}
			}
		}

		// Second pass: update room center adjacencies
		for (int row = 0; row < MAX_ROWS; row++) {
			for (int col = 0; col < MAX_COLS; col++) {
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
	
	// Converts color data, used in loadSetupConfig
	private java.awt.Color convertColor(String strColor) {
	    return switch (strColor.toLowerCase()) {
	        case "red" -> java.awt.Color.RED;
	        case "green" -> java.awt.Color.GREEN;
	        case "blue" -> java.awt.Color.BLUE;
	        case "yellow" -> java.awt.Color.YELLOW;
	        case "orange" -> java.awt.Color.ORANGE;
	        case "purple" -> new java.awt.Color(128, 0, 128);
	        default -> java.awt.Color.GRAY; // Fallback for unknown color
	    };
	}
	
	// Chooses card person weapon for the solution and deals cards to players
	private void selectAnswerAndDealCards() {
	    
	    List<Card> deckList = new ArrayList<>(deck);
	    Collections.shuffle(deckList);

	    Card person = null;
	    Card weapon = null;
	    Card room = null;

	    // Randomly pick one of each type for the solution
	    for (Card c : deckList) {
	        if (c.getType() == CardType.PERSON && person == null) person = c;
	        else if (c.getType() == CardType.WEAPON && weapon == null) weapon = c;
	        else if (c.getType() == CardType.ROOM && room == null) room = c;

	        if (person != null && weapon != null && room != null) break;
	    }

	    // Save as the solution
	    solution = new Solution(person, weapon, room);

	    // Remove solution cards from the deck
	    deckList.remove(person);
	    deckList.remove(weapon);
	    deckList.remove(room);

	    // Deal remaining cards to players
	    int playerIndex = 0;
	    while (!deckList.isEmpty()) {
	        Card nextCard = deckList.remove(0);
	        players.get(playerIndex % players.size()).giveCard(nextCard);
	        playerIndex++;
	    }
	}

	public Set<BoardCell> getAdjList(int row, int col) {
		return getCell(row,col).getAdjList(); 
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	// Returns the cell at the specified row and column
	public BoardCell getCell(int row, int column) {
		return grid[row][column];
	}

	// getRoom can either take a boardCell or character as a parameter
	public Room getRoom(BoardCell cell) {
		char key = cell.getInitial();
		return roomMap.get(key);
	}
	// getter for room based on char parameter
	public Room getRoom(char value) {
		return roomMap.get(value);
	}

	public int getNumRows() {
		return MAX_ROWS;
	}
	
	public int getNumColumns() {
		return MAX_COLS;
	}

	public List<Player> getPlayers() {
	    return players; 
	}
	public Set<Card> getDeck() {
	    return deck; 
	}
	public Solution getSolution() {
	    return solution; 
	}

}
	
