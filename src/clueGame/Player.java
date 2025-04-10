package clueGame;

import java.util.*;
import java.util.ArrayList;
import java.util.Random;

import tests.Suggestion;



public abstract class Player {
    protected String name;
    protected java.awt.Color color;
    protected int row, column;
    protected Set<Card> hand = new HashSet<>();
    protected Set<Card> seen = new HashSet<>();
    protected HashSet<Card> suggestion = new HashSet<>();


    public Player(String name, java.awt.Color color, int row, int col) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.column = col;
    }
    
   
    // Allows player to check if they can disprove a suggestion and returns the card that disproves it
    public Card disproveSuggestion(Suggestion suggestion) {
    	/*
    	ArrayList<Card> possibleDisprove = new ArrayList<>();
    	Random random = new Random();
    	
    	
    	for(Card card : this.hand) {
    		if(suggestion.contains(card)) {
    			possibleDisprove.add(card);
    		}
    	}
    	
    	if(possibleDisprove != null) {
    		Card randomCard = possibleDisprove.get(random.nextInt(possibleDisprove.size()));
    		return randomCard;
    	}
    	*/
    	return null;
    }
    
    public void updateSeen(Card seenCard) {
    	seen.add(seenCard);
    }
    
//    public void updateSuggestion(Card suggestionCard) {
//    	suggestion.add(suggestionCard);
//    }

    public String getName() { return name; }
    public java.awt.Color getColor() { return color; }
    public int getRow() { return row; }
    public int getColumn() { return column; }
//    public HashSet<Card> getSuggestion(){ return suggestion;}
//    public void resetSuggestion() { suggestion.clear(); }
    public void giveCard(Card c) { hand.add(c); }
    public Set<Card> getHand() { return hand; }
    public void resetHand() { hand.clear(); }
    
    	
}