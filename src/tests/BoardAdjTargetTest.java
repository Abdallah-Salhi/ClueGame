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

	//Locations with only walkways as adjacent locations
	@Test
	public void testWalkwayAdjacencies() {
		//walkway surrounded by walkways
		Set<BoardCell> testList = board.getAdjList(29, 20);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(28, 20)));
		assertTrue(testList.contains(board.getCell(30, 20)));
		assertTrue(testList.contains(board.getCell(29, 21)));
		assertTrue(testList.contains(board.getCell(29, 19)));

		//walkway near unused
		testList = board.getAdjList(38, 16);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(38, 15)));
		assertTrue(testList.contains(board.getCell(38, 17)));
		assertTrue(testList.contains(board.getCell(37, 16)));

		//walkway near closet
		testList = board.getAdjList(18,11);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(18, 10)));
		assertTrue(testList.contains(board.getCell(17, 11)));
		assertTrue(testList.contains(board.getCell(19, 11)));

	}
	//Locations within rooms not center
	@Test
	public void testRoomNoncenterAdjacencies() {
		/*
		//test inside chamber of secrets should not be able to have any adjacent cells
		Set<BoardCell> testList = board.getAdjList(16, 4);
		assertEquals(0, testList.size());
		assertFalse(testList.contains(board.getCell(16, 5)));
		assertFalse(testList.contains(board.getCell(15, 4)));

		//test inside HufflePuff dormitory should not be able to have any adjacent cells
		testList = board.getAdjList(36, 9);
		assertEquals(0, testList.size());
		assertFalse(testList.contains(board.getCell(36, 8)));
		assertFalse(testList.contains(board.getCell(37, 9)));
		*/
	}

	//Locations that are at each edge of the board which in our board is all Unused spaces (no adjacencies)
	@Test
	public void testEdgeAdjacencies() {
		/*
		//Right edge of board
		Set<BoardCell> testList = board.getAdjList(17, 28);
		assertEquals(0, testList.size());
		assertFalse(testList.contains(board.getCell(17, 27)));
		assertFalse(testList.contains(board.getCell(17, 29)));		

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
		assertFalse(testList.contains(board.getCell(42, 14)));
		assertFalse(testList.contains(board.getCell(41, 13)));
		*/
	}

	//Locations that are beside a room cell that is not a doorway
	@Test
	public void testWalkwayRoomAdjacencies() {
		Set<BoardCell> testList = board.getAdjList(17, 8);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(17, 9)));
		assertTrue(testList.contains(board.getCell(17, 10)));
		assertTrue(testList.contains(board.getCell(18, 8)));

	}

	//Locations that are doorways
	@Test
	public void testDoorwayAdjacencies() {
		Set<BoardCell> testList = board.getAdjList(32, 21);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(32, 20)));
		assertTrue(testList.contains(board.getCell(31, 21)));
		assertTrue(testList.contains(board.getCell(34, 23)));

	}

	//Locations that are connected by secret passage
	@Test
	public void testSecretPassageAdjacencies() {

	}

	//Targets along walkways, at various distances
	@Test
	public void testWalkwayTargets() {
		// Test a roll of 1
		board.calcTargets(board.getCell(17, 8), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(17, 9)));
		assertTrue(targets.contains(board.getCell(18, 8)));	

		// Test a roll of 3
		board.calcTargets(board.getCell(17, 8), 3);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(15, 6)));
		assertTrue(targets.contains(board.getCell(15, 9)));
		assertTrue(targets.contains(board.getCell(19, 7)));

		// test a roll of 4
		board.calcTargets(board.getCell(17, 8), 4);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(15, 6)));
		assertTrue(targets.contains(board.getCell(14, 9)));
		assertTrue(targets.contains(board.getCell(19, 10)));	
	}

	//Targets that allow the user to enter a room
	@Test
	public void testRoomEntranceTargets() {
		// test a roll of 1
		board.calcTargets(board.getCell(8, 22), 1);
		Set<BoardCell> targets= board.getTargets();
		
		assertTrue(targets.contains(board.getCell(6, 23)));
	}

	//Targets calculated when leaving a room without secret passage
	@Test
	public void testRoomExitTargets() {

	}

	//Targets calculated when leaving a room using a secret passage
	@Test
	public void testSecretPassageExitTargets() {

	}

	//Targets that reflect blocking by other players
	@Test
	public void testOccupiedTargets() {
		// test a roll of 4 blocked 2 down
		board.getCell(12, 19).setOccupied(true);
		board.calcTargets(board.getCell(10, 19), 3);
		board.getCell(15, 7).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(11, targets.size()); // would be 12 if not occupied
		assertTrue(targets.contains(board.getCell(12, 20)));
		assertTrue(targets.contains(board.getCell(12, 18)));
		assertTrue(targets.contains(board.getCell(11, 21)));	
		assertFalse( targets.contains( board.getCell(13, 19)));
		assertFalse( targets.contains( board.getCell(12, 19)));


		// test a room center that has doorway occupied
		board.getCell(35, 15).setOccupied(true);
		board.calcTargets(board.getCell(30, 15), 3);
		board.getCell(35, 15).setOccupied(false);
		targets = board.getTargets();
		assertEquals(3, targets.size()); // would be 6 if not occupied
		assertTrue(targets.contains(board.getCell(26, 14)));
		assertTrue(targets.contains(board.getCell(26, 16)));
		assertTrue(targets.contains(board.getCell(25, 15)));	
		assertFalse( targets.contains( board.getCell(35, 15)));
		assertFalse( targets.contains( board.getCell(35, 16)));
	}

}
