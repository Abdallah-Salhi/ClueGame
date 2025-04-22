package clueGame;

/*
* Suggestion:
* Represents suggestion objects to store information when an accusation occurs. Necessary for modularization and for ease in access.
* 
* Authors/Contributors:
* Abdallah Salhi
* Montgomery Hughes
*/
public class Suggestion {

	private Player suggestor;
	private Card person;
	private Card weapon;
	private Card room;
	
	// Main constructor, which sets information about the suggestion and who made it
	public Suggestion(Player suggestor, Card person, Card weapon, Card room) {
		this.suggestor = suggestor;
		this.person = person;
		this.weapon = weapon;
		this.room = room;
	}
	
	// Getters and setters 

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
