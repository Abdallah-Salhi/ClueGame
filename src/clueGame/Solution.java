package clueGame;

public class Solution {
    private Card person;
    private Card weapon;
    private Card room;

    public Solution(Card person, Card weapon, Card room) {
        this.person = person;
        this.weapon = weapon;
        this.room = room;
    }

    public Card getPerson() { return person; }
    public Card getWeapon() { return weapon; }
    public Card getRoom() { return room; }
}
