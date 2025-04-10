package clueGame;

public class Suggestion {

	private Player suggestor;
	private Card person;
	private Card weapon;
	private Card room;
	
	public Suggestion(Player suggestor, Card person, Card weapon, Card room) {
		super();
		this.suggestor = suggestor;
		this.person = person;
		this.weapon = weapon;
		this.room = room;
	}
	
	public Player getSuggestor() {
		return suggestor;
	}
	
	public Card getPerson() {
		return person;
	}
	
	public Card getWeapon() {
		return weapon;
	}
	public Card getRoom() {
		return room;
	}
	
	
	
}
