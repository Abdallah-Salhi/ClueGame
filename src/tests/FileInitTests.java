package tests;

/*
 * This program tests that config files are loaded properly.
 * 
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 *
 */

// Doing a static import allows me to write assertEquals rather than
// Assert.assertEquals
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

public class FileInitTests {
	// Constants that we will use to test whether the file was loaded correctly
	public static final int NUM_ROWS = 42;
	public static final int NUM_COLUMNS = 29;
	private static Board board;

	@BeforeAll
	public static void setup() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

    @Test
    public void testRoomLabels() {
        // Ensure rooms are correctly loaded from setup file
        assertEquals("Great Hall", board.getRoom('A').getName());
        assertEquals("Gryffindor Dormitory", board.getRoom('G').getName());
        assertEquals("Chamber of Secrets", board.getRoom('C').getName());
        assertEquals("Headmaster's Office", board.getRoom('O').getName());
        assertEquals("Room of Requirement", board.getRoom('Q').getName());
        assertEquals("Walkway", board.getRoom('W').getName());
    }

	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}
	
	@Test
	public void testFourDirections() {
	    // Check a doorway that opens LEFT
	    BoardCell cell = board.getCell(25, 10);
	    assertTrue(cell.isDoorway()); 
	    assertEquals(DoorDirection.LEFT, cell.getDoorDirection());

	    // Check a doorway that opens UP
	    cell = board.getCell(17, 7);
	    assertTrue(cell.isDoorway());  
	    assertEquals(DoorDirection.UP, cell.getDoorDirection());

	    // Check a doorway that opens RIGHT
	    cell = board.getCell(19, 19);
	    assertTrue(cell.isDoorway());  
	    assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());

	    // Check a doorway that opens DOWN
	    cell = board.getCell(13, 25);
	    assertTrue(cell.isDoorway());  
	    assertEquals(DoorDirection.DOWN, cell.getDoorDirection());

	    // Test that a walkway is NOT a door
	    cell = board.getCell(3, 12);
	    assertFalse(cell.isDoorway());  
	}
	
    @Test
    public void testNumberOfDoors() {
        int numDoors = 0;
        for (int row = 0; row < board.getNumRows(); row++) {
            for (int col = 0; col < board.getNumColumns(); col++) {
                BoardCell cell = board.getCell(row, col);
                if (cell.isDoorway()) {
                    numDoors++;
                }
            }
        }
        assertEquals(13, numDoors); 
    }

	@Test
	public void testRooms() {
	    // Just test a standard room location
	    BoardCell cell = board.getCell(23, 23);
	    Room room = board.getRoom('A'); // Great Hall
	    assertNotNull(room);

	    // Test a label cell
	    cell = board.getCell(5, 6);
		assertNotNull(room);
	    assertTrue(cell.isLabel());

	    // Test a room center cell
	    cell = board.getCell(15, 6);
	    assertTrue(cell.isRoomCenter());


	    // Test a secret passage
	    cell = board.getCell(6, 25);
	    assertEquals(0, cell.getSecretPassage()); 

	    // Test a walkway
	    cell = board.getCell(3, 4);
	    room = board.getRoom(cell); // Walkway
	    assertNotNull(room);
	    assertEquals('W',cell.getInitial());
		assertEquals(room.getName(), "Walkway" );
	    

	    // Test a closet
	    cell = board.getCell(24, 15);
	    room = board.getRoom(cell); // Unused
	    assertNotNull(room);
	    assertFalse(cell.isRoomCenter());
	    assertFalse(cell.isLabel());
	    assertEquals('X',cell.getInitial());
	}
}
