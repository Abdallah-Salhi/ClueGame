package tests;

/*
 * This program tests that config files are loaded properly.
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
    public void testRoomSetup() {
        // Ensure board.getRoom() is returning null for now (expected failure)
        assertNull(board.getRoom('A')); // Great Hall
        assertNull(board.getRoom('G')); // Gryffindor Dormitory
        assertNull(board.getRoom('C')); // Chamber of Secrets
        assertNull(board.getRoom('O')); // Headmaster's Office
        assertNull(board.getRoom('Q')); // Room of Requirement
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
	    BoardCell cell = board.getCell(10, 5);
	    assertTrue(cell.isDoorway()); 
	    assertEquals(DoorDirection.LEFT, cell.getDoorDirection());

	    // Check a doorway that opens UP
	    cell = board.getCell(15, 12);
	    assertTrue(cell.isDoorway());  
	    assertEquals(DoorDirection.UP, cell.getDoorDirection());

	    // Check a doorway that opens RIGHT
	    cell = board.getCell(20, 8);
	    assertTrue(cell.isDoorway());  
	    assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());

	    // Check a doorway that opens DOWN
	    cell = board.getCell(25, 14);
	    assertTrue(cell.isDoorway());  
	    assertEquals(DoorDirection.DOWN, cell.getDoorDirection());

	    // Test that a walkway is NOT a door
	    cell = board.getCell(12, 14);
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
        assertEquals(20, numDoors); 
    }


	@Test
	public void testInitial() {

	}

	@Test
	public void testRooms() {
	    // Just test a standard room location
	    BoardCell cell = board.getCell(23, 23);
	    Room room = board.getRoom('A'); // Great Hall
	    assertNull(room); 

	    // Test a label cell
	    cell = board.getCell(2, 19);
	    room = board.getRoom('C'); // Chamber of Secrets
	    assertNull(room); 

	    // Test a room center cell
	    cell = board.getCell(20, 11);
	    room = board.getRoom('O'); // Headmaster’s Office
	    assertNull(room); 

	    // Test a secret passage
	    cell = board.getCell(3, 0);
	    room = board.getRoom('Q'); // Room of Requirement
	    assertNull(room); 
	    assertEquals(0, cell.getSecretPassage()); 

	    // Test a walkway
	    cell = board.getCell(5, 0);
	    room = board.getRoom('W'); // Walkway
	    assertNull(room);
	    assertFalse(cell.isRoomCenter());
	    assertFalse(cell.isLabel());

	    // Test a closet
	    cell = board.getCell(24, 18);
	    room = board.getRoom('X'); // Unused
	    assertNull(room);
	    assertFalse(cell.isRoomCenter());
	    assertFalse(cell.isLabel());
	}
}
