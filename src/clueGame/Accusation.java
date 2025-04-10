package clueGame;

public class Accusation {

	private Player accuser;
	private Card person;
	private Card weapon;
	private Card room;
	
	public Accusation(Player accuser, Card person, Card weapon, Card room) {
		super();
		this.accuser = accuser;
		this.person = person;
		this.weapon = weapon;
		this.room = room;
	}
	
	public Player getSuggestor() {
		return accuser;
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
	