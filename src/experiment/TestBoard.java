package experiment;

import java.util.HashSet;
import java.util.Set;

/*
 * TestBoard contains board and includes methods to calculate targets and perform operations on board, along with getters and setters
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 */
public class TestBoard {
	
	//Constructor
	public TestBoard() {
		// TODO Auto-generated constructor stub
	}
	// calculates legal targets for a move from startCell of length pathLength
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		
	}
	
	//returns the cell from the board at row, col
	public TestBoardCell getCell(int row, int column) {
		
		TestBoardCell emptyCell = new TestBoardCell();
		
		return emptyCell;
		
	}
	
	//gets the targets last created by calcTargets()
	Set<TestBoardCell> getTargets(){
		
		Set<TestBoardCell> emptySet = new HashSet<>();
		
		return emptySet;
		
	}
}
