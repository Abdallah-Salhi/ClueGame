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
}
