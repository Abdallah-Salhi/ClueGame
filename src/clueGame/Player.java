package clueGame;

import java.util.*;
import java.util.ArrayList;
import java.util.Random;


/*
* Player class:
* Represents the player class, which will have several methods that are needed for any type of player but will be abstract to ensure the creation of Human and Computer player types
*
* Authors/Contributors:
* Abdallah Salhi
* Montgomery Hughes
*/
public abstract class Player {
    protected String name;
    protected java.awt.Color color;
    protected int row, column;
    protected Set<Card> hand = new HashSet<>();
    protected Set<Card> seen = new HashSet<>();
    protected HashSet<Card> suggestion = new HashSet<>();
    protected boolean movedBySuggestion = false;

    // Main constructor, which sets name, color, and starting position of players
    public Player(String name, java.awt.Color color, int row, int col) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.column = col;
    }
    
   
    // Allows player to check if they can disprove a suggestion and returns the card that disproves it
    public Card disproveSuggestion(AccusationOrSuggestion suggestion) {
    	
    	ArrayList<Card> possibleDisprove = new ArrayList<>();
    	Random random = new Random();
    	
    	
    	for(Card card : this.hand) {
    		if(card == suggestion.getPerson() || card == suggestion.getWeapon() || card == suggestion.getRoom()) {
    			possibleDisprove.add(card);
    		}
    	}
    	
    	if(!possibleDisprove.isEmpty()) {
    		return possibleDisprove.get(random.nextInt(possibleDisprove.size()));
    	}
    	
    	return null;
    }
    
    // Add cards to seen list which allows for dynamic changes as game progresses
    public void updateSeen(Card seenCard) {
    	seen.add(seenCard);
    }
    
    // Sets new position cell for players to establish movement for game logic
    public void movePlayer(BoardCell cell) {
    	this.row = cell.getRow();
    	this.column = cell.getColumn();
    }

    // Getters
    public String getName() { return name; }
    public java.awt.Color getColor() { return color; }
    public int getRow() { return row; }
    public int getColumn() { return column; }
    public void giveCard(Card c) { hand.add(c); }
    public Set<Card> getHand() { return hand; }
    public void resetHand() { hand.clear(); }
    
    // Get / Set to check for suggestion moving
    public boolean wasMovedBySuggestion() {
        return movedBySuggestion;
    }
    public void setMovedBySuggestion(boolean moved) {
        this.movedBySuggestion = moved;
    }	
}