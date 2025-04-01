package clueGame;

public class Card {
    private String cardName;
    private CardType type;

    public Card(String name, CardType type) {
        this.cardName = name;
        this.type = type;
    }

    public String getCardName() {
        return cardName;
    }

    public CardType getType() {
        return type;
    }

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