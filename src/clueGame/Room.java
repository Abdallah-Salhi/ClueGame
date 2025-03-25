package clueGame;
/*
 * Room
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 */
public class Room {
    private String name;
    private BoardCell centerCell;
    private BoardCell labelCell;
    private BoardCell secretPassageCell;

    // Constructor
    public Room(String name) {
        this.name = name;
    }

    // Setters
    public void setCenterCell(BoardCell centerCell) {
        this.centerCell = centerCell;
    }

    public void setLabelCell(BoardCell labelCell) {
        this.labelCell = labelCell;
    }
    
    public void setSecretPassage(BoardCell secretPassageCell) {
    	this.secretPassageCell = secretPassageCell;
    }

    // Getters
    public String getName() {
        return name;
    }

    public BoardCell getCenterCell() {
        return centerCell;
    }

    public BoardCell getLabelCell() {
        return labelCell;
    }
    
    public BoardCell getSecretPassage() {
    	return secretPassageCell;
    }
}
