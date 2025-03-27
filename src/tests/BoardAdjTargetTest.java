package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	// Locations with only walkways as adjacent locations
	@Test
	public void testWalkwayAdjacencies() {
		// Walkway surrounded by other walkways
		Set<BoardCell> testList = board.getAdjList(29, 20);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(28, 20)));
		assertTrue(testList.contains(board.getCell(30, 20)));
		assertTrue(testList.contains(board.getCell(29, 21)));
		assertTrue(testList.contains(board.getCell(29, 19)));

		// Walkway near unused space
		testList = board.getAdjList(38, 16);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(38, 15)));
		assertTrue(testList.contains(board.getCell(38, 17)));
		assertTrue(testList.contains(board.getCell(37, 16)));

		// Walkway near closet
		testList = board.getAdjList(18,11);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(18, 10)));
		assertTrue(testList.contains(board.getCell(17, 11)));
		assertTrue(testList.contains(board.getCell(19, 11)));

	}
	// Locations within rooms not center
	@Test
	public void testRoomNoncenterAdjacencies() {
		// Test inside chamber of secrets should not be able to have any adjacent cells
		Set<BoardCell> testList = board.getAdjList(16, 4);
		assertEquals(0, testList.size());
		assertFalse(testList.contains(board.getCell(16, 5)));
		assertFalse(testList.contains(board.getCell(15, 4)));

		// Test inside chamber of secrets should not be able to have any adjacent cells
		testList = board.getAdjList(36, 9);
		assertEquals(0, testList.size());
		assertFalse(testList.contains(board.getCell(16, 5)));
		assertFalse(testList.contains(board.getCell(15, 4)));
	}

	//Locations that are at each edge of the board which in our board is all Unused spaces (no adjacencies)
	@Test
	public void testEdgeAdjacencies() {

		//Right edge of board
		Set<BoardCell> testList = board.getAdjList(17, 28);
		assertEquals(0, testList.size());
		assertFalse(testList.contains(board.getCell(17, 27)));
		assertFalse(testList.contains(board.getCell(18, 28)));		

		//Left edge of board
		testList = board.getAdjList(25, 0);
		assertEquals(0, testList.size());
		assertFalse(testList.contains(board.getCell(26, 0)));
		assertFalse(testList.contains(board.getCell(25, 1)));

		//top edge of board
		testList = board.getAdjList(0, 13);
		assertEquals(0, testList.size());
		assertFalse(testList.contains(board.getCell(0, 14)));
		assertFalse(testList.contains(board.getCell(1, 13)));

		//bottom edge of board
		testList = board.getAdjList(41, 14);
		assertEquals(0, testList.size());
		assertFalse(testList.contains(board.getCell(40, 14)));
		assertFalse(testList.contains(board.getCell(41, 13)));
	}

	// Locations that are beside a room cell that is not a doorway
	@Test
	public void testWalkwayRoomAdjacencies() {
		Set<BoardCell> testList = board.getAdjList(17, 8);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(17, 9)));
		assertTrue(testList.contains(board.getCell(17, 7)));
		assertTrue(testList.contains(board.getCell(18, 8)));
	}

	// Locations that are doorways
	@Test
	public void testDoorwayAdjacencies() {
		Set<BoardCell> testList = board.getAdjList(32, 21);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(32, 20)));
		assertTrue(testList.contains(board.getCell(31, 21)));
		assertTrue(testList.contains(board.getCell(34, 23)));
	}

	// Locations that are connected by secret passage
	@Test
	public void testSecretPassageAdjacencies() {
		// Test SC/CS Secret Passage Adjacency
		Set<BoardCell> testList = board.getAdjList(34, 23);
		assertEquals(2, testList.size()); // Should connect to the other room center 
		assertTrue(testList.contains(board.getCell(15, 6))); // Other room center

		// Test HG/GH Secret Passage Adjacency
		testList = board.getAdjList(6, 23);
		assertEquals(2, testList.size()); // Should connect to the other room center
		assertTrue(testList.contains(board.getCell(35, 6))); // Other room center
	}

	// Targets along walkways, at various distances
	@Test
	public void testWalkwayTargets() {
		// Test a roll of 1
		board.calcTargets(board.getCell(17, 8), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(17, 9)));
		assertTrue(targets.contains(board.getCell(17, 7)));
		assertTrue(targets.contains(board.getCell(18, 8)));	

		// Test a roll of 3
		board.calcTargets(board.getCell(17, 8), 3);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(15, 9)));
		assertTrue(targets.contains(board.getCell(15, 6)));
		assertTrue(targets.contains(board.getCell(18, 10)));

		// Test a roll of 4
		board.calcTargets(board.getCell(17, 8), 4);
		targets= board.getTargets();
		assertEquals(13, targets.size());
		assertTrue(targets.contains(board.getCell(15, 6)));
		assertTrue(targets.contains(board.getCell(14, 9)));
		assertTrue(targets.contains(board.getCell(16, 11)));
	}

	// Targets that allow the user to enter a room
	@Test
	public void testRoomEntranceTargets() {

		// Test a roll of 1
		board.calcTargets(board.getCell(8, 22), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(6, 23)));
		assertTrue(targets.contains(board.getCell(8, 21)));	

		// Test a roll of 3
		board.calcTargets(board.getCell(8, 22), 3);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(6, 23)));
		assertTrue(targets.contains(board.getCell(10, 23)));
		assertTrue(targets.contains(board.getCell(7, 20)));

		// Test a roll of 4
		board.calcTargets(board.getCell(8, 22), 4);
		targets= board.getTargets();
		assertEquals(13, targets.size());
		assertTrue(targets.contains(board.getCell(6, 23)));
		assertTrue(targets.contains(board.getCell(10, 22)));
		assertTrue(targets.contains(board.getCell(6, 20)));
		// assertFalse(targets.contains(board.getCell(6, 17)));
	}

	// Targets calculated when leaving a room without secret passage
	@Test
	public void testRoomExitTargets() {
		// Test a roll of 1
		board.calcTargets(board.getCell(30, 15), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(26, 15)));
		assertTrue(targets.contains(board.getCell(35, 15)));	

		// Test a roll of 3
		board.calcTargets(board.getCell(30, 15), 3);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(26, 13)));
		assertTrue(targets.contains(board.getCell(35, 17)));
		assertTrue(targets.contains(board.getCell(26, 17)));
	}


	// Targets calculated when leaving a room with secret passage
	@Test
	public void testSecretPassageExitTargets() {
		// Test a roll of 1
		// W/ a roll of 1, can only move from center of one room to another
		board.calcTargets(board.getCell(35, 6), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(6, 23)));

		// Test a roll of 2
		board.calcTargets(board.getCell(35, 6), 2);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(6, 23)));
		assertTrue(targets.contains(board.getCell(31, 6)));
		assertTrue(targets.contains(board.getCell(32, 7)));

		// Test a roll of 4
		board.calcTargets(board.getCell(35, 6), 4);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(6, 23)));
		assertTrue(targets.contains(board.getCell(29, 6)));
		assertTrue(targets.contains(board.getCell(32, 9)));
	}

	// Targets that reflect blocking by other players
	@Test
	public void testOccupiedTargets() {
		// test a roll of 3 blocked 2 down
		board.getCell(12, 19).setOccupied(true);
		board.calcTargets(board.getCell(10, 19), 3);
		board.getCell(15, 7).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(15, targets.size()); // Would be 16 if not occupied
		assertTrue(targets.contains(board.getCell(12, 20)));
		assertTrue(targets.contains(board.getCell(12, 18)));
		assertTrue(targets.contains(board.getCell(11, 21)));	
		assertFalse( targets.contains( board.getCell(13, 19)));
		assertFalse( targets.contains( board.getCell(12, 19)));


		// Test a room center that has doorway occupied
		board.getCell(35, 15).setOccupied(true);
		board.calcTargets(board.getCell(30, 15), 3);
		board.getCell(35, 15).setOccupied(false);
		targets = board.getTargets();
		assertEquals(4, targets.size()); // Would be 8 if not occupied
		assertTrue(targets.contains(board.getCell(26, 13)));
		assertTrue(targets.contains(board.getCell(26, 17)));
		assertTrue(targets.contains(board.getCell(25, 14)));	
		assertFalse(targets.contains( board.getCell(35, 13)));
		assertFalse(targets.contains( board.getCell(36, 16)));
	}
}
