package tests;

import java.util.Set;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiment.TestBoard;
import experiment.TestBoardCell;

/*
 * BoardTestsExp is the Junit testing for the methods in the TestBoard and TestBoardCell classes.
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 */


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
	    // Top left corner (0,0)
	    TestBoardCell cell = board.getCell(0, 0);
	    Set<TestBoardCell> testList = cell.getAdjList();
	    Assert.assertTrue(testList.contains(board.getCell(0, 1))); 
	    Assert.assertTrue(testList.contains(board.getCell(1, 0))); 
	    Assert.assertFalse(testList.contains(board.getCell(0, 0))); 
	    Assert.assertFalse(testList.contains(board.getCell(3, 3))); 

	    // Bottom right corner (3,3)
	    cell = board.getCell(3, 3);
	    testList = cell.getAdjList();
	    Assert.assertTrue(testList.contains(board.getCell(3, 2))); 
	    Assert.assertTrue(testList.contains(board.getCell(2, 3))); 
	    Assert.assertFalse(testList.contains(board.getCell(3, 3))); 

	    // Right edge (1,3)
	    cell = board.getCell(1, 3);
	    testList = cell.getAdjList();
	    Assert.assertTrue(testList.contains(board.getCell(0, 3)));
	    Assert.assertTrue(testList.contains(board.getCell(2, 3))); 
	    Assert.assertTrue(testList.contains(board.getCell(1, 2))); 
	    Assert.assertFalse(testList.contains(board.getCell(1, 3)));

	    // Left edge (3,0)
	    cell = board.getCell(3, 0);
	    testList = cell.getAdjList();
	    Assert.assertTrue(testList.contains(board.getCell(2, 0))); 
	    Assert.assertTrue(testList.contains(board.getCell(3, 1)));
	    Assert.assertFalse(testList.contains(board.getCell(3, 0))); 
	}
	

	/*
	 * Test target creation for maximum die roll test case on 4x4 board
	 */
    @Test
    public void testTargetMax() {
        TestBoardCell cell = board.getCell(0, 0);
        board.calcTargets(cell, 6);
        Set<TestBoardCell> targets = board.getTargets();
        Assert.assertTrue(targets.contains(board.getCell(3, 3))); // Reachable within 6 moves
    }
	
	/*
	 * Test target creation on empty 4x4 board
	 * 2 test cases for 2 different starting locations but same amount of steps
	 * 2 test cases for different amount of steps
	 */
	@Test
	void testTargetNormal() {
		// Test Case 2 - Starting location
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0,2)));
		Assert.assertTrue(targets.contains(board.getCell(1,1)));
		Assert.assertTrue(targets.contains(board.getCell(2,0)));
		
		// Test Case 3 - Starting location
		TestBoardCell cell2 = board.getCell(2, 0);
		board.calcTargets(cell2, 2);
		Set<TestBoardCell> targets2 = board.getTargets();
		Assert.assertEquals(4, targets2.size());
		Assert.assertTrue(targets2.contains(board.getCell(0,0)));
		Assert.assertTrue(targets2.contains(board.getCell(3,1)));
		Assert.assertTrue(targets2.contains(board.getCell(2,2)));
		Assert.assertTrue(targets2.contains(board.getCell(1,1)));
		
		// Test case 4 - Different steps from 0,0
		board.calcTargets(cell, 1);
		Assert.assertEquals(2, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0,1)));
		Assert.assertTrue(targets.contains(board.getCell(1,0)));
		
		// Test case 5 - Different steps from 0,0
		board.calcTargets(cell, 4);
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(1,3)));
		Assert.assertTrue(targets.contains(board.getCell(2,2)));
		Assert.assertTrue(targets.contains(board.getCell(3,1)));
		Assert.assertTrue(targets.contains(board.getCell(1,1)));
		Assert.assertTrue(targets.contains(board.getCell(0,2)));
		Assert.assertTrue(targets.contains(board.getCell(2,0)));
	}

}
