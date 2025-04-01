package clueGame;

import java.util.*;

public abstract class Player {
    protected String name;
    protected java.awt.Color color;
    protected int row, column;
    protected Set<Card> hand = new HashSet<>();

    public Player(String name, java.awt.Color color, int row, int col) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.column = col;
    }

    public String getName() { return name; }
    public java.awt.Color getColor() { return color; }
    public int getRow() { return row; }
    public int getColumn() { return column; }

    public void giveCard(Card c) { hand.add(c); }
    public Set<Card> getHand() { return hand; }
}