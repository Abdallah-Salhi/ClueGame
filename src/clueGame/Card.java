package clueGame;

/*
 * Card:
 * Represents the Card object which essentially stores information about the card and provides a place to create methods that make the game logic in
 * other areas of the code easier to achieve
 *
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 */
public class Card {
    private String cardName;
    private CardType type;

    // Constructor which is only necessary for storing the name of the card and the type of card it is
    public Card(String name, CardType type) {
        this.cardName = name;
        this.type = type;
    }
    
    // Getters and Setters

    public String getCardName() {
        return cardName;
    }

    public CardType getType() {
        return type;
    }
    
    // Override equals function to accommodate card type
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card other)) return false;
        return cardName.equals(other.cardName) && type == other.type;
    }

    @Override
    public int hashCode() {
        return cardName.hashCode() + type.hashCode();
    }
}