package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiment.TestBoard;
import experiment.TestBoardCell;
import junit.framework.Assert;

public class BoardTestsExp {


 
	TestBoard board;

	@BeforeEach // Run before each test, @BeforeAll would work just as well
	public void setup() {
		// board should create adjacency list
		board = new TestBoard();
	}

	/*
	 * Test adjacencies for several different locations in 4x4 adjacency list
	 * Test corners and edges and center
	 */
	@Test
	public void testAdjacency() {
		TestBoardCell cell = board.getCell(0, 0);
		Set‹TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(0, 0)));
		Assert.assertTrue(testList.contains(board.getCell(3, 3)));
		Assert.assertTrue(testList.contains(board.getCell(1, 3)));
		Assert.assertTrue(testList.contains(board.getCell(3, 0)));
		Assert.assertTrue(testList.contains(board.getCell(2, 2)));
		
	}
	

	/*
	 * Test target creation on 4x4 board
	 */
	@Test
	void testTargetCreation() {
		TestBoardCell cell = board.getCell(0, 0);

		fail("Not yet implemented");
	}

}
