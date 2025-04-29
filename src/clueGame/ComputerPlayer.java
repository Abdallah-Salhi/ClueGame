package clueGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/*
 * Computer Player:
 * Represents the computer players that are involved in the game, which are children of the player class. Necessary for modularization of classes and 
 * ease in computer player logic. 
 *
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 */
public class ComputerPlayer extends Player {
    private Set<Card> seen = new HashSet<>();
    private boolean accusationFlag = false;
	private AccusationOrSuggestion successfulSuggestion;
	

    // Constructor only calls super because each computer player only has the same information as player object. Only diff is in logic.
    public ComputerPlayer(String name, java.awt.Color color, int row, int col) {
        super(name, color, row, col);
    }
    
    // Method to create a new suggestion based on what the computer player has in their hand or has already seen
    public AccusationOrSuggestion createSuggestion(Board board) {
        Room currentRoom = board.getRoom(board.getCell(row, column));
        Card roomCard = new Card(currentRoom.getName(), CardType.ROOM);

        List<Card> unseenPeople = new ArrayList<>();
        List<Card> unseenWeapons = new ArrayList<>();

        for (Card c : board.getDeck()) {
            if (!seen.contains(c) && !hand.contains(c)) {
                if (c.getType() == CardType.PERSON) unseenPeople.add(c);
                else if (c.getType() == CardType.WEAPON) unseenWeapons.add(c);
            }
        }
        // Use random to pick from unseen cards which will help progress the computer player to making an accusation that is correct
        Random rand = new Random();
        Card personCard = unseenPeople.size() == 1 ? unseenPeople.get(0) :
            unseenPeople.get(rand.nextInt(unseenPeople.size()));

        Card weaponCard = unseenWeapons.size() == 1 ? unseenWeapons.get(0) :
            unseenWeapons.get(rand.nextInt(unseenWeapons.size()));

        return new AccusationOrSuggestion(this, personCard, weaponCard, roomCard);
    }

    // Select target for player to move to based on if the target is a room or from available targets all done randomly
    public BoardCell selectTarget(Set<BoardCell> targets, Board board) {
        List<BoardCell> unseenRooms = new ArrayList<>();

        // First, collect all unseen room centers from the target list
        for (BoardCell cell : targets) {
            if (cell.isRoomCenter()) {
                Room room = board.getRoom(cell);
                Card roomCard = new Card(room.getName(), CardType.ROOM);
                if (!seen.contains(roomCard)) {
                    unseenRooms.add(cell);
                }
            }
        }

        Random rand = new Random();

        if (!unseenRooms.isEmpty()) {
            // Pick randomly from unseen rooms
            return unseenRooms.get(rand.nextInt(unseenRooms.size()));
        }

        // Fallback: randomly choose from all available targets
        List<BoardCell> allTargets = new ArrayList<>(targets);
        Collections.shuffle(allTargets); // Ensure real randomness
        return allTargets.get(0);
    }
    
	// Allows Computer players to make an accusation
    public AccusationOrSuggestion createAccusation() {
		return successfulSuggestion;
	}
    
    // Add cards to seen list which allows for dynamic changes as game progresses
    public void addSeenCard(Card card) {
        seen.add(card);
    }
    // Getter for seen list
    public Set<Card> getSeenCards() {
        return seen;
    }
    
    // Setter for accusation flag
    public void setFlag(boolean flag, AccusationOrSuggestion suggestion) {
    	this.accusationFlag = flag;
    	successfulSuggestion = suggestion;
    }
    
    // getter for accusation flag
    public boolean getFlag() {
    	return accusationFlag;
    }

	
}