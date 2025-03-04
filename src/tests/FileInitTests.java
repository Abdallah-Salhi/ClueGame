package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Board;

public class FileInitTests {
	// Constants that we will use to test whether the file was loaded correctly
	public static final int NUM_ROWS = 42;
	public static final int NUM_COLUMNS = 29;
	private static Board board;

	@BeforeEach
	public static void setup{
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

	@Test
	public void testSetup{
		// To ensure data is correctly loaded, test retrieving a few rooms
		// from the hash, including the first and last in the file and a few others
		assertEquals("Great Hall", board.getRoom('C').getName() );
		assertEquals("Gryffindor Dormitory", board.getRoom('B').getName() );
		assertEquals("", board.getRoom('R').getName() );
		assertEquals("", board.getRoom('D').getName() );
		assertEquals("", board.getRoom('W').getName() );
	}

	@Test
	public void testFourDirections{
		//Each Direction

		//Cells that don't have doorway return false for isDoorway()
	}
	@Test
	public void testNumOfDoors{

	}

	@Test
	public void testInitial{

	}

	@Test
	public void testRoomLabels{
		//Check if room have proper center cell

		//Check if room have propoer label cell
	}
}
